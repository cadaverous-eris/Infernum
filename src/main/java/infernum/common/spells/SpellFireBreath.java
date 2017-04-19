package infernum.common.spells;

import infernum.common.entities.EntityFireBreath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpellFireBreath extends Spell {
	
	public SpellFireBreath() {
		super("fire_breath", 1, 0, 2, EnumCastingType.CONTINUOUS);
	}
	
	@Override
	public void onCastTick(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote) {
			
			if (player.getItemInUseCount() % 2 == 0 && consumePower(player)) {
				Vec3d lookVec = player.getLookVec();
				double x = player.posX + lookVec.xCoord;
				double y = player.posY + lookVec.yCoord + player.getEyeHeight();
				double z = player.posZ + lookVec.zCoord;
				EntityFireBreath fireBreath = new EntityFireBreath(world, player, 5);
				fireBreath.setPosition(x, y, z);
				fireBreath.setAim(player, player.rotationPitch, player.rotationYaw, 1F, 0.1F);
				world.spawnEntity(fireBreath);
			}
		}
	}
	
}
