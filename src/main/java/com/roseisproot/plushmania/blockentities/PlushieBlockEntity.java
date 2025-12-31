package com.roseisproot.plushmania.blockentities;

import com.haki.rosarium.common.utils.ProfileUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.roseisproot.plushmania.registry.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlushieBlockEntity extends BlockEntity {

    GameProfile gameProfile;
    boolean isSheared;
    float direction;

    public PlushieBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegister.PLUSH.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {

        super.loadAdditional(tag, registries);
        if(tag.contains("uuid")) {
            UUID profileId = UUIDUtil.uuidFromIntArray(tag.getIntArray("uuid"));
            String username = tag.getString("name");
            String texture = tag.getString("texture");
            gameProfile = new GameProfile(profileId, username);
            gameProfile.getProperties().put("textures", new Property("textures", texture));
        }
        isSheared = tag.getBoolean("isSheared");
        direction = tag.getFloat("direction");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if(gameProfile != null) {
            tag.putIntArray("uuid", UUIDUtil.uuidToIntArray(gameProfile.getId()));
            tag.putString("name", gameProfile.getName());
            tag.putString("texture", gameProfile.getProperties().get("textures").stream().toList().get(0).value());
        }
        tag.putBoolean("isSheared", isSheared);
        tag.putFloat("direction", direction);

        super.saveAdditional(tag, registries);
    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithFullMetadata(pRegistries);
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public PlushieBlockEntity setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
        return this;
    }

    public boolean isSheared() {
        return isSheared;
    }

    public PlushieBlockEntity setSheared(boolean sheared) {
        isSheared = sheared;
        return this;
    }

    public float getDirection() {
        return direction;
    }

    public PlushieBlockEntity setDirection(float direction) {
        this.direction = direction;
        return this;
    }
}
