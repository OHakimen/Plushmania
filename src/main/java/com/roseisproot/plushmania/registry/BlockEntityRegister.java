package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.blockentities.PlushieBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Plushmania.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlushieBlockEntity>> PLUSH = BLOCK_ENTITY_TYPE.register("plush", () ->
            BlockEntityType.Builder
                    .of(PlushieBlockEntity::new, BlockRegister.PLUSHIE.get())
                    .build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITY_TYPE.register(bus);
    }
}
