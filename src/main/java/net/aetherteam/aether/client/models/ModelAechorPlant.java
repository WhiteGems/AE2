package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelAechorPlant extends ModelBase
{
    private ModelRenderer[] petal;
    private ModelRenderer[] leaf;
    private ModelRenderer[] stamen;
    private ModelRenderer[] stamen2;
    private ModelRenderer[] thorn;
    private ModelRenderer stem;
    private ModelRenderer head;
    private static int petals = 10;
    private static int thorns = 4;
    private static int stamens = 3;
    public float sinage;
    public float sinage2;
    public float size;
    private float pie;

    public ModelAechorPlant()
    {
        this(0.0F);
    }

    public ModelAechorPlant(float var1)
    {
        this(var1, 0.0F);
    }

    public ModelAechorPlant(float var1, float var2)
    {
        this.pie = ((float)Math.PI * 2F);
        this.size = 1.0F;
        this.petal = new ModelRenderer[petals];
        this.leaf = new ModelRenderer[petals];
        int var3;

        for (var3 = 0; var3 < petals; ++var3)
        {
            this.petal[var3] = new ModelRenderer(this, 0, 0);

            if (var3 % 2 == 0)
            {
                this.petal[var3] = new ModelRenderer(this, 29, 3);
                this.petal[var3].addBox(-4.0F, -1.0F, -12.0F, 8, 1, 9, var1 - 0.25F);
                this.petal[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
            }
            else
            {
                this.petal[var3].addBox(-4.0F, -1.0F, -13.0F, 8, 1, 10, var1 - 0.125F);
                this.petal[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
            }

            this.leaf[var3] = new ModelRenderer(this, 38, 13);
            this.leaf[var3].addBox(-2.0F, -1.0F, -9.5F, 4, 1, 8, var1 - 0.15F);
            this.leaf[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        }

        this.stamen = new ModelRenderer[stamens];
        this.stamen2 = new ModelRenderer[stamens];

        for (var3 = 0; var3 < stamens; ++var3)
        {
            this.stamen[var3] = new ModelRenderer(this, 36, 13);
            this.stamen[var3].addBox(0.0F, -9.0F, -1.5F, 1, 6, 1, var1 - 0.25F);
            this.stamen[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        }

        for (var3 = 0; var3 < stamens; ++var3)
        {
            this.stamen2[var3] = new ModelRenderer(this, 32, 15);
            this.stamen2[var3].addBox(0.0F, -10.0F, -1.5F, 1, 1, 1, var1 + 0.125F);
            this.stamen2[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        }

        this.head = new ModelRenderer(this, 0, 12);
        this.head.addBox(-3.0F, -3.0F, -3.0F, 6, 2, 6, var1 + 0.75F);
        this.head.setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        this.stem = new ModelRenderer(this, 24, 13);
        this.stem.addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2, var1);
        this.stem.setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        this.thorn = new ModelRenderer[thorns];

        for (var3 = 0; var3 < thorns; ++var3)
        {
            this.thorn[var3] = new ModelRenderer(this, 32, 13);
            this.thorn[var3].setRotationPoint(0.0F, 1.0F + var2, 0.0F);
        }

        this.thorn[0].addBox(-1.75F, 1.25F, -1.0F, 1, 1, 1, var1 - 0.25F);
        this.thorn[1].addBox(-1.0F, 2.25F, 0.75F, 1, 1, 1, var1 - 0.25F);
        this.thorn[2].addBox(0.75F, 1.25F, 0.0F, 1, 1, 1, var1 - 0.25F);
        this.thorn[3].addBox(0.0F, 2.25F, -1.75F, 1, 1, 1, var1 - 0.25F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        this.setRotationAngles(var2, var3, var4, var5, var6, var7);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.2F, 0.0F);
        GL11.glScalef(this.size, this.size, this.size);
        int var8;

        for (var8 = 0; var8 < petals; ++var8)
        {
            this.petal[var8].render(var7);
            this.leaf[var8].render(var7);
        }

        for (var8 = 0; var8 < stamens; ++var8)
        {
            this.stamen[var8].render(var7);
            this.stamen2[var8].render(var7);
        }

        this.head.render(var7);
        this.stem.render(var7);

        for (var8 = 0; var8 < thorns; ++var8)
        {
            this.thorn[var8].render(var7);
        }

        GL11.glPopMatrix();
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        this.head.rotateAngleX = 0.0F;
        this.head.rotateAngleY = var5 / (180F / (float)Math.PI);
        float var7 = this.sinage2;
        this.stem.rotateAngleY = this.head.rotateAngleY;
        this.stem.rotationPointY = var7 * 0.5F;
        int var8;

        for (var8 = 0; var8 < thorns; ++var8)
        {
            this.thorn[var8].rotateAngleY = this.head.rotateAngleY;
            this.thorn[var8].rotationPointY = var7 * 0.5F;
        }

        for (var8 = 0; var8 < petals; ++var8)
        {
            this.petal[var8].rotateAngleX = var8 % 2 == 0 ? -0.25F : -0.4125F;
            this.petal[var8].rotateAngleX += this.sinage;
            this.petal[var8].rotateAngleY = this.head.rotateAngleY;
            this.petal[var8].rotateAngleY += this.pie / (float)petals * (float)var8;
            this.leaf[var8].rotateAngleX = var8 % 2 == 0 ? 0.1F : 0.2F;
            this.leaf[var8].rotateAngleX += this.sinage * 0.75F;
            this.leaf[var8].rotateAngleY = this.head.rotateAngleY + this.pie / (float)petals / 2.0F;
            this.leaf[var8].rotateAngleY += this.pie / (float)petals * (float)var8;
            this.petal[var8].rotationPointY = var7;
            this.leaf[var8].rotationPointY = var7;
        }

        for (var8 = 0; var8 < stamens; ++var8)
        {
            this.stamen[var8].rotateAngleX = 0.2F + (float)var8 / 15.0F;
            this.stamen[var8].rotateAngleY = this.head.rotateAngleY + 0.1F;
            this.stamen[var8].rotateAngleY += this.pie / (float)stamens * (float)var8;
            this.stamen[var8].rotateAngleX += this.sinage * 0.4F;
            this.stamen2[var8].rotateAngleX = 0.2F + (float)var8 / 15.0F;
            this.stamen2[var8].rotateAngleY = this.head.rotateAngleY + 0.1F;
            this.stamen2[var8].rotateAngleY += this.pie / (float)stamens * (float)var8;
            this.stamen2[var8].rotateAngleX += this.sinage * 0.4F;
            this.stamen[var8].rotationPointY = var7 + this.sinage * 2.0F;
            this.stamen2[var8].rotationPointY = var7 + this.sinage * 2.0F;
        }

        this.head.rotationPointY = var7 + this.sinage * 2.0F;
    }
}
