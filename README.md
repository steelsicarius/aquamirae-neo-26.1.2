# Aquamirae — port a NeoForge / Minecraft 26.1.2

Port del mod **Aquamirae** (Obscuria, NeoForge/MC 1.21.1) a **NeoForge 26.1.2.114**.

- Origen (solo lectura): `~/proyectos/Aquamirae/aquamirae-1.21.1`
- Destino: este directorio (MDK ModDevGradle 26.1.2, plugin `net.neoforged.moddev` 2.0.148)
- Dependencias: GeckoLib 5.5.2, Fragmentum 26.1-4.0.5, YACL 3.9.7+26.1-neoforge, MixinExtras 0.5.5, Lombok

## Compilar y ejecutar

```bash
JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 ./gradlew build
JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 ./gradlew runServer   # puerto 25599 (25565 ocupado)
xvfb-run -a env JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 ./gradlew runClient --no-configuration-cache
```

Estado verificado:

| Prueba | Resultado |
|---|---|
| `gradlew build` | OK |
| `runServer` (puerto 25599) | Arranca limpio: `Done (0.3s)`, sin ERROR/WARN de datos |
| `runClient` (Xvfb, aarch64) | Carga mods, registros, texturas/atlasses y renderiza el menú estable (>3 min); sin fallos de mixins |

## Cambios del port (1.21.1 → 26.1.2)

### Registro / carga del mod
- `Identifier` sustituye a `ResourceLocation`; `Registry.holders()` → `stream()+wrapAsHolder`; `getHolderOrThrow` → `HolderGetter.getOrThrow`.
- **Ids obligatorios en properties**: `BlockBehaviour.Properties.of().setId(key)` y `Item.Properties().setId(key)` (los constructores resuelven id/description/drops en 26.x). Todas las factorías de `AquamiraeBlocks`/`AquamiraeItems` reciben properties con el `ResourceKey` ya fijado (62 ítems, 12 bloques).
- **Atributos de entidades diferidos**: `AttributeSupplier.Builder` resuelve `Holder<Attribute>` de forma eager y los atributos del propio NeoForge (`neoforge:swim_speed`, `nametag_distance`) solo se enlazan tras su `RegisterEvent`; como en dev este mod se carga antes que `neoforge`, el registro se difiere al `RegisterEvent` de `ENTITY_TYPE` (vía `NeoAquamirae`).
- **Spawn placements**: vía `RegisterSpawnPlacementsEvent` (Fragmentum v4 eliminó `registerSpawnPlacement`).
- **Datos sincronizados del jugador**: NeoForge 26.x prohíbe inyectar `EntityDataAccessor` desde mixins; el bonus del Remnant Saber vive ahora en un `AttachmentType` (`aquamirae:remnant_saber_bonus`, serialize+sync+copyOnDeath) en `AquamiraeAttachments`.
- Biome modifier de ice maze: el `RegistryAccess` se toma de `ServerLifecycleHooks.getCurrentServer()` (vanilla movió `buildServerStatus` tras `initServer` y el proxy de Fragmentum aún no está disponible durante los biome modifiers). `clearSpawns` usa los mapas públicos del builder de spawners (AT de NeoForge) en vez de reflexión.

### Sistema de daño
- `hurt` → `hurtServer(ServerLevel, …)`; `actuallyHurt` gana `ServerLevel`; `onEffectRemoved(1)` → `onEffectsRemoved(Collection)`; el chequeo de escudo `isDamageSourceBlocked` → `applyItemBlocking`; `isInvulnerableTo` gana `ServerLevel`.
- Animación de daño por `handleDamageEvent` únicamente.

### Save/load de entidades
- `read/addAdditionalSaveData` usan `ValueInput/ValueOutput` (nuestro hook del bonus se eliminó: lo cubre el attachment).

