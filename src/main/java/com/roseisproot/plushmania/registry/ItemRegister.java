package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.item.NeedleItem;
import com.roseisproot.plushmania.item.ScissorBladeItem;
import com.roseisproot.plushmania.item.SeamstressNeedleItem;
import com.roseisproot.plushmania.item.SpoolThreadItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Plushmania.MODID);

    public static final DeferredHolder<Item, Item> NEEDLE = ITEMS.register("needle", NeedleItem::new);
    public static final DeferredHolder<Item, Item> SEAMSTRESS_NEEDLE = ITEMS.register("seamstress_needle", SeamstressNeedleItem::new);
    public static final DeferredHolder<Item, Item> SPOOL_OF_THREAD = ITEMS.register("spool_of_thread", SpoolThreadItem::new);
    public static final DeferredHolder<Item, Item> SCISSOR_BLADE = ITEMS.register("scissor_blade", ScissorBladeItem::new);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
