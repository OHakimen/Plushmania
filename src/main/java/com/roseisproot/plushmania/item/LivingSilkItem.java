package com.roseisproot.plushmania.item;

import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class LivingSilkItem extends Item {
    public LivingSilkItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(player.getData(DataAttachmentRegister.PLUSHIE).isPlushie() && player.getHealth() < player.getMaxHealth()){
            level.playSound(player, player.getOnPos(), SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 1,1);player.startUsingItem(usedHand);
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        if(livingEntity instanceof Player player && player.getData(DataAttachmentRegister.PLUSHIE).isPlushie() && player.getHealth() < player.getMaxHealth()){
            level.playSound(player, player.getOnPos(), SoundEvents.SHEEP_SHEAR, SoundSource.NEUTRAL, 1,1);
            player.heal(8); // 4 hearts
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, 20);
        }

        return super.finishUsingItem(stack, level, livingEntity);
    }
}
