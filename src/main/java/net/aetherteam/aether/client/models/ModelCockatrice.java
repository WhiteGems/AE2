package net.aetherteam.aether.client.models;

import java.util.Random;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCockatrice extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer legs;
    public ModelRenderer legs2;
    public ModelRenderer wings;
    public ModelRenderer wings2;
    public ModelRenderer jaw;
    public ModelRenderer neck;
    public ModelRenderer feather1;
    public ModelRenderer feather2;
    public ModelRenderer feather3;
    public Random random;

    public ModelCockatrice()
    {
        byte byte0 = 16;
        this.random = new Random();
        this.head = new ModelRenderer(this, 0, 13);
        this.head.addBox(-2.0F, -4.0F, -6.0F, 4, 4, 8, 0.0F);
        this.head.setRotationPoint(0.0F, (float)(-8 + byte0), -4.0F);
        this.jaw = new ModelRenderer(this, 24, 13);
        this.jaw.addBox(-2.0F, -1.0F, -6.0F, 4, 1, 8, -0.1F);
        this.jaw.setRotationPoint(0.0F, (float)(-8 + byte0), -4.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-3.0F, -3.0F, 0.0F, 6, 8, 5, 0.0F);
        this.body.setRotationPoint(0.0F, (float)(0 + byte0), 0.0F);
        this.legs = new ModelRenderer(this, 22, 0);
        this.legs.addBox(-1.0F, -1.0F, -1.0F, 2, 9, 2);
        this.legs.setRotationPoint(-2.0F, (float)(0 + byte0), 1.0F);
        this.legs2 = new ModelRenderer(this, 22, 0);
        this.legs2.addBox(-1.0F, -1.0F, -1.0F, 2, 9, 2);
        this.legs2.setRotationPoint(2.0F, (float)(0 + byte0), 1.0F);
        this.wings = new ModelRenderer(this, 52, 0);
        this.wings.addBox(-1.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings.setRotationPoint(-3.0F, (float)(-4 + byte0), 0.0F);
        this.wings2 = new ModelRenderer(this, 52, 0);
        this.wings2.addBox(0.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings2.setRotationPoint(3.0F, (float)(-4 + byte0), 0.0F);
        this.neck = new ModelRenderer(this, 44, 0);
        this.neck.addBox(-1.0F, -6.0F, -1.0F, 2, 6, 2);
        this.neck.setRotationPoint(0.0F, (float)(-2 + byte0), -4.0F);
        this.feather1 = new ModelRenderer(this, 30, 0);
        this.feather1.addBox(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather1.setRotationPoint(0.0F, (float)(1 + byte0), 1.0F);
        this.feather2 = new ModelRenderer(this, 30, 0);
        this.feather2.addBox(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather2.setRotationPoint(0.0F, (float)(1 + byte0), 1.0F);
        this.feather3 = new ModelRenderer(this, 30, 0);
        this.feather3.addBox(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather3.setRotationPoint(0.0F, (float)(1 + byte0), 1.0F);
        this.feather1.rotationPointY += 0.5F;
        this.feather2.rotationPointY += 0.5F;
        this.feather3.rotationPointY += 0.5F;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.setRotationAngles(f, f1, f2, f3, f4, f5);
        this.head.render(f5);
        this.jaw.render(f5);
        this.body.render(f5);
        this.legs.render(f5);
        this.legs2.render(f5);
        this.wings.render(f5);
        this.wings2.render(f5);
        this.neck.render(f5);
        this.feather1.render(f5);
        this.feather2.render(f5);
        this.feather3.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (float)Math.PI;
        this.head.rotateAngleX = f4 / (180F / (float)Math.PI);
        this.head.rotateAngleY = f3 / (180F / (float)Math.PI);
        this.jaw.rotateAngleX = this.head.rotateAngleX;
        this.jaw.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.legs.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.legs2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;

        if (f2 > 0.001F)
        {
            this.wings.rotationPointZ = -1.0F;
            this.wings2.rotationPointZ = -1.0F;
            this.wings.rotationPointY = 12.0F;
            this.wings2.rotationPointY = 12.0F;
            this.wings.rotateAngleX = 0.0F;
            this.wings2.rotateAngleX = 0.0F;
            this.wings.rotateAngleZ = f2;
            this.wings2.rotateAngleZ = -f2;
            this.legs.rotateAngleX = 0.6F;
            this.legs2.rotateAngleX = 0.6F;
        }
        else
        {
            this.wings.rotationPointZ = -3.0F;
            this.wings2.rotationPointZ = -3.0F;
            this.wings.rotationPointY = 14.0F;
            this.wings2.rotationPointY = 14.0F;
            this.wings.rotateAngleX = f6 / 2.0F;
            this.wings2.rotateAngleX = f6 / 2.0F;
            this.wings.rotateAngleZ = 0.0F;
            this.wings2.rotateAngleZ = 0.0F;
        }

        this.feather1.rotateAngleY = -0.375F;
        this.feather2.rotateAngleY = 0.0F;
        this.feather3.rotateAngleY = 0.375F;
        this.feather1.rotateAngleX = 0.25F;
        this.feather2.rotateAngleX = 0.25F;
        this.feather3.rotateAngleX = 0.25F;
        this.neck.rotateAngleX = 0.0F;
        this.neck.rotateAngleY = this.head.rotateAngleY;
        this.jaw.rotateAngleX += 0.35F;
    }
}
