package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.item.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Plushmania.MODID);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Plushmania.MODID);


    public static final DeferredHolder<Item, Item> NEEDLE = ITEMS.register("needle", NeedleItem::new);
    public static final DeferredHolder<Item, Item> SPOOL_OF_THREAD = ITEMS.register("spool_of_thread", SpoolThreadItem::new);
    public static final DeferredHolder<Item, Item> SEAMSTRESS_NEEDLE = ITEMS.register("seamstress_needle", SeamstressNeedleItem::new);
    public static final DeferredHolder<Item, Item> LIVING_SILK = ITEMS.register("living_silk", LivingSilkItem::new);
    public static final DeferredHolder<Item, Item> SCISSOR_BLADE = ITEMS.register("scissor_blade", ScissorBladeItem::new);

    public static final DeferredHolder<Item, Item> PLUSHIE = ITEMS.register("plushie", () -> new PlushieBlockItem(BlockRegister.PLUSHIE.get(), new Item.Properties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(SEAMSTRESS_NEEDLE.get()::getDefaultInstance)
            .title(Component.translatable("tabs.plushmania.main.title"))
            .displayItems((itemDisplayParameters, output) -> {
                ITEMS.getEntries().forEach(item -> {
                    output.accept(item.get());
                });
            })
            .build());

    public static void register(IEventBus bus){
        ITEMS.register(bus);
        TABS.register(bus);
    }
}
