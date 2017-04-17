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
import infernum.common.items.ItemSpellPage;
import infernum.common.spells.Spell;
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

public class ModelSpellPage implements IPerspectiveAwareModel {

	private static final ResourceLocation font = new ResourceLocation("minecraft", "textures/font/ascii.png");
    private static final ResourceLocation font2 = new ResourceLocation("minecraft", "font/ascii");
	private IBakedModel baseSpellPageModel;
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(
			InfernumItems.SPELL_PAGE.getRegistryName().toString());

	public ModelSpellPage(IBakedModel parentModel) {
		this.baseSpellPageModel = parentModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baseSpellPageModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseSpellPageModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseSpellPageModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseSpellPageModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseSpellPageModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseSpellPageModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return SpellPageOverrideList.INSTANCE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if (baseSpellPageModel instanceof IPerspectiveAwareModel) {
			Matrix4f matrix4f = ((IPerspectiveAwareModel) baseSpellPageModel).handlePerspective(cameraTransformType)
					.getRight();
			return Pair.of(this, matrix4f);
		}
		ItemCameraTransforms itemCameraTransforms = baseSpellPageModel.getItemCameraTransforms();
		ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
		TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
		Matrix4f mat = null;
		if (tr != null) {
			mat = tr.getMatrix();
		}
		return Pair.of(this, mat);
	}
	
	private static BakedModelSpellPage rebake(ModelSpellPage model, Spell spell) {	
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
		SimpleModelFontRenderer fontRenderer = new SimpleModelFontRenderer(Minecraft.getMinecraft().gameSettings, font, Minecraft.getMinecraft().getTextureManager(), false, m, DefaultVertexFormats.ITEM) {
			@Override
			protected float renderUnicodeChar(char c, boolean italic) {
				return super.renderDefaultChar(126, italic);
			}
		};
		TextureAtlasSprite fontSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(font2.toString());
		List<BakedQuad> textQuads = new ArrayList<BakedQuad>();
		
		fontRenderer.setSprite(fontSprite);
        fontRenderer.setFillBlanks(false);
        String name = TextFormatting.BOLD + I18n.translateToLocal(spell.getUnlocalizedName() + ".name");
        String text = I18n.translateToLocal(spell.getUnlocalizedName() + ".desc");
        String castingType = "--" + I18n.translateToLocal(spell.getCastingType().getName() + ".name") + "--";
        
        int yOffset = 0;
        fontRenderer.drawString(name, (126 - fontRenderer.getStringWidth(name)) / 2, yOffset, 0x007A0000);
        yOffset += (fontRenderer.FONT_HEIGHT + 1);
        List<String> lines = fontRenderer.listFormattedStringToWidth(text, 126);
        for (int line = 0; line < lines.size(); line++) {
        	int offset = ((126 - fontRenderer.getStringWidth(lines.get(line))) / 2);
        	fontRenderer.drawString(lines.get(line), offset, yOffset, 0x004F0000);
        	yOffset += fontRenderer.FONT_HEIGHT;
        }
        yOffset += 5;
        fontRenderer.drawString(castingType, (126 - fontRenderer.getStringWidth(castingType)) / 2, yOffset, 0x00210000);
		
        textQuads.addAll(fontRenderer.build());
        return new BakedModelSpellPage(model, textQuads);
	}

	private static final class SpellPageOverrideList extends ItemOverrideList {
		public static final SpellPageOverrideList INSTANCE = new SpellPageOverrideList();
		
		private final Map<String, IBakedModel> cache;

		public SpellPageOverrideList() {
			super(ImmutableList.<ItemOverride>of());
			this.cache = new HashMap<String, IBakedModel>();
		}

		@Override
		@Nonnull
		public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
				@Nullable World world, @Nullable EntityLivingBase entity) {

			Spell spell = ItemSpellPage.getSpell(stack);

			if (spell == null || spell.equals(Spell.EMPTY_SPELL)) {
				return originalModel;
			}
			
			String name = spell.getRegistryName().toString();
			
			if (!cache.containsKey(name)) {
				ModelSpellPage model = (ModelSpellPage) originalModel;
				TextureAtlasSprite fontSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(font2.toString());
				
				BakedModelSpellPage bakedBakedModel = rebake(model, spell);
				cache.put(name, bakedBakedModel);
				return bakedBakedModel;
			}

			return cache.get(name);
		}

	}

}
