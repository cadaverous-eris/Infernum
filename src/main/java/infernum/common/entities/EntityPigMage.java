package infernum.common.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import infernum.Infernum;
import infernum.common.items.InfernumItems;
import infernum.common.items.ItemSpellPage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigMage extends EntityCreature implements INpc {

	public static final String MESSAGE_PREFIX = "pigman.say.";
	public static final String MESSAGE_SUFFIX = ".message";

	private int greediness = 0;
	private int cost = -1;
	private int angerLevel = 0;
	private UUID angerTargetUUID;

	public EntityPigMage(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
		this.greediness = this.world.rand.nextInt(5) + 1;
		this.angerLevel = 0;
	}

	@Override
	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPigZombieMage.class, 8.0F, 0.8D, 0.8D));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPigZombie.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.targetTasks.addTask(1, new EntityPigMage.AIHurtByAggressor(this));
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_) {
		p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);
		return p_190672_2_;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Greed", this.greediness);
		compound.setInteger("Cost", this.cost);
		compound.setShort("Anger", (short) this.angerLevel);

		if (this.angerTargetUUID != null) {
			compound.setString("HurtBy", this.angerTargetUUID.toString());
		} else {
			compound.setString("HurtBy", "");
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.greediness = compound.getInteger("Greed");
		this.cost = compound.getInteger("Cost");
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
	}

	@Override
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

	@Override
	public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
		super.setRevengeTarget(livingBase);

		if (livingBase != null) {
			this.angerTargetUUID = livingBase.getUniqueID();
		}
	}

	private void becomeAngryAt(Entity p_70835_1_) {
		this.angerLevel = 400 + this.rand.nextInt(400);
		if (p_70835_1_ instanceof EntityLivingBase) {
			this.setRevengeTarget((EntityLivingBase) p_70835_1_);
		}
	}

	public boolean isAngry() {
		return this.angerLevel > 0;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return this.isChild() ? 0.81F : 1.62F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_PIG;
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!this.world.isRemote && !this.isDead) {
			EntityPigZombieMage entitymage = new EntityPigZombieMage(this.world);
			entitymage.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			entitymage.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitymage)),
					(IEntityLivingData) null);
			entitymage.setNoAI(this.isAIDisabled());

			if (this.hasCustomName()) {
				entitymage.setCustomNameTag(this.getCustomNameTag());
				entitymage.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
			}

			this.world.spawnEntity(entitymage);
			this.setDead();
		}
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND && !this.world.isRemote) {
			if (this.angerTargetUUID != null && this.angerTargetUUID.equals(player.getUniqueID())) {
				if (!this.world.isRemote) {
					this.sayMessage(player, "angry");
				}
				return false;
			}
			if (player.getHeldItem(hand).func_190926_b() || player.getHeldItem(hand).getItem() != Items.GOLD_INGOT) {
				if (!this.world.isRemote) {
					this.sayMessage(player, "need_gold");
				}
				return false;
			}

			if (this.greediness <= 0) {
				this.greediness = this.world.rand.nextInt(5) + 1;
			}

			if (this.cost < 0) {
				this.cost = (int) (this.greediness * (this.world.rand.nextFloat() + 1));
			}

			ItemStack goldStack = player.getHeldItem(hand);

			if (goldStack.func_190916_E() < this.cost) {
				if (!this.world.isRemote) {
					this.sayMessage(player, "more_gold");
				}
				return false;
			}
			if (goldStack.func_190916_E() >= this.cost) {
				if (player instanceof EntityPlayerMP) {
					((EntityPlayerMP) player).getHeldItem(hand).func_190918_g(this.cost);
					((EntityPlayerMP) player).updateHeldItem();
				}
				ItemStack spellStack = new ItemStack(InfernumItems.SPELL_PAGE);
				ItemSpellPage.setSpell(spellStack, Infernum.SPELL_REGISTRY.randSpell(this.world.rand));
				if (!player.inventory.addItemStackToInventory(spellStack)) {
					player.dropItem(spellStack, false);
				}
				if (!this.world.isRemote) {
					this.sayMessage(player, "thanks");
				}
				this.cost = -1;
			}
		}
		return false;
	}

	public void sayMessage(EntityPlayer player, String message) {
		message = MESSAGE_PREFIX + message + MESSAGE_SUFFIX;
		String text = I18n.translateToLocal(message);
		player.sendStatusMessage(new TextComponentString("<" + this.getName() + "> " + I18n.translateToLocal(message)),
				false);
	}

	static class AIHurtByAggressor extends EntityAIHurtByTarget {
		public AIHurtByAggressor(EntityPigMage p_i45828_1_) {
			super(p_i45828_1_, true, new Class[0]);
		}

		protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
			super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);

			if (creatureIn instanceof EntityPigMage) {
				((EntityPigMage) creatureIn).becomeAngryAt(entityLivingBaseIn);
			}
		}
	}

}
