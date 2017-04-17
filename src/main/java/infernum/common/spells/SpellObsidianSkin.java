package infernum.common.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpellObsidianSkin extends Spell {
	
	public SpellObsidianSkin() {
		super("obsidian_skin", 10, 0, 2, EnumCastingType.INSTANT);
	}
	
	@Override
	public void onCast(World world, EntityPlayer player, ItemStack stack) {
		
		if (!world.isRemote && consumePower(player)) {
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 1));
			player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
			if (world instanceof WorldServer) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, player.posX, player.posY + (player.height / 2F), player.posZ, world.rand.nextInt(8) + 12, player.width / 2F, player.height / 2F, player.width / 2F, 0.01F);
			}
		}
		
	}

}
