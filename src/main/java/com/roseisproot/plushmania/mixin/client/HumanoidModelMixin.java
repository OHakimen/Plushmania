package com.roseisproot.plushmania.mixin.client;

import com.haki.rosarium.common.utils.ColorUtils;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(LivingEntityRenderer.class)
public class HumanoidModelMixin<T extends LivingEntity> {

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void render(EntityModel instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i1, int i2, int i3, @Local(argsOnly = true) T entity) {

        PlushieData data = entity.getData(DataAttachmentRegister.PLUSHIE.get());
        if(data.isPlushie()){
            instance.renderToBuffer(poseStack, vertexConsumer, i1, i2, ColorUtils.packRGB(new int[]{255 - (int)(data.sogPercentage() * 100), 255 - (int)(data.sogPercentage() * 100), 255 - (int)(data.sogPercentage() * 50)}) |  0xFF000000);
        }else{
            instance.renderToBuffer(poseStack, vertexConsumer, i1, i2, i3);
        }
    }
}
