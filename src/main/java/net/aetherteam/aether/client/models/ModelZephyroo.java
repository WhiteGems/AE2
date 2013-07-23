package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityZephyroo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelZephyroo extends ModelBase
{
    ModelRenderer LeftHand;
    ModelRenderer LeftArm;
    ModelRenderer LeftFoot;
    ModelRenderer LeftLeg;
    ModelRenderer LeftHip;
    ModelRenderer LeftShoulder;
    ModelRenderer TailBottom;
    ModelRenderer Pouch;
    ModelRenderer Snout;
    ModelRenderer RightHip;
    ModelRenderer RightLeg;
    ModelRenderer RightFoot;
    ModelRenderer RightShoulder;
    ModelRenderer RightArm;
    ModelRenderer RightHand;
    ModelRenderer TailTop;
    ModelRenderer EarLeft;
    ModelRenderer Neck;
    ModelRenderer EarRight;
    ModelRenderer Head;
    ModelRenderer Body;

    public ModelZephyroo()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.LeftHand = new ModelRenderer(this, 50, 50);
        this.LeftHand.addBox(0.0F, 0.0F, 0.0F, 2, 1, 4);
        this.LeftHand.setRotationPoint(5.0F, 10.5F, -10.5F);
        this.LeftHand.setTextureSize(128, 64);
        this.LeftHand.mirror = true;
        this.setRotation(this.LeftHand, -0.3665191F, 0.0F, 0.0F);
        this.LeftArm = new ModelRenderer(this, 40, 38);
        this.LeftArm.addBox(0.0F, 0.0F, 0.0F, 2, 9, 2);
        this.LeftArm.setRotationPoint(5.0F, 3.0F, -7.0F);
        this.LeftArm.setTextureSize(128, 64);
        this.LeftArm.mirror = true;
        this.setRotation(this.LeftArm, -0.4363323F, 0.0F, 0.0F);
        this.LeftFoot = new ModelRenderer(this, 29, 19);
        this.LeftFoot.addBox(0.0F, 8.0F, -7.0F, 3, 2, 6);
        this.LeftFoot.setRotationPoint(3.0F, 14.0F, 1.0F);
        this.LeftFoot.setTextureSize(128, 64);
        this.LeftFoot.mirror = true;
        this.setRotation(this.LeftFoot, 0.0F, 0.0F, 0.0F);
        this.LeftLeg = new ModelRenderer(this, 0, 0);
        this.LeftLeg.addBox(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.LeftLeg.setRotationPoint(3.0F, 14.0F, 1.0F);
        this.LeftLeg.setTextureSize(128, 64);
        this.LeftLeg.mirror = true;
        this.setRotation(this.LeftLeg, -0.4363323F, 0.0F, 0.0F);
        this.LeftHip = new ModelRenderer(this, 0, 41);
        this.LeftHip.addBox(0.0F, 0.0F, 0.0F, 2, 7, 7);
        this.LeftHip.setRotationPoint(4.0F, 8.0F, 0.5F);
        this.LeftHip.setTextureSize(128, 64);
        this.LeftHip.mirror = true;
        this.setRotation(this.LeftHip, 0.0349066F, 0.0F, 0.0F);
        this.LeftShoulder = new ModelRenderer(this, 40, 49);
        this.LeftShoulder.addBox(0.0F, 0.0F, 0.0F, 2, 3, 3);
        this.LeftShoulder.setRotationPoint(5.0F, 2.0F, -7.5F);
        this.LeftShoulder.setTextureSize(128, 64);
        this.LeftShoulder.mirror = true;
        this.setRotation(this.LeftShoulder, 0.0349066F, 0.0F, 0.0F);
        this.TailBottom = new ModelRenderer(this, 44, 25);
        this.TailBottom.addBox(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.TailBottom.setRotationPoint(-1.5F, 21.0F, 15.0F);
        this.TailBottom.setTextureSize(128, 64);
        this.TailBottom.mirror = true;
        this.setRotation(this.TailBottom, 1.32645F, 0.0F, 0.0F);
        this.Pouch = new ModelRenderer(this, 13, 0);
        this.Pouch.addBox(0.0F, 0.0F, 0.0F, 9, 2, 8);
        this.Pouch.setRotationPoint(-4.5F, 10.0F, -4.0F);
        this.Pouch.setTextureSize(128, 64);
        this.Pouch.mirror = true;
        this.setRotation(this.Pouch, -((float)Math.PI / 4F), 0.0F, 0.0F);
        this.Snout = new ModelRenderer(this, 0, 22);
        this.Snout.addBox(0.0F, 0.0F, 0.0F, 4, 4, 9);
        this.Snout.setRotationPoint(-2.0F, -2.0F, -14.5F);
        this.Snout.setTextureSize(128, 64);
        this.Snout.mirror = true;
        this.setRotation(this.Snout, 0.3490659F, 0.0F, 0.0F);
        this.RightHip = new ModelRenderer(this, 0, 41);
        this.RightHip.addBox(0.0F, 0.0F, 0.0F, 2, 7, 7);
        this.RightHip.setRotationPoint(-6.0F, 8.0F, 0.5F);
        this.RightHip.setTextureSize(128, 64);
        this.RightHip.mirror = true;
        this.setRotation(this.RightHip, 0.0349066F, 0.0F, 0.0F);
        this.RightLeg = new ModelRenderer(this, 0, 0);
        this.RightLeg.addBox(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.RightLeg.setRotationPoint(-6.0F, 14.0F, 1.0F);
        this.RightLeg.setTextureSize(128, 64);
        this.RightLeg.mirror = true;
        this.setRotation(this.RightLeg, -0.4363323F, 0.0F, 0.0F);
        this.RightFoot = new ModelRenderer(this, 29, 19);
        this.RightFoot.addBox(0.0F, 8.0F, -7.0F, 3, 2, 6);
        this.RightFoot.setRotationPoint(-6.0F, 14.0F, 1.0F);
        this.RightFoot.setTextureSize(128, 64);
        this.RightFoot.mirror = true;
        this.setRotation(this.RightFoot, 0.0F, 0.0F, 0.0F);
        this.RightShoulder = new ModelRenderer(this, 40, 49);
        this.RightShoulder.addBox(0.0F, 0.0F, 0.0F, 2, 3, 3);
        this.RightShoulder.setRotationPoint(-7.0F, 2.0F, -7.5F);
        this.RightShoulder.setTextureSize(128, 64);
        this.RightShoulder.mirror = true;
        this.setRotation(this.RightShoulder, 0.0349066F, 0.0F, 0.0F);
        this.RightArm = new ModelRenderer(this, 40, 38);
        this.RightArm.addBox(0.0F, 0.0F, 0.0F, 2, 9, 2);
        this.RightArm.setRotationPoint(-7.0F, 3.0F, -7.0F);
        this.RightArm.setTextureSize(128, 64);
        this.RightArm.mirror = true;
        this.setRotation(this.RightArm, -0.4363323F, 0.0F, 0.0F);
        this.RightHand = new ModelRenderer(this, 50, 50);
        this.RightHand.addBox(0.0F, 0.0F, 0.0F, 2, 1, 4);
        this.RightHand.setRotationPoint(-7.0F, 10.5F, -10.5F);
        this.RightHand.setTextureSize(128, 64);
        this.RightHand.mirror = true;
        this.setRotation(this.RightHand, -0.3665191F, 0.0F, 0.0F);
        this.TailTop = new ModelRenderer(this, 48, 37);
        this.TailTop.addBox(0.0F, 0.0F, 0.0F, 3, 10, 3);
        this.TailTop.setRotationPoint(-1.5F, 14.0F, 8.0F);
        this.TailTop.setTextureSize(128, 64);
        this.TailTop.mirror = true;
        this.setRotation(this.TailTop, 0.8028515F, 0.0F, 0.0F);
        this.EarLeft = new ModelRenderer(this, 0, 41);
        this.EarLeft.addBox(0.0F, 0.0F, 0.0F, 1, 5, 2);
        this.EarLeft.setRotationPoint(2.0F, -10.5F, -10.0F);
        this.EarLeft.setTextureSize(128, 64);
        this.EarLeft.mirror = true;
        this.setRotation(this.EarLeft, 0.0F, 0.0F, 0.2443461F);
        this.Neck = new ModelRenderer(this, 0, 14);
        this.Neck.addBox(0.0F, 0.0F, 0.0F, 4, 3, 5);
        this.Neck.setRotationPoint(-2.0F, 0.0F, -9.5F);
        this.Neck.setTextureSize(128, 64);
        this.Neck.mirror = true;
        this.setRotation(this.Neck, 0.6108652F, 0.0F, 0.0F);
        this.EarRight = new ModelRenderer(this, 0, 41);
        this.EarRight.addBox(0.0F, 0.0F, 0.0F, 1, 5, 2);
        this.EarRight.setRotationPoint(-3.0F, -10.0F, -10.0F);
        this.EarRight.setTextureSize(128, 64);
        this.EarRight.mirror = true;
        this.setRotation(this.EarRight, 0.0F, 0.0F, -0.2617994F);
        this.Head = new ModelRenderer(this, 26, 27);
        this.Head.addBox(0.0F, 0.0F, 0.0F, 4, 3, 5);
        this.Head.setRotationPoint(-2.0F, -6.0F, -11.0F);
        this.Head.setTextureSize(128, 64);
        this.Head.mirror = true;
        this.setRotation(this.Head, 0.0F, 0.0F, 0.0F);
        this.Body = new ModelRenderer(this, 0, 35);
        this.Body.addBox(0.0F, 0.0F, 0.0F, 10, 9, 20);
        this.Body.setRotationPoint(-5.0F, -2.0F, -4.0F);
        this.Body.setTextureSize(128, 64);
        this.Body.mirror = true;
        this.setRotation(this.Body, -((float)Math.PI / 4F), 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.LeftHand.render(var7);
        this.LeftArm.render(var7);
        this.LeftFoot.render(var7);
        this.LeftLeg.render(var7);
        this.LeftHip.render(var7);
        this.LeftShoulder.render(var7);
        this.TailBottom.render(var7);
        this.Pouch.render(var7);
        this.Snout.render(var7);
        this.RightHip.render(var7);
        this.RightLeg.render(var7);
        this.RightFoot.render(var7);
        this.RightShoulder.render(var7);
        this.RightArm.render(var7);
        this.RightHand.render(var7);
        this.TailTop.render(var7);
        this.EarLeft.render(var7);
        this.Neck.render(var7);
        this.EarRight.render(var7);
        this.Head.render(var7);
        this.Body.render(var7);
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

        if (var7.motionX != 0.0D && var7.motionZ != 0.0D)
        {
            this.RightLeg.rotateAngleX = (float)Math.cos((double)(((EntityZephyroo)var7).getTimeTilJump() * 0.5F));
            this.LeftLeg.rotateAngleX = (float)Math.cos((double)(((EntityZephyroo)var7).getTimeTilJump() * 0.5F));
            this.RightFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
            this.LeftFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
        }
        else
        {
            this.RightLeg.rotateAngleX = 0.0F;
            this.LeftLeg.rotateAngleX = 0.0F;
            this.RightFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
            this.LeftFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
        }
    }
}
