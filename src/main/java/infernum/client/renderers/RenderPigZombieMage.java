package infernum.client.renderers;

import infernum.client.models.ModelPigZombieMage;
import infernum.common.entities.EntityPigZombieMage;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderPigZombieMage extends RenderBiped<EntityPigZombieMage> {

	private static final ResourceLocation ZOMBIE_PIGMAN_MAGE_TEXTURE = new ResourceLocation(
			"infernum:textures/entity/zombie_pigman_mage.png");

	public RenderPigZombieMage(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelPigZombieMage(), 0.5F);
	}

	protected ResourceLocation getEntityTexture(EntityPigZombieMage entity) {
		return ZOMBIE_PIGMAN_MAGE_TEXTURE;
	}
	
	public static class Factory implements IRenderFactory<EntityPigZombieMage> {

		@Override
		public Render<? super EntityPigZombieMage> createRenderFor(RenderManager manager) {
			return new RenderPigZombieMage(manager);
		}

	}

}
