package infernum.common.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import infernum.Infernum;
import infernum.client.renderers.RenderInvisibleEntity;
import infernum.client.renderers.RenderPigMage;
import infernum.client.renderers.RenderPigZombieMage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfernumEntities {
	
	public static void init() {
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(Infernum.MODID + ":withering_bolt"), EntityWitheringBolt.class, "withering_bolt", id++, Infernum.instance, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Infernum.MODID + ":zombie_pigman_mage"), EntityPigZombieMage.class, "zombie_pigman_mage", id++, Infernum.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Infernum.MODID + ":zombie_pigman_mage"), 14581128, 11799808);
		EntityRegistry.registerModEntity(new ResourceLocation(Infernum.MODID + ":pigman_mage"), EntityPigMage.class, "pigman_mage", id++, Infernum.instance, 64, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation(Infernum.MODID + ":pigman_mage"), 14581128, 11665527);
		EntityRegistry.registerModEntity(new ResourceLocation(Infernum.MODID + ":fire_breath"), EntityFireBreath.class, "fire_breath", id++, Infernum.instance, 64, 1, true);
		
		List<Biome> spawnBiomes = new ArrayList<Biome>();
		spawnBiomes.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER));
		spawnBiomes.add(Biomes.HELL);
		for (Biome b : spawnBiomes) {
			System.out.println(b.getBiomeName());
		}
		EntityRegistry.addSpawn(EntityPigZombieMage.class, 150, 1, 2, EnumCreatureType.MONSTER, spawnBiomes.toArray(new Biome[spawnBiomes.size()]));
	}
	
	@SideOnly(Side.CLIENT)
	public static void initRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityWitheringBolt.class, new RenderInvisibleEntity.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPigZombieMage.class, new RenderPigZombieMage.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityPigMage.class, new RenderPigMage.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireBreath.class, new RenderInvisibleEntity.Factory());
	}

}
