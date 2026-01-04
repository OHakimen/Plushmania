package com.roseisproot.plushmania.client.renderers.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.entity.ThrowNeedleEntity;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL32;

public class ThrowNeedleEntityRenderer extends EntityRenderer<ThrowNeedleEntity> {
    public ThrowNeedleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowNeedleEntity throwNeedleEntity) {
        return ResourceLocation.withDefaultNamespace("empty");
    }

    @Override
    public void render(ThrowNeedleEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, p_entity.yRotO, p_entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTick, p_entity.xRotO, p_entity.getXRot()) + 45));


        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        poseStack.scale(2,2,2);


        Minecraft.getInstance().getItemRenderer().renderStatic(
                ItemRegister.NEEDLE.get().getDefaultInstance(),
                ItemDisplayContext.FIXED,
                packedLight,
                0,
                poseStack,
                bufferSource,
                Minecraft.getInstance().level,
                0
        );

        poseStack.popPose();
    }
}
