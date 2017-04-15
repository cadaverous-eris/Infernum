package infernum.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

public class InfernumItems {
	
	public static ItemZombiePigmanHeart ZOMBIE_PIGMAN_HEART;
	public static ItemSpellBook SPELL_BOOK;
	
	public static void init() {
		ZOMBIE_PIGMAN_HEART = new ItemZombiePigmanHeart();
		SPELL_BOOK = new ItemSpellBook();
	}
	
	public static void initItemModels() {
		ZOMBIE_PIGMAN_HEART.initModel();
		SPELL_BOOK.initModel();
	}

}
