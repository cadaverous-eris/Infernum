package infernum.common;

import infernum.common.items.InfernumItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerCommon {

	@SubscribeEvent
	public void onZombiePigmanDropsEvent(LivingDropsEvent event) {
		if (event.getEntityLiving().getEntityWorld().rand.nextInt(16) == 0) {
			if (event.getEntityLiving() instanceof EntityPigZombie) {
				World world = event.getEntityLiving().getEntityWorld();
				EntityPigZombie entity = (EntityPigZombie) event.getEntityLiving();
				ItemStack stack = new ItemStack(InfernumItems.ZOMBIE_PIGMAN_HEART, 1, world.rand.nextInt(252) + 1);
				event.getDrops().add(new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack));
			}
		}
	}
	
}
