package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelAerbunny extends ModelBase
{
    public ModelRenderer a;
    public ModelRenderer b;
    public ModelRenderer b2;
    public ModelRenderer b3;
    public ModelRenderer e1;
    public ModelRenderer e2;
    public ModelRenderer ff1;
    public ModelRenderer ff2;
    public ModelRenderer g;
    public ModelRenderer g2;
    public ModelRenderer h;
    public ModelRenderer h2;
    public float puffiness;

    public ModelAerbunny()
    {
        byte var1 = 16;
        this.a = new ModelRenderer(this, 0, 0);
        this.a.addBox(-2.0F, -1.0F, -4.0F, 4, 4, 6, 0.0F);
        this.a.setRotationPoint(0.0F, (float) (-1 + var1), -4.0F);
        this.g = new ModelRenderer(this, 14, 0);
        this.g.addBox(-2.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g.setRotationPoint(0.0F, (float) (-1 + var1), -4.0F);
        this.g2 = new ModelRenderer(this, 14, 0);
        this.g2.addBox(1.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g2.setRotationPoint(0.0F, (float) (-1 + var1), -4.0F);
        this.h = new ModelRenderer(this, 20, 0);
        this.h.addBox(-4.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h.setRotationPoint(0.0F, (float) (-1 + var1), -4.0F);
        this.h2 = new ModelRenderer(this, 20, 0);
        this.h2.addBox(2.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h2.setRotationPoint(0.0F, (float) (-1 + var1), -4.0F);
        this.b = new ModelRenderer(this, 0, 10);
        this.b.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.b.setRotationPoint(0.0F, (float) (0 + var1), 0.0F);
        this.b2 = new ModelRenderer(this, 0, 24);
        this.b2.addBox(-2.0F, 4.0F, -2.0F, 4, 3, 4, 0.0F);
        this.b2.setRotationPoint(0.0F, (float) (0 + var1), 0.0F);
        this.b3 = new ModelRenderer(this, 29, 0);
        this.b3.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.b3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.e1 = new ModelRenderer(this, 24, 16);
        this.e1.addBox(-2.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e1.setRotationPoint(3.0F, (float) (3 + var1), -3.0F);
        this.e2 = new ModelRenderer(this, 24, 16);
        this.e2.addBox(0.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e2.setRotationPoint(-3.0F, (float) (3 + var1), -3.0F);
        this.ff1 = new ModelRenderer(this, 16, 24);
        this.ff1.addBox(-2.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff1.setRotationPoint(3.0F, (float) (3 + var1), 4.0F);
        this.ff2 = new ModelRenderer(this, 16, 24);
        this.ff2.addBox(0.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff2.setRotationPoint(-3.0F, (float) (3 + var1), 4.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        this.setRotationAngles(var2, var3, var4, var5, var6, var7);
        float var8;

        if (this.isChild)
        {
            var8 = 2.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, var7, var7);
            this.a.render(var7);
            this.g.render(var7);
            this.g2.render(var7);
            this.h.render(var7);
            this.h2.render(var7);
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GL11.glTranslatef(0.0F, 18.0F * var7, 0.0F);
            this.b.render(var7);
            this.b2.render(var7);
            this.e1.render(var7);
            this.e2.render(var7);
            this.ff1.render(var7);
            this.ff2.render(var7);
            float var9 = 1.0F + this.puffiness * 0.5F;
            GL11.glTranslatef(0.0F, 1.0F, 0.0F);
            GL11.glScalef(var9, var9, var9);
            this.b3.render(var7);
        } else
        {
            this.a.render(var7);
            this.g.render(var7);
            this.g2.render(var7);
            this.h.render(var7);
            this.h2.render(var7);
            this.b.render(var7);
            this.b2.render(var7);
            GL11.glPushMatrix();
            var8 = 1.0F + this.puffiness * 0.5F;
            GL11.glTranslatef(0.0F, 1.0F, 0.0F);
            GL11.glScalef(var8, var8, var8);
            this.b3.render(var7);
            GL11.glPopMatrix();
            this.e1.render(var7);
            this.e2.render(var7);
            this.ff1.render(var7);
            this.ff2.render(var7);
        }
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        this.a.rotateAngleX = -(var5 / (180F / (float) Math.PI));
        this.a.rotateAngleY = var4 / (180F / (float) Math.PI);
        this.g.rotateAngleX = this.a.rotateAngleX;
        this.g.rotateAngleY = this.a.rotateAngleY;
        this.g2.rotateAngleX = this.a.rotateAngleX;
        this.g2.rotateAngleY = this.a.rotateAngleY;
        this.h.rotateAngleX = this.a.rotateAngleX;
        this.h.rotateAngleY = this.a.rotateAngleY;
        this.h2.rotateAngleX = this.a.rotateAngleX;
        this.h2.rotateAngleY = this.a.rotateAngleY;
        this.b.rotateAngleX = ((float) Math.PI / 2F);
        this.b2.rotateAngleX = ((float) Math.PI / 2F);
        this.b3.rotateAngleX = ((float) Math.PI / 2F);
        this.e1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.0F * var2;
        this.ff1.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.2F * var2;
        this.e2.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.0F * var2;
        this.ff2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.2F * var2;
    }
}
