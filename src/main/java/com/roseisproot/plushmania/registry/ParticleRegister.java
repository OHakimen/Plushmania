package com.roseisproot.plushmania.registry;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.item.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleRegister {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Plushmania.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOOF =
            PARTICLE_TYPES.register("floof", () -> new SimpleParticleType(true));


    public static void register(IEventBus bus){
        PARTICLE_TYPES.register(bus);
    }
}
