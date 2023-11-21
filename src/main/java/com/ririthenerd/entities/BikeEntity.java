package com.ririthenerd.entities;

import com.google.common.collect.Maps;
import com.ririthenerd.items.ItemManager;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BikeEntity extends AnimalEntity {
    public BikeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    private int clientInterpolationSteps;
    private double clientX;
    private double clientY;
    private double clientZ;
    private double clientYaw;
    private double clientPitch;
    private double clientXVelocity;
    private double clientYVelocity;
    private double clientZVelocity;

    protected UUID ownerUUID;
    protected boolean locked = false;
    protected int rearGear;
    protected int maxRearGear;
    protected int frontGear;
    protected int maxFrontGear;


    public static DefaultAttributeContainer.Builder createBikeAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack mh = player.getMainHandStack();
        UUID playerUUID = player.getUuid();

        if(!this.hasPassengers()){
           if(this.locked){
                if(isPlayerOwner(playerUUID)){
                    player.startRiding(this);
                    player.getInventory().insertStack(new ItemStack(ItemManager.BIKE_LOCK_ITEM));
                    this.locked = false;
                }
           }else{
               player.startRiding(this);
           }
        }

        if(player.isSneaking()){
            if(isPlayerOwner(playerUUID)){
                if(mh.getItem() == ItemManager.BIKE_LOCK_ITEM){
                    this.locked = true;
                    mh.decrement(1);
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    private boolean isPlayerOwner(UUID playerUUID){
        if(playerUUID == this.ownerUUID){
            return true;
        }else{
            return false;
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public void setOwnerUUID(UUID ownerUUID){
        this.ownerUUID = ownerUUID;
    }

    public boolean isPushable() {
        return !locked;
    }

    protected double getMaxSpeed() {
        return (this.isTouchingWater() ? 4.0 : 8.0) / 20.0;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        if(nbt.contains("ownerUUID") && nbt.getUuid("ownerUUID") != null){
            this.ownerUUID = nbt.getUuid("ownerUUID");
        }

        if(nbt.contains("locked")){
            this.locked = nbt.getBoolean("locked");
        }else{
            this.locked = false;
        }

        if(nbt.contains("rearGear")){
            this.rearGear = nbt.getInt("rearGear");
        }else{
            this.rearGear = 7;
        }

        if(nbt.contains("maxRearGear")){
            this.maxRearGear = nbt.getInt("maxRearGear");
        }else{
            this.maxRearGear = 7;
        }

        if(nbt.contains("frontGear")){
            this.frontGear = nbt.getInt("frontGear");
        }else{
            this.frontGear = 1;
        }

        if(nbt.contains("maxFrontGear")){
            this.maxFrontGear = nbt.getInt("maxFrontGear");
        }else{
            this.maxFrontGear = 3;
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        if(nbt.contains("ownerUUID")){
            nbt.putUuid("ownerUUID", ownerUUID);
        }
        nbt.putBoolean("locked", locked);

        nbt.putInt("rearGear", rearGear);
        nbt.putInt("maxRearGear", maxRearGear);
        nbt.putInt("frontGear", frontGear);
        nbt.putInt("maxFrontGear", maxFrontGear);
    }

    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.clientX = x;
        this.clientY = y;
        this.clientZ = z;
        this.clientYaw = yaw;
        this.clientPitch = pitch;
        this.clientInterpolationSteps = interpolationSteps + 2;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
    }

    public void setVelocityClient(double x, double y, double z) {
        this.clientXVelocity = x;
        this.clientYVelocity = y;
        this.clientZVelocity = z;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
    }

    public void tick() {
        double d;
        if (this.getWorld().isClient) {
            if (this.clientInterpolationSteps > 0) {
                d = this.getX() + (this.clientX - this.getX()) / (double)this.clientInterpolationSteps;
                double e = this.getY() + (this.clientY - this.getY()) / (double)this.clientInterpolationSteps;
                double f = this.getZ() + (this.clientZ - this.getZ()) / (double)this.clientInterpolationSteps;
                double g = MathHelper.wrapDegrees(this.clientYaw - (double)this.getYaw());
                this.setYaw(this.getYaw() + (float)g / (float)this.clientInterpolationSteps);
                this.setPitch(this.getPitch() + (float)(this.clientPitch - (double)this.getPitch()) / (float)this.clientInterpolationSteps);
                --this.clientInterpolationSteps;
                this.setPosition(d, e, f);
                this.setRotation(this.getYaw(), this.getPitch());
            } else {
                this.refreshPosition();
                this.setRotation(this.getYaw(), this.getPitch());
            }
        } else {
            if (!this.hasNoGravity()) {
                d = this.isTouchingWater() ? -0.005 : -0.04;
                this.setVelocity(this.getVelocity().add(0.0, d, 0.0));
            }

            double ms = this.getMaxSpeed();
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(MathHelper.clamp(vec3d.x, -ms, ms), vec3d.y, MathHelper.clamp(vec3d.z, -ms, ms));
            if (this.isOnGround()) {
                this.setVelocity(this.getVelocity().multiply(0.5));
            }

            this.move(MovementType.SELF, this.getVelocity());
            if (!this.isOnGround()) {
                this.setVelocity(this.getVelocity().multiply(0.95));
            }

            pedal();

        }
    }

    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            double d = 0.08;
            boolean bl = this.getVelocity().y <= 0.0;
            if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01;
            }

            float f;
            double e;
            if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid()) {
                e = this.getY();
                f = this.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();
                float g = 0.02F;
                if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    f = 0.96F;
                }

                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }

                this.setVelocity(vec3d.multiply(f, 0.800000011920929, f));
                Vec3d vec3d2 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579 - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.30000001192092896, vec3d2.z);
                }
            } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid()) {
                e = this.getY();
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d3;
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                    this.setVelocity(this.getVelocity().multiply(0.5, 0.800000011920929, 0.5));
                    vec3d3 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                    this.setVelocity(vec3d3);
                } else {
                    this.setVelocity(this.getVelocity().multiply(0.5));
                }

                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, -d / 4.0, 0.0));
                }

                vec3d3 = this.getVelocity();
                if (this.horizontalCollision && this.doesNotCollide(vec3d3.x, vec3d3.y + 0.6000000238418579 - this.getY() + e, vec3d3.z)) {
                    this.setVelocity(vec3d3.x, 0.30000001192092896, vec3d3.z);
                }
            } else if (this.isFallFlying()) {
                this.limitFallDistance();
                Vec3d vec3d4 = this.getVelocity();
                Vec3d vec3d5 = this.getRotationVector();
                f = this.getPitch() * 0.017453292F;
                double i = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                double j = vec3d4.horizontalLength();
                double k = vec3d5.length();
                double l = Math.cos(f);
                l = l * l * Math.min(1.0, k / 0.4);
                vec3d4 = this.getVelocity().add(0.0, d * (-1.0 + l * 0.75), 0.0);
                double m;
                if (vec3d4.y < 0.0 && i > 0.0) {
                    m = vec3d4.y * -0.1 * l;
                    vec3d4 = vec3d4.add(vec3d5.x * m / i, m, vec3d5.z * m / i);
                }

                if (f < 0.0F && i > 0.0) {
                    m = j * (double)(-MathHelper.sin(f)) * 0.04;
                    vec3d4 = vec3d4.add(-vec3d5.x * m / i, m * 3.2, -vec3d5.z * m / i);
                }

                if (i > 0.0) {
                    vec3d4 = vec3d4.add((vec3d5.x / i * j - vec3d4.x) * 0.1, 0.0, (vec3d5.z / i * j - vec3d4.z) * 0.1);
                }

                this.setVelocity(vec3d4.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision && !this.getWorld().isClient) {
                    m = this.getVelocity().horizontalLength();
                    double n = j - m;
                    float o = (float)(n * 10.0 - 3.0);
                    if (o > 0.0F) {
                        this.damage(this.getDamageSources().flyIntoWall(), o);
                    }
                }

                if (this.isOnGround() && !this.getWorld().isClient) {
                    this.setFlag(7, false);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                float p = this.getWorld().getBlockState(blockPos).getBlock().getSlipperiness();
                f = this.isOnGround() ? p * 0.91F : 0.91F;
                Vec3d vec3d6 = this.applyMovementInput(movementInput, p);
                double q = vec3d6.y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    q += (0.05 * (double)(this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
                } else if (this.getWorld().isClient && !this.getWorld().isChunkLoaded(blockPos)) {
                    if (this.getY() > (double)this.getWorld().getBottomY()) {
                        q = -0.1;
                    } else {
                        q = 0.0;
                    }
                } else if (!this.hasNoGravity()) {
                    q -= d;
                }
                this.setVelocity(vec3d6.x * (double)f, q * 0.9800000190734863, vec3d6.z * (double)f);
            }
        }
    }
    private void pedal(){
        if(this.hasPassengers()){
            PlayerEntity p = (PlayerEntity) this.getFirstPassenger();

            assert p != null;
            this.setYaw(p.getYaw());

            float f = p.sidewaysSpeed * 0.5F;
            float g = p.forwardSpeed;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            Vec3d v = new Vec3d(f, 0.0, g);
            if(f > 0 || f < 0 || g < 0){
                this.setVelocity(Vec3d.ZERO);
            }else{
                this.travel(v);
            }
        }
    }

    private final Map<StatusEffect, StatusEffectInstance> activeStatusEffects = Maps.newHashMap();

    public boolean hasStatusEffect(StatusEffect effect) {
        return this.activeStatusEffects.containsKey(effect);
    }

    protected boolean shouldSwimInFluids() {
        return true;
    }
    public boolean canWalkOnFluid() {
        return false;
    }
    protected float getBaseMovementSpeedMultiplier() {
        return 1f;
    }

    public void changeGear(boolean rearGear, int amt){
        if(rearGear){
            this.rearGear = this.rearGear + amt;
        }else{
            this.frontGear = this.frontGear + amt;
        }
    }

    private Optional<BlockPos> climbingPos;
    private boolean canEnterTrapdoor(BlockPos pos, BlockState state) {
        if (state.get(TrapdoorBlock.OPEN)) {
            BlockState blockState = this.getWorld().getBlockState(pos.down());
            if (blockState.isOf(Blocks.LADDER) && blockState.get(LadderBlock.FACING) == state.get(TrapdoorBlock.FACING)) {
                return true;
            }
        }

        return false;
    }
    public boolean isClimbing() {
        if (this.isSpectator()) {
            return false;
        } else {
            BlockPos blockPos = this.getBlockPos();
            BlockState blockState = this.getBlockStateAtPos();
            if (blockState.isIn(BlockTags.CLIMBABLE)) {
                this.climbingPos = Optional.of(blockPos);
                return true;
            } else if (blockState.getBlock() instanceof TrapdoorBlock && this.canEnterTrapdoor(blockPos, blockState)) {
                this.climbingPos = Optional.of(blockPos);
                return true;
            } else {
                return false;
            }
        }
    }

    public Vec3d applyFluidMovingSpeed(double gravity, boolean falling, Vec3d motion) {
        if (!this.hasNoGravity() && !this.isSprinting()) {
            double d;
            if (falling && Math.abs(motion.y - 0.005) >= 0.003 && Math.abs(motion.y - gravity / 16.0) < 0.003) {
                d = -0.003;
            } else {
                d = motion.y - gravity / 16.0;
            }

            return new Vec3d(motion.x, d, motion.z);
        } else {
            return motion;
        }
    }
    public boolean isFallFlying() {
        return this.getFlag(7);
    }

    @Nullable
    public StatusEffectInstance getStatusEffect(StatusEffect effect) {
        return this.activeStatusEffects.get(effect);
    }
    private float getMovementSpeed(float slipperiness) {
        return this.isOnGround() ? getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness)) : this.getOffGroundSpeed();
    }
    protected float getOffGroundSpeed() {
        return this.getControllingPassenger() instanceof PlayerEntity ? getMovementSpeed() * 0.1F : 0.02F;
    }
    public float getMovementSpeed(){
        float x = this.frontGear + rearGear;
        float y = (float) (Math.pow(x, 3.9) / 10000);
        return y;
    }

    public Vec3d applyMovementInput(Vec3d movementInput, float slipperiness) {
        this.updateVelocity(this.getMovementSpeed(slipperiness), movementInput);
        this.setVelocity(this.applyClimbingSpeed(this.getVelocity()));
        this.move(MovementType.SELF, this.getVelocity());
        Vec3d vec3d = this.getVelocity();
        if ((this.horizontalCollision) && (this.isClimbing() || this.getBlockStateAtPos().isOf(Blocks.POWDER_SNOW) && PowderSnowBlock.canWalkOnPowderSnow(this))) {
            vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
        }

        return vec3d;
    }
    private Vec3d applyClimbingSpeed(Vec3d motion) {
        if (this.isClimbing()) {
            this.onLanding();
            float f = 0.15F;
            double d = MathHelper.clamp(motion.x, -0.15000000596046448, 0.15000000596046448);
            double e = MathHelper.clamp(motion.z, -0.15000000596046448, 0.15000000596046448);
            double g = Math.max(motion.y, -0.15000000596046448);
            if (g < 0.0 && !this.getBlockStateAtPos().isOf(Blocks.SCAFFOLDING)) {
                g = 0.0;
            }

            motion = new Vec3d(d, g, e);
        }

        return motion;
    }
}
