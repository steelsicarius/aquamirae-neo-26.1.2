package dev.obscuria.aquamirae.client.renderer.entity.state;

import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class MawRenderState extends LivingEntityRenderState {
    public AnimState attackState;
    public AnimState deathState;
    public final ItemStackRenderState mouthItem = new ItemStackRenderState();
}
