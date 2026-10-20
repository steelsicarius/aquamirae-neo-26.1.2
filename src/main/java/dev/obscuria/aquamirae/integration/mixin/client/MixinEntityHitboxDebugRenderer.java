package dev.obscuria.aquamirae.integration.mixin.client;

import dev.obscuria.aquamirae.api.common.entity.MultipartEntity;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class MixinEntityHitboxDebugRenderer {

    @Inject(require = 0, method = "showHitboxes", at = @At("TAIL"))
    private void showHitboxes$onTail(
            Entity entity, float partialTicks, boolean isServerEntity, CallbackInfo ci
    ) {
        if (entity instanceof MultipartEntity multipartEntity) {
            for (var subPart : multipartEntity.getSubParts()) {
                var offset = subPart.getPosition(partialTicks).subtract(subPart.position());
                Gizmos.cuboid(
                        subPart.getBoundingBox().move(offset),
                        GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.0F, 0.5F, 1.0F))
                );
            }
        }
    }
}
