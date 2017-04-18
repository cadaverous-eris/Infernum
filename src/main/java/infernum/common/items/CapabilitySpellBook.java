package infernum.common.items;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilitySpellBook implements ICapabilitySerializable<NBTTagCompound> {

	protected ItemStackHandler spells = new ItemStackHandler(ItemSpellBook.INV_SIZE) {
		@Override
		public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
			if (stack.getItem().equals(InfernumItems.SPELL_PAGE)) {
				super.setStackInSlot(slot, stack);
			}
		}

		@Override
		@Nonnull
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (stack.getItem().equals(InfernumItems.SPELL_PAGE)) {
				return super.insertItem(slot, stack, simulate);
			}
			return stack;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	};

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) spells;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("spells", spells.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		spells.deserializeNBT(nbt.getCompoundTag("spells"));
	}

}
