package com.roseisproot.plushmania.item;

import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class SpoolThreadItem extends Item {

    static final int MAX_CHARGES = 64;

    static CompoundTag DEFAULT = new CompoundTag();
    static{
        DEFAULT.putInt("Charges", MAX_CHARGES);
    }

    public SpoolThreadItem() {
        super(new Properties()
                .component(DataComponents.CUSTOM_DATA, CustomData.of(DEFAULT))
                .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        /// Maybe add assorted phrases
        if (data != null) {
            CompoundTag tag = data.copyTag();
            tooltipComponents.add(Component.literal("Charges: %s/%s".formatted(tag.get("Charges"), MAX_CHARGES)).withColor(0xff0052));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
