package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.bosses.EntitySliderHostMimic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSliderMimicHost extends ModelBase
{
    ModelRenderer body3;
    ModelRenderer frontLeftLeg;
    ModelRenderer frontRightLeg;
    ModelRenderer backLeftLeg;
    ModelRenderer backRightLeg;
    ModelRenderer body;
    ModelRenderer body2;
    ModelRenderer body5;
    ModelRenderer body4;
    ModelRenderer body6;
    ModelRenderer fakeBody;

    public ModelSliderMimicHost()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.fakeBody = new ModelRenderer(this, 0, 0);
        this.fakeBody.addBox(0.0F, 0.0F, 0.0F, 32, 32, 32);
        this.fakeBody.setRotationPoint(-16.0F, -8.0F, -16.0F);
        this.fakeBody.setTextureSize(128, 64);
        this.fakeBody.mirror = true;
        this.setRotation(this.fakeBody, 0.0F, 0.0F, 0.0F);
        this.body3 = new ModelRenderer(this, 21, 11);
        this.body3.addBox(22.0F, 2.0F, 0.0F, 11, 11, 32);
        this.body3.setRotationPoint(-17.0F, -10.0F, -16.0F);
        this.body3.setTextureSize(128, 64);
        this.body3.mirror = true;
        this.frontLeftLeg = new ModelRenderer(this, 0, 0);
        this.frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.frontLeftLeg.setRotationPoint(10.0F, 11.0F, -16.0F);
        this.frontLeftLeg.setTextureSize(128, 64);
        this.frontLeftLeg.mirror = true;
        this.setRotation(this.frontLeftLeg, 0.0F, 0.0F, 0.0F);
        this.frontRightLeg = new ModelRenderer(this, 0, 0);
        this.frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.frontRightLeg.setRotationPoint(-16.0F, 11.0F, -16.0F);
        this.frontRightLeg.setTextureSize(128, 64);
        this.frontRightLeg.mirror = true;
        this.setRotation(this.frontRightLeg, 0.0F, 0.0F, 0.0F);
        this.backLeftLeg = new ModelRenderer(this, 0, 0);
        this.backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.backLeftLeg.setRotationPoint(10.0F, 11.0F, 10.0F);
        this.backLeftLeg.setTextureSize(128, 64);
        this.backLeftLeg.mirror = true;
        this.setRotation(this.backLeftLeg, 0.0F, 0.0F, 0.0F);
        this.backRightLeg = new ModelRenderer(this, 0, 0);
        this.backRightLeg.addBox(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.backRightLeg.setRotationPoint(-16.0F, 11.0F, 10.0F);
        this.backRightLeg.setTextureSize(128, 64);
        this.backRightLeg.mirror = true;
        this.setRotation(this.backRightLeg, 0.0F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(0.0F, 2.0F, 0.0F, 32, 10, 32);
        this.body.setRotationPoint(-16.0F, -20.0F, -16.0F);
        this.body.setTextureSize(128, 64);
        this.body.mirror = true;
        this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
        this.body2 = new ModelRenderer(this, 58, 25);
        this.body2.addBox(0.0F, 2.0F, 0.0F, 13, 11, 18);
        this.body2.setRotationPoint(-6.0F, -10.0F, -2.0F);
        this.body2.setTextureSize(128, 64);
        this.body2.mirror = true;
        this.setRotation(this.body2, 0.0F, 0.0F, 0.0F);
        this.body5 = new ModelRenderer(this, 106, 42);
        this.body5.addBox(0.0F, 2.0F, 0.0F, 10, 11, 1);
        this.body5.setRotationPoint(-16.0F, -10.0F, 15.0F);
        this.body5.setTextureSize(128, 64);
        this.body5.mirror = true;
        this.setRotation(this.body5, 0.0F, 0.0F, 0.0F);
        this.body4 = new ModelRenderer(this, 0, 22);
        this.body4.addBox(0.0F, 2.0F, 0.0F, 32, 10, 32);
        this.body4.setRotationPoint(-16.0F, 1.0F, -16.0F);
        this.body4.setTextureSize(128, 64);
        this.body4.mirror = true;
        this.setRotation(this.body4, 0.0F, 0.0F, 0.0F);
        this.body6 = new ModelRenderer(this, 1, 11);
        this.body6.addBox(0.0F, 2.0F, 0.0F, 10, 11, 31);
        this.body6.setRotationPoint(-16.0F, -10.0F, -16.0F);
        this.body6.setTextureSize(128, 64);
        this.body6.mirror = true;
        this.setRotation(this.body6, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if (entity instanceof EntitySliderHostMimic && !((EntitySliderHostMimic)entity).isAwake())
        {
            this.fakeBody.render(f5);
        }
        else
        {
            this.body3.render(f5);
            this.frontLeftLeg.render(f5);
            this.frontRightLeg.render(f5);
            this.backLeftLeg.render(f5);
            this.backRightLeg.render(f5);
            this.body.render(f5);
            this.body2.render(f5);
            this.body5.render(f5);
            this.body4.render(f5);
            this.body6.render(f5);
        }
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
        this.frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.frontRightLeg.rotateAngleY = 0.0F;
        this.frontLeftLeg.rotateAngleY = 0.0F;
        this.backRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.backLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.backRightLeg.rotateAngleY = 0.0F;
        this.backLeftLeg.rotateAngleY = 0.0F;
    }
}
