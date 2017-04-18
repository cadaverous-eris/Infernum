package infernum.common.items;

import java.util.ArrayList;
import java.util.List;

import infernum.Infernum;
import infernum.common.spells.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfernumItems {
	
	public static ItemZombiePigmanHeart ZOMBIE_PIGMAN_HEART;
	public static ItemBeatingPigmanHeart BEATING_PIGMAN_HEART;
	//public static ItemSpellBook SPELL_BOOK;
	public static ItemSpellPage SPELL_PAGE;
	
	public static void init() {
		ZOMBIE_PIGMAN_HEART = new ItemZombiePigmanHeart();
		BEATING_PIGMAN_HEART = new ItemBeatingPigmanHeart();
		//SPELL_BOOK = new ItemSpellBook();
		SPELL_PAGE = new ItemSpellPage();
	}
	
	public static void initItemModels() {
		ZOMBIE_PIGMAN_HEART.initModel();
		BEATING_PIGMAN_HEART.initModel();
		//SPELL_BOOK.initModel();
		SPELL_PAGE.initModel();
	}

}
