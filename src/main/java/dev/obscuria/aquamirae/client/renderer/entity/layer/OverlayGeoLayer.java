package dev.obscuria.aquamirae.client.renderer.entity.layer;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.TextureLayerGeoLayer;

import java.util.function.Function;

/// GeckoLib render layer rendering the entity texture with an `_overlay` suffix as an emissive pass.
/// Ported to the 5.5.2 render-state based layer API (extends TextureLayerGeoLayer).
public class OverlayGeoLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TextureLayerGeoLayer<T, O, R> {

    public OverlayGeoLayer(GeoRenderer<T, O, R> renderer, Function<Identifier, RenderType> renderTypeFunction) {
        super(renderer, Identifier.withDefaultNamespace("textures/misc/white.png"), renderTypeFunction);
    }

    @Override
    protected Identifier getTextureResource(R renderState) {
        return getRenderer().getTextureLocation(renderState).withPath(OverlayGeoLayer::pathToOverlay);
    }

    protected static String pathToOverlay(String path) {
        return path.replace(".png", "_overlay.png");
    }
}