### Cliente
- Render states por defecto: los renderers con `LivingEntityRenderState` implementan `createRenderState()`.
- `OrderedSubmitNodeCollector.submitModel` con `outlineColor` nuevo.
- GUI 2D: `GuiGraphics.pose()` es `Matrix3x2fStack` (`pushMatrix/popMatrix`, `translate(x,y)`, `scale(x,y)`).
- Tooltips: lore de items en `ItemStack.addDetailsToTooltipComponents` (NeoForge divide el método) y `addToTooltip` gana `TooltipDisplay`.
- Hitboxes de sub-partes: sistema **Gizmos** (`MixinEntityHitboxDebugRenderer`; `LevelRenderer.renderLineBox` ya no existe).
- Texturas piratas de illagers: `getTextureLocation(IllagerRenderState)`; el check de ice maze usa la posición interpolada del render state.
- Panorama temático: `CubeMap` (reemplaza a `PanoramaRenderer`) crea buffers GPU, por lo que se instancia y registra en `GameRenderer#registerPanoramaTextures` (no en un static initializer).
- Logo del menú: blit de `LogoRenderer.extractRenderState(…,IFI)` con `RenderPipeline` en `com.mojang.blaze3d.pipeline`.
- `bobView(CameraRenderState, PoseStack)` sin partialTicks; `EchoCompassAngle` implementa `type()` con su `MAP_CODEC`.

### Datapacks (formato 26.x)
- Ingredientes: `{"item": x}` → `"x"`; `{"tag": x}` → `"#x"` (38 recetas + 3 de cocina).
- Number providers: `{"min","max"}` → `{"type":"minecraft:uniform",…}` (70 en loot tables y worldgen).
- Entrada `"minecraft:air"` con peso (loot de Captain Cornelia) → tipo `"minecraft:empty"`.
- Keepsakes remotos: `Color.codec()` de Fragmentum v4 acepta objeto/decimal/hex (`#RRGGBB`).

### Items con textura y tooltips (encontrados en pruebas de juego reales)
- **`assets/aquamirae/items/*.json` (75 definiciones)**: MC 26.x requiere un *item definition* por ítem (desde 1.21.4; en 1.21.1 el enlace a `models/item` era automático). Sin ellas el juego pinta los ítems sin textura ("Missing item model for location aquamirae:…"). Incluye:
  - 11 spawn eggs con dos `tints` constantes (los colores ya no viven en el ítem, sino en la definición cliente).
  - `echo_compass`: `range_dispatch` con 33 entradas (umbrales = ángulo×32 → modelos vanilla `recovery_compass*`) usando la propiedad `aquamirae:echo_compass/angle` registrada vía `RegisterRangeSelectItemModelPropertyEvent`.
- **`MixinRichTextLexer`**: parchea un bug de Fragmentum 4.0.5/4.1.0 que crashea con `IllegalStateException: No match found` cualquier texto con tags (`[color=…][/br]`) — tooltips de abilities, tooltips de ítems y lore de keepsakes. El lexer guarda el `Matcher` vivo en cada span y llama `group()` cuando ya está agotado; en Java 25 `Matcher.toMatchResult()` devuelve un snapshot que no es `Matcher`, así que se usa el matcher ya agotado. El mixin redirige `toMatchResult()` para devolver un matcher "congelado" sobre el match actual (`require = 0` para convivir con una futura versión corregida).

### Encontrado en la segunda ronda de pruebas de juego
- **GeckoLib 5.5.2 usa `assets/<ns>/geckolib/models|animations/`** en 26.x (no `geo/`): migrados los 13 modelos de armadura, 3 de entidad y 3 animaciones. Sin esto las armaduras equipadas no se renderizan y `Loaded 0 models`.
- **Huevos de spawn**: `minecraft:item/template_spawn_egg` ya no existe en 26.x. El mod ahora incluye `textures/item/spawn_egg(_overlay).png` (extraídas del client.jar 1.21.1) + un `models/item/template_spawn_egg.json` propio; los 11 huevos heredan de él y los `items/*.json` tiñen las dos capas con tints constantes (sustituto del color por item eliminado).
- **Bug de matching en `TooltipComponentRegistry` de Fragmentum v4** (hallado al re-test): el registry compara componentes por *clase exacta* (`getClass().equals(type)`), pero `FragmentumClient.init()` registra el renderer del grupo contra la *interfaz* `TooltipStack`, así que la implementación interna `_TooltipStack` **nunca matchea** y el hover sobre ítems con 2+ tooltip images (p. ej. Dreadwake: ability set + keepsake) crashea con `Unknown TooltipComponent`. Solución: `AquamiraeTooltipStack` — implementación concreta del mod registrada contra su clase exacta (`ClientTooltipStack::create` acepta cualquier implementación de la interfaz); `HookItemStack` y `MixinItemStack` agrupan con ella. Verificado con test standalone contra el jar de Fragmentum 4.1.0: la clase interna de Fragmentum no matchea, la nuestra sí.
- `AquamiraeSpawnData.SCUTTLER` pasa a `AMBIENT` para coincidir con el EntityType (corrige el warning de NeoForge sobre mob caps).

