package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import com.geckolib.model.DefaultedGeoModel;

public class ModelScuttler extends DefaultedGeoModel<Scuttler> {
    // TODO(port): the Scuttler molang walk variable is now fed per-frame in the renderer (addRenderData),
    // since GeckoLib 5.5.2 GeoModel no longer exposes applyMolangQueries.

    public ModelScuttler() {
        super(Aquamirae.id("scuttler"));
    }


    @Override
    protected String subtype() {
        return "entity";
    }

}
