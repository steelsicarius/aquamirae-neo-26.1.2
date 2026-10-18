package dev.obscuria.aquamirae.client.renderer.entity.state;

import dev.obscuria.aquamirae.common.entity.animation.AnimState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class CaptainCorneliaRenderState extends HumanoidRenderState {
    public AnimState weaponSwitchState;
    public AnimState pullAttackState;
    public AnimState swingAttackState;
    public AnimState thurstAttackState;
    public AnimState deathState;
    public final ItemStackRenderState mainHandItem = new ItemStackRenderState();
    public final ItemStackRenderState offHandItem = new ItemStackRenderState();
}
