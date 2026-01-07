package com.roseisproot.plushmania.entity;

import com.roseisproot.plushmania.Plushmania;
import com.roseisproot.plushmania.registry.ItemRegister;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.UUID;

public class ThrowNeedleEntity extends ThrowableProjectile {

    public static final EntityDataAccessor<Optional<UUID>> OWNER_DATA = SynchedEntityData.defineId(ThrowNeedleEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(ThrowNeedleEntity.class, EntityDataSerializers.ITEM_STACK);


    public ThrowNeedleEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrowNeedleEntity setNeedleOwner(UUID owner) {
        this.entityData.set(OWNER_DATA, Optional.ofNullable(owner));
        return this;
    }

    public UUID getNeedleOwner() {
        return this.entityData.get(OWNER_DATA).orElse(null);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && target.getUUID() != getNeedleOwner();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_DATA, Optional.empty());
        builder.define(ITEM_STACK, ItemRegister.NEEDLE.get().getDefaultInstance());
    }

    public ThrowNeedleEntity setItemStack(ItemStack stack) {
        this.entityData.set(ITEM_STACK, stack);
        return this;
    }

    public ItemStack getItemStack(){
        return this.entityData.get(ITEM_STACK);
    }

    @Override
    public void handleEntityEvent(byte id) {

        super.handleEntityEvent(id);
    }

    @Override
    public void tick() {

        if(tickCount >= 100){
            discard();
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        float f = 0.99F;

        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }
        }

        this.setDeltaMovement(vec3.scale((double)f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
        this.baseTick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(level() instanceof ServerLevel serverLevel){
            if(getNeedleOwner() != null) {
                UUID uuid = getNeedleOwner();
                Player player = serverLevel.getPlayerByUUID(uuid);
                if (player != null && canHitEntity(result.getEntity())) {
                    Entity  entity = result.getEntity();
                    if(entity instanceof LivingEntity livingEntity){
                        livingEntity.hurt(serverLevel.damageSources().playerAttack(player), 0f);
                    }
                    entity.addDeltaMovement(player.getPosition(0).subtract(result.getEntity().getPosition(0)).normalize().scale(1));
                    entity.hurtMarked = true;
                    serverLevel.sendParticles(
                            ParticleTypes.DAMAGE_INDICATOR, entity.getX() + serverLevel.random.triangle(0,0.25), entity.getY() + 1, entity.getZ() + serverLevel.random.triangle(0,0.25),
                            1, 0.5, 0.5, 0.5, 0.05
                    );
                    this.playSound(SoundEvents.TRIDENT_HIT, 1f, (float) serverLevel.random.triangle(1,0.2f));
                }
                this.discard();
            }
        }
        super.onHitEntity(result);
    }



    @Override
    protected void onHitBlock(BlockHitResult result) {
        if(level() instanceof ServerLevel serverLevel) {
            if(getNeedleOwner() != null){
                UUID uuid = getNeedleOwner();
                Player player = serverLevel.getPlayerByUUID(uuid);

                boolean pogoed = false;

                if (player != null) {

                    if(getItemStack().getTagEnchantments().keySet().stream().anyMatch(enchantmentHolder -> enchantmentHolder.is(Plushmania.modLoc("pogo")))){
                        Vec3 normal = new Vec3(result.getDirection().step());
                        float dot = (float) this.getViewVector(0).normalize().dot(normal);

                        if(result.getDirection() == Direction.UP && dot > 0.95 && tickCount <= 10) {
                            pogoed = true;
                            player.setDeltaMovement(player.getDeltaMovement().multiply(1,0,1).add(new Vec3(0, 1, 0)));
                            player.resetFallDistance();
                        }
                    }

                    if(!pogoed){
                        player.addDeltaMovement(result.getBlockPos().getCenter().subtract(player.getPosition(0)).normalize().scale(1.5));
                        player.resetFallDistance();
                    }

                    player.hurtMarked = true;

                    Vec3 normal = new Vec3(result.getDirection().step());
                    Vec3 pos = (result.getLocation()).add(normal.scale(0.01f));

                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            serverLevel.getBlockState(result.getBlockPos())), pos.x, pos.y, pos.z, 32, 0,0,0,0);
                    this.playSound(SoundEvents.TRIDENT_HIT_GROUND, 1f, (float) serverLevel.random.triangle(1,0.2f));
                }
            }
        }
        this.discard();
        super.onHitBlock(result);
    }
}
