package com.roseisproot.plushmania.packets;

import com.haki.rosarium.RosariumConstants;
import com.haki.rosarium.common.api.item.IVariantHolder;
import com.haki.rosarium.common.utils.PlayerUtils;
import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncBackRenderingC2S(boolean state) implements CustomPacketPayload {
    public static final Type<SyncBackRenderingC2S> TYPE = new Type<SyncBackRenderingC2S>(RosariumConstants.modLoc("sync_back_rendering"));


    public static final StreamCodec<RegistryFriendlyByteBuf, SyncBackRenderingC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            SyncBackRenderingC2S::state,
            SyncBackRenderingC2S::new
    );

    public static void serverHandler(SyncBackRenderingC2S packet, IPayloadContext context) {
        context.enqueueWork(
                () -> {
                    Player player = context.player();

                    PlushieData data = player.getData(DataAttachmentRegister.PLUSHIE.get());
                    data.setShouldRender(packet.state);
                    player.setData(DataAttachmentRegister.PLUSHIE.get(), data);
                }
        );

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
