package com.roseisproot.plushmania.event;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.packets.SyncBackRenderingC2S;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Plushmania.MODID)
public class NetworkingRegistry {

    @SubscribeEvent
    public static void networkRegister(RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SyncBackRenderingC2S.TYPE,
                SyncBackRenderingC2S.STREAM_CODEC,
                SyncBackRenderingC2S::serverHandler
        );
    }
}
