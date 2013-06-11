package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMimic extends ModelBase
{
    ModelRenderer box = new ModelRenderer(this, 0, 0);
    ModelRenderer boxLid;
    ModelRenderer leftLeg;
    ModelRenderer rightLeg;

    public ModelMimic()
    {
        this.box.addBox(-8.0F, 0.0F, -8.0F, 16, 10, 16);
        this.box.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.boxLid = new ModelRenderer(this, 16, 10);
        this.boxLid.addBox(0.0F, 0.0F, 0.0F, 16, 6, 16);
        this.boxLid.setRotationPoint(-8.0F, 0.0F, 8.0F);
        this.leftLeg = new ModelRenderer(this, 0, 0);
        this.leftLeg.addBox(-3.0F, 0.0F, -3.0F, 6, 15, 6);
        this.leftLeg.setRotationPoint(-4.0F, 9.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 0);
        this.rightLeg.addBox(-3.0F, 0.0F, -3.0F, 6, 15, 6);
        this.rightLeg.setRotationPoint(4.0F, 9.0F, 0.0F);
    }

    public void render1(float var1, float var2, float var3, float var4, float var5, float var6, EntityMimic var7)
    {
        this.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
        this.boxLid.rotateAngleX = (float) Math.PI - var7.mouth;
        this.rightLeg.rotateAngleX = var7.legs;
        this.leftLeg.rotateAngleX = -var7.legs;
        this.box.render(var6);
    }

    public void render2(float var1, float var2, float var3, float var4, float var5, float var6, EntityMimic var7)
    {
        this.boxLid.render(var6);
        this.leftLeg.render(var6);
        this.rightLeg.render(var6);
    }
}
