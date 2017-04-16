package infernum.common.spells;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class InfernumSpells {
	
	public static Spell BLAZING_FIST;
	public static Spell SOUL_DRAIN;
	
	public static void init() {
		BLAZING_FIST = new SpellBlazingFist();
		SOUL_DRAIN = new SpellSoulDrain();
	}

}
