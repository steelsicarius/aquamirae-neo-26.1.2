package dev.obscuria.aquamirae.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SeaFoodItem extends Item {

    public SeaFoodItem(Properties properties) {
        super(properties);
    }

    /**
     * MC 26.1.2: food effects live on the CONSUMABLE component instead of
     * FoodProperties; the tooltip is built from it.
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> components, TooltipFlag isAdvanced) {
        @Nullable var consumable = stack.get(DataComponents.CONSUMABLE);
        if (consumable == null) return;
        var foodEffects = consumable.onConsumeEffects().stream()
                .filter(ApplyStatusEffectsConsumeEffect.class::isInstance)
                .map(ApplyStatusEffectsConsumeEffect.class::cast)
                .flatMap(effect -> effect.effects().stream())
                .toList();
        if (foodEffects.isEmpty()) return;
        components.accept(CommonComponents.EMPTY);
        PotionContents.addPotionTooltip(foodEffects, components, 1f, context.tickRate());
    }
}
