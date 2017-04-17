package infernum.client;

import infernum.client.models.BakedModelSpellPage;
import infernum.client.models.ModelSpellPage;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerClient {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		Object object = event.getModelRegistry().getObject(ModelSpellPage.modelResourceLocation);
		if (object instanceof IBakedModel) {
			IBakedModel existingModel = (IBakedModel) object;
			ModelSpellPage customModel = new ModelSpellPage(existingModel);
			event.getModelRegistry().putObject(ModelSpellPage.modelResourceLocation, customModel);
		}
	}

}
