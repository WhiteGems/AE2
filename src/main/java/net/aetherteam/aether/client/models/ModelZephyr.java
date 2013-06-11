package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZephyr extends ModelBase
{
    ModelRenderer LeftFace;
    ModelRenderer BodyRightSide2;
    ModelRenderer Mouth;
    ModelRenderer CloudButt;
    ModelRenderer Tail3;
    ModelRenderer RightFace;
    ModelRenderer BodyLeftSide1;
    ModelRenderer BodyLeftSide2;
    ModelRenderer Body;
    ModelRenderer BodyRightSide1;
    ModelRenderer Tail1;
    ModelRenderer Tail2;

    public ModelZephyr()
    {
        this.textureWidth = 128;
        this.textureHeight = 32;
        this.setTextureOffset("Tail1.tail1", 96, 22);
        this.setTextureOffset("Tail2.tail2", 80, 24);
        this.setTextureOffset("Tail3.tail3", 84, 18);
        this.Tail1 = new ModelRenderer(this, "Tail1");
        this.Tail1.setRotationPoint(0.0F, 0.0F, 12.4F);
        this.Tail1.addBox("tail1", -2.5F, -2.5F, -2.5F, 5, 5, 5);
        this.Tail2 = new ModelRenderer(this, "Tail2");
        this.Tail2.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.Tail2.addBox("tail2", -2.0F, -2.0F, -1.966667F, 4, 4, 4);
        this.Tail3 = new ModelRenderer(this, "Tail3");
        this.Tail3.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.Tail3.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
        this.Tail1.addChild(this.Tail2);
        this.Tail2.addChild(this.Tail3);
        this.LeftFace = new ModelRenderer(this, 67, 11);
        this.LeftFace.addBox(3.0F, -1.0F, -9.0F, 4, 6, 2);
        this.LeftFace.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.LeftFace.setTextureSize(128, 32);
        this.LeftFace.mirror = true;
        this.setRotation(this.LeftFace, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide2 = new ModelRenderer(this, 25, 11);
        this.BodyRightSide2.addBox(-2.0F, -3.333333F, -2.5F, 2, 6, 6);
        this.BodyRightSide2.setRotationPoint(-5.5F, 9.0F, 2.0F);
        this.BodyRightSide2.setTextureSize(128, 32);
        this.BodyRightSide2.mirror = true;
        this.setRotation(this.BodyRightSide2, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide2.mirror = false;
        this.Mouth = new ModelRenderer(this, 66, 19);
        this.Mouth.addBox(-3.0F, 1.0F, -8.0F, 6, 3, 1);
        this.Mouth.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.Mouth.setTextureSize(128, 32);
        this.Mouth.mirror = true;
        this.setRotation(this.Mouth, 0.0F, 0.0F, 0.0F);
        this.CloudButt = new ModelRenderer(this, 0, 0);
        this.CloudButt.addBox(-6.0F, -3.0F, 0.0F, 8, 6, 2);
        this.CloudButt.setRotationPoint(2.0F, 8.0F, 7.0F);
        this.CloudButt.setTextureSize(128, 32);
        this.CloudButt.mirror = true;
        this.setRotation(this.CloudButt, 0.0F, 0.0F, 0.0F);
        this.RightFace = new ModelRenderer(this, 67, 11);
        this.RightFace.addBox(-7.0F, -1.0F, -9.0F, 4, 6, 2);
        this.RightFace.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.RightFace.setTextureSize(128, 32);
        this.RightFace.mirror = true;
        this.setRotation(this.RightFace, 0.0F, 0.0F, 0.0F);
        this.RightFace.mirror = false;
        this.BodyLeftSide1 = new ModelRenderer(this, 0, 20);
        this.BodyLeftSide1.addBox(0.0F, -3.0F, -3.0F, 2, 6, 6);
        this.BodyLeftSide1.setRotationPoint(6.0F, 8.0F, -4.0F);
        this.BodyLeftSide1.setTextureSize(128, 32);
        this.BodyLeftSide1.mirror = true;
        this.setRotation(this.BodyLeftSide1, 0.0F, 0.0F, 0.0F);
        this.BodyLeftSide2 = new ModelRenderer(this, 25, 11);
        this.BodyLeftSide2.addBox(0.0F, -3.333333F, -2.5F, 2, 6, 6);
        this.BodyLeftSide2.setRotationPoint(5.5F, 9.0F, 2.0F);
        this.BodyLeftSide2.setTextureSize(128, 32);
        this.BodyLeftSide2.mirror = true;
        this.setRotation(this.BodyLeftSide2, 0.0F, 0.0F, 0.0F);
        this.Body = new ModelRenderer(this, 27, 9);
        this.Body.addBox(-6.0F, -4.0F, -7.0F, 12, 9, 14);
        this.Body.setRotationPoint(0.0F, 8.0F, 0.0F);
        this.Body.setTextureSize(128, 32);
        this.setRotation(this.Body, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide1 = new ModelRenderer(this, 0, 20);
        this.BodyRightSide1.addBox(-2.0F, -3.0F, -3.0F, 2, 6, 6);
        this.BodyRightSide1.setRotationPoint(-6.0F, 8.0F, -4.0F);
        this.BodyRightSide1.setTextureSize(128, 32);
        this.BodyRightSide1.mirror = true;
        this.setRotation(this.BodyRightSide1, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide1.mirror = false;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.LeftFace.render(var7);
        this.BodyRightSide2.render(var7);
        this.Mouth.render(var7);
        this.CloudButt.render(var7);
        this.RightFace.render(var7);
        this.BodyLeftSide1.render(var7);
        this.BodyLeftSide2.render(var7);
        this.Body.render(var7);
        this.BodyRightSide1.render(var7);
        this.Tail1.render(var7);
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
        float var8 = (float) (Math.sin((double) (var1 * 20.0F) / (180D / Math.PI)) * (double) var2 * 0.5D);
        float var9 = (float) Math.PI;
        float var10 = var9 / 2.0F;
        float var11 = var9 * 3.0F / 11.0F;
        this.LeftFace.rotationPointY = var8 + 8.0F;
        this.LeftFace.rotationPointX = var8 * 0.5F;
        this.BodyLeftSide1.rotationPointY = 8.0F - var8 * 0.5F;
        this.BodyLeftSide2.rotationPointY = 9.0F + var8 * 0.5F;
        this.RightFace.rotationPointY = 8.0F - var8;
        this.RightFace.rotationPointX = -var8 * 0.5F;
        this.BodyRightSide1.rotationPointY = 8.0F - var8 * 0.5F;
        this.BodyRightSide2.rotationPointY = 9.0F + var8 * 0.5F;
        this.Tail1.rotationPointX = (float) (Math.sin((double) (var1 * 20.0F) / (180D / Math.PI)) * (double) var2 * 0.75D);
        this.Tail1.rotateAngleY = (float) Math.pow(0.9900000095367432D, -4.0D) * 1.0F * var9 / 4.0F * MathHelper.cos(-0.055F * var1 + var10);
        this.Tail1.rotationPointY = 8.0F - var8;
        this.Tail2.rotationPointX = (float) Math.pow(0.9900000095367432D, 1.0D) * 1.0F * var9 / 4.0F * MathHelper.cos(-0.055F * var1 + var10);
        this.Tail2.rotationPointY = var8 * 1.25F;
        this.Tail2.rotateAngleY = this.Tail1.rotateAngleY + 0.25F;
        this.Tail3.rotationPointX = (float) Math.pow(0.9900000095367432D, 2.0D) * 1.0F * var9 / 4.0F * MathHelper.cos(-0.055F * var1 + var10);
        this.Tail3.rotationPointY = -var8;
        this.Tail3.rotateAngleY = this.Tail2.rotateAngleY + 0.35F;
    }
}