## Workarounds de entorno (aarch64 / Oracle Linux 9)

- **LWJGL arm64**: el manifiesto vanilla 26.1.2 solo declara `natives-linux` (x64) para Linux. Los 8 jars `natives-linux-arm64` de Maven Central viven en `lib/natives-linux-arm64/` y se añaden al runtime classpath (`runtimeOnly fileTree`); LWJGL elige el arch correcto.
- **OpenAL**: el `libopenal` arm64 de LWJGL requiere `GLIBCXX_3.4.30` (EL9 trae 3.4.29) y MC 26 exige `ALC_SOFT_system_events` (openal-soft ≥ 1.24). `lib/aarch64/libopenal.so.1.24.2` es openal-soft 1.24.2 compilado localmente con gcc 11 (GLIBCXX 3.4.29) y se carga con `-Dorg.lwjgl.openal.libname` (configurado en el run `client`).
- **IPv6 deshabilitado** en el host: `run/server.properties` usa `server-ip=0.0.0.0`; el run `server` pasa `--port 25599`.
- En cliente headless (Xvfb) no hay narrador (`flite`) ni dispositivo de audio; MC los degrada sin crash.

### Tercera ronda de pruebas de juego
- **Shipbreaker invisible**: el render se enganchaba con un mixin frágil sobre `LevelRenderer.renderLevel`; ahora usa `RenderLevelStageEvent.AfterWeather` (API pública de NeoForge), que dibuja dentro del pass activo del frame graph. El spawner, los temblores, la física y los payloads ya funcionaban.
- **Echo compass**: `minecraft:item/recovery_compass` (frame base) no existe en 26.x; los umbrales 0.0/31.5 apuntan a `recovery_compass_16` (equivalente vanilla).
- **Traducciones**: en 26.x todos los ítems (incl. BlockItems) usan `item.<ns>.<id>` como description id (antes los BlockItems pedían `block.*`); añadidas las claves que faltan en los 18 idiomas.
- **Aclaraciones de fidelidad** (verificado contra el origen 1.21.1, no son bugs del port): `niveis_tear` es un asset huérfano (lang/modelo/textura, nunca registrado ni usado); el Shipbreaker se invoca con el *cracked record* en un jukebox dentro del bioma ice maze, o por el spawner aleatorio del bioma; **no existe** ninguna dimensión "ice maze" en el original (ni `data/…/dimension`, ni envío de `ClientboundTravelPayload`), y los custom spawners solo se registran en el OVERWORLD.

### Corrección final: sonido del Shell Horn
- El item `shell_horn` usaba botón derecho y reproducía `AquamiraeSounds.ITEM_SHELL_HORN_USE`, registrado como `stack.shell_horn.use`, mientras que `assets/aquamirae/sounds.json` definía `item.shell_horn.use`. Corregido el registro a `item.` (junto con los otros `stack.` → `item.` de items relacionados) para que coincida con el archivo de sonidos.

## Limitaciones conocidas / pendiente
- Ambient particles/fog/sky/water-fog de biomas: en 26.x son *environment attributes* datapack-driven que los biome modifiers de NeoForge no pueden parchear — temporalmente no-op en `NeoBiomePatcher` (habría que hacerlo con un datapack override del biome).
- La textura del panorama temático se registra junto al panorama vanilla; el menú temático requiere `themed_main_menu` activo en la config de cliente.
- Queda por validar en juego (no solo smoke test): interacciones, spawns en el ice maze, abilidades, GUIs (config screen YACL) — el servidor/cliente arrancan y registran todo, pero no hay tests funcionales automáticos.
