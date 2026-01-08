package com.roseisproot.plushmania.mixin;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getFrictionInfluencedSpeed", at = @At("RETURN"), cancellable = true)
    public void getFrictionInfluencedSpeed(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if(entity instanceof Player player){
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            if(stack.is(ItemRegister.NEEDLE.get()) && stack.getTagEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(Plushmania.modLoc("pogo")))){
                CustomData data = stack.get(DataComponents.CUSTOM_DATA);
                if(data != null){
                    CompoundTag tag = data.copyTag();
                    if(tag.contains("Pogo") && tag.getBoolean("Pogo")){
                        cir.setReturnValue(0.05F);
                    }
                }
            }
        }
    }
}
