package com.roseisproot.plushmania.mixin;

import com.roseisproot.plushmania.client.BackItemRenderLayer;
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
    }

}
