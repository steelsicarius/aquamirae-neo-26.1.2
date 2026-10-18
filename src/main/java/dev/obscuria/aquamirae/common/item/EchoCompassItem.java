package dev.obscuria.aquamirae.common.item;

import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EchoCompassItem extends Item {

    public EchoCompassItem(Properties properties) {
        super(properties);
    }

    /**
     * MC 26.1.2: inventoryTick only runs on the server side now; the tracked
     * position component syncs to clients automatically.
     */
    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (entity.tickCount % 20 != 0) return;
        var entities = level.getEntities(entity, entity.getBoundingBox().inflate(128));
        for (var target : entities) {
            if (!hasEcho(target)) continue;
            stack.set(AquamiraeDataComponents.ECHO_TRACKER, GlobalPos.of(level.dimension(), target.blockPosition()));
            return;
        }
        stack.remove(AquamiraeDataComponents.ECHO_TRACKER);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(AquamiraeDataComponents.ECHO_TRACKER);
    }

    private static boolean hasEcho(Entity entity) {
        if (entity instanceof ItemEntity item) {
            return item.getItem().is(AquamiraeItems.SHIP_GRAVEYARD_ECHO.asItem());
        } else if (entity instanceof Scuttler scuttler) {
            return scuttler.getStolenItem().is(AquamiraeItems.SHIP_GRAVEYARD_ECHO.asItem());
        } else return false;
    }
}
