package infernum.client.renderers;

import infernum.client.models.ModelPigman;
import infernum.common.entities.EntityPigMage;
import infernum.common.entities.EntityPigZombieMage;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderPigMage extends RenderLiving<EntityPigMage> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("infernum:textures/entity/pigman_mage.png");

	protected RenderPigMage(RenderManager renderManager) {
		super(renderManager, new ModelPigman(), 0.5F);
	}

	public ModelPigman getMainModel() {
		return (ModelPigman) super.getMainModel();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPigMage entity) {
		return TEXTURE;
	}

	protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;
		GlStateManager.scale(f, f, f);
	}

	public static class Factory implements IRenderFactory<EntityPigMage> {

		@Override
		public Render<? super EntityPigMage> createRenderFor(RenderManager manager) {
			return new RenderPigMage(manager);
		}

	}

}
