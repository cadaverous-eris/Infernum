package infernum.client;

import infernum.client.models.BakedModelSpellPage;
import infernum.client.models.ModelKnowledgeBook;
import infernum.client.models.ModelSpellPage;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerClient {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelSpellPageBakeEvent(ModelBakeEvent event) {
		Object object = event.getModelRegistry().getObject(ModelSpellPage.modelResourceLocation);
		if (object instanceof IBakedModel) {
			IBakedModel existingModel = (IBakedModel) object;
			ModelSpellPage spellPageModel = new ModelSpellPage(existingModel);
			event.getModelRegistry().putObject(ModelSpellPage.modelResourceLocation, spellPageModel);
			ModelSpellPage.SpellPageOverrideList.INSTANCE.clearCache();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelKnowledgeBookBakeEvent(ModelBakeEvent event) {
		Object object = event.getModelRegistry().getObject(ModelKnowledgeBook.modelResourceLocation);
		if (object instanceof IBakedModel) {
			IBakedModel existingModel = (IBakedModel) object;
			ModelKnowledgeBook knowledgeBookModel = new ModelKnowledgeBook(existingModel);
			event.getModelRegistry().putObject(ModelKnowledgeBook.modelResourceLocation, knowledgeBookModel);
			ModelKnowledgeBook.KnowledgeBookOverrideList.INSTANCE.clearCache();
		}
	}

}
