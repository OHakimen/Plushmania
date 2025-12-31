package com.roseisproot.plushmania.client.renderers.bewlr;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.client.models.PlushieModel;
import com.roseisproot.plushmania.client.models.SlimPlushieModel;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PlushmaniaBEWLR extends BlockEntityWithoutLevelRenderer {

    SlimPlushieModel slimPlushieModel;
    PlushieModel plushieModel;

    public PlushmaniaBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

        ModelPart slimRoot = SlimPlushieModel.createBodyLayer().bakeRoot();
        ModelPart root = PlushieModel.createBodyLayer().bakeRoot();

        slimPlushieModel = new SlimPlushieModel(slimRoot);
        plushieModel = new PlushieModel(root);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (stack.is(ItemRegister.PLUSHIE.get())) {
            CompoundTag tag = null;
            AtomicReference<GameProfile> gameProfile = new AtomicReference<>();
            boolean isSheared = false;
            if(data != null) {
                tag = data.copyTag();

                if (tag.contains("uuid")) {
                    UUID profileId = UUIDUtil.uuidFromIntArray(tag.getIntArray("uuid"));
                    String username = tag.getString("name");
                    String texture = tag.getString("texture");
                    gameProfile.set(new GameProfile(profileId, username));
                    gameProfile.get().getProperties().put("textures", new Property("textures", texture));
                }
               tag.getBoolean("isSheared");
            }

            GameProfile profile = gameProfile.get();

            if (profile == null) {
                profile = UUIDUtil.createOfflineProfile("Steve");
            }


            if (profile != null) {

                PlayerSkin playerSkin = Minecraft.getInstance().getSkinManager().getInsecureSkin(
                        profile
                );


                poseStack.pushPose();

                ResourceLocation resourceLocation = !isSheared ? playerSkin.texture() : Plushmania.modLoc("textures/base_plush.png");

                VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(resourceLocation));



                switch (displayContext){

                    case GUI -> {
                        poseStack.translate(0.55, 0.85, 0.5);
                        poseStack.scale(-0.5f, -0.5f, -0.5f);
                        poseStack.mulPose(Axis.XP.rotationDegrees(30));
                        poseStack.mulPose(Axis.YP.rotationDegrees(45));
                    }

                    case HEAD -> {
                        poseStack.translate(0.5, 2.0, 0.5);
                        poseStack.scale(-0.75f, -0.75f, -0.75f);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }

                    case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                        poseStack.translate(0.5, 0.65, 1.25);
                        poseStack.scale(-0.5f, -0.5f, -0.5f);
                        poseStack.mulPose(Axis.XP.rotationDegrees(90));
                    }

                    case null, default -> {
                        if (profile != null && profile.getName().toLowerCase().equals("dinnerbone")) {
                            poseStack.translate(0.5, -0.1, 0.5);
                            poseStack.scale(-0.5f, 0.5f, -0.5f);
                        } else {
                            poseStack.translate(0.5, 1.1, 0.5);
                            poseStack.scale(-0.5f, -0.5f, -0.5f);
                        }
                    }
                }

                if(playerSkin.model().equals(PlayerSkin.Model.SLIM)) {
                    slimPlushieModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 0xffffffff);
                }else{
                    plushieModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 0xffffffff);

                }poseStack.popPose();
            }

            super.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
