package com.roseisproot.plushmania.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.File;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class BackItemRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public BackItemRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer abstractClientPlayer, float v, float v1, float partialTicks, float v3, float v4, float v5) {


        Optional<ItemStack> neddleItem = abstractClientPlayer.getInventory().items.stream().filter((e) -> e.is(ItemRegister.NEEDLE.get())).findFirst();
        Optional<ItemStack> spoolOfThreadItem = abstractClientPlayer.getInventory().items.stream().filter((e) -> e.is(ItemRegister.SPOOL_OF_THREAD.get())).findFirst();

        if (neddleItem.isPresent()) {
            poseStack.pushPose();
            ItemStack needle = neddleItem.get();

            int slots = abstractClientPlayer.getInventory().findSlotMatchingItem(needle);
            boolean isHotbar = slots >= 0 && slots <= 8;

            if (!abstractClientPlayer.getItemInHand(InteractionHand.MAIN_HAND).equals(needle) && isHotbar) {
                double d0 = Mth.lerp((double) partialTicks, abstractClientPlayer.xCloakO, abstractClientPlayer.xCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.xo, abstractClientPlayer.getX());
                double d1 = Mth.lerp((double) partialTicks, abstractClientPlayer.yCloakO, abstractClientPlayer.yCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.yo, abstractClientPlayer.getY());
                double d2 = Mth.lerp((double) partialTicks, abstractClientPlayer.zCloakO, abstractClientPlayer.zCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.zo, abstractClientPlayer.getZ());


                poseStack.translate(0.0F, (abstractClientPlayer.isCrouching() ? 0.4 : 0.25), (abstractClientPlayer.isCrouching() ? 0.3 : 0.15));
                poseStack.scale(1.5f, 1.5f, 1.5f);

                poseStack.mulPose(Axis.XP.rotationDegrees((float) d0 + (abstractClientPlayer.isCrouching() ? 35f : 0)));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) d1 - 90f));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) d2));
                Minecraft.getInstance().getItemRenderer()
                        .renderStatic(needle,
                                ItemDisplayContext.FIXED,
                                packedLight,
                                packedLight,
                                poseStack,
                                multiBufferSource,
                                abstractClientPlayer.level(),
                                0);
            }
            poseStack.popPose();
        }

        if(spoolOfThreadItem.isPresent()) {
            poseStack.pushPose();
            ItemStack spool = spoolOfThreadItem.get();

            if (!abstractClientPlayer.getItemInHand(InteractionHand.MAIN_HAND).equals(spool)) {
                double d0 = Mth.lerp((double) partialTicks, abstractClientPlayer.xCloakO, abstractClientPlayer.xCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.xo, abstractClientPlayer.getX());
                double d1 = Mth.lerp((double) partialTicks, abstractClientPlayer.yCloakO, abstractClientPlayer.yCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.yo, abstractClientPlayer.getY());
                double d2 = Mth.lerp((double) partialTicks, abstractClientPlayer.zCloakO, abstractClientPlayer.zCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.zo, abstractClientPlayer.getZ());


                poseStack.translate(0.0F, (abstractClientPlayer.isCrouching() ? 0.4 : 0.3), (abstractClientPlayer.isCrouching() ? 0.5 : 0.4));
                poseStack.scale(0.75f, 0.75f, 0.75f);

                poseStack.mulPose(Axis.XP.rotationDegrees((float) d0 + (abstractClientPlayer.isCrouching() ? 35f : 0)));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) d1 - 90f));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) d2));
                Minecraft.getInstance().getItemRenderer()
                        .renderStatic(spool,
                                ItemDisplayContext.FIXED,
                                packedLight,
                                packedLight,
                                poseStack,
                                multiBufferSource,
                                abstractClientPlayer.level(),
                                0);
            }
            poseStack.popPose();
        }

    }
}
