package com.roseisproot.plushmania.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class SeamstressNeedleItem extends Item {
    public SeamstressNeedleItem() {
        super(new Properties()
                .stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        /// Maybe add assorted phrases
        if(data != null) {
            CompoundTag tag = data.copyTag();
            tooltipComponents.add(Component.literal(tag.getBoolean("Charged") ? "I would be careful with this needle." : "You weren't careful enough!").withColor(0xff0052));
        }

    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        if (data == null) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Charged", true);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }

        return data.copyTag().getBoolean("Charged");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        if (data != null) {
            CompoundTag tag = data.copyTag();
            tag.putBoolean("Charged", !tag.getBoolean("Charged"));
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }

        return InteractionResultHolder.success(stack);
    }

}
