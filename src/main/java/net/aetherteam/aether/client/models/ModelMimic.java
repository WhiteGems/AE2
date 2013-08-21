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

    public void render1(float f, float f1, float f2, float f3, float f4, float f5, EntityMimic mimic)
    {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, mimic);
        this.boxLid.rotateAngleX = (float)Math.PI - mimic.mouth;
        this.rightLeg.rotateAngleX = mimic.legs;
        this.leftLeg.rotateAngleX = -mimic.legs;
        this.box.render(f5);
    }

    public void render2(float f, float f1, float f2, float f3, float f4, float f5, EntityMimic mimic)
    {
        this.boxLid.render(f5);
        this.leftLeg.render(f5);
        this.rightLeg.render(f5);
    }
}
