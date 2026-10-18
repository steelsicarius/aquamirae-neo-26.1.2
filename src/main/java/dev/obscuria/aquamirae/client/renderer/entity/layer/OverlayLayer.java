package dev.obscuria.aquamirae.client.renderer.entity.layer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class OverlayLayer<S extends EntityRenderState, M extends EntityModel<S>> extends EyesLayer<S, M> {

    private final RenderType renderType;

    public OverlayLayer(RenderLayerParent<S, M> parent, Identifier texture) {
        super(parent);
        this.renderType = RenderTypes.eyes(texture);
    }

    @Override
    public RenderType renderType() {
        return this.renderType;
    }
}
