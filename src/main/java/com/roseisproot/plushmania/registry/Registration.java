package com.roseisproot.plushmania.registry;

import net.neoforged.bus.api.IEventBus;

public class Registration {
    public static void register(IEventBus bus){
        BlockEntityRegister.register(bus);



        DataAttachmentRegister.register(bus);
        ParticleRegister.register(bus);

        BlockRegister.register(bus);
        ItemRegister.register(bus);


    }
}
