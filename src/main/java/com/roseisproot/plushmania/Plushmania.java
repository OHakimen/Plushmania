package com.roseisproot.plushmania;


import com.mojang.logging.LogUtils;
import com.roseisproot.plushmania.registry.Registration;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Plushmania.MODID)
public class Plushmania {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "plushmania";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation modLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public Plushmania(IEventBus modEventBus, ModContainer modContainer) {
        Registration.register(modEventBus);
    }
}
