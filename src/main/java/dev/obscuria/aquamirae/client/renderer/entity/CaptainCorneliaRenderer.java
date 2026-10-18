package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelCaptainCornelia;
import dev.obscuria.aquamirae.client.renderer.entity.layer.CaptainCorneliaWeaponLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.CaptainCorneliaRenderState;
import dev.obscuria.aquamirae.common.entity.CaptainCorneliaBoss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.item.ItemDisplayContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class CaptainCorneliaRenderer<T extends CaptainCorneliaBoss> extends MobRenderer<T, CaptainCorneliaRenderState, ModelCaptainCornelia> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/captain_cornelia.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/captain_cornelia_overlay.png");

    public CaptainCorneliaRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelCaptainCornelia(context.bakeLayer(AquamiraeModelLayers.CAPTAIN_CORNELIA)), 0.5f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
        this.addLayer(new CaptainCorneliaWeaponLayer(this));
    }

    @Override
    public CaptainCorneliaRenderState createRenderState() {
        return new CaptainCorneliaRenderState();
    }

    @Override
    public void extractRenderState(T entity, CaptainCorneliaRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.weaponSwitchState = entity.weaponSwitchState;
        state.pullAttackState = entity.pullAttackState;
        state.swingAttackState = entity.swingAttackState;
        state.thurstAttackState = entity.thurstAttackState;
        state.deathState = entity.deathState;

        ItemStack mainHand = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if (mainHand.isEmpty()) {
            state.mainHandItem.clear();
        } else {
            this.itemModelResolver.updateForLiving(state.mainHandItem, mainHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
        }
        ItemStack offHand = entity.getItemBySlot(EquipmentSlot.OFFHAND);
        if (offHand.isEmpty()) {
            state.offHandItem.clear();
        } else {
            this.itemModelResolver.updateForLiving(state.offHandItem, offHand, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
        }
    }

    @Override
    public Identifier getTextureLocation(CaptainCorneliaRenderState state) {
        return TEXTURE;
    }
}
