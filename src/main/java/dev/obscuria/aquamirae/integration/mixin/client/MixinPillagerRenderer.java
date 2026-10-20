package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.obscuria.aquamirae.AquamiraeHelper;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PillagerRenderer.class)
public abstract class MixinPillagerRenderer {

    // MC 26.1.2: renderers receive an IllagerRenderState instead of the entity;
    // the ice maze check is done from the state's interpolated position.
    @ModifyReturnValue(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/IllagerRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"))
    private Identifier getTextureLocation$modifyReturnValue(Identifier original, IllagerRenderState state) {
        var level = Minecraft.getInstance().level;
        if (level == null) return original;
        if (!AquamiraeHelper.isIceMazeAt(level, BlockPos.containing(state.x, state.y, state.z))) return original;
        return AquamiraeClient.PIRATE_PILLAGER_TEXTURE;
    }
}
