package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityNewZephyr;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelNewZephyr extends ModelBase
{
    ModelRenderer mainbody;
    ModelRenderer RBcloud;
    ModelRenderer LBcloud;
    ModelRenderer tail2;
    ModelRenderer FRcloud;
    ModelRenderer FLcloud;
    ModelRenderer tail1;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    public float sinage;
    public float sinage2;

    public ModelNewZephyr()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.mainbody = new ModelRenderer(this, 0, 0);
        this.mainbody.addBox(-6.0F, -4.0F, -7.0F, 12, 8, 14);
        this.mainbody.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.mainbody.setTextureSize(64, 64);
        this.mainbody.mirror = true;
        this.setRotation(this.mainbody, 0.0F, 0.0F, 0.0F);
        this.RBcloud = new ModelRenderer(this, 16, 22);
        this.RBcloud.addBox(-7.0F, -2.0F, 0.0F, 2, 6, 6);
        this.RBcloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.RBcloud.setTextureSize(64, 64);
        this.RBcloud.mirror = true;
        this.setRotation(this.RBcloud, 0.0F, 0.0F, 0.0F);
        this.LBcloud = new ModelRenderer(this, 16, 22);
        this.LBcloud.addBox(5.0F, -2.0F, 0.0F, 2, 6, 6);
        this.LBcloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.LBcloud.setTextureSize(64, 64);
        this.LBcloud.mirror = true;
        this.setRotation(this.LBcloud, 0.0F, 0.0F, 0.0F);
        this.LBcloud.mirror = false;
        this.tail2 = new ModelRenderer(this, 32, 22);
        this.tail2.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
        this.tail2.setRotationPoint(0.0F, 10.0F, 19.0F);
        this.tail2.setTextureSize(64, 64);
        this.tail2.mirror = true;
        this.setRotation(this.tail2, 0.0F, 0.0F, 0.0F);
        this.FRcloud = new ModelRenderer(this, 0, 22);
        this.FRcloud.addBox(-8.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FRcloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.FRcloud.setTextureSize(64, 64);
        this.FRcloud.mirror = true;
        this.setRotation(this.FRcloud, 0.0F, 0.0F, 0.0F);
        this.FLcloud = new ModelRenderer(this, 0, 22);
        this.FLcloud.addBox(6.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FLcloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.FLcloud.setTextureSize(64, 64);
        this.FLcloud.mirror = true;
        this.setRotation(this.FLcloud, 0.0F, 0.0F, 0.0F);
        this.FLcloud.mirror = false;
        this.tail1 = new ModelRenderer(this, 0, 34);
        this.tail1.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        this.tail1.setRotationPoint(0.0F, 10.0F, 12.0F);
        this.tail1.setTextureSize(64, 64);
        this.tail1.mirror = true;
        this.setRotation(this.tail1, 0.0F, 0.0F, 0.0F);
        this.Shape1 = new ModelRenderer(this, 0, 1);
        this.Shape1.addBox(-7.0F, 0.0F, -9.0F, 4, 4, 2);
        this.Shape1.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Shape1.setTextureSize(64, 64);
        this.Shape1.mirror = true;
        this.setRotation(this.Shape1, 0.0F, 0.0F, 0.0F);
        this.Shape2 = new ModelRenderer(this, 0, 1);
        this.Shape2.addBox(3.0F, 0.0F, -9.0F, 4, 4, 2);
        this.Shape2.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Shape2.setTextureSize(64, 64);
        this.Shape2.mirror = true;
        this.setRotation(this.Shape2, 0.0F, 0.0F, 0.0F);
        this.Shape3 = new ModelRenderer(this, 0, 7);
        this.Shape3.addBox(-3.0F, 3.0F, -8.0F, 6, 3, 1);
        this.Shape3.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Shape3.setTextureSize(64, 64);
        this.Shape3.mirror = true;
        this.setRotation(this.Shape3, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.mainbody.render(var7);
        this.RBcloud.render(var7);
        this.LBcloud.render(var7);
        this.tail2.render(var7);
        this.FRcloud.render(var7);
        this.FLcloud.render(var7);
        this.tail1.render(var7);
        this.Shape1.render(var7);
        this.Shape2.render(var7);
        this.Shape3.render(var7);
    }

    private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.rotateAngleX = var2;
        var1.rotateAngleY = var3;
        var1.rotateAngleZ = var4;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7)
    {
        super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
        float var8 = this.sinage2;
        float var9 = 5.5F;
        float var10 = (float) (Math.sin((double) (var1 * 20.0F) / (180D / Math.PI)) * (double) var2 * 0.5D);
        float var11 = (float) Math.PI;
        float var12 = var11 / 2.0F;
        float var13 = var11 * 3.0F / 11.0F;
        this.FRcloud.rotationPointY = var10 + 10.0F;
        this.FLcloud.rotationPointX = var10 * 0.5F;
        this.LBcloud.rotationPointY = 8.0F - var10 * 0.5F;
        this.RBcloud.rotationPointY = 9.0F + var10 * 0.5F;
        this.Shape2.rotationPointY = 10.0F - var10;
        this.Shape2.rotationPointX = -var10 * 0.5F;
        this.Shape1.rotationPointY = 10.0F + var10;
        this.Shape1.rotationPointX = var10 * 0.5F;
        this.tail1.rotationPointX = (float) (Math.sin((double) (var1 * 20.0F) / (180D / Math.PI)) * (double) var2 * 0.75D);
        this.tail1.rotateAngleY = (float) Math.pow(0.9900000095367432D, -4.0D) * 1.0F * var11 / 4.0F * MathHelper.cos(-0.055F * var1 + var12);
        this.tail1.rotationPointY = 10.0F - var10;
        this.tail2.rotationPointX = (float) Math.pow(0.9900000095367432D, 1.0D) * 1.0F * var11 / 4.0F * MathHelper.cos(-0.055F * var1 + var12);
        this.tail2.rotationPointY = 10.0F - var10 * 1.25F;
        this.tail2.rotateAngleY = this.tail1.rotateAngleY + 0.25F;
        this.Shape3.rotationPointY = (float) (8 + ((EntityNewZephyr) var7).attackTime + 1);
        this.mainbody.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.RBcloud.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.LBcloud.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.tail2.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.FRcloud.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.FLcloud.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.tail1.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.Shape1.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.Shape2.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.Shape3.rotationPointY = var8 + var9 + this.sinage * 2.0F;
    }
}
