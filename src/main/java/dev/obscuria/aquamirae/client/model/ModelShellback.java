package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.Shellback;
import com.geckolib.model.DefaultedGeoModel;

public class ModelShellback extends DefaultedGeoModel<Shellback> {
    // TODO(port): the Shellback molang walk variable is now fed per-frame in the renderer (addRenderData),
    // since GeckoLib 5.5.2 GeoModel no longer exposes applyMolangQueries.

    public ModelShellback() {
        super(Aquamirae.id("shellback"));
    }


    @Override
    protected String subtype() {
        return "entity";
    }

}
