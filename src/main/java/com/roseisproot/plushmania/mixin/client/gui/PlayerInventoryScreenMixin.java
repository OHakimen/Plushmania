package com.roseisproot.plushmania.mixin.client.gui;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.client.widgets.ToggleIconButton;
import com.roseisproot.plushmania.utils.WidgetUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InventoryScreen.class)
public class PlayerInventoryScreenMixin extends Screen {

    protected PlayerInventoryScreenMixin(Component title) {
        super(title);
    }

    @Inject(at=@At("RETURN"), method = "init")
    public void init(CallbackInfo ci) {


        addRenderableWidget(WidgetUtils.addWidget((InventoryScreen) (Object) this));
    }
}
