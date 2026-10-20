package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.integration.hook.HookLivingEntity;
import dev.obscuria.aquamirae.neoforge.common.IceMazeBiomeModifier;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.fragmentum.v2.api.common.registry.FragmentumRegistry;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Aquamirae.MOD_ID)
public final class NeoAquamirae {

    public NeoAquamirae(IEventBus eventBus) {
        Aquamirae.init();
        eventBus.addListener(NeoAquamirae::registerCustomAttributes);
        eventBus.addListener(NeoAquamirae::registerEntityAttributes);
        registerBiomeModifiers();
    }

    /**
     * MC 26.1.2: attribute suppliers must be built after the attribute
     * registry is bound (NeoForge's own mod attributes included). The
     * attribute registry is processed first, but RegisterEvent is dispatched
     * per mod container in load order and this mod loads before neoforge, so
     * the deferred registration runs on the later ENTITY_TYPE RegisterEvent,
     * when all attribute holders are guaranteed to be bound.
     */
    private static void registerEntityAttributes(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.ENTITY_TYPE) return;
        AquamiraeEntityTypes.registerAttributes();
    }

    private static void registerCustomAttributes(EntityAttributeModificationEvent event) {
        for (var entityType : event.getTypes()) {
            HookLivingEntity.createLivingAttributes(attribute -> event.add(entityType, attribute));
        }
    }

    private static void registerBiomeModifiers() {
        var registrar = FragmentumRegistry.registrar(Aquamirae.MOD_ID);
        registrar.register(
                NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,
                Aquamirae.id("ice_maze"),
                () -> IceMazeBiomeModifier.CODEC);
    }
}