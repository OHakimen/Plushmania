package com.roseisproot.plushmania.client.renderers.layers;

import com.haki.rosarium.common.utils.ColorUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

public class StitchesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    PlayerModel model;

    boolean isSlim;
    public StitchesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, boolean isSlim) {
        super(renderer);
        this.isSlim = isSlim;
        model = renderer.getModel();
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer abstractClientPlayer, float v, float v1, float partialTicks, float v3, float v4, float v5) {

        PlushieData data = abstractClientPlayer.getData(DataAttachmentRegister.PLUSHIE);



        if(data.isPlushie()){
            ResourceLocation seamTexture = Plushmania.modLoc("textures/entity/plush/seams.png");

            if(isSlim){
                seamTexture = Plushmania.modLoc("textures/entity/plush/seams_slim.png");
            }

            int color = ColorUtils.packRGB(new int[]{255 - (int)(data.sogPercentage() * 100), 255 - (int)(data.sogPercentage() * 100), 255 - (int)(data.sogPercentage() * 50)});

            VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(seamTexture,false));

            int i = getOverlayCoords(abstractClientPlayer, 0);
            model.renderToBuffer(poseStack, consumer, packedLight, i, (0xff0052 & color)  | 0xff000000);
        }
    }
}
