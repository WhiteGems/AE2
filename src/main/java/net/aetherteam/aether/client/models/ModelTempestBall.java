package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelTempestBall extends ModelMinecart
{
    bdi Shape2;
    bdi Shape21;
    bdi Shape23;
    bdi Shape24;

    public ModelTempestBall()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Shape2 = new bdi(this, 0, 16);
        this.Shape2.a(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape2.a(-3.0F, 1.0F, 1.0F);
        this.Shape2.b(64, 32);
        this.Shape2.i = true;
        setRotation(this.Shape2, 0.26025F, 0.4833219F, -0.0743572F);
        this.Shape21 = new bdi(this, 0, 0);
        this.Shape21.a(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape21.a(9.0F, 2.0F, 7.0F);
        this.Shape21.b(64, 32);
        this.Shape21.i = true;
        setRotation(this.Shape21, 0.0F, -(float)Math.PI, -0.1745329F);
        this.Shape23 = new bdi(this, 32, 0);
        this.Shape23.a(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape23.a(0.0F, 0.0F, 0.0F);
        this.Shape23.b(64, 32);
        this.Shape23.i = true;
        setRotation(this.Shape23, 0.0F, -0.1745329F, -0.1745329F);
        this.Shape24 = new bdi(this, 32, 16);
        this.Shape24.a(0.0F, 0.0F, 0.0F, 8, 8, 8);
        this.Shape24.a(0.0F, 0.0F, 0.0F);
        this.Shape24.b(64, 32);
        this.Shape24.i = true;
        setRotation(this.Shape24, 0.0F, 0.111536F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Shape2.a(f5);
        this.Shape21.a(f5);
        this.Shape23.a(f5);
        this.Shape24.a(f5);
    }

    public void render(Entity entity, float f5)
    {
        this.Shape2.a(f5);
        this.Shape21.a(f5);
        this.Shape23.a(f5);
        this.Shape24.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}

