package com.roseisproot.plushmania.client.events;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.client.itemextensions.PlushieClientExtensions;
import com.roseisproot.plushmania.client.renderers.blockentity.PlushieBlockEntityRenderer;
import com.roseisproot.plushmania.particles.FloofParticle;
import com.roseisproot.plushmania.registry.BlockEntityRegister;
import com.roseisproot.plushmania.registry.ItemRegister;
import com.roseisproot.plushmania.registry.ParticleRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Plushmania.MODID, value = Dist.CLIENT)
public class RegisterRenderersEvent {

    @SubscribeEvent
    public static void onAddProperties(final EntityRenderersEvent.RegisterRenderers event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegister.FLOOF.get(),
                FloofParticle.FloofParticleProvider::new);

        event.registerBlockEntityRenderer(BlockEntityRegister.PLUSH.get(), PlushieBlockEntityRenderer::new);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                // The only instance of our IClientItemExtensions, and as such, the only instance of our BEWLR.
                new PlushieClientExtensions(),
                // A vararg list of items that use this BEWLR.
                ItemRegister.PLUSHIE.get()
        );
    }
}
