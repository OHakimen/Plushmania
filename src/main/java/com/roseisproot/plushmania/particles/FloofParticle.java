package com.roseisproot.plushmania.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Random;

public class FloofParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public FloofParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet sprites, double vX, double vY, double vZ) {
        super(pLevel, pX, pY, pZ);

        this.xd = vX;
        this.yd = vY;
        this.zd = vZ;
        this.quadSize *= pLevel.getRandom().nextFloat() * 2;

        this.gravity = 0.5f;
        this.lifetime = 300;

        this.sprites = sprites;
        this.sprite = sprites.get(this.level.random);
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize = Math.clamp(this.quadSize - 0.001f, 0, 2);
    }

    protected void renderRotatedQuad(VertexConsumer p_345690_, Camera p_344809_, Quaternionf p_344798_, float p_345099_) {
        Vec3 vec3 = p_344809_.getPosition();
        float f = (float)(Mth.lerp((double)p_345099_, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)p_345099_, this.yo, this.y) - vec3.y() + 0.01 + (this.hashCode() % 1000)/100000f);
        float f2 = (float)(Mth.lerp((double)p_345099_, this.zo, this.z) - vec3.z());


        this.renderRotatedQuad(p_345690_, p_344798_, f, f1, f2, p_345099_);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        this.getFacingCameraMode().setRotation(quaternionf, renderInfo, partialTicks);
        if (this.roll != 0.0F) {
            quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }

        if(this.onGround){
            quaternionf.rotationXYZ((float) -(Math.PI * .5), 0, 0);
        }

        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FloofParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public FloofParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double vX, double vY, double vZ) {
            Random r = new Random();

            FloofParticle particle =  new FloofParticle(clientLevel, x, y, z, sprites, vX, vY, vZ);

            particle.xd *= 0.1f;
            particle.yd *= 0.1f;
            particle.zd *= 0.1f;

            return particle;
        }
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
