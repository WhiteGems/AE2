package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelAerwhale extends ModelMinecart
{
    bdi FrontBody;
    bdi RightFin;
    bdi BottomPartHead;
    bdi LeftFin;
    bdi BottomPartMiddlebody;
    bdi Head;
    bdi MiddleFin;
    bdi BackfinRight;
    bdi BackBody;
    bdi BackfinLeft;
    bdi Middlebody;

    public ModelAerwhale()
    {
        this.textureWidth = 512;
        this.textureHeight = 64;
        this.FrontBody = new bdi(this, 0, 0);
        this.FrontBody.a(-11.5F, -1.0F, -0.5F, 19, 5, 21);
        this.FrontBody.a(2.0F, 6.0F, 38.0F);
        this.FrontBody.b(512, 64);
        this.FrontBody.i = true;
        setRotation(this.FrontBody, -0.10472F, 0.0F, 0.0F);
        this.RightFin = new bdi(this, 446, 1);
        this.RightFin.a(-20.0F, -2.0F, -6.0F, 19, 3, 14);
        this.RightFin.a(-10.0F, 4.0F, 10.0F);
        this.RightFin.b(512, 64);
        this.RightFin.i = true;
        setRotation(this.RightFin, -0.148353F, 0.20944F, 0.0F);
        this.RightFin.i = false;
        this.BottomPartHead = new bdi(this, 116, 28);
        this.BottomPartHead.a(-13.0F, 4.0F, -15.0F, 26, 6, 30);
        this.BottomPartHead.a(0.0F, 0.0F, 0.0F);
        this.BottomPartHead.b(512, 64);
        this.BottomPartHead.i = true;
        setRotation(this.BottomPartHead, 0.0F, 0.0F, 0.0F);
        this.LeftFin = new bdi(this, 446, 1);
        this.LeftFin.a(1.0F, -2.0F, -6.0F, 19, 3, 14);
        this.LeftFin.a(10.0F, 4.0F, 10.0F);
        this.LeftFin.b(512, 64);
        this.LeftFin.i = true;
        setRotation(this.LeftFin, -0.148353F, -0.20944F, 0.0F);
        this.BottomPartMiddlebody = new bdi(this, 16, 32);
        this.BottomPartMiddlebody.a(-12.0F, 5.0F, -1.0F, 24, 6, 26);
        this.BottomPartMiddlebody.a(0.0F, -1.0F, 14.0F);
        this.BottomPartMiddlebody.b(512, 64);
        this.BottomPartMiddlebody.i = true;
        setRotation(this.BottomPartMiddlebody, 0.0F, 0.0F, 0.0F);
        this.Head = new bdi(this, 408, 18);
        this.Head.a(-12.0F, -9.0F, -14.0F, 24, 18, 28);
        this.Head.a(0.0F, 0.0F, 0.0F);
        this.Head.b(512, 64);
        this.Head.i = true;
        setRotation(this.Head, 0.0F, 0.0F, 0.0F);
        this.MiddleFin = new bdi(this, 318, 35);
        this.MiddleFin.a(-1.0F, -11.0F, 7.0F, 2, 7, 8);
        this.MiddleFin.a(0.0F, -1.0F, 14.0F);
        this.MiddleFin.b(512, 64);
        this.MiddleFin.i = true;
        setRotation(this.MiddleFin, -0.14417F, 0.0F, 0.0F);
        this.BackfinRight = new bdi(this, 261, 5);
        this.BackfinRight.a(-11.0F, 0.0F, -6.0F, 15, 3, 24);
        this.BackfinRight.a(-4.0F, 5.0F, 59.0F);
        this.BackfinRight.b(512, 64);
        this.BackfinRight.i = true;
        setRotation(this.BackfinRight, -0.10472F, -0.7330383F, 0.0F);
        this.BackfinRight.i = false;
        this.BackBody = new bdi(this, 228, 32);
        this.BackBody.a(-10.5F, -9.0F, -2.0F, 17, 10, 22);
        this.BackBody.a(2.0F, 5.0F, 38.0F);
        this.BackBody.b(512, 64);
        this.BackBody.i = true;
        setRotation(this.BackBody, -0.10472F, 0.0F, 0.0F);
        this.BackfinLeft = new bdi(this, 261, 5);
        this.BackfinLeft.a(-4.0F, 0.0F, -6.0F, 13, 3, 24);
        this.BackfinLeft.a(5.0F, 5.0F, 59.0F);
        this.BackfinLeft.b(512, 64);
        this.BackfinLeft.i = true;
        setRotation(this.BackfinLeft, -0.10472F, 0.7330383F, 0.0F);
        this.Middlebody = new bdi(this, 314, 25);
        this.Middlebody.a(-11.0F, -5.0F, -1.0F, 22, 14, 25);
        this.Middlebody.a(0.0F, -1.0F, 14.0F);
        this.Middlebody.b(512, 64);
        this.Middlebody.i = true;
        setRotation(this.Middlebody, -0.0698132F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.FrontBody.a(f5);
        this.RightFin.a(f5);
        this.BottomPartHead.a(f5);
        this.LeftFin.a(f5);
        this.BottomPartMiddlebody.a(f5);
        this.Head.a(f5);
        this.MiddleFin.a(f5);
        this.BackfinRight.a(f5);
        this.BackBody.a(f5);
        this.BackfinLeft.a(f5);
        this.Middlebody.a(f5);
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

