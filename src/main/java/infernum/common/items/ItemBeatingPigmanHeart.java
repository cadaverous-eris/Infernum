package infernum.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBeatingPigmanHeart extends ItemBase implements IInfernalPowerItem {

	public ItemBeatingPigmanHeart() {
		super("beating_pigman_heart");
		setMaxDamage(512);
		setMaxStackSize(1);
		setHasSubtypes(false);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn.dimension == -1 && entityIn.ticksExisted % 3 == 0) {
			stack.setItemDamage(stack.getItemDamage() - 1);
		}
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
