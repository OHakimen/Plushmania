package com.roseisproot.plushmania.mixin.client;

import com.roseisproot.plushmania.client.renderers.layers.BackItemRenderLayer;
import com.roseisproot.plushmania.client.renderers.layers.StitchesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class AttachToPlayerRendererMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void injectAddLayer(EntityRendererProvider.Context context, boolean useSlimModel, CallbackInfo ci){
        PlayerRenderer renderer = ((PlayerRenderer)(Object)this);
        renderer.addLayer(new BackItemRenderLayer(renderer));
        renderer.addLayer(new StitchesLayer(renderer,useSlimModel));
    }



}
