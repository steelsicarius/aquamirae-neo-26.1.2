package dev.obscuria.aquamirae.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class ModelHangingChest extends Model<Void> {

    private final ModelPart root;

    public ModelHangingChest(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(26, 21).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(Void state) {}
}