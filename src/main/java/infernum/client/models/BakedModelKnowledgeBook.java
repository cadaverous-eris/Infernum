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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.SimpleModelFontRenderer;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedModelKnowledgeBook implements IPerspectiveAwareModel {

	private final ModelKnowledgeBook parentModel;
	private final List<BakedQuad> textQuads;

	public BakedModelKnowledgeBook(ModelKnowledgeBook parent, List<BakedQuad> textQuads) {
		this.parentModel = parent;
		this.textQuads = textQuads;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> combinedQuadsList = new ArrayList(parentModel.getQuads(state, side, rand));
		combinedQuadsList.addAll(textQuads);
		return combinedQuadsList;
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
