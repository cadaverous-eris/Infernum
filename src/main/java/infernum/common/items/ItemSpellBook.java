package infernum.common.items;

import infernum.common.spells.Spell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSpellBook extends ItemBase {

	public ItemSpellBook() {
		super("infernal_spell_book");
		setMaxStackSize(0);
		setMaxDamage(0);
	}

	public boolean isDamageable() {
		return false;
	}

	public void addSpell(ItemStack stack, Spell spell) {
		// TO-DO
	}

	public Spell getCurrentSpell() {
		return null;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		this.getCurrentSpell().onCast(world, player);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			this.getCurrentSpell().onCastFinish(world, player);
		}
		return stack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entityLivingBase, int count) {
		if (entityLivingBase instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLivingBase;
			this.getCurrentSpell().onCastTick(player.getEntityWorld(), player);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		if (this.getCurrentSpell().getRarity() >= 4) {
			return EnumRarity.EPIC;
		}
		if (this.getCurrentSpell().getRarity() >= 3) {
			return EnumRarity.RARE;
		}
		if (this.getCurrentSpell().getRarity() >= 3) {
			return EnumRarity.UNCOMMON;
		}
		return EnumRarity.COMMON;
	}

}
