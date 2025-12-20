package com.roseisproot.plushmania.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

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

        return stack.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("Charged");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        if (data != null) {

            CompoundTag tag = data.copyTag();
            boolean canPlush = tag.getBoolean("Charged");
            if ((canPlush && !player.getData(DataAttachmentRegister.PLUSHIE.get())) || (!canPlush && player.getData(DataAttachmentRegister.PLUSHIE.get()))) {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        if (data != null && livingEntity instanceof Player player) {

            CompoundTag tag = data.copyTag();
            boolean canPlush = tag.getBoolean("Charged");
            if(canPlush && !livingEntity.getData(DataAttachmentRegister.PLUSHIE.get())){
                livingEntity.setData(DataAttachmentRegister.PLUSHIE.get(), true);
                tag.putBoolean("Charged", false);
            }else if(!canPlush && livingEntity.getData(DataAttachmentRegister.PLUSHIE.get())){
                livingEntity.setData(DataAttachmentRegister.PLUSHIE.get(), false);
                tag.putBoolean("Charged", true);
            }
            player.getCooldowns().addCooldown(this, 20);



            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

}
