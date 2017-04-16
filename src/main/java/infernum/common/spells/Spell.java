package infernum.common.spells;

import infernum.Infernum;
import infernum.common.items.IInfernalPowerItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class Spell {

	public static final Spell EMPTY_SPELL = new Spell("empty", 0, 0, 0) {
	};

	private final ResourceLocation registryName;
	private final String unlocalizedName;
	private final int cost;
	private final int useTime;
	private final int rarity;

	public Spell(String name, int cost, int useTime, int rarity) {
		this.unlocalizedName = Infernum.MODID + "." + name;
		this.cost = cost;
		this.useTime = useTime;
		this.rarity = rarity;
		this.registryName = new ResourceLocation(name);
		Infernum.SPELL_REGISTRY.register(this);
	}

	public void onCast(World world, EntityPlayer player, ItemStack stack) {
	}

	public void onCastFinish(World world, EntityPlayer player, ItemStack stack) {
	}

	public void onCastTick(World world, EntityPlayer player, ItemStack stack) {
	}

	public boolean onCastMelee(World world, EntityPlayer player, Entity entity, ItemStack stack) {
		return false;
	}

	protected boolean consumePower(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.OFF_HAND);
		if (stack.getItem() instanceof IInfernalPowerItem) {
			if ((stack.getMaxDamage() - stack.getItemDamage()) >= getCost()) {
				stack.damageItem(cost, player);
				return true;
			}
		}
		return false;
	}

	public ResourceLocation getRegistryName() {
		return this.registryName;
	}

	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}

	public int getCost() {
		return this.cost;
	}

	public int getUseTime() {
		return this.useTime;
	}

	public int getRarity() {
		return this.rarity;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Spell)) {
			return false;
		}
		Spell spell2 = (Spell) o;
		if (!getRegistryName().equals(spell2.getRegistryName())) {
			return false;
		}
		if (!getUnlocalizedName().equals(spell2.getUnlocalizedName())) {
			return false;
		}
		if (getCost() != spell2.getCost()) {
			return false;
		}
		if (getUseTime() != spell2.getUseTime()) {
			return false;
		}
		if (getRarity() != spell2.getRarity()) {
			return false;
		}
		return true;
	}

}
