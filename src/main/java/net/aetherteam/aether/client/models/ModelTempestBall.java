package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTempestBall extends ModelBase
{
    ModelRenderer Shape2;
    ModelRenderer Shape21;
    ModelRenderer Shape23;
    ModelRenderer Shape24;

    public ModelTempestBall()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Shape2 = new ModelRenderer(this, 0, 16);
        this.Shape2.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape2.setRotationPoint(-3.0F, 1.0F, 1.0F);
        this.Shape2.setTextureSize(64, 32);
        this.Shape2.mirror = true;
        this.setRotation(this.Shape2, 0.2602503F, 0.4833219F, -0.0743572F);
        this.Shape21 = new ModelRenderer(this, 0, 0);
        this.Shape21.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape21.setRotationPoint(9.0F, 2.0F, 7.0F);
        this.Shape21.setTextureSize(64, 32);
        this.Shape21.mirror = true;
        this.setRotation(this.Shape21, 0.0F, -(float)Math.PI, -0.1745329F);
        this.Shape23 = new ModelRenderer(this, 32, 0);
        this.Shape23.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape23.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Shape23.setTextureSize(64, 32);
        this.Shape23.mirror = true;
        this.setRotation(this.Shape23, 0.0F, -0.1745329F, -0.1745329F);
        this.Shape24 = new ModelRenderer(this, 32, 16);
        this.Shape24.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape24.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Shape24.setTextureSize(64, 32);
        this.Shape24.mirror = true;
        this.setRotation(this.Shape24, 0.0F, 0.1115358F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Shape2.render(f5);
        this.Shape21.render(f5);
        this.Shape23.render(f5);
        this.Shape24.render(f5);
    }

    public void render(Entity entity, float f5)
    {
        this.Shape2.render(f5);
        this.Shape21.render(f5);
        this.Shape23.render(f5);
        this.Shape24.render(f5);
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
    }
}
