package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelAngel extends ModelGhast
{
    bdi Scalf;
    bdi Wing_base;
    bdi Wing;
    bdi hair;
    bdi hair_back;
    bdi Shape1;

    public ModelAngel()
    {
        this.textureWidth = 118;
        this.textureHeight = 42;
        this.Scalf = new bdi(this, 0, 32);
        this.Scalf.a(-4.0F, 0.0F, -4.0F, 8, 2, 8);
        this.Scalf.a(0.0F, -0.4F, -0.6F);
        this.Scalf.b(118, 42);
        this.Scalf.i = true;
        setRotation(this.Scalf, 0.1487144F, 0.0F, -0.0371786F);
        this.c = new bdi(this, 0, 0);
        this.c.a(-4.0F, -8.0F, -4.0F, 8, 8, 8);
        this.c.a(0.0F, 0.0F, 0.0F);
        this.c.b(118, 42);
        this.c.i = true;
        setRotation(this.c, 0.0F, 0.0F, 0.0F);
        this.e = new bdi(this, 0, 16);
        this.e.a(-4.0F, 0.0F, -2.0F, 8, 12, 4);
        this.e.a(0.0F, 0.0F, 0.0F);
        this.e.b(118, 42);
        this.e.i = true;
        setRotation(this.e, 0.0F, 0.0F, 0.0F);
        this.f = new bdi(this, 48, 0);
        this.f.a(-3.0F, -2.0F, -2.0F, 4, 12, 4);
        this.f.a(-5.0F, 2.0F, 0.0F);
        this.f.b(118, 42);
        this.f.i = true;
        setRotation(this.f, 0.0F, 0.0F, 0.0F);
        this.g.i = true;
        this.g = new bdi(this, 48, 0);
        this.g.a(-1.0F, -2.0F, -2.0F, 4, 12, 4);
        this.g.a(5.0F, 2.0F, 0.0F);
        this.g.b(118, 42);
        this.g.i = true;
        setRotation(this.g, 0.0F, 0.0F, 0.0F);
        this.g.i = false;
        this.h = new bdi(this, 32, 0);
        this.h.a(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.h.a(-2.0F, 12.0F, 0.0F);
        this.h.b(118, 42);
        this.h.i = true;
        setRotation(this.h, 0.0F, 0.0F, 0.0F);
        this.i.i = true;
        this.i = new bdi(this, 32, 0);
        this.i.a(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        this.i.a(2.0F, 12.0F, 0.0F);
        this.i.b(118, 42);
        this.i.i = true;
        setRotation(this.i, 0.0F, 0.0F, 0.0F);
        this.i.i = false;
        this.Wing_base = new bdi(this, 64, 13);
        this.Wing_base.a(0.0F, 0.0F, 0.0F, 1, 2, 9);
        this.Wing_base.a(-2.0F, 1.0F, 1.0F);
        this.Wing_base.b(118, 42);
        this.Wing_base.i = true;
        setRotation(this.Wing_base, 0.3346075F, -0.2974289F, 0.0F);
        this.Wing = new bdi(this, 100, 0);
        this.Wing.a(0.0F, -1.0F, -4.0F, 2, 22, 7);
        this.Wing.a(-3.7F, 0.0F, 6.6F);
        this.Wing.b(118, 42);
        this.Wing.i = true;
        setRotation(this.Wing, 0.2974289F, -0.3346075F, 0.0F);
        this.hair = new bdi(this, 64, 0);
        this.hair.a(-4.533333F, -8.5F, -4.5F, 9, 4, 9);
        this.hair.a(0.0F, 0.0F, 0.2F);
        this.hair.b(118, 42);
        this.hair.i = true;
        setRotation(this.hair, 0.111536F, 0.0F, 0.0F);
        this.hair_back = new bdi(this, 24, 25);
        this.hair_back.a(-2.0F, 0.0F, 0.0F, 5, 6, 1);
        this.hair_back.a(1.0F, -1.0F, 3.0F);
        this.hair_back.b(118, 42);
        this.hair_back.i = true;
        setRotation(this.hair_back, 0.446143F, 0.0F, 0.0F);
        this.Shape1 = new bdi(this, 24, 16);
        this.Shape1.a(-3.5F, 0.0F, 0.0F, 7, 2, 1);
        this.Shape1.a(0.0F, 1.5F, -4.0F);
        this.Shape1.b(118, 42);
        this.Shape1.i = true;
        setRotation(this.Shape1, 0.8922867F, 0.111536F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.d.k = true;
        this.hair.g = this.c.g;
        this.hair.f = this.c.f;
        this.hair_back.g = this.c.g;
        this.hair_back.f = this.c.f;
        this.Scalf.a(f5);
        this.c.a(f5);
        this.e.a(f5);
        this.f.a(f5);
        this.g.a(f5);
        this.h.a(f5);
        this.i.a(f5);
        this.Wing_base.a(f5);
        this.Wing.a(f5);
        this.hair.a(f5);
        this.hair_back.a(f5);
        this.Shape1.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }
}

