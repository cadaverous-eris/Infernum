package infernum.common.spells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.BiMap;

import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public class SpellRegistry {
	
	private Map<ResourceLocation, Spell> REGISTRY = new HashMap<ResourceLocation, Spell>();
	
	public boolean register(Spell spell) {
		if (REGISTRY.containsValue(spell)) {
			return false;
		}
		REGISTRY.put(spell.getRegistryName(), spell);
		return true;
	}
	
	public Spell getSpell(ResourceLocation key) {
		if (REGISTRY.containsKey(key)) {
			return REGISTRY.get(key);
		}
		return Spell.EMPTY_SPELL;
	}
	
	public boolean containsKey(ResourceLocation key) {
		return REGISTRY.containsKey(key);
	}
	
	public boolean containsValue(Spell spell) {
		return REGISTRY.containsValue(spell);
	}
	
	public Collection<Spell> getValues() {
		return REGISTRY.values();
	}
	
	public Set<ResourceLocation> getKeys() {
		return REGISTRY.keySet();
	}
	
	public Spell randSpell(Random rand) {
		Collection<Spell> spells = REGISTRY.values();
		spells.remove(Spell.EMPTY_SPELL);
		Spell[] spellList = spells.toArray(new Spell[spells.size()]);
		return spellList[rand.nextInt(spellList.length)];
	}

}
