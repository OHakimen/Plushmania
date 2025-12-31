package com.roseisproot.plushmania.item;

import com.haki.rosarium.common.utils.ProfileUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlushieBlockItem extends BlockItem {
    public PlushieBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if(data != null) {
            CompoundTag tag = data.copyTag();
            if(tag != null && tag.contains("name") && !tag.isEmpty()){
                tooltipComponents.add(Component.literal(tag.getString("name")).withColor(0xff0052));
            }
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        Component component = stack.get(DataComponents.CUSTOM_NAME);

        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        boolean isSame = false;
        if(data != null && component != null){
            isSame = data.copyTag().getString("name").toLowerCase().equals(component.toString());
        }

        if(component != null && !isSame && entity.tickCount % 20 == 0){
            String username = component.getString();

            ProfileUtils.getProfile(username).thenAccept(gp -> {
                gp.ifPresent(gameProfile -> {
                    CompoundTag tag = new CompoundTag();
                    tag.putString("id", "plushmania:plush");
                    tag.putIntArray("uuid", UUIDUtil.uuidToIntArray(gameProfile.getId()));
                    tag.putString("name", gameProfile.getName());
                    tag.putString("texture", gameProfile.getProperties().get("textures").stream().toList().get(0).value());

                    stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
                });
            }).orTimeout(2, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return armorType == EquipmentSlot.HEAD;
    }
}
