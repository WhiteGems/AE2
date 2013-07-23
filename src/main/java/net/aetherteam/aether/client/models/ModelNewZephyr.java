package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityNewZephyr;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;

public class ModelNewZephyr extends ModelMinecart
{
    bdi mainbody;
    bdi RBcloud;
    bdi LBcloud;
    bdi tail2;
    bdi FRcloud;
    bdi FLcloud;
    bdi tail1;
    bdi Shape1;
    bdi Shape2;
    bdi Shape3;
    public float sinage;
    public float sinage2;

    public ModelNewZephyr()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.mainbody = new bdi(this, 0, 0);
        this.mainbody.a(-6.0F, -4.0F, -7.0F, 12, 8, 14);
        this.mainbody.a(0.0F, 10.0F, 0.0F);
        this.mainbody.b(64, 64);
        this.mainbody.i = true;
        setRotation(this.mainbody, 0.0F, 0.0F, 0.0F);
        this.RBcloud = new bdi(this, 16, 22);
        this.RBcloud.a(-7.0F, -2.0F, 0.0F, 2, 6, 6);
        this.RBcloud.a(0.0F, 10.0F, 0.0F);
        this.RBcloud.b(64, 64);
        this.RBcloud.i = true;
        setRotation(this.RBcloud, 0.0F, 0.0F, 0.0F);
        this.LBcloud = new bdi(this, 16, 22);
        this.LBcloud.a(5.0F, -2.0F, 0.0F, 2, 6, 6);
        this.LBcloud.a(0.0F, 10.0F, 0.0F);
        this.LBcloud.b(64, 64);
        this.LBcloud.i = true;
        setRotation(this.LBcloud, 0.0F, 0.0F, 0.0F);
        this.LBcloud.i = false;
        this.tail2 = new bdi(this, 32, 22);
        this.tail2.a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
        this.tail2.a(0.0F, 10.0F, 19.0F);
        this.tail2.b(64, 64);
        this.tail2.i = true;
        setRotation(this.tail2, 0.0F, 0.0F, 0.0F);
        this.FRcloud = new bdi(this, 0, 22);
        this.FRcloud.a(-8.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FRcloud.a(0.0F, 10.0F, 0.0F);
        this.FRcloud.b(64, 64);
        this.FRcloud.i = true;
        setRotation(this.FRcloud, 0.0F, 0.0F, 0.0F);
        this.FLcloud = new bdi(this, 0, 22);
        this.FLcloud.a(6.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FLcloud.a(0.0F, 10.0F, 0.0F);
        this.FLcloud.b(64, 64);
        this.FLcloud.i = true;
        setRotation(this.FLcloud, 0.0F, 0.0F, 0.0F);
        this.FLcloud.i = false;
        this.tail1 = new bdi(this, 0, 34);
        this.tail1.a(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        this.tail1.a(0.0F, 10.0F, 12.0F);
        this.tail1.b(64, 64);
        this.tail1.i = true;
        setRotation(this.tail1, 0.0F, 0.0F, 0.0F);
        this.Shape1 = new bdi(this, 0, 1);
        this.Shape1.a(-7.0F, 0.0F, -9.0F, 4, 4, 2);
        this.Shape1.a(0.0F, 10.0F, 0.0F);
        this.Shape1.b(64, 64);
        this.Shape1.i = true;
        setRotation(this.Shape1, 0.0F, 0.0F, 0.0F);
        this.Shape2 = new bdi(this, 0, 1);
        this.Shape2.a(3.0F, 0.0F, -9.0F, 4, 4, 2);
        this.Shape2.a(0.0F, 10.0F, 0.0F);
        this.Shape2.b(64, 64);
        this.Shape2.i = true;
        setRotation(this.Shape2, 0.0F, 0.0F, 0.0F);
        this.Shape3 = new bdi(this, 0, 7);
        this.Shape3.a(-3.0F, 3.0F, -8.0F, 6, 3, 1);
        this.Shape3.a(0.0F, 10.0F, 0.0F);
        this.Shape3.b(64, 64);
        this.Shape3.i = true;
        setRotation(this.Shape3, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.mainbody.a(f5);
        this.RBcloud.a(f5);
        this.LBcloud.a(f5);
        this.tail2.a(f5);
        this.FRcloud.a(f5);
        this.FLcloud.a(f5);
        this.tail1.a(f5);
        this.Shape1.a(f5);
        this.Shape2.a(f5);
        this.Shape3.a(f5);
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
        float boff = this.sinage2;
        float yOffset = 5.5F;
        float vertMotion = (float)(Math.sin(f * 20.0F / (180D / Math.PI)) * f1 * 0.5D);
        float PI = (float)Math.PI;
        float initialOffset = PI / 2.0F;
        float offset = PI * 3.0F / 11.0F;
        this.FRcloud.d = (vertMotion + 10.0F);
        this.FLcloud.c = (vertMotion * 0.5F);
        this.LBcloud.d = (8.0F - vertMotion * 0.5F);
        this.RBcloud.d = (9.0F + vertMotion * 0.5F);
        this.Shape2.d = (10.0F - vertMotion);
        this.Shape2.c = (-vertMotion * 0.5F);
        this.Shape1.d = (10.0F + vertMotion);
        this.Shape1.c = (vertMotion * 0.5F);
        this.tail1.c = ((float)(Math.sin(f * 20.0F / (180D / Math.PI)) * f1 * 0.75D));
        this.tail1.g = ((float)Math.pow(0.9900000095367432D, -4.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.tail1.d = (10.0F - vertMotion);
        this.tail2.c = ((float)Math.pow(0.9900000095367432D, 1.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.tail2.d = (10.0F - vertMotion * 1.25F);
        this.tail2.g = (this.tail1.g + 0.25F);
        this.Shape3.d = (8 + (((EntityNewZephyr)entity).attackTime + 1));
        this.mainbody.d = (boff + yOffset + this.sinage * 2.0F);
        this.RBcloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.LBcloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.tail2.d = (boff + yOffset + this.sinage * 2.0F);
        this.FRcloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.FLcloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.tail1.d = (boff + yOffset + this.sinage * 2.0F);
        this.Shape1.d = (boff + yOffset + this.sinage * 2.0F);
        this.Shape2.d = (boff + yOffset + this.sinage * 2.0F);
        this.Shape3.d = (boff + yOffset + this.sinage * 2.0F);
    }
}

