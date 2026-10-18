package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.common.region.BiomePatcher;
import dev.obscuria.fragmentum.v2.api.FragmentumProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.List;

public record NeoBiomePatcher(
        ModifiableBiomeInfo.BiomeInfo.Builder builder,
        Registry<ConfiguredWorldCarver<?>> configuredCarvers,
        Registry<PlacedFeature> placedFeatures
) implements BiomePatcher {

    public static NeoBiomePatcher create(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        var registryAccess = FragmentumProxy.registryAccess();
        return new NeoBiomePatcher(builder,
                registryAccess.registryOrThrow(Registries.CONFIGURED_CARVER),
                registryAccess.registryOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public void clearSpawns() {
        this.builder.getMobSpawnSettings().spawners.values().forEach(List::clear);
        this.builder.getMobSpawnSettings().mobSpawnCosts.clear();
    }

    /**
     * MC 26.1.2: the carving generation step was removed; carvers attach to the
     * biome without a step. The step parameter was dropped from {@link BiomePatcher}.
     */
    @Override
    public void addCarver(ResourceKey<ConfiguredWorldCarver<?>> carverKey) {
        this.builder.getGenerationSettings().addCarver(configuredCarvers.getHolderOrThrow(carverKey));
    }

    @Override
    public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey) {
        this.builder.getGenerationSettings().addFeature(step, placedFeatures.getHolderOrThrow(placedFeatureKey));
    }

    @Override
    public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry) {
        this.builder.getMobSpawnSettings().addSpawn(spawnGroup, spawnEntry);
    }

    @Override
    public void setPrecipitation(boolean hasPrecipitation) {
        this.builder.getClimateSettings().setHasPrecipitation(hasPrecipitation);
    }

    @Override
    public void setTemperature(float temperature) {
        this.builder.getClimateSettings().setTemperature(temperature);
    }

    @Override
    public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
        this.builder.getClimateSettings().setTemperatureModifier(temperatureModifier);
    }

    @Override
    public void setDownfall(float downfall) {
        this.builder.getClimateSettings().setDownfall(downfall);
    }

    /**
     * MC 26.1.2: ambient particles moved out of biomes into the environment
     * attribute system (EnvironmentAttributes.AMBIENT_PARTICLES), which is baked
     * from biome datapack JSON. NeoForge biome modifiers cannot patch it, so
     * this is a no-op for now. A datapack biome override could restore it.
     */
    @Override
    public void setAmbientParticle(AmbientParticle settings) {
        // no-op: see class javadoc
    }

    /**
     * MC 26.1.2: ambient loops are an environment attribute now
     * (EnvironmentAttributes.AMBIENT_SOUNDS); not patchable via biome modifiers. No-op for now.
     */
    @Override
    public void setAmbientLoop(Holder<SoundEvent> sound) {
        // no-op: see class javadoc
    }

    /**
     * MC 26.1.2: fog color is an environment attribute
     * (EnvironmentAttributes.FOG_COLOR); not patchable via biome modifiers. No-op for now.
     */
    @Override
    public void setFogColor(int color) {
        // no-op: see class javadoc
    }

    /**
     * MC 26.1.2: sky color is an environment attribute
     * (EnvironmentAttributes.SKY_COLOR); not patchable via biome modifiers. No-op for now.
     */
    @Override
    public void setSkyColor(int color) {
        // no-op: see class javadoc
    }

    @Override
    public void setWaterColor(int color) {
        this.builder.getSpecialEffects().waterColor(color);
    }

    /**
     * MC 26.1.2: water fog color is an environment attribute
     * (EnvironmentAttributes.WATER_FOG_COLOR); not patchable via biome modifiers. No-op for now.
     */
    @Override
    public void setWaterFogColor(int color) {
        // no-op: see class javadoc
    }
}
