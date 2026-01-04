package com.roseisproot.plushmania.item;

import com.haki.rosarium.Rosarium;
import com.haki.rosarium.common.api.item.IVariantHolder;
import com.haki.rosarium.extras.SupporterHelper;
import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.entity.ThrowNeedleEntity;
import com.roseisproot.plushmania.registry.EntityRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.List;

public class NeedleItem extends TieredItem implements IVariantHolder {

    private static final Tier NEEDLE_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 520, 9.0F, 4.0F, 15, Ingredient::of);

    public NeedleItem() {
        super(NEEDLE_TIER, new Properties()
                .stacksTo(1)
                .attributes(SwordItem.createAttributes(NEEDLE_TIER, 3, -2.4F))
        );
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }


    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);


        if(stack.getTagEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(Plushmania.modLoc("hooking")))) {
            if (!level.isClientSide) {

                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                ThrowNeedleEntity entity = new ThrowNeedleEntity(EntityRegister.THROW_NEEDLE.get(), level);
                entity.setNeedleOwner(player.getUUID());
                entity.setItemStack(stack);
                entity.setPos(player.getEyePosition());
                entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3, 1);

                level.addFreshEntity(entity);

                player.getCooldowns().addCooldown(this, 40);
            } else {
                level.playSound(player, player.getOnPos(), SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1f, (float) level.random.triangle(1, 0.2f));
            }

            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return super.use(level, player, usedHand);
    }


    @Override
    public int getVariant(ItemStack itemStack){
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        if(data != null){
            CompoundTag tag = data.copyTag();

            return tag.contains("Variant") ? tag.getInt("Variant") : 0;
        }

        return 0;
    }

    @Override
    public int getMaxVariants(ItemStack itemStack) {
        return 5;
    }

    @Override
    public void setVariant(ItemStack itemStack, int i) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Variant", i);

        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public List<Component> variantNames(ItemStack itemStack) {
        return List.of(
                Component.translatable("item.plushmania.needle"),
                Component.translatable("item.plushmania.needle.golden"),
                Component.translatable("item.plushmania.needle.flowering"),
                Component.translatable("item.plushmania.needle.corrupted"),
                Component.translatable("item.plushmania.needle.pale")
        );
    }

    @Override
    public Component getName(ItemStack stack) {
        return variantNames(stack).get(getVariant(stack));
    }
}
