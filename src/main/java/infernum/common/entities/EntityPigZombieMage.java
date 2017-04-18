package infernum.common.entities;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import infernum.common.entities.ai.EntityAIAttackRangedWitherBolt;
import infernum.common.items.InfernumItems;
import infernum.common.items.ItemSpellPage;
import infernum.common.spells.InfernumSpells;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombieMage extends EntityMob implements IRangedAttackMob {

	private final EntityAIAttackRangedWitherBolt aiSpellAttack = new EntityAIAttackRangedWitherBolt(this, 1.0D, 20,
			15.0F);
	private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);

	private int angerLevel;
	private int randomSoundDelay;
	private UUID angerTargetUUID;
	private float pigZombieWidth = -1.0F;
	private float pigZombieHeight;

	public EntityPigZombieMage(World worldIn) {
		super(worldIn);
		this.setCombatTask();
		this.setSize(0.6F, 1.95F);
		this.isImmuneToFire = true;
	}

	public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
		super.setRevengeTarget(livingBase);

		if (livingBase != null) {
			this.angerTargetUUID = livingBase.getUniqueID();
		}
	}

	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.targetTasks.addTask(1, new EntityPigZombieMage.AIHurtByAggressor(this));
		this.targetTasks.addTask(2, new EntityPigZombieMage.AITargetAggressor(this));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	protected void updateAITasks() {
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		if (this.isAngry()) {
			--this.angerLevel;
		}

		if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
			this.playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, this.getSoundVolume() * 2.0F,
					((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		if (this.angerLevel > 0 && this.angerTargetUUID != null && this.getAITarget() == null) {
			EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
			this.setRevengeTarget(entityplayer);
			this.attackingPlayer = entityplayer;
			this.recentlyHit = this.getRevengeTimer();
		}

		super.updateAITasks();
	}

	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	public boolean isNotColliding() {
		return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this)
				&& this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
				&& !this.world.containsAnyLiquid(this.getEntityBoundingBox());
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setShort("Anger", (short) this.angerLevel);

		if (this.angerTargetUUID != null) {
			compound.setString("HurtBy", this.angerTargetUUID.toString());
		} else {
			compound.setString("HurtBy", "");
		}
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.angerLevel = compound.getShort("Anger");
		String s = compound.getString("HurtBy");

		if (!s.isEmpty()) {
			this.angerTargetUUID = UUID.fromString(s);
			EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.angerTargetUUID);
			this.setRevengeTarget(entityplayer);

			if (entityplayer != null) {
				this.attackingPlayer = entityplayer;
				this.recentlyHit = this.getRevengeTimer();
			}
		}
		this.setCombatTask();
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			Entity entity = source.getEntity();

			if (entity instanceof EntityPlayer) {
				this.becomeAngryAt(entity);
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	public void chargeAnimation() {
		if (!world.isRemote && world instanceof WorldServer) {
			Vec3d lookVec = this.getLookVec();
			double x = this.posX + lookVec.xCoord;
			double y = this.posY + lookVec.yCoord + this.getEyeHeight();
			double z = this.posZ + lookVec.zCoord;
			((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, x, y, z, world.rand.nextInt(2),
					0.01, 0.01, 0.01, 0);
		}
	}

	private void becomeAngryAt(Entity p_70835_1_) {
		this.angerLevel = 400 + this.rand.nextInt(400);
		this.randomSoundDelay = this.rand.nextInt(40);

		if (p_70835_1_ instanceof EntityLivingBase) {
			this.setRevengeTarget((EntityLivingBase) p_70835_1_);
		}
	}

	public boolean isAngry() {
		return this.angerLevel > 0;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
	}

	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_ZOMBIE_PIGMAN;
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return false;
	}

	public float getEyeHeight() {
		float f = 1.74F;

		if (this.isChild()) {
			f = (float) ((double) f - 0.81D);
		}

		return f;
	}

	protected final void setSize(float width, float height) {
		boolean flag = this.pigZombieWidth > 0.0F && this.pigZombieHeight > 0.0F;
		this.pigZombieWidth = width;
		this.pigZombieHeight = height;

		if (!flag) {
			this.multiplySize(1.0F);
		}
	}

	protected final void multiplySize(float size) {
		super.setSize(this.pigZombieWidth * size, this.pigZombieHeight * size);
	}

	public double getYOffset() {
		return this.isChild() ? 0.0D : -0.45D;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		Vec3d lookVec = this.getLookVec();
		double x = this.posX + lookVec.xCoord;
		double y = this.posY + lookVec.yCoord + this.getEyeHeight();
		double z = this.posZ + lookVec.zCoord;
		EntityWitheringBolt bolt = new EntityWitheringBolt(this.world, this, 0, x, y, z);
		/*
		bolt.setAim(this, this.rotationPitch, this.rotationYaw,
				(float) (0.3 + (0.05F * (this.world.getDifficulty().getDifficultyId()))),
				(float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
		*/
		double d0 = target.posX - this.posX;
        double d1 = target.posY - this.posY;
        double d2 = target.posZ - this.posZ;
        bolt.setThrowableHeading(d0, d1, d2, (float) (0.45 + (0.1F * (this.world.getDifficulty().getDifficultyId()))), (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
		
		this.world.spawnEntity(bolt);
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		ItemStack scroll = new ItemStack(InfernumItems.SPELL_PAGE);
		ItemSpellPage.setSpell(scroll, InfernumSpells.WITHERING_BOLT);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, scroll);
	}

	protected ItemStack func_190732_dj() {
		return ItemStack.field_190927_a;
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		float f = difficulty.getClampedAdditionalDifficulty();
		this.setCanPickUpLoot(false);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		this.setCombatTask();

		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(
				new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
		double d0 = this.rand.nextDouble() * 1.5D * (double) f;

		if (d0 > 1.0D) {
			this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
					.applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
		}

		return livingdata;
	}

	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		super.setItemStackToSlot(slotIn, stack);

		if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
			this.setCombatTask();
		}
	}

	public void setCombatTask() {
		if (this.world != null && !this.world.isRemote) {
			this.tasks.removeTask(this.aiAttackOnCollide);
			this.tasks.removeTask(this.aiSpellAttack);
			ItemStack itemstack = this.getHeldItemMainhand();

			if (itemstack.getItem() == InfernumItems.SPELL_PAGE
					&& ItemSpellPage.getSpell(itemstack).equals(InfernumSpells.WITHERING_BOLT)) {
				int i = 30;

				if (this.world.getDifficulty() != EnumDifficulty.HARD) {
					i = 50;
				}

				this.aiSpellAttack.setAttackCooldown(i);
				this.tasks.addTask(4, this.aiSpellAttack);
			} else {
				this.tasks.addTask(4, this.aiAttackOnCollide);
			}
		}
	}

	static class AIHurtByAggressor extends EntityAIHurtByTarget {
		public AIHurtByAggressor(EntityPigZombieMage entityPigZombieMage) {
			super(entityPigZombieMage, true, new Class[0]);
		}

		protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
			super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);

			if (creatureIn instanceof EntityPigZombieMage) {
				((EntityPigZombieMage) creatureIn).becomeAngryAt(entityLivingBaseIn);
			}
		}
	}

	static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {
		public AITargetAggressor(EntityPigZombieMage p_i45829_1_) {
			super(p_i45829_1_, EntityPlayer.class, true);
		}

		public boolean shouldExecute() {
			return ((EntityPigZombieMage) this.taskOwner).isAngry() && super.shouldExecute();
		}
	}

}
