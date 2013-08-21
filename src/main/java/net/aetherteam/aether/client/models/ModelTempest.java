package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelTempest extends ModelBase
{
    ModelRenderer main_body;
    ModelRenderer RB_cloud;
    ModelRenderer LB_cloud;
    ModelRenderer tail_2;
    ModelRenderer FR_cloud;
    ModelRenderer FL_cloud;
    ModelRenderer tail_1;
    public float sinage;
    public float sinage2;

    public ModelTempest()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.main_body = new ModelRenderer(this, 0, 0);
        this.main_body.addBox(-6.0F, -4.0F, -7.0F, 12, 8, 14);
        this.main_body.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.main_body.setTextureSize(64, 64);
        this.main_body.mirror = true;
        this.setRotation(this.main_body, 0.0F, 0.0F, 0.0F);
        this.RB_cloud = new ModelRenderer(this, 16, 22);
        this.RB_cloud.addBox(-8.0F, -2.0F, 0.0F, 2, 6, 6);
        this.RB_cloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.RB_cloud.setTextureSize(64, 64);
        this.RB_cloud.mirror = true;
        this.setRotation(this.RB_cloud, 0.0F, 0.0F, 0.0F);
        this.LB_cloud = new ModelRenderer(this, 16, 22);
        this.LB_cloud.addBox(6.0F, -2.0F, 0.0F, 2, 6, 6);
        this.LB_cloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.LB_cloud.setTextureSize(64, 64);
        this.LB_cloud.mirror = true;
        this.setRotation(this.LB_cloud, 0.0F, 0.0F, 0.0F);
        this.LB_cloud.mirror = false;
        this.tail_2 = new ModelRenderer(this, 32, 22);
        this.tail_2.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
        this.tail_2.setRotationPoint(0.0F, 10.0F, 19.0F);
        this.tail_2.setTextureSize(64, 64);
        this.tail_2.mirror = true;
        this.setRotation(this.tail_2, 0.0F, 0.0F, 0.0F);
        this.FR_cloud = new ModelRenderer(this, 0, 22);
        this.FR_cloud.addBox(-8.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FR_cloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.FR_cloud.setTextureSize(64, 64);
        this.FR_cloud.mirror = true;
        this.setRotation(this.FR_cloud, 0.0F, 0.0F, 0.0F);
        this.FL_cloud = new ModelRenderer(this, 0, 22);
        this.FL_cloud.addBox(6.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FL_cloud.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.FL_cloud.setTextureSize(64, 64);
        this.FL_cloud.mirror = true;
        this.setRotation(this.FL_cloud, 0.0F, 0.0F, 0.0F);
        this.FL_cloud.mirror = false;
        this.tail_1 = new ModelRenderer(this, 0, 34);
        this.tail_1.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        this.tail_1.setRotationPoint(0.0F, 10.0F, 12.0F);
        this.tail_1.setTextureSize(64, 64);
        this.tail_1.mirror = true;
        this.setRotation(this.tail_1, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.main_body.render(f5);
        this.RB_cloud.render(f5);
        this.LB_cloud.render(f5);
        this.FR_cloud.render(f5);
        this.FL_cloud.render(f5);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        this.tail_1.render(f5);
        this.tail_2.render(f5);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void renderTransparentTail(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.tail_1.render(f5);
        this.tail_2.render(f5);
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
        float boff = this.sinage2;
        float yOffset = 7.5F;
        float vertMotion = (float)(Math.sin((double)(f * 20.0F) / (180D / Math.PI)) * (double)this.sinage * 0.5D);
        float PI = (float)Math.PI;
        float initialOffset = PI / 2.0F;
        float offset = PI * 3.0F / 11.0F;
        this.FR_cloud.rotationPointY = vertMotion + 10.0F;
        this.FL_cloud.rotationPointX = vertMotion * 0.5F;
        this.LB_cloud.rotationPointY = 8.0F - vertMotion * 0.5F;
        this.RB_cloud.rotationPointY = 9.0F + vertMotion * 0.5F;
        this.tail_1.rotationPointX = (float)(Math.sin((double)(f * 20.0F) / (180D / Math.PI)) * (double)f1 * 0.75D);
        this.tail_1.rotateAngleY = (float)Math.pow(0.9900000095367432D, -4.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset);
        this.tail_1.rotationPointY = 10.0F - vertMotion;
        this.tail_2.rotationPointX = (float)Math.pow(0.9900000095367432D, 1.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset);
        this.tail_2.rotationPointY = 10.0F - vertMotion * 1.25F;
        this.tail_2.rotateAngleY = this.tail_1.rotateAngleY + 0.25F;
        this.main_body.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.RB_cloud.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.LB_cloud.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.tail_2.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.FR_cloud.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.FL_cloud.rotationPointY = boff + yOffset + this.sinage * 2.0F;
        this.tail_1.rotationPointY = boff + yOffset + this.sinage * 2.0F;
    }
}
