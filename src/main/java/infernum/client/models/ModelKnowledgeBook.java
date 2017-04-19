package infernum.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import infernum.common.items.InfernumItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelKnowledgeBook implements IPerspectiveAwareModel {

	private static final ResourceLocation font = new ResourceLocation("minecraft", "textures/font/ascii.png");
    private static final ResourceLocation font2 = new ResourceLocation("minecraft", "font/ascii");
	private IBakedModel baseKnowledgeBookModel;
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
			InfernumItems.KNOWLEDGE_BOOK.getRegistryName().toString());

	public ModelKnowledgeBook(IBakedModel parentModel) {
		this.baseKnowledgeBookModel = parentModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baseKnowledgeBookModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseKnowledgeBookModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseKnowledgeBookModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseKnowledgeBookModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseKnowledgeBookModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseKnowledgeBookModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return KnowledgeBookOverrideList.INSTANCE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (baseKnowledgeBookModel instanceof IPerspectiveAwareModel) {
			Matrix4f matrix4f = ((IPerspectiveAwareModel) baseKnowledgeBookModel).handlePerspective(cameraTransformType)
					.getRight();
			return Pair.of(this, matrix4f);
		}
		ItemCameraTransforms itemCameraTransforms = baseKnowledgeBookModel.getItemCameraTransforms();
		ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
		TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
		Matrix4f mat = null;
		if (tr != null) {
			mat = tr.getMatrix();
		}
		return Pair.of(this, mat);
	}
	
	private static BakedModelKnowledgeBook rebake(ModelKnowledgeBook model, String name) {	
		Matrix4f m = new Matrix4f();
        m.m20 = 1f / 128f;
        m.m01 = m.m12 = -m.m20;
        m.m33 = 1;
        Matrix3f rotation = new Matrix3f();
        m.getRotationScale(rotation);
        Matrix3f angleZ = new Matrix3f();
        angleZ.rotZ(-1.5708F);
        rotation.mul(rotation, angleZ);
        m.setRotationScale(rotation);
        m.setScale(0.66666666667F * m.getScale());
        m.setTranslation(new Vector3f(0.1875F, 0.2505F, 0.125F));
		SimpleModelFontRenderer fontRenderer = new SimpleModelFontRenderer(Minecraft.getMinecraft().gameSettings, font, Minecraft.getMinecraft().getTextureManager(), false, m, DefaultVertexFormats.ITEM) {
			@Override
			protected float renderUnicodeChar(char c, boolean italic) {
				return super.renderDefaultChar(126, italic);
			}
		};
		int maxLineWidth = 96;
		TextureAtlasSprite fontSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(font2.toString());
		List<BakedQuad> textQuads = new ArrayList<BakedQuad>();
		
		fontRenderer.setSprite(fontSprite);
        fontRenderer.setFillBlanks(false);
        
        int yOffset = 2;
        String title = I18n.translateToLocal(name);
        List<String> lines = fontRenderer.listFormattedStringToWidth(title, maxLineWidth);
        for (int line = 0; line < lines.size(); line++) {
        	int offset = ((maxLineWidth - fontRenderer.getStringWidth(lines.get(line))) / 2);
        	fontRenderer.drawString(lines.get(line), offset, yOffset, 0x00000000);
        	yOffset += (fontRenderer.FONT_HEIGHT - 1 + 4);
        }
		
        textQuads.addAll(fontRenderer.build());
        return new BakedModelKnowledgeBook(model, textQuads);
	}

	public static final class KnowledgeBookOverrideList extends ItemOverrideList {
		public static final KnowledgeBookOverrideList INSTANCE = new KnowledgeBookOverrideList();
		
		private final Map<String, IBakedModel> cache;

		public KnowledgeBookOverrideList() {
			super(ImmutableList.<ItemOverride>of());
			this.cache = new HashMap<String, IBakedModel>();
		}
		
		public void clearCache() {
			this.cache.clear();
		}

		@Override
		@Nonnull
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
				@Nullable World world, @Nullable EntityLivingBase entity) {
			
			String name = stack.getDisplayName();
			
			if (!cache.containsKey(name)) {
				ModelKnowledgeBook model = (ModelKnowledgeBook) originalModel;
				TextureAtlasSprite fontSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(font2.toString());
				
				BakedModelKnowledgeBook bakedBakedModel = rebake(model, name);
				cache.put(name, bakedBakedModel);

				return bakedBakedModel;
			}
			return cache.get(name);
		}

	}

}
