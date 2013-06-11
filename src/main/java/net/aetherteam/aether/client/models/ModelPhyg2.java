package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelPhyg2 extends ModelBase
{
    private ModelRenderer leftWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer leftWingOuter = new ModelRenderer(this, 20, 0);
    private ModelRenderer rightWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer rightWingOuter = new ModelRenderer(this, 40, 0);
    public static EntityPhyg pig;

    public ModelPhyg2()
    {
        this.leftWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.leftWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.rotateAngleY = (float) Math.PI;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        float var8;
        float var9;
        float var10;
        float var11;
        float var12;
        float var13;

        if (this.isChild)
        {
            var8 = 2.0F;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GL11.glTranslatef(0.0F, 24.0F * var7, 0.0F);
            var9 = -((float) Math.acos((double) pig.wingFold));
            var10 = 32.0F * pig.wingFold / 4.0F;
            var11 = -32.0F * (float) Math.sqrt((double) (1.0F - pig.wingFold * pig.wingFold)) / 4.0F;
            var12 = 0.0F;
            var13 = var10 * (float) Math.cos((double) pig.wingAngle) - var11 * (float) Math.sin((double) pig.wingAngle);
            float var14 = var10 * (float) Math.sin((double) pig.wingAngle) + var11 * (float) Math.cos((double) pig.wingAngle);
            this.leftWingInner.setRotationPoint(4.0F + var13, var14 + 12.0F, var12);
            this.rightWingInner.setRotationPoint(-4.0F - var13, var14 + 12.0F, var12);
            var10 *= 3.0F;
            var13 = var10 * (float) Math.cos((double) pig.wingAngle) - var11 * (float) Math.sin((double) pig.wingAngle);
            var14 = var10 * (float) Math.sin((double) pig.wingAngle) + var11 * (float) Math.cos((double) pig.wingAngle);
            this.leftWingOuter.setRotationPoint(4.0F + var13, var14 + 12.0F, var12);
            this.rightWingOuter.setRotationPoint(-4.0F - var13, var14 + 12.0F, var12);
            this.leftWingInner.rotateAngleZ = pig.wingAngle + var9 + ((float) Math.PI / 2F);
            this.leftWingOuter.rotateAngleZ = pig.wingAngle - var9 + ((float) Math.PI / 2F);
            this.rightWingInner.rotateAngleZ = -(pig.wingAngle + var9 - ((float) Math.PI / 2F));
            this.rightWingOuter.rotateAngleZ = -(pig.wingAngle - var9 + ((float) Math.PI / 2F));
            this.leftWingOuter.render(var7);
            this.leftWingInner.render(var7);
            this.rightWingOuter.render(var7);
            this.rightWingInner.render(var7);
        } else
        {
            var8 = -((float) Math.acos((double) pig.wingFold));
            var9 = 32.0F * pig.wingFold / 4.0F;
            var10 = -32.0F * (float) Math.sqrt((double) (1.0F - pig.wingFold * pig.wingFold)) / 4.0F;
            var11 = 0.0F;
            var12 = var9 * (float) Math.cos((double) pig.wingAngle) - var10 * (float) Math.sin((double) pig.wingAngle);
            var13 = var9 * (float) Math.sin((double) pig.wingAngle) + var10 * (float) Math.cos((double) pig.wingAngle);
            this.leftWingInner.setRotationPoint(4.0F + var12, var13 + 12.0F, var11);
            this.rightWingInner.setRotationPoint(-4.0F - var12, var13 + 12.0F, var11);
            var9 *= 3.0F;
            var12 = var9 * (float) Math.cos((double) pig.wingAngle) - var10 * (float) Math.sin((double) pig.wingAngle);
            var13 = var9 * (float) Math.sin((double) pig.wingAngle) + var10 * (float) Math.cos((double) pig.wingAngle);
            this.leftWingOuter.setRotationPoint(4.0F + var12, var13 + 12.0F, var11);
            this.rightWingOuter.setRotationPoint(-4.0F - var12, var13 + 12.0F, var11);
            this.leftWingInner.rotateAngleZ = pig.wingAngle + var8 + ((float) Math.PI / 2F);
            this.leftWingOuter.rotateAngleZ = pig.wingAngle - var8 + ((float) Math.PI / 2F);
            this.rightWingInner.rotateAngleZ = -(pig.wingAngle + var8 - ((float) Math.PI / 2F));
            this.rightWingOuter.rotateAngleZ = -(pig.wingAngle - var8 + ((float) Math.PI / 2F));
            this.leftWingOuter.render(var7);
            this.leftWingInner.render(var7);
            this.rightWingOuter.render(var7);
            this.rightWingInner.render(var7);
        }
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {}
}
