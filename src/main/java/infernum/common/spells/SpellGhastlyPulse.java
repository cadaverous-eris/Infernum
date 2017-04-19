package infernum.common.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpellGhastlyPulse extends Spell {

	public SpellGhastlyPulse() {
		super("ghastly_pulse", 9, 0, 3, EnumCastingType.INSTANT);
	}

	@Override
	public void onCast(World world, EntityPlayer player, ItemStack stack) {

		if (consumePower(player)) {
			if (world.isRemote) {
				Vec3d lookVector = player.getLookVec();
				if (!player.isSneaking()) {
					player.setVelocity(lookVector.xCoord * 1.2, lookVector.yCoord * 1.2, lookVector.zCoord * 1.2);
				} else {
					player.setVelocity(lookVector.xCoord * -1.2, lookVector.yCoord * -1.2, lookVector.zCoord * -1.2);
				}
				player.fallDistance = 0;
				player.velocityChanged = true;
			} else if (!world.isRemote) {
				player.fallDistance = 0;
				if (world instanceof WorldServer) {
					((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, true, player.posX, player.posY,
							player.posZ, world.rand.nextInt(12) + 8, 0.5, 0.5, 0.5, 0.5);
				}
			}

		}

	}

}
