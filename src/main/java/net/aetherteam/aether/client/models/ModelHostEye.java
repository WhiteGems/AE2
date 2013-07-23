package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelHostEye extends ModelMinecart
{
    bdi Eye;

    public ModelHostEye()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Eye = new bdi(this, 0, 0);
        this.Eye.a(0.0F, 0.0F, 0.0F, 11, 11, 11);
        this.Eye.a(-5.5F, 13.0F, -5.5F);
        this.Eye.b(64, 32);
        this.Eye.i = true;
        setRotation(this.Eye, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Eye.a(f5);
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

