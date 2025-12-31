package com.roseisproot.plushmania.client.renderers.blockentity;

import com.haki.rosarium.common.utils.ColorUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.blockentities.PlushieBlockEntity;
import com.roseisproot.plushmania.client.models.PlushieModel;
import com.roseisproot.plushmania.client.models.SlimPlushieModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieBlockEntity> {

    SlimPlushieModel slimPlushieModel;
    PlushieModel plushieModel;

    BlockEntityRendererProvider.Context context;

    public PlushieBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;

        ModelPart slimRoot = SlimPlushieModel.createBodyLayer().bakeRoot();
        ModelPart root = PlushieModel.createBodyLayer().bakeRoot();

        slimPlushieModel = new SlimPlushieModel(slimRoot);
        plushieModel = new PlushieModel(root);
    }

    @Override
    public void render(PlushieBlockEntity plushieBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        GameProfile profile = plushieBlockEntity.getGameProfile();

        if(profile == null){
            profile = UUIDUtil.createOfflineProfile("Steve");
        }


        if(profile != null){

            PlayerSkin playerSkin = Minecraft.getInstance().getSkinManager().getInsecureSkin(
                    profile
            );


            poseStack.pushPose();

            ResourceLocation resourceLocation = !plushieBlockEntity.isSheared() ? playerSkin.texture() : Plushmania.modLoc("textures/base_plush.png");

            VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(resourceLocation));


            if(profile != null && profile.getName().toLowerCase().equals("dinnerbone")){
                poseStack.translate(0.5,-0.1,0.5);
                poseStack.scale(-0.75f,0.75f,-0.75f);
            }else{
                poseStack.translate(0.5,1.1,0.5);
                poseStack.scale(-0.75f,-0.75f,-0.75f);
            }
            poseStack.mulPose(Axis.YP.rotation(plushieBlockEntity.getDirection()));


            int rgba = 0xffffffff;
            if(profile != null && profile.getName().toLowerCase().equals("jeb_")){
                rgba = ColorUtils.HSVtoRGB(((System.nanoTime() + pPartialTick) % 1000f)/1000f, 1,1) | 0xff000000;
            }

            if(playerSkin.model().equals(PlayerSkin.Model.SLIM)) {
                slimPlushieModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, rgba);
            }else{
                plushieModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, rgba);
            }
            poseStack.popPose();
        }

    }
}
