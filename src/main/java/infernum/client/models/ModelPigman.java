package infernum.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPigman extends ModelBase {

	public ModelRenderer pigmanHead;
	public ModelRenderer pigmanBody;
	public ModelRenderer pigmanArms;
	public ModelRenderer rightPigmanLeg;
	public ModelRenderer leftPigmanLeg;
	public ModelRenderer pigmanNose;
	public ModelRenderer leftPigmanEar;
	public ModelRenderer rightPigmanEar;

	public ModelPigman() {
		this(0F);
	}

	public ModelPigman(float scale) {
		this(scale, 0.0F, 64, 64);
	}

	public ModelPigman(float scale, float p_i1164_2_, int width, int height) {
		this.pigmanHead = (new ModelRenderer(this)).setTextureSize(width, height);
		this.pigmanHead.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
		this.pigmanHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, scale);
		this.pigmanHead.setTextureOffset(32, 0).addBox(-4, -10, -4, 8, 10, 8, scale + 0.5F);

		this.pigmanNose = new ModelRenderer(this).setTextureSize(width, height);;
		this.pigmanNose.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.pigmanNose.setTextureOffset(24, 0).addBox(-2.0F, -2.0F, -5.0F, 4, 3, 1, scale);

		this.rightPigmanEar = (new ModelRenderer(this)).setTextureSize(width, height);;
		this.rightPigmanEar.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.rightPigmanEar.setTextureOffset(24, 4).addBox(-8.0F, -3.0F, 0.0F, 4, 3, 0, scale);
		this.leftPigmanEar = (new ModelRenderer(this)).setTextureSize(width, height);;
		this.leftPigmanEar.mirror = true;
		this.leftPigmanEar.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.leftPigmanEar.setTextureOffset(24, 4).addBox(4.0F, -3.0F, 0.0F, 4, 3, 0, scale);

		this.pigmanHead.addChild(this.pigmanNose);
		this.pigmanHead.addChild(this.rightPigmanEar);
		this.pigmanHead.addChild(this.leftPigmanEar);
		this.setRotateAngle(rightPigmanEar, 0.0F, 0.0F, 0.2792526803190927F);
		this.setRotateAngle(leftPigmanEar, 0.0F, 0.0F, -0.2792526803190927F);

		this.pigmanBody = (new ModelRenderer(this)).setTextureSize(width, height);
		this.pigmanBody.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
		this.pigmanBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, scale);
		this.pigmanBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F);
		this.pigmanArms = (new ModelRenderer(this)).setTextureSize(width, height);
		this.pigmanArms.setRotationPoint(0.0F, 0.0F + p_i1164_2_ + 2.0F, 0.0F);
		this.pigmanArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, scale);
		this.pigmanArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, scale);
		this.pigmanArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, scale);
		this.rightPigmanLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
		this.rightPigmanLeg.setRotationPoint(-2.0F, 12.0F + p_i1164_2_, 0.0F);
		this.rightPigmanLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.leftPigmanLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
		this.leftPigmanLeg.mirror = true;
		this.leftPigmanLeg.setRotationPoint(2.0F, 12.0F + p_i1164_2_, 0.0F);
		this.leftPigmanLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		this.pigmanHead.render(scale);
		this.pigmanBody.render(scale);
		this.rightPigmanLeg.render(scale);
		this.leftPigmanLeg.render(scale);
		this.pigmanArms.render(scale);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		this.pigmanHead.rotateAngleY = netHeadYaw * 0.017453292F;
		this.pigmanHead.rotateAngleX = headPitch * 0.017453292F;
		this.pigmanArms.rotationPointY = 3.0F;
		this.pigmanArms.rotationPointZ = -1.0F;
		this.pigmanArms.rotateAngleX = -0.75F;
		this.rightPigmanLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.leftPigmanLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount
				* 0.5F;
		this.rightPigmanLeg.rotateAngleY = 0.0F;
		this.leftPigmanLeg.rotateAngleY = 0.0F;
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
