package infernum.common.entities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFireBreath extends Entity {

	private EntityLivingBase caster;
	private int maxLife = 0;

	public EntityFireBreath(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		this.isImmuneToFire = true;
	}

	public EntityFireBreath(World world, EntityLivingBase caster, int maxLife) {
		super(world);
		this.caster = caster;
		this.maxLife = maxLife;
	}

	@Override
	protected void entityInit() {

	}

	public void setAim(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float f1 = -MathHelper.sin(pitch * 0.017453292F);
		float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		this.setAim((double) f, (double) f1, (double) f2, velocity, inaccuracy);
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

	public void setAim(double x, double y, double z, float velocity, float inaccuracy) {
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
	public void onUpdate() {
		super.onUpdate();

		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		this.setPosition(posX, posY, posZ);

		this.motionX *= 0.93F;
		this.motionY *= 0.93F;
		this.motionZ *= 0.93F;

		RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksExisted >= 25,
				this.caster);
		if (raytraceresult != null) {
			this.onImpact(raytraceresult);
			if (!this.world.isRemote && this.world instanceof WorldServer) {
				((WorldServer) this.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY,
						this.posZ, this.world.rand.nextInt(3) + 1, 0.25, 0.25, 0.25,
						this.world.rand.nextDouble() * 0.1);
				this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, rand.nextFloat() * 0.6F + 0.3F,
						rand.nextFloat() * 0.3F + 0.7F);
				this.setDead();
			}
		}
		
		if (this.inWater) {
			if (!this.world.isRemote && this.world instanceof WorldServer) {
				((WorldServer) this.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY,
						this.posZ, this.world.rand.nextInt(3) + 1, 0.25, 0.25, 0.25,
						this.world.rand.nextDouble() * 0.1);
				this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, rand.nextFloat() * 0.6F + 0.3F,
						rand.nextFloat() * 0.3F + 0.7F);
				this.setDead();
			}
		}

		if (this.ticksExisted >= this.maxLife) {
			if (!this.world.isRemote) {
				this.setDead();
			}
		}

		if (this.world.isRemote) {
			int numParticles = this.world.rand.nextInt(4) + 6;
			world.spawnParticle(EnumParticleTypes.FLAME, true, this.randOffsetX(), this.randOffsetY(),
					this.randOffsetZ(), 0, 0, 0);
		}
	}

	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && this.world instanceof WorldServer) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit != null
					&& !result.entityHit.equals(this.caster)) {
				Entity entity = result.entityHit;
				if (!entity.isImmuneToFire()) {
					entity.setFire(7);
				}
			} else if (result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos() != null
					&& result.sideHit != null) {
				BlockPos offsetPos = result.getBlockPos().offset(result.sideHit);
				if (this.world.getBlockState(offsetPos).getMaterial() != Material.WATER
						&& (this.world.isAirBlock(offsetPos)
								|| this.world.getBlockState(offsetPos).getBlock().isReplaceable(this.world, offsetPos))
						&& Blocks.FIRE.canPlaceBlockAt(this.world, offsetPos)) {
					this.world.setBlockState(offsetPos, Blocks.FIRE.getDefaultState(), 11);
				}
			}
		}
	}

	private double randOffsetX() {
		return this.posX + (this.width * (this.world.rand.nextFloat() - 0.5F));
	}

	private double randOffsetY() {
		return this.posY + (this.height * (this.world.rand.nextFloat() - 0.5F)) + this.height / 2F;
	}

	private double randOffsetZ() {
		return this.posZ + (this.width * (this.world.rand.nextFloat() - 0.5F));
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

		if (compound.hasKey("maxLife", 3)) {
			this.maxLife = compound.getInteger("maxLife");
		}

		this.caster = null;
		if (compound.hasKey("casterName", 8)) {
			String shooterName = compound.getString("casterName");
			if (shooterName != null && !shooterName.isEmpty()) {
				this.caster = this.world.getPlayerEntityByName(shooterName);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag("position", this.newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
		compound.setTag("direction", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
		compound.setInteger("ticksAlive", this.ticksExisted);
		compound.setInteger("maxLife", this.maxLife);
		if (this.caster != null && this.caster instanceof EntityPlayer) {
			compound.setString("casterName", this.caster.getName() == null ? "" : this.caster.getName());
		}
	}

}
