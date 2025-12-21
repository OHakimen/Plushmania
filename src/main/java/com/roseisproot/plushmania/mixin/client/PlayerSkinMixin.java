package com.roseisproot.plushmania.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerRenderer.class)
public class PlayerSkinMixin {

    @Inject(at=@At("RETURN"), method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", cancellable = true)
    private void getTextureLocation(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(entity.getData(DataAttachmentRegister.PLUSHIE.get()) ? Plushmania.modLoc("textures/img.png") : cir.getReturnValue());
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/PlayerSkin;texture()Lnet/minecraft/resources/ResourceLocation;", shift = At.Shift.BY, by = 1), method = "renderHand")
    private void renderHand(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, ModelPart rendererArm, ModelPart rendererArmwear, CallbackInfo ci, @Local LocalRef<ResourceLocation> location) {
        if(player.getData(DataAttachmentRegister.PLUSHIE.get())) {
            location.set(Plushmania.modLoc("textures/img.png"));
        }
    }

    @Inject(at = @At("RETURN"), method = "getArmPose", cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir){
        if(player.getItemInHand(hand) instanceof ItemStack stack && stack.is(ItemRegister.SCISSOR_BLADE.get())){
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_CHARGE);
        }
    }
}
