package com.roseisproot.plushmania.block;

import com.haki.rosarium.common.utils.ProfileUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.math.Axis;
import com.roseisproot.plushmania.blockentities.PlushieBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.xml.crypto.Data;
import java.io.IOException;

public class PlushieBlock extends Block implements EntityBlock {

    public PlushieBlock() {
        super(Properties.ofFullCopy(Blocks.WHITE_WOOL)
                .noOcclusion()
                .dynamicShape()
                .noCollission()
                .isSuffocating((blockState, blockGetter, blockPos) -> false));
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        for (int i = 0; i < 8; i++) {
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_WOOL.defaultBlockState()), pos.getX() + level.getRandom().triangle(0.5,0.25), pos.getY() + level.getRandom().triangle(0.5,0.25), pos.getZ() + level.getRandom().triangle(0.5,0.25), 0.0D, 0.0D, 0.0D);
        }
        level.playSound(player, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

    }


    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(level instanceof ClientLevel) {
            level.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.addParticle(ParticleTypes.HEART, pos.getX() + level.getRandom().triangle(0.5,0.25), 0.75 + pos.getY() + level.getRandom().triangle(0.5,0.25), pos.getZ() + level.getRandom().triangle(0.5,0.25), 0.0D, 0.0D, 0.0D);

        }


        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(stack.is(Items.NAME_TAG) && blockEntity instanceof PlushieBlockEntity plushieBlockEntity){
            Component component = stack.get(DataComponents.CUSTOM_NAME);
            if(component != null){
                String username = component.getString();

                ProfileUtils.getProfile(username).thenAccept(gameProfile -> {
                    gameProfile.ifPresent(plushieBlockEntity::setGameProfile);
                });
            }
            return ItemInteractionResult.SUCCESS;
        } else if(stack.is(Items.SHEARS)  && blockEntity instanceof PlushieBlockEntity plushieBlockEntity && !plushieBlockEntity.isSheared()){
            plushieBlockEntity.setSheared(true);
            level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, (float)level.getRandom().triangle(1f, 0.125f));
            return ItemInteractionResult.SUCCESS;
        }else if(stack.is(ItemTags.WOOL) && blockEntity instanceof PlushieBlockEntity plushieBlockEntity && plushieBlockEntity.isSheared()){
            plushieBlockEntity.setSheared(false);
            level.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, (float)level.getRandom().triangle(1f, 0.125f));
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        PlushieBlockEntity blockEntity = new PlushieBlockEntity(blockPos, blockState);

        return blockEntity;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof PlushieBlockEntity plushieBlockEntity && placer != null){

            Vec3 thingToMe =  placer.getPosition(0)
                    .subtract(
                            plushieBlockEntity.getBlockPos().getCenter()
                    );

            plushieBlockEntity.setDirection(
                    (((float)(Math.atan2(thingToMe.x, thingToMe.z)))/ 8) * 8
            );

            Component component = stack.get(DataComponents.CUSTOM_NAME);
            if(component != null){
                String username = component.getString();

                ProfileUtils.getProfile(username).thenAccept(gameProfile -> {
                    gameProfile.ifPresent(plushieBlockEntity::setGameProfile);
                });
            }

        }

        super.setPlacedBy(level, pos, state, placer, stack);
    }
}
