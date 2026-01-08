package com.roseisproot.plushmania.client.events;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

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

        ItemProperties.register(ItemRegister.SEAMSTRESS_NEEDLE.get(), Plushmania.modLoc("state"), (itemStack, clientLevel, livingEntity, i) -> {
            CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
            if(data != null) {
                CompoundTag tag = data.copyTag();

                if(tag.contains("Charged")){
                    return tag.getBoolean("Charged") ? 1 : 0;
                }
            }
            return 0;
        });
    }

    @SubscribeEvent
    public static void registerColors(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, i) -> {

            DyedItemColor color = itemStack.get(DataComponents.DYED_COLOR);

            if(i == 1){
                int rgb = 0xff0052;
                if(color != null){
                    rgb = color.rgb();
                }


                return rgb | 0xff000000;
            }

            return 0xffffffff;
        }, ItemRegister.SEAMSTRESS_NEEDLE::get);
    }
}
