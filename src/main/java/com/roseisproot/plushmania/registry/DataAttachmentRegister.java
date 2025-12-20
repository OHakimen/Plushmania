package com.roseisproot.plushmania.registry;

import com.mojang.serialization.Codec;
import com.roseisproot.plushmania.Plushmania;
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

    public static final Supplier<AttachmentType<Boolean>> PLUSHIE = ATTACHMENT_TYPES.register(
            "plushie", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("isPlushie").codec())
                    .sync(new AttachmentSyncHandler<Boolean>() {
                        @Override
                        public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, Boolean aBoolean, boolean initialSync) {
                            registryFriendlyByteBuf.writeBoolean(aBoolean);
                        }

                        @Override
                        public @Nullable Boolean read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable Boolean aBoolean) {
                            return registryFriendlyByteBuf.readBoolean();
                        }
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
