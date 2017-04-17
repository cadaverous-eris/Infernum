package infernum.client.models;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedModelSpellPage implements IPerspectiveAwareModel {

	private final ModelSpellPage parentModel;
	private final String description;
	private final TextureAtlasSprite fontTexture;
	
	private static final ResourceLocation font = new ResourceLocation("minecraft", "textures/font/ascii.png");
    private static final ResourceLocation font2 = new ResourceLocation("minecraft", "font/ascii");
    private SimpleModelFontRenderer fontRenderer;

	public BakedModelSpellPage(ModelSpellPage parent, String desc, TextureAtlasSprite fontTexture) {
		this.parentModel = parent;
		this.description = desc;
		this.fontTexture = fontTexture;
		Matrix4f m = new Matrix4f();
        m.m20 = 1f / 128f;
        m.m01 = m.m12 = -m.m20;
        m.m33 = 1;
        Matrix3f rotation = new Matrix3f();
        m.getRotationScale(rotation);
        Matrix3f angle = new Matrix3f();
        angle.rotZ(1.5708F);
        rotation.mul(rotation, angle);
        m.setRotationScale(rotation);
        m.setScale(0.75F * m.getScale());
        m.setTranslation(new Vector3f(0.8625F, 0.095F, 0.8625F));
		this.fontRenderer = new SimpleModelFontRenderer(Minecraft.getMinecraft().gameSettings, font, Minecraft.getMinecraft().getTextureManager(), false, m, DefaultVertexFormats.ITEM) {
			@Override
			protected float renderUnicodeChar(char c, boolean italic) {
				return super.renderDefaultChar(126, italic);
			}
		};
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> combinedQuadsList = new ArrayList(parentModel.getQuads(state, side, rand));

		if (side == null) {
			TextureAtlasSprite fontSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(font2.toString());
			fontRenderer.setSprite(fontSprite);
            fontRenderer.setFillBlanks(false);
            String text = I18n.translateToLocal(description);
            List<String> lines = fontRenderer.listFormattedStringToWidth(text, 126);
            int y = 0;
            for (String line: lines) {
            	int offset = ((126 - fontRenderer.getStringWidth(line)) / 2);
            	fontRenderer.drawString(line, offset, y * fontRenderer.FONT_HEIGHT, 0x007A0000);
            	y++;
            }
            combinedQuadsList.addAll(fontRenderer.build());
		}
		
		return combinedQuadsList;
	}
	
	public static List<String> splitLines(String string, int maxWidth, FontRenderer fr) {
		String text = string + " ";
		List<String> lines = new ArrayList<String>();
		
		List<Integer> spaceIndices = new ArrayList<Integer>();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				spaceIndices.add(i);
			}
		}
		int startIndex = 0;
		for (int i = spaceIndices.size() - 1; i >= 0; i--) {
			if (i > spaceIndices.size()) {
				i = spaceIndices.size() - 1;
			}
			if (i < 0) {
				return lines;
			}
			String subText = text.substring(startIndex, spaceIndices.get(i));
			if (fr.getStringWidth(subText) <= maxWidth) {
				lines.add(subText);
				startIndex = spaceIndices.get(i) + 1;
				for (int j = i; j >= 0; j--) {
					spaceIndices.remove(j);
				}
				i = spaceIndices.size();
			}
		}
		
		return lines;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return parentModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return parentModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return parentModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return parentModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return parentModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		throw new UnsupportedOperationException("The finalized model does not have an override list.");
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (parentModel instanceof IPerspectiveAwareModel) {
			Matrix4f matrix4f = ((IPerspectiveAwareModel) parentModel).handlePerspective(cameraTransformType)
					.getRight();
			return Pair.of(this, matrix4f);
		}
		ItemCameraTransforms itemCameraTransforms = parentModel.getItemCameraTransforms();
		ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
		TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
		Matrix4f mat = null;
		if (tr != null) {
			mat = tr.getMatrix();
		}
		return Pair.of(this, mat);
	}

}
