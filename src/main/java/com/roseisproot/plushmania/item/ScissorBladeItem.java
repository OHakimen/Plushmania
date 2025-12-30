package com.roseisproot.plushmania.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ScissorBladeItem extends TieredItem{

    private static final Tier SCISSOR_BLADE = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 520, 9.0F, 4.0F, 15, Ingredient::of);
    public ScissorBladeItem() {
        super(SCISSOR_BLADE, new Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(SCISSOR_BLADE, 10, -3.5f))
        );
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        entity.level().playSound(null, entity.getOnPos(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1f,1f);
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

}
