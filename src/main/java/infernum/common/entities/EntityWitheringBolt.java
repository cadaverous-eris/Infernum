package infernum.common.entities;

import javax.annotation.Nullable;

import infernum.common.spells.EntityDamageSourceSpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityWitheringBolt extends Entity implements IProjectile {

	public EntityLivingBase shootingEntity;
	public int strength;

	public EntityWitheringBolt(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public EntityWitheringBolt(World world, EntityLivingBase shooter, int strength, double x, double y, double z,
			double xVel, double yVel, double zVel, float vel) {
		super(world);
		this.setSize(0.5F, 0.5F);
		this.strength = strength;
		this.setPosition(x, y, z);
		this.shootingEntity = shooter;
	}

	public void setAim(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		this.setThrowableHeading((double) f, (double) f1, (double) f2, velocity, inaccuracy);
		this.motionX += shooter.motionX;
		this.motionZ += shooter.motionZ;
		if (!shooter.onGround) {
			this.motionY += shooter.motionY;
		}
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.setPosition(posX, posY, posZ);

		RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksExisted >= 25,
				this.shootingEntity);
		if (raytraceresult != null) {
			onImpact(raytraceresult);
			if (!this.world.isRemote && this.world instanceof WorldServer) {
				((WorldServer) this.world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, this.posX, this.posY,
						this.posZ, this.world.rand.nextInt(8) + 8, 0.25, 0.25, 0.25,
						this.world.rand.nextDouble() * 0.02);
				this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, rand.nextFloat() * 0.6F + 0.3F, rand.nextFloat() * 0.3F + 0.7F);
				this.setDead();
			}
		}

		if (this.ticksExisted > 600) {
			if (!this.world.isRemote) {
				this.setDead();
			}
		}

		if (this.world.isRemote) {
			int numParticles = this.world.rand.nextInt(4) + 6;
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY + (this.height / 2),
					this.posZ, 0, 0, 0);
		}
	}

	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null
				&& result.entityHit instanceof EntityLivingBase && !result.entityHit.equals(this.shootingEntity)) {
			EntityLivingBase entity = (EntityLivingBase) result.entityHit;
			entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 150, this.strength));
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 0.5F;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("position", 9) && compound.getTagList("position", 6).tagCount() == 3) {
			NBTTagList nbttaglist1 = compound.getTagList("position", 6);
			this.posX = nbttaglist1.getDoubleAt(0);
			this.posY = nbttaglist1.getDoubleAt(1);
			this.posZ = nbttaglist1.getDoubleAt(2);
		}

		if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3) {
			NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
			this.motionX = nbttaglist1.getDoubleAt(0);
			this.motionY = nbttaglist1.getDoubleAt(1);
			this.motionZ = nbttaglist1.getDoubleAt(2);
		} else {
			if (!this.world.isRemote) {
				this.setDead();
			}
		}

		if (compound.hasKey("tickAlive", 3)) {
			this.ticksExisted = compound.getInteger("ticksAlive");
		}

		if (compound.hasKey("strength", 3)) {
			this.strength = compound.getInteger("strength");
		} else {
			this.strength = 0;
		}

		this.shootingEntity = null;
		if (compound.hasKey("shooterName", 8)) {
			String shooterName = compound.getString("shooterName");
			if (shooterName != null && !shooterName.isEmpty()) {
				this.shootingEntity = this.world.getPlayerEntityByName(shooterName);
			}
		}

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("position", this.newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
		compound.setTag("direction", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
		compound.setInteger("ticksAlive", this.ticksExisted);
		compound.setInteger("strength", this.strength);
		if (this.shootingEntity != null && this.shootingEntity instanceof EntityPlayer) {
			compound.setString("shooterName",
					this.shootingEntity.getName() == null ? "" : this.shootingEntity.getName());
		}
	}

	@Nullable
	public EntityLivingBase getShootingEntity() {
		return this.shootingEntity;
	}

}
