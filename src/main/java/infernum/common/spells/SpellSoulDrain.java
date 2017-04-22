package infernum.common.spells;

import infernum.common.network.MessageSoulDrainFX;
import infernum.common.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class SpellSoulDrain extends Spell {

	public SpellSoulDrain() {
		super("soul_drain", 2, 0, 2, EnumCastingType.CONTINUOUS);
	}

	@Override
	public void onCastTick(World world, EntityPlayer player, ItemStack stack) {
		if (!world.isRemote) {
			BlockPos centerPos = player.getPosition();
			AxisAlignedBB area = new AxisAlignedBB(centerPos).expandXyz(3F);
			for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(player, area)) {
				if (entity instanceof EntityLivingBase) {
					WorldServer worldServer = (WorldServer) world;
					if (player.getItemInUseCount() % 10 == 0 && consumePower(player)) {
						MessageSoulDrainFX message = new MessageSoulDrainFX(entity.posX,
								entity.posY + (entity.height / 2.0F), entity.posZ, player.posX,
								player.posY + (player.height / 2.0F), player.posZ);
						PacketHandler.INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(
								player.dimension, player.posX, player.posY, player.posZ, 128));
						entity.attackEntityFrom(DamageSource.magic, 1);
						world.spawnEntity(new EntityXPOrb(world, entity.posX, entity.posY + 0.5, entity.posZ, 1));
					}
				}
			}
		}
	}

}
