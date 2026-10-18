package dev.obscuria.aquamirae.integration.mixin.client;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import dev.obscuria.aquamirae.integration.hook.client.HookItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC 26.1.2: item light is passed to ItemStackRenderState.submit as lightCoords;
 * NO_SHADING items captured by MixinItemModelResolver render full bright.
 */
@Mixin(ItemStackRenderState.class)
public abstract class MixinItemStackRenderState {

    // lightCoords is the first int parameter of submit
    @ModifyVariable(require = 0, method = "submit", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int submit$modifyLight(int lightCoords) {
        return HookItemRenderer.modifyLight((ItemStackRenderState) (Object) this, lightCoords);
    }
}
