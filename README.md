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

## Workarounds de entorno (aarch64 / Oracle Linux 9)

- **LWJGL arm64**: el manifiesto vanilla 26.1.2 solo declara `natives-linux` (x64) para Linux. Los 8 jars `natives-linux-arm64` de Maven Central viven en `lib/natives-linux-arm64/` y se añaden al runtime classpath (`runtimeOnly fileTree`); LWJGL elige el arch correcto.
- **OpenAL**: el `libopenal` arm64 de LWJGL requiere `GLIBCXX_3.4.30` (EL9 trae 3.4.29) y MC 26 exige `ALC_SOFT_system_events` (openal-soft ≥ 1.24). `lib/aarch64/libopenal.so.1.24.2` es openal-soft 1.24.2 compilado localmente con gcc 11 (GLIBCXX 3.4.29) y se carga con `-Dorg.lwjgl.openal.libname` (configurado en el run `client`).
- **IPv6 deshabilitado** en el host: `run/server.properties` usa `server-ip=0.0.0.0`; el run `server` pasa `--port 25599`.
- En cliente headless (Xvfb) no hay narrador (`flite`) ni dispositivo de audio; MC los degrada sin crash.

## Limitaciones conocidas / pendiente

- Ambient particles/fog/sky/water-fog de biomas: en 26.x son *environment attributes* datapack-driven que los biome modifiers de NeoForge no pueden parchear — temporalmente no-op en `NeoBiomePatcher` (habría que hacerlo con un datapack override del biome).
- La textura del panorama temático se registra junto al panorama vanilla; el menú temático requiere `themed_main_menu` activo en la config de cliente.
- Queda por validar en juego (no solo smoke test): interacciones, spawns en el ice maze, abilidades, GUIs (config screen YACL) — el servidor/cliente arrancan y registran todo, pero no hay tests funcionales automáticos.
