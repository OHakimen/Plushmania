package com.roseisproot.plushmania.client.events;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Plushmania.MODID, value = Dist.CLIENT)
public class AddPropertiesEvent {

    @SubscribeEvent
    public static void onAddProperties(final FMLClientSetupEvent event) {
        ItemProperties.register(ItemRegister.SPOOL_OF_THREAD.get(), Plushmania.modLoc("state"), (itemStack, clientLevel, livingEntity, i) -> {
            CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
            if(data != null) {
                CompoundTag tag = data.copyTag();

                if(tag.contains("Charges")){
                    int charges = tag.getInt("Charges");

                    return charges > 0 ? (3 - (int)((charges / 64f) * 4)) : 4;
                }
            }
            return 0;
        });
    }
}
