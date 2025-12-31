package com.roseisproot.plushmania.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.client.models.SlimPlushieModel;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

@OnlyIn(Dist.CLIENT)
public class BackItemRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {



    public BackItemRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);


    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer abstractClientPlayer, float v, float v1, float partialTicks, float v3, float v4, float v5) {
        Optional<ItemStack> neddleItem = abstractClientPlayer.getInventory().items.stream().filter((e) -> e.is(ItemRegister.NEEDLE.get())).findFirst();
        Optional<ItemStack> scissorBlade = abstractClientPlayer.getInventory().items.stream().filter((e) -> e.is(ItemRegister.SCISSOR_BLADE.get())).findFirst();
        Optional<ItemStack> spoolOfThreadItem = abstractClientPlayer.getInventory().items.stream().filter((e) -> e.is(ItemRegister.SPOOL_OF_THREAD.get())).filter(stack -> {
            CustomData stackData = stack.get(DataComponents.CUSTOM_DATA);
            return stackData != null && stackData.copyTag().contains("Charges") && stackData.copyTag().getInt("Charges") > 0;
        }).min((o1, o2) -> {
            CustomData stackData1 = o1.get(DataComponents.CUSTOM_DATA);
            CustomData stackData2 = o2.get(DataComponents.CUSTOM_DATA);

            if (stackData1 != null && stackData2 != null) {
                CompoundTag data1 = stackData1.copyTag();
                CompoundTag data2 = stackData2.copyTag();

                return Integer.compare(data1.getInt("Charges"), data2.getInt("Charges"));
            }

            return 0;
        });

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

        if (scissorBlade.isPresent()) {
            poseStack.pushPose();
            ItemStack scissor = scissorBlade.get();

            int slots = abstractClientPlayer.getInventory().findSlotMatchingItem(scissor);
            boolean isHotbar = slots >= 0 && slots <= 8;

            if (!abstractClientPlayer.getItemInHand(InteractionHand.MAIN_HAND).equals(scissor) && isHotbar) {
                double d0 = Mth.lerp((double) partialTicks, abstractClientPlayer.xCloakO, abstractClientPlayer.xCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.xo, abstractClientPlayer.getX());
                double d1 = Mth.lerp((double) partialTicks, abstractClientPlayer.yCloakO, abstractClientPlayer.yCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.yo, abstractClientPlayer.getY());
                double d2 = Mth.lerp((double) partialTicks, abstractClientPlayer.zCloakO, abstractClientPlayer.zCloak) - Mth.lerp((double) partialTicks, abstractClientPlayer.zo, abstractClientPlayer.getZ());


                poseStack.translate(0.0F, (abstractClientPlayer.isCrouching() ? 0.4 : 0.25), (abstractClientPlayer.isCrouching() ? 0.3 : 0.25));
                poseStack.scale(1.5f, 1.5f, 1.5f);

                poseStack.mulPose(Axis.XP.rotationDegrees((float) d0 + (abstractClientPlayer.isCrouching() ? 35f : 10)));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) d1 + 15));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) d2));
                Minecraft.getInstance().getItemRenderer()
                        .renderStatic(scissor,
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
