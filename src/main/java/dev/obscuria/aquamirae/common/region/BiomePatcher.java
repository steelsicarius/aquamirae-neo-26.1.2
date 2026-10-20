package dev.obscuria.aquamirae.common.region;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface BiomePatcher {

    void clearSpawns();

    /**
     * MC 26.1.2: the carving generation step is gone; carvers are attached to
     * the biome directly.
     */
    void addCarver(ResourceKey<ConfiguredWorldCarver<?>> carverKey);

    void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey);

    /**
     * MC 26.1.2: biome spawners are weight-aware (WeightedList); the weight
     * travels with the entry.
     */
    void addSpawn(MobCategory spawnGroup, net.minecraft.util.random.Weighted<MobSpawnSettings.SpawnerData> spawnEntry);

    void setPrecipitation(boolean hasPrecipitation);

    void setTemperature(float temperature);

    void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier);

    void setDownfall(float downfall);

    void setAmbientParticle(AmbientParticle settings);

    void setAmbientLoop(Holder<SoundEvent> sound);

    void setFogColor(int color);

    void setSkyColor(int color);

    void setWaterColor(int color);

    void setWaterFogColor(int color);
}
