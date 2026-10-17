# Aquamirae 1.21.1 -> 26.1.2 Porting Guide

Every mapping in this guide was **verified against the actual 26.1.2 sources**. When in doubt,
look things up yourself — never guess an API.

## Where to look things up (IMPORTANT)

- MC + NeoForge 26.1.2 sources (extracted, greppable): `/tmp/mc-sources` (`rg ... net/minecraft/...`).
  Contains `net/minecraft/**` and `net/neoforged/**` .java sources.
- Original mod sources (READ ONLY, never modify): `/home/goose/proyectos/Aquamirae/aquamirae-1.21.1`
- Port destination (the files you edit): `/home/goose/proyectos/Aquamirae/aquamirae-26.1.2`
- Fragmentum v4 (API used at runtime): sources at `/tmp/fragmentum-v41` — v2 API is
  `dev.obscuria.fragmentum.v2.api.**` (+ internal impls in `...v2.core.**`).
- GeckoLib 5.5.2 sources: `/tmp/gl-5.5.2-sources` (package root `com.geckolib`).
- Jars (last resort, use `unzip -l` / `unzip -p`): MC merged sources jar
  `/home/goose/.gradle/caches/neoformruntime/intermediate_results/mergeWithSources_c5b71c646393237837615ce1a490bf4854e45a82_output.jar`,
  neoforge sources `/home/goose/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/26.1.2.114/85d479e73d84cbdc0acbca3b9a12e81d9cb226b3/neoforge-26.1.2.114-sources.jar`.

## Global renames ALREADY APPLIED (do not redo)

- `ResourceLocation` -> `Identifier` (`net.minecraft.resources.Identifier`).
  `Identifier.of(a,b)` does not exist -> `Identifier.fromNamespaceAndPath(a,b)`.
- `net.minecraft.Util` -> `net.minecraft.util.Util`
- `net.minecraft.advancements.critereon` -> `net.minecraft.advancement.criterion`
- `net.minecraft.advancements` -> `net.minecraft.advancement`
- `net.minecraft.world.entity.monster.{AbstractIllager,Pillager,Vindicator,Evoker}` -> `...monster.illager.*`
- `net.minecraft.world.entity.npc.AbstractVillager` -> `...npc.villager.AbstractVillager`
- `net.minecraft.world.entity.animal.{WaterAnimal,AbstractFish}` -> `...animal.fish.*`
- `net.minecraft.world.entity.projectile.AbstractArrow` -> `...projectile.arrow.AbstractArrow`
- `net.minecraft.world.item.armortrim` -> `net.minecraft.world.item.equipment.trim`
- `level.isClientSide` -> `level.isClientSide()` (method now), `xxx.random` -> `xxx.getRandom()`
- `noCollission()` -> `noCollision()`
- Fragmentum: `content.registry.*` -> `v2.api.common.registry.*`; `Deferred<T,V>` -> `Deferred<T>`
  (single type arg!); `content.network.FragmentumNetworking` -> `v2.api.common.network.FragmentumNetworking`;
  `content.util.easing.{Easing,EasingFunction}` -> `v2.api.common.{Easing,EasingFunction}`;
  `config.{ConfigBuilder,ConfigValue}` -> `v2.api.config.*`; `client.FragmentumClientRegistry` ->
  `v2.api.client.FragmentumClientRegistry`; `server.FragmentumServerRegistry` -> `v2.api.server.FragmentumServerRegistry`;
  `FragmentumProxy` -> `v2.api.FragmentumProxy`; `Tooltips` -> `RichText` (`v2.api.common.richtext.RichText`);
  `TooltipOptions` -> `RichTextOptions` (constants `RichTextOptions.DESCRIPTION` / `.LORE` exist);
  `GroupTooltip` -> `_TooltipStack` (`dev.obscuria.fragmentum.v2.core.common._TooltipStack`, a record:
  `new _TooltipStack(List<TooltipComponent>)`, static `maybeGroup(first, second)`).
  Old `content.util.color.{ARGB,HSV,RGB,Colors}` -> new unified `dev.obscuria.fragmentum.v2.api.common.Color`
  (check its javadoc/source for the API; NOTE: not yet ported anywhere).
  Old `registerMobEffect(...)` is GONE -> use `REGISTRAR.register(Registries.MOB_EFFECT, id, supplier)`.
  Old `createVanillaRegistry(key)` is GONE -> use `REGISTRAR.createRegistry(key)`.
  Old `registerSpawnPlacement(...)` is GONE -> use NeoForge's `RegisterSpawnPlacementsEvent`.
- GeckoLib: `software.bernie.geckolib` -> `com.geckolib` with package moves already applied
  (AnimatableManager->`com.geckolib.animatable.manager`, EasingType->`com.geckolib.animation.object`,
  cache.object->`com.geckolib.cache.model`, renderer.layer builtin layers -> `renderer.layer.builtin.*`,
  GeoRenderer -> `renderer.base.GeoRenderer`).
  NOT yet ported: `AnimationState<T>` (software.bernie.geckolib.animation) no longer exists:
  `AnimationController` now takes `AnimationController.AnimationStateHandler<T>` receiving
  `com.geckolib.animation.state.AnimationTest<T>`. See `AnimationController.java` in /tmp/gl-5.5.2-sources.
  `registerSpawnPlacement` gone (NeoForge event). `EasingType` import may already point to
  `com.geckolib.animation.object.EasingType`.

## Verified MC 26.1.2 API changes (vs 1.21.1)

