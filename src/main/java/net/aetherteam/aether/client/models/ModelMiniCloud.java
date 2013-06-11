package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMiniCloud extends ModelBase
{
    public ModelRenderer[] head;

    public ModelMiniCloud()
    {
        this(0.0F);
    }

    public ModelMiniCloud(float var1)
    {
        this(var1, 0.0F);
    }

    public ModelMiniCloud(float var1, float var2)
    {
        this.head = new ModelRenderer[5];
        this.head[0] = new ModelRenderer(this, 0, 0);
        this.head[1] = new ModelRenderer(this, 36, 0);
        this.head[2] = new ModelRenderer(this, 36, 0);
        this.head[3] = new ModelRenderer(this, 36, 8);
        this.head[4] = new ModelRenderer(this, 36, 8);
        this.head[0].addBox(-4.5F, -4.5F, -4.5F, 9, 9, 9, var1);
        this.head[0].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
        this.head[1].addBox(-3.5F, -3.5F, -5.5F, 7, 7, 1, var1);
        this.head[1].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
        this.head[2].addBox(-3.5F, -3.5F, 4.5F, 7, 7, 1, var1);
        this.head[2].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
        this.head[3].addBox(-5.5F, -3.5F, -3.5F, 1, 7, 7, var1);
        this.head[3].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
        this.head[4].addBox(4.5F, -3.5F, -3.5F, 1, 7, 7, var1);
        this.head[4].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        this.setRotationAngles(var2, var3, var4, var5, var6, var7);

        for (int var8 = 0; var8 < 5; ++var8)
        {
            this.head[var8].render(var7);
        }
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        for (int var7 = 0; var7 < 5; ++var7)
        {
            this.head[var7].rotateAngleY = var4 / (180F / (float) Math.PI);
            this.head[var7].rotateAngleX = var5 / (180F / (float) Math.PI);
        }
    }
}
