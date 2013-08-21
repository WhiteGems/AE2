package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSentryGolem extends ModelBiped
{
    ModelRenderer LowerBody = new ModelRenderer(this);
    ModelRenderer LeftArmHand;
    ModelRenderer RightArmHand;
    ModelRenderer SentryHead;
    ModelRenderer SentryBody;
    public boolean isDefault = false;
    public byte armState = 2;
    float armAngle = 0.0F;
    float[] armsAngles = new float[] {1.0F, 1.0F, 0.5F, 0.5F};

    public ModelSentryGolem()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-5.5F, -10.0F, -4.5F, 11, 8, 9);
        this.bipedHead.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.bipedHead.setTextureSize(128, 64);
        this.bipedHead.mirror = true;
        this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 42, 47);
        this.bipedLeftLeg.addBox(-3.3F, 0.0F, -3.0F, 5, 11, 6);
        this.bipedLeftLeg.setRotationPoint(4.0F, 13.0F, 0.0F);
        this.bipedLeftLeg.setTextureSize(128, 64);
        this.bipedLeftLeg.mirror = true;
        this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 40, 3);
        this.bipedLeftArm.addBox(3.0F, -1.0F, -3.0F, 4, 8, 6);
        this.bipedLeftArm.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.bipedLeftArm.setTextureSize(128, 64);
        this.bipedLeftArm.mirror = true;
        this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
        this.LowerBody = new ModelRenderer(this, 0, 50);
        this.LowerBody.addBox(-6.0F, 6.0F, -4.5F, 12, 5, 9);
        this.LowerBody.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.LowerBody.setTextureSize(128, 64);
        this.LowerBody.mirror = true;
        this.setRotation(this.LowerBody, 0.0F, 0.0F, 0.0F);
        this.bipedRightLeg.mirror = true;
        this.bipedRightLeg = new ModelRenderer(this, 42, 47);
        this.bipedRightLeg.addBox(-1.633333F, 0.0F, -3.0F, 5, 11, 6);
        this.bipedRightLeg.setRotationPoint(-4.0F, 13.0F, 0.0F);
        this.bipedRightLeg.setTextureSize(128, 64);
        this.bipedRightLeg.mirror = true;
        this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
        this.bipedRightLeg.mirror = false;
        this.bipedBody = new ModelRenderer(this, 0, 17);
        this.bipedBody.addBox(-8.0F, -4.0F, -5.5F, 16, 10, 11);
        this.bipedBody.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.bipedBody.setTextureSize(128, 64);
        this.bipedBody.mirror = true;
        this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
        this.RightArmHand = new ModelRenderer(this, 54, 17);
        this.RightArmHand.addBox(-5.0F, 6.0F, -3.5F, 5, 6, 7);
        this.RightArmHand.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.RightArmHand.setTextureSize(128, 64);
        this.RightArmHand.mirror = true;
        this.setRotation(this.RightArmHand, 0.0F, 0.0F, 0.0F);
        this.RightArmHand.mirror = false;
        this.bipedRightArm.mirror = true;
        this.bipedRightArm = new ModelRenderer(this, 40, 3);
        this.bipedRightArm.addBox(-7.0F, -1.0F, -3.0F, 4, 8, 6);
        this.bipedRightArm.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.bipedRightArm.setTextureSize(128, 64);
        this.bipedRightArm.mirror = true;
        this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
        this.bipedRightArm.mirror = false;
        this.LeftArmHand = new ModelRenderer(this, 54, 17);
        this.LeftArmHand.addBox(0.0F, 6.0F, -3.5F, 5, 6, 7);
        this.LeftArmHand.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.LeftArmHand.setTextureSize(128, 64);
        this.LeftArmHand.mirror = true;
        this.setRotation(this.LeftArmHand, 0.0F, 0.0F, 0.0F);
        this.SentryBody = new ModelRenderer(this, 64, 48);
        this.SentryBody.addBox(-5.5F, -8.0F, -4.5F, 8, 8, 8);
        this.SentryBody.setRotationPoint(1.5F, 4.0F, -12.0F);
        this.SentryBody.setTextureSize(128, 64);
        this.SentryBody.mirror = true;
        this.setRotation(this.SentryBody, 0.0F, 0.0F, 0.0F);
        this.SentryHead = new ModelRenderer(this, 64, 48);
        this.SentryHead.addBox(-5.5F, -8.0F, -4.5F, 8, 8, 8);
        this.SentryHead.setRotationPoint(1.5F, -11.0F, 0.0F);
        this.SentryHead.setTextureSize(128, 64);
        this.SentryHead.mirror = true;
        this.setRotation(this.SentryHead, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.bipedBody.render(f5);
        this.bipedLeftLeg.render(f5);
        this.bipedLeftArm.render(f5);
        this.LowerBody.render(f5);
        this.bipedRightLeg.render(f5);
        this.bipedBody.render(f5);
        this.RightArmHand.render(f5);
        this.bipedRightArm.render(f5);
        this.LeftArmHand.render(f5);
        this.SentryHead.render(f5);
        this.SentryBody.render(f5);
        this.SentryHead.isHidden = true;
        this.SentryBody.isHidden = true;
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if (entity instanceof EntitySentryGolem)
        {
            EntitySentryGolem entityS = (EntitySentryGolem)entity;
            this.armState = entityS.getHandState();

            if (entityS.progress < this.armsAngles[this.armState])
            {
                entityS.progress += 0.02F;
            }

            if (entityS.progress > this.armsAngles[this.armState])
            {
                entityS.progress -= 0.02F;
            }

            this.bipedRightArm.rotateAngleX = -3.0F * entityS.progress;
            this.bipedLeftArm.rotateAngleX = -3.0F * entityS.progress;
            this.bipedRightArm.rotateAngleY -= 0.3F * entityS.progress;
            this.bipedLeftArm.rotateAngleY += 0.3F * entityS.progress;
            this.bipedRightArm.rotateAngleZ += 0.3F * entityS.progress;
            this.bipedLeftArm.rotateAngleZ -= 0.3F * entityS.progress;
        }

        this.RightArmHand.rotateAngleX = this.bipedRightArm.rotateAngleX;
        this.RightArmHand.rotateAngleY = this.bipedRightArm.rotateAngleY;
        this.RightArmHand.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
        this.LeftArmHand.rotateAngleX = this.bipedLeftArm.rotateAngleX;
        this.LeftArmHand.rotateAngleY = this.bipedLeftArm.rotateAngleY;
        this.LeftArmHand.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
        this.LowerBody.rotateAngleX = this.bipedBody.rotateAngleX;
        this.LowerBody.rotateAngleY = this.bipedBody.rotateAngleY;
        this.LowerBody.rotateAngleZ = this.bipedBody.rotateAngleZ;
    }
}
