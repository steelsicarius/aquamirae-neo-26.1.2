package dev.obscuria.aquamirae.client.renderer;

import dev.obscuria.aquamirae.client.renderer.entity.layer.ArmorAutoGlowingLayer;
import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import com.geckolib.renderer.GeoArmorRenderer;

import java.util.List;

public class AquamiraeArmorRenderer extends GeoArmorRenderer<AquamiraeArmorItem, HumanoidRenderState> {

    public AquamiraeArmorRenderer(AquamiraeArmorItem armorItem) {
        super(armorItem);
        this.withRenderLayer(new ArmorAutoGlowingLayer(this, armorItem));
    }

    @Override
    public List<ArmorSegment> getSegmentsForSlot(HumanoidRenderState renderState, EquipmentSlot slot) {
        // Ported from the old applyBoneVisibilityBySlot override:
        // the leg armor piece also shows the body ("armorBody") bone.
        if (slot == EquipmentSlot.LEGS)
            return List.of(ArmorSegment.LEFT_LEG, ArmorSegment.RIGHT_LEG, ArmorSegment.CHEST);
        return super.getSegmentsForSlot(renderState, slot);
    }
}
