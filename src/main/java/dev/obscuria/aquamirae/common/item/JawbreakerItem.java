package dev.obscuria.aquamirae.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

public class JawbreakerItem extends Item {

    public JawbreakerItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // MC 26.1.2: eating is performed through the CONSUMABLE component
        // (same flow as Item#finishUsingItem), then the sword takes damage.
        var consumable = stack.getOrDefault(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD);
        var result = consumable.onConsume(level, entity, stack);
        result.hurtAndBreak(10, entity, entity.getUsedItemHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
        level.playSound(
                null, entity.getX(), entity.getY(), entity.getZ(),
                consumable.sound().value(), SoundSource.NEUTRAL,
                1.0F, (float) entity.getRandom().triangle(1.0, 0.2));
        return result;
    }
}
