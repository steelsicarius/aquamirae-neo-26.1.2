package dev.obscuria.aquamirae.client.renderer.entity.state;

import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class EelRenderState extends LivingEntityRenderState {
    public AnimState specialIdleState;
    public AnimState attackState;
    public AnimState roarState;
    public AnimState digState;
    public AnimState deathState;
}
