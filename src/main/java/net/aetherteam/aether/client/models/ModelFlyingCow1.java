package net.aetherteam.aether.client.models;

import net.minecraft.client.model.TexturedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelFlyingCow1 extends TexturedQuad
{
    bdi udders;
    bdi horn1;
    bdi horn2;

    public ModelFlyingCow1()
    {
        super(12, 0.0F);
        this.a = new bdi(this, 0, 0);
        this.a.a(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.a.a(0.0F, 4.0F, -8.0F);
        this.horn1 = new bdi(this, 22, 0);
        this.horn1.a(-4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.horn1.a(0.0F, 3.0F, -7.0F);
        this.horn2 = new bdi(this, 22, 0);
        this.horn2.a(3.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.horn2.a(0.0F, 3.0F, -7.0F);
        this.udders = new bdi(this, 52, 0);
        this.udders.a(-2.0F, -3.0F, 0.0F, 4, 6, 2, 0.0F);
        this.udders.a(0.0F, 14.0F, 6.0F);
        this.udders.f = ((float)Math.PI / 2F);
        this.b = new bdi(this, 18, 4);
        this.b.a(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
        this.b.a(0.0F, 5.0F, 2.0F);
        this.c.c -= 1.0F;
        this.d.c += 1.0F;
        this.c.e += 0.0F;
        this.d.e += 0.0F;
        this.e.c -= 1.0F;
        this.f.c += 1.0F;
        this.e.e -= 1.0F;
        this.f.e -= 1.0F;
    }

    public void a(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.a(e, f, f1, f2, f3, f4, f5);

        if (this.s)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
            this.horn1.a(f5);
            this.horn2.a(f5);
            this.udders.a(f5);
        }
        else
        {
            this.horn1.a(f5);
            this.horn2.a(f5);
            this.udders.a(f5);
        }
    }

    public void a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.a(f, f1, f2, f3, f4, f5, entity);
        this.horn1.g = this.a.g;
        this.horn1.f = this.a.f;
        this.horn2.g = this.a.g;
        this.horn2.f = this.a.f;
    }
}

