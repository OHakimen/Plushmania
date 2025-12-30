package com.roseisproot.plushmania.item;

import com.roseisproot.plushmania.data.PlushieData;
import com.roseisproot.plushmania.registry.DataAttachmentRegister;
import com.roseisproot.plushmania.registry.ParticleRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SeamstressNeedleItem extends Item {

    static CompoundTag DEFAULT = new CompoundTag();

    static {
        DEFAULT.putBoolean("Charged", true);
    }

    public SeamstressNeedleItem() {
        super(new Properties()
                .component(DataComponents.CUSTOM_DATA, CustomData.of(DEFAULT))
                .stacksTo(1));
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        /// Maybe add assorted phrases
        if (data != null) {
            CompoundTag tag = data.copyTag();
            tooltipComponents.add(Component.literal(tag.getBoolean("Charged") ? "I would be careful with this needle." : "You weren't careful enough!").withColor(0xff0052));
        }
    }


    @Override
    public boolean isFoil(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        return data.copyTag().getBoolean("Charged");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack stack = player.getItemInHand(usedHand);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);

        PlushieData plushData = player.getData(DataAttachmentRegister.PLUSHIE.get());
        plushData.setPlushie(data.copyTag().getBoolean("Charged"));
        plushData.setSogPercentage(0);
        player.setData(DataAttachmentRegister.PLUSHIE.get(), plushData);

        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Charged", !data.copyTag().getBoolean("Charged"));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        if (level.isClientSide) {
            for (int i = 0; i < 32; i++) {
                level.addParticle(
                        ParticleRegister.FLOOF.get(), player.getX(), player.getY() + level.getRandom().triangle(1, 1), player.getZ(), level.getRandom().triangle(0, 2),  level.getRandom().triangle(1, 1), level.getRandom().triangle(0, 2)
                );

                level.addParticle(
                        ParticleTypes.CLOUD, player.getX(), player.getY() + level.getRandom().triangle(1, 1), player.getZ(), level.getRandom().triangle(0, 0.25),  level.getRandom().triangle(0.1, 0.1), level.getRandom().triangle(0, 0.25)
                );
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {

        return super.finishUsingItem(stack, level, livingEntity);
    }

}