### Interaction / damage
- `InteractionResultHolder<T>` GONE. `ItemInteractionResult` GONE (merged into InteractionResult).
- `net.minecraft.world.InteractionResult` is a sealed interface:
  `SUCCESS` (swing client), `SUCCESS_SERVER`, `CONSUME` (= old SUCCESS + no swing? verify semantic),
  `FAIL`, `PASS`, `TRY_WITH_EMPTY_HAND` constants; `new InteractionResult.Success(SwingSource, ItemContext)`
  possible; `Success.heldItemTransformed(stack)`, `.withoutItem()`.
- Old `InteractionResult.sidedSuccess(level)` -> `SUCCESS_SERVER` if server side else `SUCCESS`.
- `Entity.hurt` is now `public final void hurt(DamageSource, float)` (no boolean!). Damage
  events/preconditions check via `LivingEntity#hurt` overrides... (verify per use case; NeoForge
  `LivingDamageEvent`/`LivingIncomingDamageEvent` is the modern way to intercept damage).
- `Mob.doHurtTarget(ServerLevel, Entity)` — takes ServerLevel first arg now.
- `MobSpawnType` -> `net.minecraft.world.entity.EntitySpawnReason` (check constant names, e.g.
  `EntitySpawnReason.SPAWNER` etc. — grep the file).
- `Mob.finalizeSpawn(ServerLevelAccessor, EntitySpawnReason, SpawnGroupData)` (reason type changed).

### Items
- `SwordItem` GONE -> `new Item(new Item.Properties().sword(ToolMaterial material, float attackDamage, float attackSpeed))`
- `Tiers` GONE -> `net.minecraft.world.item.ToolMaterial` + `net.minecraft.world.item.ToolMaterials` enum.
- `ArmorItem` GONE (in most cases) -> `new Item(new Item.Properties().humanoidArmor(ArmorMaterial material, ArmorType type))`
  (+ optional `.equippable(new Equippable(...))` for visuals; check Items.java / Item.java in sources).
- `ArmorMaterial` -> `net.minecraft.world.item.equipment.ArmorMaterial` (check if record/codec-based).
- `ArmorMaterials` -> `net.minecraft.world.item.equipment.ArmorMaterials`.
- `MobEffect` constructor is `protected` -> keep using the access transformer entry
  `public net.minecraft.world.effect.MobEffect <init>(Lnet/minecraft/world/effect/MobEffectCategory;I)V` (already in AT file)
  or subclass.
- `MobEffect.addAttributeModifier(Holder<Attribute>, Identifier, double, Operation)` still exists.

### Blocks / level
- `BlockBehaviour.Properties` -> `BlockBehaviour.Properties`/`Block` (verify), `noCollision()` fixed spelling.
- `updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
  Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)`
  (new arg order, check super methods).
- `Level.isClientSide()` method; `Level.getRandom()` method (fields private/protected now).

### Rendering (client) — BIG CHANGES
- Entity renderers are render-state based: `EntityRenderer<T extends Entity, S extends EntityRenderState>`.
  Override `createRenderState()`, fill data in `extractRenderState(T, S, float)`, render from `S`.
  Vanilla render states exist: `EntityRenderState`, `LivingEntityRenderState`, `HumanoidRenderState`, etc.
  in `net.minecraft.client.renderer.entity.state`.
- `LivingEntityRenderer<T, S extends LivingEntityRenderState, M extends EntityModel<? super S>>` (3 generics).
- `EntityModel` has no no-arg constructor — models take `ModelLayerLocation` via `LayerDefinition`/`MeshDefinition`.
- Render types: `net.minecraft.client.renderer.rendertype.RenderType` (class) & `RenderTypes` (factories).
  `RenderType.eyes(tex)` -> `RenderTypes.eyes(tex)`; `entityCutoutNoCull`/`entitySolid` etc. in RenderTypes.
- Fog: `FogRenderer` GONE (replaced by `net.minecraft.client.renderer.fog.FogRenderer` + `FogData`).
  `FogShape` GONE. NeoForge events: `ViewportEvent.RenderFog` (has `getFogData()`, near/far plane setters)
  and `ViewportEvent.ComputeFogColor` (float r/g/b/a on Vector4f).
- `PanoramaRenderer`, `LightTexture`, `GlStateManager`, `BossHealthOverlay` moved/renamed — grep in /tmp/mc-sources.
- Particles: `xd/yd/zd`, `quadSize`, `lifetime`, `gravity`, `hasPhysics` are protected fields on
  `Particle`/`SingleQuadParticle`; `setParticleSpeed(double,double,double)`, `setLifetime(int)`.
  `TextureSheetParticle.setSpriteFromAge` GONE — check current API (`setSprite`, `SpriteSet.get(...)`).
- `GuiGraphics` — verify methods (blit/drawSpecial etc. renamed — grep per use).

### Networking (NeoForge)
- `net.neoforged.neoforge.network.registration.PayloadRegistrar`, `CustomPacketPayload`,
  `Registrar` flow: verify names in `/tmp/mc-sources/net/neoforged/neoforge/network/**`.
  Fragmentum v2 provides `dev.obscuria.fragmentum.v2.api.common.network.FragmentumNetworking` — check its API.

### NeoForge misc
- `RegisterSpawnPlacementsEvent` (for spawn placements), `EntityAttributeCreationEvent` — verify names in
  `/tmp/mc-sources/net/neoforged/neoforge/event/**`.
- Biome modifiers: `dev.obscuria.aquamirae.neoforge.common.IceMazeBiomeModifier` — check
  `net.neoforged.neoforge.common.world.biome.*` paths.

## Build
- `cd /home/goose/proyectos/Aquamirae/aquamirae-26.1.2 && JAVA_HOME=/usr/java/jdk-25-oracle-aarch64 ./gradlew build`
- Server tests must use port 25599 (25565 is taken on this machine).
