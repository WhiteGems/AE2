package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAngel extends ModelBiped
{
    ModelRenderer Scalf;
    ModelRenderer Wing_base;
    ModelRenderer Wing;
    ModelRenderer hair;
    ModelRenderer hair_back;
    ModelRenderer Shape1;

    public ModelAngel()
    {
        this.textureWidth = 118;
        this.textureHeight = 42;
        this.Scalf = new ModelRenderer(this, 0, 32);
        this.Scalf.addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8);
        this.Scalf.setRotationPoint(0.0F, -0.4F, -0.6F);
        this.Scalf.setTextureSize(118, 42);
        this.Scalf.mirror = true;
        this.setRotation(this.Scalf, 0.1487144F, 0.0F, -0.0371786F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHead.setTextureSize(118, 42);
        this.bipedHead.mirror = true;
        this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody.setTextureSize(118, 42);
        this.bipedBody.mirror = true;
        this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 48, 0);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.setTextureSize(118, 42);
        this.bipedRightArm.mirror = true;
        this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm = new ModelRenderer(this, 48, 0);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.setTextureSize(118, 42);
        this.bipedLeftArm.mirror = true;
        this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm.mirror = false;
        this.bipedRightLeg = new ModelRenderer(this, 32, 0);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.bipedRightLeg.setTextureSize(118, 42);
        this.bipedRightLeg.mirror = true;
        this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg = new ModelRenderer(this, 32, 0);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg.setTextureSize(118, 42);
        this.bipedLeftLeg.mirror = true;
        this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg.mirror = false;
        this.Wing_base = new ModelRenderer(this, 64, 13);
        this.Wing_base.addBox(0.0F, 0.0F, 0.0F, 1, 2, 9);
        this.Wing_base.setRotationPoint(-2.0F, 1.0F, 1.0F);
        this.Wing_base.setTextureSize(118, 42);
        this.Wing_base.mirror = true;
        this.setRotation(this.Wing_base, 0.3346075F, -0.2974289F, 0.0F);
        this.Wing = new ModelRenderer(this, 100, 0);
        this.Wing.addBox(0.0F, -1.0F, -4.0F, 2, 22, 7);
        this.Wing.setRotationPoint(-3.7F, 0.0F, 6.6F);
        this.Wing.setTextureSize(118, 42);
        this.Wing.mirror = true;
        this.setRotation(this.Wing, 0.2974289F, -0.3346075F, 0.0F);
        this.hair = new ModelRenderer(this, 64, 0);
        this.hair.addBox(-4.533333F, -8.5F, -4.5F, 9, 4, 9);
        this.hair.setRotationPoint(0.0F, 0.0F, 0.2F);
        this.hair.setTextureSize(118, 42);
        this.hair.mirror = true;
        this.setRotation(this.hair, 0.1115358F, 0.0F, 0.0F);
        this.hair_back = new ModelRenderer(this, 24, 25);
        this.hair_back.addBox(-2.0F, 0.0F, 0.0F, 5, 6, 1);
        this.hair_back.setRotationPoint(1.0F, -1.0F, 3.0F);
        this.hair_back.setTextureSize(118, 42);
        this.hair_back.mirror = true;
        this.setRotation(this.hair_back, 0.4461433F, 0.0F, 0.0F);
        this.Shape1 = new ModelRenderer(this, 24, 16);
        this.Shape1.addBox(-3.5F, 0.0F, 0.0F, 7, 2, 1);
        this.Shape1.setRotationPoint(0.0F, 1.5F, -4.0F);
        this.Shape1.setTextureSize(118, 42);
        this.Shape1.mirror = true;
        this.setRotation(this.Shape1, 0.8922867F, 0.1115358F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.bipedHeadwear.isHidden = true;
        this.hair.rotateAngleY = this.bipedHead.rotateAngleY;
        this.hair.rotateAngleX = this.bipedHead.rotateAngleX;
        this.hair_back.rotateAngleY = this.bipedHead.rotateAngleY;
        this.hair_back.rotateAngleX = this.bipedHead.rotateAngleX;
        this.Scalf.render(var7);
        this.bipedHead.render(var7);
        this.bipedBody.render(var7);
        this.bipedRightArm.render(var7);
        this.bipedLeftArm.render(var7);
        this.bipedRightLeg.render(var7);
        this.bipedLeftLeg.render(var7);
        this.Wing_base.render(var7);
        this.Wing.render(var7);
        this.hair.render(var7);
        this.hair_back.render(var7);
        this.Shape1.render(var7);
    }

    private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.rotateAngleX = var2;
        var1.rotateAngleY = var3;
        var1.rotateAngleZ = var4;
    }
}
