package dev.obscuria.aquamirae.client.model;

import com.google.common.base.Suppliers;
import dev.obscuria.aquamirae.client.animation.AnimPipeline;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.function.Supplier;

public abstract class AnimatedEntityModel<S extends LivingEntityRenderState> extends EntityModel<S> {

    protected final ModelPart main;
    protected final Supplier<AnimPipeline<S>> animations;

    protected AnimatedEntityModel(ModelPart root, String rootName) {
        super(root);
        this.main = root.getChild(rootName);
        this.animations = Suppliers.memoize(this::createAnimations);
    }

    @Override
    public void setupAnim(S state) {
        var context = new AnimPipeline.Context(
                state.walkAnimationPos, state.walkAnimationSpeed,
                state.ageInTicks, state.yRot, state.xRot, state.partialTick);
        this.animations.get().evaluate(state, context);
    }

    protected abstract AnimPipeline<S> createAnimations();
}
