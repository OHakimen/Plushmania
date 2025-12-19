package com.roseisproot.plushmania.registry;

import net.neoforged.bus.api.IEventBus;

public class Registration {
    public static void register(IEventBus bus){
        ItemRegister.register(bus);
    }
}
