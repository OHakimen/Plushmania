package com.roseisproot.plushmania.client.itemextensions;

import com.roseisproot.plushmania.client.renderers.bewlr.PlushmaniaBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class PlushieClientExtensions implements IClientItemExtensions {

    PlushmaniaBEWLR bewlr;

    public PlushieClientExtensions() {
        this.bewlr = new PlushmaniaBEWLR();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return bewlr;
    }


}
