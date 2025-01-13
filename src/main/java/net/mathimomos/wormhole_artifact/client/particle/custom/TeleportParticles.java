package net.mathimomos.wormhole_artifact.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class TeleportParticles extends TextureSheetParticle {
    private final float initialAngle;

    protected TeleportParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.xd = 0;
        this.yd = 0.2F;
        this.zd = 0;
        this.quadSize *= 0.75f;
        this.lifetime = 20;
        this.setSpriteFromAge(pSprites);

        this.rCol = 1F;
        this.gCol = 1F;
        this.bCol = 1F;

        this.initialAngle = pLevel.random.nextFloat() * (float) (2 * Math.PI);
    }

    @Override
    public void tick() {
        super.tick();

        float oscillationSpeed = 0.25F;
        float amplitude = 0.2F;


        this.xd = amplitude * (float) Math.sin(this.age * oscillationSpeed + initialAngle);
        this.zd = amplitude * (float) Math.cos(this.age * oscillationSpeed + initialAngle);

        this.x += this.xd;
        this.z += this.zd;
        this.y += this.yd;

        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (float) Math.pow(1 - (age / (float) lifetime), 2);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet pSprites;

        public Provider(SpriteSet pSprites) {
            this.pSprites = pSprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2,
                                                 double v3, double v4, double v5) {
            return new TeleportParticles(clientLevel, v, v1, v2, v3, v4, v5, pSprites);
        }
    }
}
