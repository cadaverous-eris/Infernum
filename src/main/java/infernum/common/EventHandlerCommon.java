package infernum.common;

import infernum.common.entities.EntityPigZombieMage;
import infernum.common.items.InfernumItems;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCombatEvent.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class EventHandlerCommon {

	@SubscribeEvent
	public void onZombiePigmanDropsEvent(LivingDropsEvent event) {
		if (event.getEntityLiving() instanceof EntityPigZombie) {
			if (event.getEntityLiving().getEntityWorld().rand.nextInt(16) == 0) {
				World world = event.getEntityLiving().getEntityWorld();
				EntityPigZombie entity = (EntityPigZombie) event.getEntityLiving();
				ItemStack stack = new ItemStack(InfernumItems.ZOMBIE_PIGMAN_HEART, 1, world.rand.nextInt(252) + 1);
				event.getDrops().add(new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack));
			}
		} else if (event.getEntityLiving() instanceof EntityPigZombieMage) {
			if (event.getEntityLiving().getEntityWorld().rand.nextInt(8) == 0) {
				World world = event.getEntityLiving().getEntityWorld();
				EntityPigZombieMage entity = (EntityPigZombieMage) event.getEntityLiving();
				ItemStack stack = new ItemStack(InfernumItems.BEATING_PIGMAN_HEART, 1, world.rand.nextInt(1015) + 1);
				event.getDrops().add(new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack));
			}
		}
	}

	/*
	@SubscribeEvent
	public void onZombiePigmanSpawnEvent(LivingSpawnEvent.SpecialSpawn event) {
		if (event.getEntityLiving() instanceof EntityPigZombie && event.getWorld().rand.nextInt(8) == 0) {
			event.setCanceled(true);
			event.getEntityLiving().setDead();
			EntityPigZombieMage pigMage = new EntityPigZombieMage(event.getWorld());
			pigMage.setPosition(event.getX(), event.getY(), event.getZ());
			pigMage.onInitialSpawn(event.getWorld().getDifficultyForLocation(new BlockPos(pigMage)), null);
			event.getWorld().spawnEntity(pigMage);
		}
	}
	*/

}
