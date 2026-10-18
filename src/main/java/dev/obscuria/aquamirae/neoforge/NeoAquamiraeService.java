package dev.obscuria.aquamirae.neoforge;

import dev.obscuria.aquamirae.AquamiraeService;
import dev.obscuria.fragmentum.v2.api.common.registry.DeferredEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class NeoAquamiraeService implements AquamiraeService {

    /**
     * MC 26.1.2: DeferredSpawnEggItem was removed from NeoForge. Spawn eggs are
     * data-driven now: the target entity is carried by the ENTITY_DATA component,
     * which is attached via Item.Properties#spawnEgg(EntityType). Egg colors are
     * no longer part of the item and live in the client item definition (tints).
     */
    @Override
    public <T extends Mob> SpawnEggItem newSpawnEggItem(DeferredEntity<T> entitySupplier, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(properties.spawnEgg(entitySupplier.get()));
    }
}
