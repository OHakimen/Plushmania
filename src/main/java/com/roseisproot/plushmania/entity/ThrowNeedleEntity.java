package com.roseisproot.plushmania.entity;

import com.roseisproot.plushmania.registry.ParticleRegister;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import javax.sound.sampled.Line;
import java.util.Optional;
import java.util.UUID;

public class ThrowNeedleEntity extends ThrowableProjectile {

    public static final EntityDataAccessor<Optional<UUID>> OWNER_DATA = SynchedEntityData.defineId(ThrowNeedleEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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

        //super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        //this.updateRotation();
        float f;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double)f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
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
                        livingEntity.hurt(serverLevel.damageSources().playerAttack(player), 2f);
                    }
                    entity.addDeltaMovement(player.getPosition(0).subtract(result.getEntity().getPosition(0)).normalize().scale(2));
                    entity.hurtMarked = true;
                    serverLevel.sendParticles(
                            ParticleTypes.DAMAGE_INDICATOR, entity.getX() + serverLevel.random.triangle(0,0.25), entity.getY() + 1, entity.getZ() + serverLevel.random.triangle(0,0.25),
                            1, 0.5, 0.5, 0.5, 0.05
                    );
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
                if (player != null ) {
                    player.addDeltaMovement(result.getBlockPos().getBottomCenter().subtract(player.getPosition(0)).normalize().scale(2));
                    player.hurtMarked = true;
                }
            }
        }
        this.discard();
        super.onHitBlock(result);
    }
}
