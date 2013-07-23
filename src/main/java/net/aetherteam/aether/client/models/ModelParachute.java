package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelParachute extends ModelMinecart
{
    public bdi Cloud1;
    public bdi Cloud2;
    public bdi Cloud3;
    public bdi Cloud4;
    public bdi Cloud5;
    public bdi Shape2;
    public bdi Shape3;
    public bdi Shape4;
    public bdi Shape1;
    public float sinage;

    public ModelParachute()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.Cloud1 = new bdi(this, 0, 0);
        this.Cloud1.a(0.0F, 0.0F, 0.0F, 8, 8, 16);
        this.Cloud1.a(-29.5F, 2.0F, 0.0F);
        this.Cloud1.b(64, 64);
        this.Cloud1.i = true;
        setRotation(this.Cloud1, 0.0F, 0.0F, -((float)Math.PI * 2F / 9F));
        this.Cloud2 = new bdi(this, 0, 0);
        this.Cloud2.a(0.0F, 0.0F, 0.0F, 8, 8, 16);
        this.Cloud2.a(23.65F, -3.0F, 0.0F);
        this.Cloud2.b(64, 64);
        this.Cloud2.i = true;
        setRotation(this.Cloud2, 0.0F, 0.0F, ((float)Math.PI * 2F / 9F));
        this.Cloud3 = new bdi(this, 0, 0);
        this.Cloud3.a(0.0F, 0.0F, 0.0F, 16, 8, 16);
        this.Cloud3.a(-8.0F, -6.0F, 0.0F);
        this.Cloud3.b(64, 64);
        this.Cloud3.i = true;
        setRotation(this.Cloud3, 0.0F, 0.0F, 0.0F);
        this.Cloud4 = new bdi(this, 0, 0);
        this.Cloud4.a(0.0F, 0.0F, 0.0F, 16, 8, 16);
        this.Cloud4.a(8.0F, -6.0F, 0.0F);
        this.Cloud4.b(64, 64);
        this.Cloud4.i = true;
        setRotation(this.Cloud4, 0.0F, 0.0F, 0.191986F);
        this.Cloud5 = new bdi(this, 0, 0);
        this.Cloud5.a(0.0F, 0.0F, 0.0F, 16, 8, 16);
        this.Cloud5.a(-23.5F, -3.0F, 0.0F);
        this.Cloud5.b(64, 64);
        this.Cloud5.i = true;
        setRotation(this.Cloud5, 0.0F, 0.0F, -0.191986F);
        this.Shape2 = new bdi(this, 6, 0);
        this.Shape2.a(0.0F, 0.0F, -3.0F, 2, 14, 2);
        this.Shape2.a(5.0F, 0.0F, 13.0F);
        this.Shape2.b(64, 64);
        this.Shape2.i = true;
        setRotation(this.Shape2, -0.191986F, 0.0F, 0.10472F);
        this.Shape3 = new bdi(this, 0, 0);
        this.Shape3.a(0.0F, 0.0F, -3.0F, 2, 14, 2);
        this.Shape3.a(-8.0F, 0.0F, 12.0F);
        this.Shape3.b(64, 64);
        this.Shape3.i = true;
        setRotation(this.Shape3, -0.191986F, 0.0F, -0.10472F);
        this.Shape4 = new bdi(this, 6, 0);
        this.Shape4.a(0.0F, 0.0F, 0.0F, 2, 14, 2);
        this.Shape4.a(5.0F, 0.0F, 1.0F);
        this.Shape4.b(64, 64);
        this.Shape4.i = true;
        setRotation(this.Shape4, 0.191986F, 0.0F, 0.10472F);
        this.Shape1 = new bdi(this, 0, 0);
        this.Shape1.a(0.0F, 0.0F, 0.0F, 2, 14, 2);
        this.Shape1.a(-8.0F, 0.0F, 1.0F);
        this.Shape1.b(64, 64);
        this.Shape1.i = true;
        setRotation(this.Shape1, 0.191986F, 0.0F, -0.10472F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Cloud1.a(f5);
        this.Cloud2.a(f5);
        this.Cloud3.a(f5);
        this.Cloud4.a(f5);
        this.Cloud5.a(f5);
        this.Shape2.a(f5);
        this.Shape3.a(f5);
        this.Shape4.a(f5);
        this.Shape1.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
    }
}

