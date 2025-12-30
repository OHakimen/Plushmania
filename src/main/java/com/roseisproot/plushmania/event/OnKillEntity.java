package com.roseisproot.plushmania.event;

import com.roseisproot.plushmania.Plushmania;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = Plushmania.MODID)
public class OnKillEntity {

    static TagKey<Item> GUILLOTINE = TagKey.create(Registries.ITEM, Plushmania.modLoc("enchantable/guillotine"));

    @SubscribeEvent
    public static void onKillEntity(LivingDeathEvent event) {
        Entity user = event.getSource().getEntity();

        if(user != null && user.level() instanceof ServerLevel level && user.getWeaponItem() instanceof ItemStack weapon && weapon.is(GUILLOTINE)
           && weapon.getTagEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(Plushmania.modLoc("guillotine")))
           && level.getRandom().nextFloat() <= 0.05f ){
            LivingEntity killed = event.getEntity();

            ItemStack toDrop = ItemStack.EMPTY;
            switch (killed.getClass().getSimpleName()){
                case "Zombie" -> {
                    toDrop = Items.ZOMBIE_HEAD.getDefaultInstance();
                }
                case "Skeleton" -> {
                    toDrop = Items.SKELETON_SKULL.getDefaultInstance();
                }
                case "Creeper" -> {
                    toDrop = Items.CREEPER_HEAD.getDefaultInstance();
                }
                case "Piglin" -> {
                    toDrop = Items.PIGLIN_HEAD.getDefaultInstance();
                }
                case "WitherSkeleton" -> {
                    toDrop = Items.WITHER_SKELETON_SKULL.getDefaultInstance();
                }
                case "Player" -> {
                    toDrop = Items.PLAYER_HEAD.getDefaultInstance();

                    if(killed instanceof Player player){
                        ResolvableProfile profile = new ResolvableProfile(player.getGameProfile());
                        toDrop.set(DataComponents.PROFILE, profile);
                    }
                }
            }

            level.addFreshEntity(new ItemEntity(
                    level,
                    killed.getX(),
                    killed.getY(),
                    killed.getZ(),
                    toDrop
            ));
        }
    }
}
