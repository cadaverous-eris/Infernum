package infernum.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemZombiePigmanHeart extends ItemBase implements IInfernalPowerItem {

	public ItemZombiePigmanHeart() {
		super("zombie_pigman_heart");
		setMaxDamage(128);
		setMaxStackSize(1);
		setHasSubtypes(false);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getPower(ItemStack stack) {
		return stack.getMaxDamage() - stack.getItemDamage();
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRGB(Math.max(0.0F, (float) getDurabilityForDisplay(stack)) / 6.0F, 1.0F, 1.0F);
	}

}
