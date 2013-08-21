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

    public ModelMiniCloud(float f)
    {
        this(f, 0.0F);
    }

    public ModelMiniCloud(float f, float f1)
    {
        this.head = new ModelRenderer[5];
        this.head[0] = new ModelRenderer(this, 0, 0);
        this.head[1] = new ModelRenderer(this, 36, 0);
        this.head[2] = new ModelRenderer(this, 36, 0);
        this.head[3] = new ModelRenderer(this, 36, 8);
        this.head[4] = new ModelRenderer(this, 36, 8);
        this.head[0].addBox(-4.5F, -4.5F, -4.5F, 9, 9, 9, f);
        this.head[0].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.head[1].addBox(-3.5F, -3.5F, -5.5F, 7, 7, 1, f);
        this.head[1].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.head[2].addBox(-3.5F, -3.5F, 4.5F, 7, 7, 1, f);
        this.head[2].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.head[3].addBox(-5.5F, -3.5F, -3.5F, 1, 7, 7, f);
        this.head[3].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
        this.head[4].addBox(4.5F, -3.5F, -3.5F, 1, 7, 7, f);
        this.head[4].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.setRotationAngles(f, f1, f2, f3, f4, f5);

        for (int i = 0; i < 5; ++i)
        {
            this.head[i].render(f5);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for (int i = 0; i < 5; ++i)
        {
            this.head[i].rotateAngleY = f3 / (180F / (float)Math.PI);
            this.head[i].rotateAngleX = f4 / (180F / (float)Math.PI);
        }
    }
}
