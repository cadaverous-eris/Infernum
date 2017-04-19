package infernum.client.renderers;

import infernum.client.models.ModelTome;
import infernum.common.items.InfernumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderKnowledgeTome {

	private static final ModelTome modelTome = new ModelTome();
	private static final ResourceLocation texture = new ResourceLocation("infernum:textures/entity/knowledge_book.png");

	public static int ticksOpen = 0;
	public static int pageFlipTicks = 0;
	public static float coverAngles = 0;
	public static float flipPageAngle = 0;
	public static int page = 0;
	public static int prevPage = 0;
	public static int totalPages = 3;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderItem(RenderSpecificHandEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (event.getHand() == EnumHand.MAIN_HAND) {
			if (minecraft.gameSettings.thirdPersonView != 0
					|| minecraft.player.getHeldItem(EnumHand.MAIN_HAND).func_190916_E() <= 0
					|| minecraft.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != InfernumItems.KNOWLEDGE_BOOK) {
				reset();
				return;
			}
			if ((minecraft.player.getHeldItem(EnumHand.OFF_HAND).equals(ItemStack.field_190927_a))) {
				event.setCanceled(true);
				try {
					ticksOpen++;
					doRender(event.getPartialTicks(), minecraft.player.getHeldItem(event.getHand()));
				} catch (Throwable throwable) {

				}
			} else {
				reset();
			}
		}
	}

	private static void doRender(float partialTicks, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		
		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(texture);
		
		//GlStateManager.translate(0F, -.25F, -2.5F);
		//GlStateManager.translate(0F, -.1F, -1F);

		GlStateManager.translate(0F, -.0125F, -1F);
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.rotate(-90F, 1F, 0F, 0F);
		GlStateManager.rotate(-90F, 0F, 0F, 1F);
		
		//GlStateManager.rotate(90F, 6F, 5F, 1F);

		if (page >= totalPages) {
			page = totalPages - 1;
		}
		if (page < 0) {
			page = 0;
		}
		
		if (pageFlipTicks <= 0) {
			prevPage = page;
		} else {
			if (prevPage < page) {
				flipPageAngle -= (0.1309F * 2F);
			} else if (prevPage > page) {
				flipPageAngle += (0.1309F * 2F);
			}
			modelTome.setRotateAngle(modelTome.loosePage, 0, 0, flipPageAngle);
		}
		
		if (ticksOpen < 10) {
			coverAngles += 0.1309F;
			if (coverAngles > 1.309F) {
				coverAngles = 1.309F;
			}
		}
		modelTome.setRotateAngle(modelTome.frontCover, 0, 0, -coverAngles);
		modelTome.setRotateAngle(modelTome.backCover, 0, 0, coverAngles);
		if (pageFlipTicks > 0) {
			modelTome.loosePage.isHidden = false;
		} else {
			modelTome.loosePage.isHidden = true;
		}
		modelTome.render(null, 0F, 0F, 0F, 0F, 0F, 1F / 16F);
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		float coverAngleDegrees = (float) (coverAngles * 180 / Math.PI);
		//right page
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, -.1255F, .4F);
		GlStateManager.rotate(90F + coverAngleDegrees, 0F, 0F, 1F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(.0625F, 0F, 0.0F);
		GlStateManager.scale(.00375F, .00375F, -.00375F);
		String rightText;
		if (prevPage < page) {
			rightText = "knowledgebook.page_" + prevPage + ".right";
		} else {
			rightText = "knowledgebook.page_" + page + ".right";
		}
		font.drawSplitString(I18n.format(rightText), 0, 0, 140, 0);
		GlStateManager.popMatrix();
		
		//left page
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, -.1255F, .4F);
		GlStateManager.rotate(270F - coverAngleDegrees, 0F, 0F, 1F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(-(9 * .0625F), 0F, 0.0F);
		GlStateManager.scale(.00375F, .00375F, -.00375F);
		String leftText;
		if (prevPage < page) {
			leftText = "knowledgebook.page_" + prevPage + ".left";
		} else {
			leftText = "knowledgebook.page_" + page + ".left";
		}
		font.drawSplitString(I18n.format(leftText), 0, 0, 140, 0);
		GlStateManager.popMatrix();
		
		//flipping page
		if (pageFlipTicks > 0) {
			float flipPageAngleDegrees = (float) (flipPageAngle * 180 / Math.PI);
			
			//front side
			GlStateManager.pushMatrix();
			GlStateManager.translate(0F, -.1255F, .4F);
			GlStateManager.rotate(270F - flipPageAngleDegrees, 0F, 0F, 1F);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.translate(-(9 * .0625F), 0F, 0.0F);
			GlStateManager.scale(.00375F, .00375F, -.00375F);
			String flipFrontText;
			if (prevPage > page) {
				flipFrontText = "knowledgebook.page_" + page + ".right";
			} else {
				flipFrontText = "knowledgebook.page_" + prevPage + ".right";
			}
			//font.drawSplitString(I18n.format(rightText), 0, 0, 140, 0);
			GlStateManager.popMatrix();
			
			//back side
			GlStateManager.pushMatrix();
			GlStateManager.translate(0F, -.1255F, .4F);
			GlStateManager.rotate(90F + flipPageAngleDegrees, 0F, 0F, 1F);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.translate(.0625F, 0F, 0.0F);
			GlStateManager.scale(.00375F, .00375F, -.00375F);
			String flipBackText;
			if (prevPage < page) {
				flipBackText = "knowledgebook.page_" + page + ".left";
			} else {
				flipBackText = "knowledgebook.page_" + prevPage + ".left";
			}
			//font.drawSplitString(I18n.format(rightText), 0, 0, 140, 0);
			GlStateManager.popMatrix();
			
			pageFlipTicks--;
		}
		
		GlStateManager.popMatrix();
	}

	private void reset() {
		ticksOpen = 0;
		pageFlipTicks = 0;
		coverAngles = 0;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseClick(MouseEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (minecraft.gameSettings.thirdPersonView != 0
				|| player.getHeldItem(EnumHand.MAIN_HAND).func_190916_E() <= 0
				|| player.getHeldItem(EnumHand.MAIN_HAND).getItem() != InfernumItems.KNOWLEDGE_BOOK) {
			return;
		}
		if (!player.getHeldItem(EnumHand.OFF_HAND).equals(ItemStack.field_190927_a)) {
			return;
		}
		if (ticksOpen < 10 || pageFlipTicks > 0) {
			return;
		}
		
		if (event.getButton() == 0 && event.isButtonstate()) {
			event.setCanceled(true);
			if (page > 0 && pageFlipTicks <= 0 && ticksOpen >= 10) {
				page--;
				pageFlipTicks = 10;
				flipPageAngle = -coverAngles;
			}
		}
		if (event.getButton() == 1 && event.isButtonstate()) {
			event.setCanceled(true);
			if (page < totalPages - 1 && pageFlipTicks <= 0 && ticksOpen >= 10) {
				page++;
				pageFlipTicks = 10;
				flipPageAngle = coverAngles;
			}
		}
		
	}

}
