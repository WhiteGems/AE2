package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelAerbunny extends ModelMinecart
{
    public bdi a;
    public bdi b;
    public bdi b2;
    public bdi b3;
    public bdi e1;
    public bdi e2;
    public bdi ff1;
    public bdi ff2;
    public bdi g;
    public bdi g2;
    public bdi h;
    public bdi h2;
    public float puffiness;

    public ModelAerbunny()
    {
        byte byte0 = 16;
        this.a = new bdi(this, 0, 0);
        this.a.a(-2.0F, -1.0F, -4.0F, 4, 4, 6, 0.0F);
        this.a.a(0.0F, -1 + byte0, -4.0F);
        this.g = new bdi(this, 14, 0);
        this.g.a(-2.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g.a(0.0F, -1 + byte0, -4.0F);
        this.g2 = new bdi(this, 14, 0);
        this.g2.a(1.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g2.a(0.0F, -1 + byte0, -4.0F);
        this.h = new bdi(this, 20, 0);
        this.h.a(-4.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h.a(0.0F, -1 + byte0, -4.0F);
        this.h2 = new bdi(this, 20, 0);
        this.h2.a(2.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h2.a(0.0F, -1 + byte0, -4.0F);
        this.b = new bdi(this, 0, 10);
        this.b.a(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.b.a(0.0F, 0 + byte0, 0.0F);
        this.b2 = new bdi(this, 0, 24);
        this.b2.a(-2.0F, 4.0F, -2.0F, 4, 3, 4, 0.0F);
        this.b2.a(0.0F, 0 + byte0, 0.0F);
        this.b3 = new bdi(this, 29, 0);
        this.b3.a(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.b3.a(0.0F, 0.0F, 0.0F);
        this.e1 = new bdi(this, 24, 16);
        this.e1.a(-2.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e1.a(3.0F, 3 + byte0, -3.0F);
        this.e2 = new bdi(this, 24, 16);
        this.e2.a(0.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e2.a(-3.0F, 3 + byte0, -3.0F);
        this.ff1 = new bdi(this, 16, 24);
        this.ff1.a(-2.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff1.a(3.0F, 3 + byte0, 4.0F);
        this.ff2 = new bdi(this, 16, 24);
        this.ff2.a(0.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff2.a(-3.0F, 3 + byte0, 4.0F);
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);

        if (this.isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, f5, f5);
            this.a.a(f5);
            this.g.a(f5);
            this.g2.a(f5);
            this.h.a(f5);
            this.h2.a(f5);
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 18.0F * f5, 0.0F);
            this.b.a(f5);
            this.b2.a(f5);
            this.e1.a(f5);
            this.e2.a(f5);
            this.ff1.a(f5);
            this.ff2.a(f5);
            float a = 1.0F + this.puffiness * 0.5F;
            GL11.glTranslatef(0.0F, 1.0F, 0.0F);
            GL11.glScalef(a, a, a);
            this.b3.a(f5);
        }
        else
        {
            this.a.a(f5);
            this.g.a(f5);
            this.g2.a(f5);
            this.h.a(f5);
            this.h2.a(f5);
            this.b.a(f5);
            this.b2.a(f5);
            GL11.glPushMatrix();
            float a = 1.0F + this.puffiness * 0.5F;
            GL11.glTranslatef(0.0F, 1.0F, 0.0F);
            GL11.glScalef(a, a, a);
            this.b3.a(f5);
            GL11.glPopMatrix();
            this.e1.a(f5);
            this.e2.a(f5);
            this.ff1.a(f5);
            this.ff2.a(f5);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.a.f = (-(f4 / (180F / (float)Math.PI)));
        this.a.g = (f3 / (180F / (float)Math.PI));
        this.g.f = this.a.f;
        this.g.g = this.a.g;
        this.g2.f = this.a.f;
        this.g2.g = this.a.g;
        this.h.f = this.a.f;
        this.h.g = this.a.g;
        this.h2.f = this.a.f;
        this.h2.g = this.a.g;
        this.b.f = ((float)Math.PI / 2F);
        this.b2.f = ((float)Math.PI / 2F);
        this.b3.f = ((float)Math.PI / 2F);
        this.e1.f = (MathHelper.cos(f * 0.6662F) * 1.0F * f1);
        this.ff1.f = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.2F * f1);
        this.e2.f = (MathHelper.cos(f * 0.6662F) * 1.0F * f1);
        this.ff2.f = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.2F * f1);
    }
}

