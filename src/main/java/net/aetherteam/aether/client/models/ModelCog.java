package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCog extends ModelBase
{
    ModelRenderer c11;
    ModelRenderer c21;
    ModelRenderer c31;
    ModelRenderer c41;
    ModelRenderer c51;
    ModelRenderer c61;
    ModelRenderer c71;
    ModelRenderer c81;
    ModelRenderer t11;
    ModelRenderer t21;
    ModelRenderer t31;
    ModelRenderer t41;
    ModelRenderer t51;
    ModelRenderer t61;
    ModelRenderer t71;
    ModelRenderer t81;
    ModelRenderer t91;
    ModelRenderer t111;
    ModelRenderer t101;
    ModelRenderer t121;

    public ModelCog()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.c11 = new ModelRenderer(this, 44, 0);
        this.c11.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c11.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c11.setTextureSize(64, 32);
        this.c11.mirror = true;
        this.setRotation(this.c11, 0.0F, 0.0F, 0.0F);
        this.c21 = new ModelRenderer(this, 44, 0);
        this.c21.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c21.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c21.setTextureSize(64, 32);
        this.c21.mirror = true;
        this.setRotation(this.c21, 0.0F, 0.0F, ((float) Math.PI / 4F));
        this.c31 = new ModelRenderer(this, 44, 0);
        this.c31.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c31.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c31.setTextureSize(64, 32);
        this.c31.mirror = true;
        this.setRotation(this.c31, 0.0F, 0.0F, ((float) Math.PI / 2F));
        this.c41 = new ModelRenderer(this, 44, 0);
        this.c41.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c41.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c41.setTextureSize(64, 32);
        this.c41.mirror = true;
        this.setRotation(this.c41, 0.0F, 0.0F, 2.356194F);
        this.c51 = new ModelRenderer(this, 44, 0);
        this.c51.addBox(-3.0F, 3.0F, -2.0F, 6, 4, 4);
        this.c51.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c51.setTextureSize(64, 32);
        this.c51.mirror = true;
        this.setRotation(this.c51, 0.0F, 0.0F, 0.0F);
        this.c61 = new ModelRenderer(this, 44, 0);
        this.c61.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c61.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c61.setTextureSize(64, 32);
        this.c61.mirror = true;
        this.setRotation(this.c61, 0.0F, 0.0F, -2.356194F);
        this.c71 = new ModelRenderer(this, 44, 0);
        this.c71.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c71.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c71.setTextureSize(64, 32);
        this.c71.mirror = true;
        this.setRotation(this.c71, 0.0F, 0.0F, -((float) Math.PI / 2F));
        this.c81 = new ModelRenderer(this, 44, 0);
        this.c81.addBox(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c81.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.c81.setTextureSize(64, 32);
        this.c81.mirror = true;
        this.setRotation(this.c81, 0.0F, 0.0F, -((float) Math.PI / 4F));
        this.t11 = new ModelRenderer(this, 0, 0);
        this.t11.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t11.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t11.setTextureSize(64, 32);
        this.t11.mirror = true;
        this.setRotation(this.t11, 0.0F, 0.0F, 0.5235988F);
        this.t21 = new ModelRenderer(this, 0, 0);
        this.t21.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t21.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t21.setTextureSize(64, 32);
        this.t21.mirror = true;
        this.setRotation(this.t21, 0.0F, 0.0F, 1.047198F);
        this.t31 = new ModelRenderer(this, 0, 0);
        this.t31.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t31.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t31.setTextureSize(64, 32);
        this.t31.mirror = true;
        this.setRotation(this.t31, 0.0F, 0.0F, ((float) Math.PI / 2F));
        this.t41 = new ModelRenderer(this, 0, 0);
        this.t41.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t41.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t41.setTextureSize(64, 32);
        this.t41.mirror = true;
        this.setRotation(this.t41, 0.0F, 0.0F, 2.094395F);
        this.t51 = new ModelRenderer(this, 0, 0);
        this.t51.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t51.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t51.setTextureSize(64, 32);
        this.t51.mirror = true;
        this.setRotation(this.t51, 0.0F, 0.0F, 2.617994F);
        this.t61 = new ModelRenderer(this, 0, 0);
        this.t61.addBox(-1.0F, 7.0F, -2.0F, 2, 2, 4);
        this.t61.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t61.setTextureSize(64, 32);
        this.t61.mirror = true;
        this.setRotation(this.t61, 0.0F, 0.0F, 0.0F);
        this.t71 = new ModelRenderer(this, 0, 0);
        this.t71.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t71.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t71.setTextureSize(64, 32);
        this.t71.mirror = true;
        this.setRotation(this.t71, 0.0F, 0.0F, -2.617994F);
        this.t81 = new ModelRenderer(this, 0, 0);
        this.t81.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t81.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t81.setTextureSize(64, 32);
        this.t81.mirror = true;
        this.setRotation(this.t81, 0.0F, 0.0F, -2.094395F);
        this.t91 = new ModelRenderer(this, 0, 0);
        this.t91.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t91.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t91.setTextureSize(64, 32);
        this.t91.mirror = true;
        this.setRotation(this.t91, 0.0F, 0.0F, -((float) Math.PI / 2F));
        this.t111 = new ModelRenderer(this, 0, 0);
        this.t111.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t111.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t111.setTextureSize(64, 32);
        this.t111.mirror = true;
        this.setRotation(this.t111, 0.0F, 0.0F, -0.5235988F);
        this.t101 = new ModelRenderer(this, 0, 0);
        this.t101.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t101.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t101.setTextureSize(64, 32);
        this.t101.mirror = true;
        this.setRotation(this.t101, 0.0F, 0.0F, -1.047198F);
        this.t121 = new ModelRenderer(this, 0, 0);
        this.t121.addBox(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t121.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.t121.setTextureSize(64, 32);
        this.t121.mirror = true;
        this.setRotation(this.t121, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
        this.c11.render(var7);
        this.c21.render(var7);
        this.c31.render(var7);
        this.c41.render(var7);
        this.c51.render(var7);
        this.c61.render(var7);
        this.c71.render(var7);
        this.c81.render(var7);
        this.t11.render(var7);
        this.t21.render(var7);
        this.t31.render(var7);
        this.t41.render(var7);
        this.t51.render(var7);
        this.t61.render(var7);
        this.t71.render(var7);
        this.t81.render(var7);
        this.t91.render(var7);
        this.t111.render(var7);
        this.t101.render(var7);
        this.t121.render(var7);
    }

    private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.rotateAngleX = var2;
        var1.rotateAngleY = var3;
        var1.rotateAngleZ = var4;
    }
}
