package com.roseisproot.plushmania.mixin;

import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import com.roseisproot.plushmania.registry.ItemRegister;
import com.roseisproot.plushmania.registry.ParticleRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Mixin(Player.class)
public abstract class PlayerMixin {


    @Inject(method = "hurt", at = @At("RETURN"), cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Player entity = (Player) (Object) this;


        PlushieData data = entity.getData(DataAttachmentRegister.PLUSHIE.get());

        if (entity.level() instanceof ServerLevel level && source.getEntity() != null && data.isPlushie()) {
            level.sendParticles(
                    ParticleRegister.FLOOF.get(), entity.getX() + level.random.triangle(0,0.25), entity.getY() + 1, entity.getZ() + level.random.triangle(0,0.25),
                    64, 0.5, 0.5, 0.5, 0.05
            );
        }
    }


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        Player entity = (Player) (Object) this;

        PlushieData data = entity.getData(DataAttachmentRegister.PLUSHIE.get());

        if (data.isPlushie()) {

            if (entity.getInventory().hasAnyMatching(stack -> stack.getItem().equals(ItemRegister.SPOOL_OF_THREAD.get())) && entity.tickCount % 40 == 0) {

                List<ItemStack> items  = new ArrayList<>();

                items.addAll(entity.getInventory().items);
                items.addAll(entity.getInventory().offhand);

                Optional<ItemStack> lowestSpool = items.stream().filter(stack -> {
                    CustomData stackData = stack.get(DataComponents.CUSTOM_DATA);
                    return stackData != null && stackData.copyTag().contains("Charges") && stackData.copyTag().getInt("Charges") > 0;
                }).min((o1, o2) -> {
                    CustomData stackData1 = o1.get(DataComponents.CUSTOM_DATA);
                    CustomData stackData2 = o2.get(DataComponents.CUSTOM_DATA);

                    if (stackData1 != null && stackData2 != null) {
                        CompoundTag data1 = stackData1.copyTag();
                        CompoundTag data2 = stackData2.copyTag();

                        return Integer.compare(data1.getInt("Charges"), data2.getInt("Charges"));
                    }

                    return 0;
                });

                if (lowestSpool.isPresent()) {
                    ItemStack spool = lowestSpool.get();
                    CustomData spoolData = spool.get(DataComponents.CUSTOM_DATA);
                    CompoundTag tag = spoolData.copyTag();
                    if (tag.getInt("Charges") > 0 &&
                        entity.getHealth() < entity.getMaxHealth()
                    ) {
                        entity.heal(1);

                        tag.putInt("Charges", Math.max(tag.getInt("Charges") - 1, 0));
                        spool.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    }
                }
            }

            // Calculate sogness
            if (entity.isInWater() && data.isPlushie() && entity.tickCount % 5 == 0 && data.sogPercentage() != 1) {
                data.setSogPercentage(Math.min(data.sogPercentage() + 0.05f, 1));
                entity.setData(DataAttachmentRegister.PLUSHIE.get(), data);
            } else if (data.isPlushie() && entity.tickCount % 10 == 0 && data.sogPercentage() != 0) {
                data.setSogPercentage(Math.max(data.sogPercentage() - 0.01f, 0));
                entity.setData(DataAttachmentRegister.PLUSHIE.get(), data);
            }

            //Drop sogness more if the player is on fire
            if (entity.isOnFire() && entity.tickCount % 10 == 0 && data.sogPercentage() != 0) {
                data.setSogPercentage(Math.max(data.sogPercentage() - 0.05f, 0));
                entity.setData(DataAttachmentRegister.PLUSHIE.get(), data);
            }


            if (entity.level() instanceof ServerLevel level && data.sogPercentage() > 0.25) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
                level.sendParticles(
                        ParticleTypes.FALLING_WATER, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1),
                        1, 0, 0, 0, 0
                );
            }
        }
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    public void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof Player player && (source.is(DamageTypes.FALL) || source.is(DamageTypes.FLY_INTO_WALL))) {
            cir.setReturnValue(player.getData(DataAttachmentRegister.PLUSHIE.get()).isPlushie());
        }

        //No fire damage when sog
        if (entity instanceof Player player && (source.is(DamageTypes.FIREBALL) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.CAMPFIRE))) {
            PlushieData data = player.getData(DataAttachmentRegister.PLUSHIE.get());
            cir.setReturnValue(data.isPlushie() && data.sogPercentage() >= 0.05f);
        }
    }

    @Inject(method = "canEat", at = @At("RETURN"), cancellable = true)
    public void canEat(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof Player player && player.getData(DataAttachmentRegister.PLUSHIE.get()).isPlushie()) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;tick(Lnet/minecraft/world/entity/player/Player;)V"))
    public void tick(FoodData instance, Player player) {
        if (!player.getData(DataAttachmentRegister.PLUSHIE.get()).isPlushie()) {
            instance.tick(player);
            instance.setFoodLevel(20);
        }
    }
}
