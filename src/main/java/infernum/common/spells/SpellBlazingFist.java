package infernum.common.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpellBlazingFist extends Spell {

	public SpellBlazingFist() {
		super("blazing_fist", 5, 0, 0);
	}

	@Override
	public boolean onCastMelee(World world, EntityPlayer player, Entity entity, ItemStack stack) {
		if (!entity.isImmuneToFire() && entity instanceof EntityLivingBase && consumePower(player)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			entityLivingBase.attackEntityFrom(new EntityDamageSourceSpell(player), 4);
			entityLivingBase.setFire(5);
			if (!world.isRemote) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.FLAME, true, entity.posX, entity.posY + (entity.height / 2F), entity.posZ, world.rand.nextInt(8) + 12, entity.width / 2F, entity.height / 2F, entity.width / 2F, 0.01F);
			}
		}
		return false;
	}

}
