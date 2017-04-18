package infernum.common.entities.ai;

import infernum.common.entities.EntityPigZombieMage;
import infernum.common.items.InfernumItems;
import infernum.common.items.ItemSpellPage;
import infernum.common.spells.InfernumSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedWitherBolt extends EntityAIBase {

	private final EntityMob entity;
	private final double moveSpeedAmp;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public EntityAIAttackRangedWitherBolt(EntityMob mob, double speedAmplifier, int delay, float maxDistance) {
		this.entity = mob;
		this.moveSpeedAmp = speedAmplifier;
		this.attackCooldown = delay;
		this.maxAttackDistance = maxDistance * maxDistance;
		this.setMutexBits(3);
	}

	public void setAttackCooldown(int p_189428_1_) {
		this.attackCooldown = p_189428_1_;
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getAttackTarget() == null ? false : this.isSpellInMainhand();
	}

	protected boolean isSpellInMainhand() {
		return !this.entity.getHeldItemMainhand().func_190926_b()
				&& this.entity.getHeldItemMainhand().getItem() == InfernumItems.SPELL_PAGE
				&& ItemSpellPage.getSpell(this.entity.getHeldItemMainhand()).equals(InfernumSpells.WITHERING_BOLT);
	}

	public boolean continueExecuting() {
		return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isSpellInMainhand();
	}

	public void startExecuting() {
		super.startExecuting();
	}

	public void resetTask() {
		super.resetTask();
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

		if (entitylivingbase != null) {
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			boolean flag1 = this.seeTime > 0;

			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
				this.entity.getNavigator().clearPathEntity();
				++this.strafingTime;
			} else {
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F,
						this.strafingClockwise ? 0.5F : -0.5F);
				this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
			} else {
				this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}
			
			if (attackTime >= 0) {
				if (this.entity instanceof EntityPigZombieMage) {
					((EntityPigZombieMage) this.entity).chargeAnimation();
				}
			}

			if (this.entity.isHandActive()) {
				if (!flag && this.seeTime < -60) {
					this.entity.resetActiveHand();
				} else if (flag) {

					if (this.entity instanceof IRangedAttackMob) {
						this.entity.resetActiveHand();
						((IRangedAttackMob) this.entity).attackEntityWithRangedAttack(entitylivingbase, 0.5F);

						this.attackTime = this.attackCooldown;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				this.entity.setActiveHand(EnumHand.MAIN_HAND);
			}
		}
	}

}
