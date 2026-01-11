package com.roseisproot.plushmania.utils;

import com.haki.rosarium.common.packets.SyncVariantChangeC2S;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.client.widgets.ToggleIconButton;
import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.packets.SyncBackRenderingC2S;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class WidgetUtils {



    public static AbstractWidget addWidget(InventoryScreen screen){


        PlushieData data = Minecraft.getInstance().player.getData(DataAttachmentRegister.PLUSHIE.get());
        

        return new ToggleIconButton(screen.getGuiLeft() - 18,screen.getGuiTop() + 1, 18,18, Component.literal(""), List.of(
                Plushmania.modLoc("textures/gui/render_toggle_on.png"),
                Plushmania.modLoc("textures/gui/render_toggle_off.png")
        )).setOnToggle((toggleIconButton, toggleState) -> {
            PacketDistributor.sendToServer(new SyncBackRenderingC2S(toggleState));
        }).setToggled(data.shouldRender());
    }
}
