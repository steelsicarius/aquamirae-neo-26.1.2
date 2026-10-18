package dev.obscuria.aquamirae.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class GhostParticle extends SingleQuadParticle {

    private final SpriteSet spriteSet;

    protected GhostParticle(
            ClientLevel level,
            double x, double y, double z,
            double vx, double vy, double vz,
            SpriteSet spriteSet
    ) {
        super(level, x, y, z, 0, 0, 0, spriteSet.first());
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 1.3f;
        this.lifetime = 40;
        this.gravity = -0.3f;
        this.hasPhysics = false;
        this.xd = vx * 0.1;
        this.yd = vy * 0.1;
        this.zd = vz * 0.1;
        this.setSpriteFromAge(spriteSet);
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
        if (!this.removed) this.setSprite(this.spriteSet.get((this.age / 3) % 12 + 1, 12));
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(
                SimpleParticleType type, ClientLevel level,
                double x, double y, double z,
                double vx, double vy, double vz,
                RandomSource random
        ) {
            return new GhostParticle(level, x, y, z, vx, vy, vz, spriteSet);
        }
    }
}
