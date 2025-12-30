package com.roseisproot.plushmania.client.events;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.particles.FloofParticle;
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

@EventBusSubscriber(modid = Plushmania.MODID, value = Dist.CLIENT)
public class RegisterRenderersEvent {

    @SubscribeEvent
    public static void onAddProperties(final EntityRenderersEvent.RegisterRenderers event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegister.FLOOF.get(),
                FloofParticle.FloofParticleProvider::new);
    }
}
