package infernum.common.spells;

import infernum.common.entities.EntityWitheringBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import network.PacketHandler;

public class SpellWitheringBolt extends Spell {

	public SpellWitheringBolt() {
		super("withering_bolt", 3, 20, 1, EnumCastingType.CHARGED);
	}
	
	@Override
	public void onCastTick(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote && world instanceof WorldServer) {
			Vec3d lookVec = player.getLookVec();
			double x = player.posX + lookVec.xCoord;
			double y = player.posY + lookVec.yCoord + player.eyeHeight;
			double z = player.posZ + lookVec.zCoord;
			((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, x, y, z, world.rand.nextInt(2), 0.01, 0.01, 0.01, 0);
		}
	}
	
	@Override
	public void onCastFinish(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote && consumePower(player)) {
			Vec3d lookVec = player.getLookVec();
			double x = player.posX + lookVec.xCoord;
			double y = player.posY + lookVec.yCoord + player.eyeHeight;
			double z = player.posZ + lookVec.zCoord;
			EntityWitheringBolt bolt = new EntityWitheringBolt(world, player, 1, x, y, z);
			bolt.setAim(player, player.rotationPitch, player.rotationYaw, 0.75F, 0.08F);
			world.spawnEntity(bolt);
		}
	}

}
