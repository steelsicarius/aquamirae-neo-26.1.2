package dev.obscuria.aquamirae.integration.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.obscuria.aquamirae.integration.hook.HookItemStack;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.fragmentum.v2.core.common._TooltipStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ModifyReturnValue(method = "getTooltipImage", at = @At("RETURN"))
    private Optional<TooltipComponent> getTooltipImage$modifyReturnValue(Optional<TooltipComponent> original) {
        var addition = HookItemStack.getTooltipImage((ItemStack) (Object) this);
        if (addition.isEmpty()) return original;
        return Optional.of(_TooltipStack.maybeGroup(original.orElse(null), addition.get()));
    }

    // MC 26.1.2: the lore tooltip lines moved from getTooltipLines into
    // addDetailsToTooltip(Components) and addToTooltip gained the
    // TooltipDisplay param.
    @Inject(method = "addDetailsToTooltipComponents", at = @At(value = "FIELD", target = "Lnet/minecraft/core/component/DataComponents;LORE:Lnet/minecraft/core/component/DataComponentType;"))
    private void addDetailsToTooltip$onAddLore(
            Item.TooltipContext context, TooltipDisplay display, Player player,
            TooltipFlag isAdvanced, Consumer<Component> consumer, CallbackInfo ci
    ) {
        this.addToTooltip(AquamiraeDataComponents.LOGBOOK_CONTENTS, context, display, consumer, isAdvanced);
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void getTooltipLines$onReturn(
            Item.TooltipContext context, Player player,
            TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir
    ) {
        HookItemStack.appendTooltipLines((ItemStack) (Object) this, cir.getReturnValue());
    }

    @Shadow
    public abstract <T extends TooltipProvider> void addToTooltip(
            DataComponentType<T> component, Item.TooltipContext context, TooltipDisplay display,
            Consumer<Component> consumer, TooltipFlag isAdvanced);
}
