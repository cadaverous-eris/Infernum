package infernum.client.renderers;

import infernum.common.entities.EntityWitheringBolt;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderInvisibleEntity extends Render<Entity> {

	protected RenderInvisibleEntity(RenderManager renderManager) {
		super(renderManager);
	}

	public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	public static class Factory implements IRenderFactory<Entity> {

		@Override
		public Render<? super Entity> createRenderFor(RenderManager manager) {
			return new RenderInvisibleEntity(manager);
		}

	}

}
