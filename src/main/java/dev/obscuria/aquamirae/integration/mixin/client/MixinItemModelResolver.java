package dev.obscuria.aquamirae.integration.mixin.client;

import dev.obscuria.aquamirae.integration.hook.client.HookItemRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC 26.1.2: ItemRenderer was removed. The ItemStack is only known while the
 * model is resolved (ItemModelResolver), so we capture the NO_SHADING flag
 * there for every item render state. All entity-related paths (held, worn,
 * dropped items) funnel through updateForTopItem.
 */
@Mixin(ItemModelResolver.class)
public abstract class MixinItemModelResolver {

    @Inject(require = 0, method = "updateForTopItem", at = @At("HEAD"))
    private void updateForTopItem$captureNoShading(
            ItemStackRenderState state, ItemStack item,
            net.minecraft.world.item.ItemDisplayContext displayContext,
            @org.jetbrains.annotations.Nullable net.minecraft.world.level.Level level,
            @org.jetbrains.annotations.Nullable net.minecraft.world.entity.ItemOwner owner,
            int seed, CallbackInfo ci
    ) {
        HookItemRenderer.captureNoShading(state, item);
    }
}
