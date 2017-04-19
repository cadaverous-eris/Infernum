package infernum.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelTome - mangoose
 * Created using Tabula 4.1.1
 */
public class ModelTome extends ModelBase {
    public ModelRenderer spine;
    public ModelRenderer frontCover;
    public ModelRenderer backCover;
    public ModelRenderer loosePage;
    public ModelRenderer frontPages;
    public ModelRenderer backPages;

    public ModelTome() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.backPages = new ModelRenderer(this, 34, 0);
        this.backPages.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backPages.addBox(0.0F, -10.0F, -7.0F, 1, 11, 14, 0.0F);
        this.loosePage = new ModelRenderer(this, 34, 11);
        this.loosePage.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.loosePage.addBox(0.0F, -10.0F, -7.0F, 0, 11, 14, 0.0F);
        this.backCover = new ModelRenderer(this, 0, 28);
        this.backCover.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.backCover.addBox(1.0F, -11.0F, -8.0F, 1, 12, 16, 0.0F);
        this.frontPages = new ModelRenderer(this, 34, 0);
        this.frontPages.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.frontPages.addBox(-1.0F, -10.0F, -7.0F, 1, 11, 14, 0.0F);
        this.spine = new ModelRenderer(this, 26, 47);
        this.spine.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spine.addBox(-1.5F, -1.0F, -8.0F, 3, 1, 16, 0.0F);
        this.frontCover = new ModelRenderer(this, 0, 0);
        this.frontCover.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.frontCover.addBox(-2.0F, -11.0F, -8.0F, 1, 12, 16, 0.0F);
        this.backCover.addChild(this.backPages);
        this.frontCover.addChild(this.frontPages);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.loosePage.render(f5);
        this.backCover.render(f5);
        this.spine.render(f5);
        this.frontCover.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
