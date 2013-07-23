package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;

public class ModelZephyr extends ModelMinecart
{
    bdi LeftFace;
    bdi BodyRightSide2;
    bdi Mouth;
    bdi CloudButt;
    bdi Tail3;
    bdi RightFace;
    bdi BodyLeftSide1;
    bdi BodyLeftSide2;
    bdi Body;
    bdi BodyRightSide1;
    bdi Tail1;
    bdi Tail2;

    public ModelZephyr()
    {
        this.textureWidth = 128;
        this.textureHeight = 32;
        setTextureOffset("Tail1.tail1", 96, 22);
        setTextureOffset("Tail2.tail2", 80, 24);
        setTextureOffset("Tail3.tail3", 84, 18);
        this.Tail1 = new bdi(this, "Tail1");
        this.Tail1.a(0.0F, 0.0F, 12.4F);
        this.Tail1.a("tail1", -2.5F, -2.5F, -2.5F, 5, 5, 5);
        this.Tail2 = new bdi(this, "Tail2");
        this.Tail2.a(0.0F, 0.0F, 6.0F);
        this.Tail2.a("tail2", -2.0F, -2.0F, -1.966667F, 4, 4, 4);
        this.Tail3 = new bdi(this, "Tail3");
        this.Tail3.a(0.0F, 0.0F, 5.0F);
        this.Tail3.a(-1.5F, -1.5F, -1.5F, 3, 3, 3);
        this.Tail1.a(this.Tail2);
        this.Tail2.a(this.Tail3);
        this.LeftFace = new bdi(this, 67, 11);
        this.LeftFace.a(3.0F, -1.0F, -9.0F, 4, 6, 2);
        this.LeftFace.a(0.0F, 8.0F, 0.0F);
        this.LeftFace.b(128, 32);
        this.LeftFace.i = true;
        setRotation(this.LeftFace, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide2 = new bdi(this, 25, 11);
        this.BodyRightSide2.a(-2.0F, -3.333333F, -2.5F, 2, 6, 6);
        this.BodyRightSide2.a(-5.5F, 9.0F, 2.0F);
        this.BodyRightSide2.b(128, 32);
        this.BodyRightSide2.i = true;
        setRotation(this.BodyRightSide2, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide2.i = false;
        this.Mouth = new bdi(this, 66, 19);
        this.Mouth.a(-3.0F, 1.0F, -8.0F, 6, 3, 1);
        this.Mouth.a(0.0F, 8.0F, 0.0F);
        this.Mouth.b(128, 32);
        this.Mouth.i = true;
        setRotation(this.Mouth, 0.0F, 0.0F, 0.0F);
        this.CloudButt = new bdi(this, 0, 0);
        this.CloudButt.a(-6.0F, -3.0F, 0.0F, 8, 6, 2);
        this.CloudButt.a(2.0F, 8.0F, 7.0F);
        this.CloudButt.b(128, 32);
        this.CloudButt.i = true;
        setRotation(this.CloudButt, 0.0F, 0.0F, 0.0F);
        this.RightFace = new bdi(this, 67, 11);
        this.RightFace.a(-7.0F, -1.0F, -9.0F, 4, 6, 2);
        this.RightFace.a(0.0F, 8.0F, 0.0F);
        this.RightFace.b(128, 32);
        this.RightFace.i = true;
        setRotation(this.RightFace, 0.0F, 0.0F, 0.0F);
        this.RightFace.i = false;
        this.BodyLeftSide1 = new bdi(this, 0, 20);
        this.BodyLeftSide1.a(0.0F, -3.0F, -3.0F, 2, 6, 6);
        this.BodyLeftSide1.a(6.0F, 8.0F, -4.0F);
        this.BodyLeftSide1.b(128, 32);
        this.BodyLeftSide1.i = true;
        setRotation(this.BodyLeftSide1, 0.0F, 0.0F, 0.0F);
        this.BodyLeftSide2 = new bdi(this, 25, 11);
        this.BodyLeftSide2.a(0.0F, -3.333333F, -2.5F, 2, 6, 6);
        this.BodyLeftSide2.a(5.5F, 9.0F, 2.0F);
        this.BodyLeftSide2.b(128, 32);
        this.BodyLeftSide2.i = true;
        setRotation(this.BodyLeftSide2, 0.0F, 0.0F, 0.0F);
        this.Body = new bdi(this, 27, 9);
        this.Body.a(-6.0F, -4.0F, -7.0F, 12, 9, 14);
        this.Body.a(0.0F, 8.0F, 0.0F);
        this.Body.b(128, 32);
        setRotation(this.Body, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide1 = new bdi(this, 0, 20);
        this.BodyRightSide1.a(-2.0F, -3.0F, -3.0F, 2, 6, 6);
        this.BodyRightSide1.a(-6.0F, 8.0F, -4.0F);
        this.BodyRightSide1.b(128, 32);
        this.BodyRightSide1.i = true;
        setRotation(this.BodyRightSide1, 0.0F, 0.0F, 0.0F);
        this.BodyRightSide1.i = false;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.LeftFace.a(f5);
        this.BodyRightSide2.a(f5);
        this.Mouth.a(f5);
        this.CloudButt.a(f5);
        this.RightFace.a(f5);
        this.BodyLeftSide1.a(f5);
        this.BodyLeftSide2.a(f5);
        this.Body.a(f5);
        this.BodyRightSide1.a(f5);
        this.Tail1.a(f5);
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
        float vertMotion = (float)(Math.sin(f * 20.0F / (180D / Math.PI)) * f1 * 0.5D);
        float PI = (float)Math.PI;
        float initialOffset = PI / 2.0F;
        float offset = PI * 3.0F / 11.0F;
        this.LeftFace.d = (vertMotion + 8.0F);
        this.LeftFace.c = (vertMotion * 0.5F);
        this.BodyLeftSide1.d = (8.0F - vertMotion * 0.5F);
        this.BodyLeftSide2.d = (9.0F + vertMotion * 0.5F);
        this.RightFace.d = (8.0F - vertMotion);
        this.RightFace.c = (-vertMotion * 0.5F);
        this.BodyRightSide1.d = (8.0F - vertMotion * 0.5F);
        this.BodyRightSide2.d = (9.0F + vertMotion * 0.5F);
        this.Tail1.c = ((float)(Math.sin(f * 20.0F / (180D / Math.PI)) * f1 * 0.75D));
        this.Tail1.g = ((float)Math.pow(0.9900000095367432D, -4.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.Tail1.d = (8.0F - vertMotion);
        this.Tail2.c = ((float)Math.pow(0.9900000095367432D, 1.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.Tail2.d = (vertMotion * 1.25F);
        this.Tail2.g = (this.Tail1.g + 0.25F);
        this.Tail3.c = ((float)Math.pow(0.9900000095367432D, 2.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.Tail3.d = (-vertMotion);
        this.Tail3.g = (this.Tail2.g + 0.35F);
    }
}

