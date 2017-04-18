package infernum.common.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import infernum.Infernum;
import infernum.common.spells.InfernumSpells;
import infernum.common.spells.Spell;
import infernum.common.spells.Spell.EnumCastingType;
import infernum.common.spells.SpellRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemSpellBook extends ItemBase {
	
	public static final int GUI_ID = 0;
	
	public static final int INV_SIZE = 27;

	public ItemSpellBook() {
		super("infernal_spell_book");
		setMaxStackSize(0);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public static Spell getSpell(ItemStack stack) {
		if (!stack.getItem().equals(InfernumItems.SPELL_BOOK)) {
			return Spell.EMPTY_SPELL;
		}
		ItemStack spellStack = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(stack.getMetadata());
		return ItemSpellPage.getSpell(spellStack);
	}

	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (!getSpell(stack).equals(Spell.EMPTY_SPELL)) {
			tooltip.add(TextFormatting.DARK_RED + "" + TextFormatting.ITALIC
					+ I18n.translateToLocal(getSpell(stack).getUnlocalizedName() + ".name"));
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		switch (getSpell(stack).getCastingType()) {
		case INSTANT:
			return EnumAction.NONE;
		case MELEE:
			return EnumAction.NONE;
		case CONTINUOUS:
			return EnumAction.BOW;
		case CHARGED:
			return EnumAction.BOW;
		}
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);

		ItemStack stack = player.getHeldItem(hand);

		this.getSpell(stack).onCast(world, player, stack);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		Spell spell = getSpell(stack);
		if (entityLiving instanceof EntityPlayer && spell.getCastingType() == EnumCastingType.CHARGED) {
			if (this.getMaxItemUseDuration(stack) - timeLeft >= spell.getUseTime()) {
				EntityPlayer player = (EntityPlayer) entityLiving;
				this.getSpell(stack).onCastFinish(world, player, stack);
			}
		}
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entityLivingBase, int count) {
		if (entityLivingBase instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLivingBase;
			this.getSpell(stack).onCastTick(player.getEntityWorld(), player, stack);
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return this.getSpell(stack).onCastMelee(player.getEntityWorld(), player, entity, stack);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		if (this.getSpell(stack).getRarity() >= 4) {
			return EnumRarity.EPIC;
		}
		if (this.getSpell(stack).getRarity() >= 3) {
			return EnumRarity.RARE;
		}
		if (this.getSpell(stack).getRarity() >= 3) {
			return EnumRarity.UNCOMMON;
		}
		return EnumRarity.COMMON;
	}

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", -1D, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0D, 0));
		}
		return multimap;
	}

	@Nullable
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			@Nullable NBTTagCompound nbt) {
		return new CapabilitySpellBook();
	}
	
	public void initModel() {
		for (int meta = 0; meta < INV_SIZE; meta++) {
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName().toString()));
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

}
