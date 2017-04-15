package infernum.common.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class Spell {
	
	private final int cost;
	private final int useTime;
	private final int rarity;
	
	public Spell(int cost, int useTime, int rarity) {
		this.cost = cost;
		this.useTime = useTime;
		this.rarity = rarity;
	}
	
	public abstract void onCast(World world, EntityPlayer player);
	
	public abstract void onCastFinish(World world, EntityPlayer player);
	
	public abstract void onCastTick(World world, EntityPlayer player);
	
	public int getCost() {
		return this.cost;
	}
	
	public int getUseTime() {
		return this.useTime;
	}
	
	public int getRarity() {
		return this.rarity;
	}

}
