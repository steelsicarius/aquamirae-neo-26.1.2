package dev.obscuria.aquamirae.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GhostShineParticle extends SingleQuadParticle {

    private final SpriteSet spriteSet;
    private final float angularAcceleration;
    private float angularVelocity;

    protected GhostShineParticle(
            ClientLevel level,
            double x, double y, double z,
            double vx, double vy, double vz,
            SpriteSet spriteSet
    ) {
        super(level, x, y, z, 0, 0, 0, spriteSet.first());
        this.spriteSet = spriteSet;
        this.quadSize *= 0.8f;
        this.lifetime = 30;
        this.gravity = -0.1f;
        this.hasPhysics = false;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.angularVelocity = 0.1f;
        this.angularAcceleration = 0.01f;
        this.setSpriteFromAge(spriteSet);
        this.setSize(0.2f, 0.2f);
    }

    @Override
    public int getLightCoords(float partialTick) {
        return 15728880;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
        if (!this.removed) this.setSprite(this.spriteSet.get((this.age / 3) % 12 + 1, 12));
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(
                SimpleParticleType type, ClientLevel level,
                double x, double y, double z,
                double vx, double vy, double vz,
                RandomSource random
        ) {
            return new GhostShineParticle(level, x, y, z, vx, vy, vz, this.spriteSet);
        }
    }
}
