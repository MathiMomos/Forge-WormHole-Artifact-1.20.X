package net.mathimomos.wormhole_artifact.client.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import org.joml.Vector3f;

import java.util.Random;

public class TeleportWaveParticles extends TextureSheetParticle {

    protected TeleportWaveParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.xd = 0;
        this.yd = 0.05F;
        this.zd = 0;
        this.quadSize = 1f;
        this.lifetime = new Random().nextInt(10 + 1) + 10;
        this.setSpriteFromAge(pSprites);

        this.rCol = 1F;
        this.gCol = 1F;
        this.bCol = 1F;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float progress = (this.age + partialTicks) / this.lifetime;
        float smoothProgress = (float) Math.pow(progress, 0.5F);
        return this.quadSize * 3 * smoothProgress;
    }

    private void fadeOut() {
        this.alpha = (float) Math.pow(1 - (age / (float) lifetime), 3);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();

        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());

        float size = this.getQuadSize(partialTicks);

        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-size, 0, -size),
                new Vector3f(-size, 0, size),
                new Vector3f(size, 0, size),
                new Vector3f(size, 0, -size)
        };

        Vector3f[] bottomVertices = new Vector3f[]{
                new Vector3f(-size, 0, -size),
                new Vector3f(-size, 0, size),
                new Vector3f(size, 0, size),
                new Vector3f(size, 0, -size)
        };

        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.add(x, y, z);

            Vector3f vertexbottom = bottomVertices[i];
            vertexbottom.add(x, y - 0.01f, z);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();

        int light = this.getLightColor(partialTicks);

        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(maxU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(maxU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(minU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(minU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();

        buffer.vertex(bottomVertices[3].x(), bottomVertices[3].y(), bottomVertices[3].z()).uv(minU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(bottomVertices[2].x(), bottomVertices[2].y(), bottomVertices[2].z()).uv(minU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(bottomVertices[1].x(), bottomVertices[1].y(), bottomVertices[1].z()).uv(maxU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(bottomVertices[0].x(), bottomVertices[0].y(), bottomVertices[0].z()).uv(maxU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
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
            return new TeleportWaveParticles(clientLevel, v, v1, v2, v3, v4, v5, pSprites);
        }
    }
}
