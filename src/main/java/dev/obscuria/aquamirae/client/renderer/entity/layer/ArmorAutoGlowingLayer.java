package dev.obscuria.aquamirae.client.renderer.entity.layer;

import dev.obscuria.aquamirae.client.renderer.AquamiraeArmorRenderer;
import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import org.jetbrains.annotations.Nullable;

public class ArmorAutoGlowingLayer extends AutoGlowingGeoLayer<AquamiraeArmorItem, GeoArmorRenderer.RenderData, HumanoidRenderState> {

    private final AquamiraeArmorItem armorItem;

    public ArmorAutoGlowingLayer(GeoRenderer<AquamiraeArmorItem, GeoArmorRenderer.RenderData, HumanoidRenderState> renderer, AquamiraeArmorItem armorItem) {
        super(renderer);
        this.armorItem = armorItem;
    }

    @Override
    protected @Nullable RenderType getRenderType(HumanoidRenderState renderState) {
        if (!this.armorItem.hasGlowingLayer()) return null;
        return super.getRenderType(renderState);
    }
}
