package infernum.client;

import infernum.client.models.BakedModelSpellPage;
import infernum.client.models.ModelKnowledgeBook;
import infernum.client.models.ModelSpellPage;
import infernum.common.items.IInfernalPowerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandlerClient {
	
	public static ResourceLocation HUD_TEXTURE = new ResourceLocation("infernum:textures/gui/hud.png");
	
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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlayPostEvent(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.ALL) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayer player = minecraft.player;
			if (player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IInfernalPowerItem) {
				int maxPower = player.getHeldItem(EnumHand.OFF_HAND).getMaxDamage();
				int power = maxPower - player.getHeldItem(EnumHand.OFF_HAND).getItemDamage();
				int width = event.getResolution().getScaledWidth();
				int height = event.getResolution().getScaledHeight();
				GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
				Minecraft.getMinecraft().getTextureManager().bindTexture(HUD_TEXTURE);
				GlStateManager.enableBlend();
				
				int y = (height / 2) - (42 / 2);
				int x = 3;
				int barHeight = (int) (((float) power / (float) maxPower) * 32);
				
				gui.drawTexturedModalRect(x, y, 0, 0, 10, 42);
				gui.drawTexturedModalRect(x + 1, y + 5 + (32 - barHeight), 10, 5, 8, barHeight);
				
				Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
				GlStateManager.disableBlend();
			}
			
		}
	}

}
