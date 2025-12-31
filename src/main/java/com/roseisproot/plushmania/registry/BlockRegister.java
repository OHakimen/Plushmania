package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.block.PlushieBlock;
import com.roseisproot.plushmania.blockentities.PlushieBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCK_ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.BLOCK, Plushmania.MODID);

    public static final DeferredHolder<Block, PlushieBlock> PLUSHIE = BLOCK_ENTITY_TYPE.register("plushie", PlushieBlock::new);

    public static void register(IEventBus bus){
        BLOCK_ENTITY_TYPE.register(bus);
    }
}
