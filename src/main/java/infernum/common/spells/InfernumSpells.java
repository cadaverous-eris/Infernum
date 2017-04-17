package infernum.common.spells;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class InfernumSpells {
	
	public static Spell BLAZING_FIST;
	public static Spell SOUL_DRAIN;
	public static Spell GHASTLY_PULSE;
	public static Spell OBSIDIAN_SKIN;
	public static Spell WITHERING_BOLT;
	
	public static void init() {
		BLAZING_FIST = new SpellBlazingFist();
		SOUL_DRAIN = new SpellSoulDrain();
		GHASTLY_PULSE = new SpellGhastlyPulse();
		OBSIDIAN_SKIN = new SpellObsidianSkin();
		WITHERING_BOLT = new SpellWitheringBolt();
	}

}
