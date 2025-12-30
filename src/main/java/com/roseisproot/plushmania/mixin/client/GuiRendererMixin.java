package com.roseisproot.plushmania.mixin.client;

import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiRendererMixin {

    @Inject(method = "renderFoodLevel", at = @At("HEAD"), cancellable = true)
    public void renderFoodLevel(GuiGraphics p_283143_, CallbackInfo ci) {
        if(Minecraft.getInstance().player.getData(DataAttachmentRegister.PLUSHIE.get()).isPlushie()){
            ci.cancel();
        }
    }
}
