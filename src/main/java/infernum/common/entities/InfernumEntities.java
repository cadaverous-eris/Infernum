package infernum.common.entities;

import infernum.Infernum;
import infernum.client.renderers.RenderInvisibleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfernumEntities {
	
	public static void init() {
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(Infernum.MODID + ":withering_bolt"), EntityWitheringBolt.class, "withering_bolt", id++, Infernum.instance, 64, 1, true);
	}
	
	@SideOnly(Side.CLIENT)
	public static void initRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityWitheringBolt.class, new RenderInvisibleEntity.Factory());
	}

}
