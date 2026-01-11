package com.roseisproot.plushmania.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ToggleIconButton extends AbstractButton {

    List<ResourceLocation> icons;

    boolean toggled = false;

    BiConsumer<ToggleIconButton, Boolean> onToggle = ((toggleIconButton, aBoolean) -> {});

    public ToggleIconButton(int x, int y, int width, int height, Component message, List<ResourceLocation> icons) {
        super(x, y, width, height, message);
        this.icons = icons;
    }

    @Override
    public void onPress() {
        toggled = !toggled;
        onToggle.accept(this, toggled);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(icons.get(toggled ? 1 : 0), this.getX() + width/10,this.getY() + height/10, 0,0, width - (width/10 * 2),height - (height/10 * 2), width - (width/10 * 2),height - (height/10 * 2));

        stack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public boolean isToggled() {
        return toggled;
    }

    public ToggleIconButton setToggled(boolean toggled) {
        this.toggled = toggled;
        return this;
    }

    public BiConsumer<ToggleIconButton,Boolean> getOnToggle() {
        return onToggle;
    }

    public ToggleIconButton setOnToggle(BiConsumer<ToggleIconButton,Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
}
