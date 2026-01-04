package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.blockentities.PlushieBlockEntity;
import com.roseisproot.plushmania.entity.ThrowNeedleEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Plushmania.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThrowNeedleEntity>> THROW_NEEDLE = ENTITY_TYPES.register("throw_needle", () ->
            EntityType.Builder.of(ThrowNeedleEntity::new, MobCategory.MISC).sized(0.5f,0.5f)
                    .build(ResourceLocation.fromNamespaceAndPath(Plushmania.MODID, "chain_prison").toString()));

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
