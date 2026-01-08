package com.roseisproot.plushmania.item;

import com.haki.rosarium.common.api.item.IVariantHolder;
import com.roseisproot.plushmania.Plushmania;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ScissorBladeItem extends TieredItem implements IVariantHolder {

    private static final Tier SCISSOR_BLADE = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 520, 9.0F, 4.0F, 15, () -> Ingredient.of(Tags.Items.INGOTS_IRON));
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
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
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
        if(player.getItemInHand(usedHand).getTagEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(Plushmania.modLoc("charge")))) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 720000;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if(timeCharged <= getUseDuration(stack, livingEntity) - 20) {
            Vec3 direction = livingEntity.getViewVector(0);

            livingEntity.addDeltaMovement(direction.multiply(1,0,1).scale(livingEntity.onGround() ? 3: 1.5f));
            livingEntity.hurtMarked = true;

            List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), livingEntity,
                    AABB.ofSize(livingEntity.getPosition(0),2,2,2)
                            .expandTowards(direction.multiply(1,0,1).scale(3)));

            for (LivingEntity entity : entities) {
                entity.hurt(level.damageSources().mobAttack(livingEntity), (float) (livingEntity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() / 3));
                entity.addDeltaMovement(direction.multiply(1,0,1));
                entity.hurtMarked = true;
            }

            if(livingEntity instanceof Player player) {
                player.getCooldowns().addCooldown(this, 100);
            }
        }
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
        return 4;
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
                Component.translatable("item.plushmania.scissor_blade"),
                Component.translatable("item.plushmania.scissor_blade.flowering"),
                Component.translatable("item.plushmania.scissor_blade.corrupted"),
                Component.translatable("item.plushmania.scissor_blade.crimson")
        );
    }

    @Override
    public Component getName(ItemStack stack) {
        return variantNames(stack).get(getVariant(stack));
    }
}
