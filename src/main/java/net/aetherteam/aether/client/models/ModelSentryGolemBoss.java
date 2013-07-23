package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class ModelSentryGolemBoss extends ModelBase
{
    ModelRenderer body;
    ModelRenderer head;
    ModelRenderer bottom;
    ModelRenderer rightforearm;
    ModelRenderer leftarm;
    ModelRenderer rightarm;
    ModelRenderer leftforearm;

    public ModelSentryGolemBoss()
    {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.body = new ModelRenderer(this, 32, 4);
        this.body.addBox(0.0F, 0.0F, 0.0F, 18, 20, 18);
        this.body.setRotationPoint(-9.0F, -16.0F, -9.0F);
        this.body.setTextureSize(128, 128);
        this.body.mirror = true;
        this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 42, 48);
        this.head.addBox(0.0F, 0.0F, 0.0F, 14, 8, 14);
        this.head.setRotationPoint(-7.0F, -24.0F, -7.0F);
        this.head.setTextureSize(128, 128);
        this.head.mirror = true;
        this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
        this.bottom = new ModelRenderer(this, 0, 42);
        this.bottom.addBox(0.0F, 0.0F, 0.0F, 14, 6, 14);
        this.bottom.setRotationPoint(-7.0F, 4.0F, -7.0F);
        this.bottom.setTextureSize(128, 128);
        this.bottom.mirror = true;
        this.setRotation(this.bottom, 0.0F, 0.0F, 0.0F);
        this.rightforearm = new ModelRenderer(this, 0, 90);
        this.rightforearm.addBox(0.0F, 0.0F, 0.0F, 10, 18, 10);
        this.rightforearm.setRotationPoint(9.0F, -5.0F, -5.5F);
        this.rightforearm.setTextureSize(128, 128);
        this.rightforearm.mirror = true;
        this.setRotation(this.rightforearm, -0.4363323F, 0.0F, 0.0F);
        this.leftarm = new ModelRenderer(this, 0, 0);
        this.leftarm.addBox(0.0F, 0.0F, 0.0F, 8, 15, 8);
        this.leftarm.setRotationPoint(-9.0F, -16.0F, 4.0F);
        this.leftarm.setTextureSize(128, 128);
        this.leftarm.mirror = true;
        this.setRotation(this.leftarm, -((float)Math.PI * 2F / 9F), ((float)Math.PI / 2F), 0.0F);
        this.rightarm = new ModelRenderer(this, 0, 0);
        this.rightarm.addBox(0.0F, 0.0F, 0.0F, 8, 15, 8);
        this.rightarm.setRotationPoint(9.0F, -16.0F, -4.0F);
        this.rightarm.setTextureSize(128, 128);
        this.rightarm.mirror = true;
        this.setRotation(this.rightarm, -((float)Math.PI * 2F / 9F), -((float)Math.PI / 2F), 0.0F);
        this.leftforearm = new ModelRenderer(this, 0, 62);
        this.leftforearm.addBox(0.0F, 0.0F, 0.0F, 10, 17, 10);
        this.leftforearm.setRotationPoint(-19.0F, -5.0F, -4.5F);
        this.leftforearm.setTextureSize(128, 128);
        this.leftforearm.mirror = true;
        this.setRotation(this.leftforearm, -0.4363323F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.body.render(var7);
        this.head.render(var7);
        this.bottom.render(var7);
        this.rightforearm.render(var7);
        this.leftarm.render(var7);
        this.rightarm.render(var7);
        this.leftforearm.render(var7);
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
        this.leftforearm.rotateAngleX = this.leftarm.rotateAngleX + var2;
        this.rightforearm.rotateAngleX = this.rightarm.rotateAngleX + var2;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLiving var1, float var2, float var3, float var4)
    {
        EntitySentryGuardian var5 = (EntitySentryGuardian)var1;
        int var6 = var5.getAttackTimer();

        if (var6 != 0)
        {
            this.leftforearm.rotateAngleX = 5.0F;
            this.rightforearm.rotateAngleX = 5.0F;
        }
    }

    private float func_78172_a(float var1, float var2)
    {
        return (Math.abs(var1 % var2 - var2 * 0.5F) - var2 * 0.25F) / (var2 * 0.25F);
    }
}
