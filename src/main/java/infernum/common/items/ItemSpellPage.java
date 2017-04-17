package infernum.common.items;

import java.util.List;

import com.google.common.collect.Multimap;

import infernum.Infernum;
import infernum.common.spells.Spell;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpellPage extends ItemBase {

	public ItemSpellPage() {
		super("spell_page");
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (Spell spell : Infernum.SPELL_REGISTRY.getValues()) {
			if (!spell.equals(Spell.EMPTY_SPELL)) {
				ItemStack stack = new ItemStack(this);
				setSpell(stack, spell);
				subItems.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (!getSpell(stack).equals(Spell.EMPTY_SPELL)) {
			tooltip.add(TextFormatting.DARK_RED + "" + TextFormatting.ITALIC
					+ I18n.translateToLocal(getSpell(stack).getUnlocalizedName() + ".name"));
		}
	}

	public static Spell getSpell(ItemStack stack) {
		if (stack.getItem() instanceof ItemSpellPage && stack.getTagCompound() != null
				&& stack.getTagCompound().getString("Spell") != null) {
			String regName = stack.getTagCompound().getString("Spell");
			if (Infernum.SPELL_REGISTRY.containsKey(new ResourceLocation(regName))) {
				return Infernum.SPELL_REGISTRY.getSpell(new ResourceLocation(regName));
			}
		}
		return Spell.EMPTY_SPELL;
	}

	public static void setSpell(ItemStack stack, Spell spell) {
		if (stack.getItem() instanceof ItemSpellPage && stack.getSubCompound("Spell") == null) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) {
				nbt = new NBTTagCompound();
			}
			nbt.setString("Spell", spell.getRegistryName().getResourcePath());
			stack.setTagCompound(nbt);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return getSpell(stack).getUseTime();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		this.getSpell(stack).onCast(world, player, stack);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			this.getSpell(stack).onCastFinish(world, player, stack);
		}
		return stack;
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

}
