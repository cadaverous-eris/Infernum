package infernum.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;

public class ModelPigZombieMage extends ModelBiped {

	public ModelRenderer bipedHood;
	public ModelRenderer bipedRobe;

	public ModelPigZombieMage() {
		this(0.0F);
	}

	public ModelPigZombieMage(float modelSize) {
		this(modelSize, 0.0F, 64, 64);
	}

	public ModelPigZombieMage(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, 64, 64);
		this.bipedHood = new ModelRenderer(this, 0, 32);
		this.bipedHood.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHood.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.75F);
		this.bipedRobe = new ModelRenderer(this, 32, 32);
		this.bipedRobe.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedRobe.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			float f = 2.0F;
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
			this.bipedRobe.render(scale);
			this.bipedHood.render(scale);
		} else {
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}
			this.bipedHead.render(scale);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
			this.bipedRobe.render(scale);
			this.bipedHood.render(scale);
		}

		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		this.bipedHood.rotateAngleX = bipedHead.rotateAngleX;
		this.bipedHood.rotateAngleY = bipedHead.rotateAngleY;
		this.bipedHood.rotateAngleZ = bipedHead.rotateAngleZ;
		this.bipedRobe.rotateAngleX = bipedBody.rotateAngleX;
		this.bipedRobe.rotateAngleY = bipedBody.rotateAngleY;
		this.bipedRobe.rotateAngleZ = bipedBody.rotateAngleZ;

		float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
		float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
		this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
        float f2 = -(float)Math.PI / 3.825F;
        this.bipedRightArm.rotateAngleX = f2;
        this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}

	@Override
	public void setInvisible(boolean invisible) {
		super.setInvisible(invisible);
		this.bipedHood.showModel = invisible;
		this.bipedRobe.showModel = invisible;
	}

}
