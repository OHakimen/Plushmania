package com.roseisproot.plushmania.registry;

import com.mojang.serialization.Codec;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.data.PlushieData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class DataAttachmentRegister {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Plushmania.MODID);

    public static final Supplier<AttachmentType<PlushieData>> PLUSHIE = ATTACHMENT_TYPES.register(
            "plushie", () -> AttachmentType.builder(() -> new PlushieData(false,0)).serialize(PlushieData.CODEC)
                    .sync(PlushieData.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
