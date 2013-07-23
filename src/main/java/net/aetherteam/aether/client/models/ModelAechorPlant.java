package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelAechorPlant extends ModelMinecart
{
    private bdi[] petal;
    private bdi[] leaf;
    private bdi[] stamen;
    private bdi[] stamen2;
    private bdi[] thorn;
    private bdi stem;
    private bdi head;
    private static int petals = 10;
    private static int thorns = 4;
    private static int stamens = 3;
    public float sinage;
    public float sinage2;
    public float size;
    private float pie = ((float)Math.PI * 2F);

    public ModelAechorPlant()
    {
        this(0.0F);
    }

    public ModelAechorPlant(float f)
    {
        this(f, 0.0F);
    }

    public ModelAechorPlant(float f, float f1)
    {
        this.size = 1.0F;
        this.petal = new bdi[petals];
        this.leaf = new bdi[petals];

        for (int i = 0; i < petals; i++)
        {
            this.petal[i] = new bdi(this, 0, 0);

            if (i % 2 == 0)
            {
                this.petal[i] = new bdi(this, 29, 3);
                this.petal[i].a(-4.0F, -1.0F, -12.0F, 8, 1, 9, f - 0.25F);
                this.petal[i].a(0.0F, 1.0F + f1, 0.0F);
            }
            else
            {
                this.petal[i].a(-4.0F, -1.0F, -13.0F, 8, 1, 10, f - 0.125F);
                this.petal[i].a(0.0F, 1.0F + f1, 0.0F);
            }

            this.leaf[i] = new bdi(this, 38, 13);
            this.leaf[i].a(-2.0F, -1.0F, -9.5F, 4, 1, 8, f - 0.15F);
            this.leaf[i].a(0.0F, 1.0F + f1, 0.0F);
        }

        this.stamen = new bdi[stamens];
        this.stamen2 = new bdi[stamens];

        for (int i = 0; i < stamens; i++)
        {
            this.stamen[i] = new bdi(this, 36, 13);
            this.stamen[i].a(0.0F, -9.0F, -1.5F, 1, 6, 1, f - 0.25F);
            this.stamen[i].a(0.0F, 1.0F + f1, 0.0F);
        }

        for (int i = 0; i < stamens; i++)
        {
            this.stamen2[i] = new bdi(this, 32, 15);
            this.stamen2[i].a(0.0F, -10.0F, -1.5F, 1, 1, 1, f + 0.125F);
            this.stamen2[i].a(0.0F, 1.0F + f1, 0.0F);
        }

        this.head = new bdi(this, 0, 12);
        this.head.a(-3.0F, -3.0F, -3.0F, 6, 2, 6, f + 0.75F);
        this.head.a(0.0F, 1.0F + f1, 0.0F);
        this.stem = new bdi(this, 24, 13);
        this.stem.a(-1.0F, 0.0F, -1.0F, 2, 6, 2, f);
        this.stem.a(0.0F, 1.0F + f1, 0.0F);
        this.thorn = new bdi[thorns];

        for (int i = 0; i < thorns; i++)
        {
            this.thorn[i] = new bdi(this, 32, 13);
            this.thorn[i].a(0.0F, 1.0F + f1, 0.0F);
        }

        this.thorn[0].a(-1.75F, 1.25F, -1.0F, 1, 1, 1, f - 0.25F);
        this.thorn[1].a(-1.0F, 2.25F, 0.75F, 1, 1, 1, f - 0.25F);
        this.thorn[2].a(0.75F, 1.25F, 0.0F, 1, 1, 1, f - 0.25F);
        this.thorn[3].a(0.0F, 2.25F, -1.75F, 1, 1, 1, f - 0.25F);
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 1.2F, 0.0F);
        GL11.glScalef(this.size, this.size, this.size);

        for (int i = 0; i < petals; i++)
        {
            this.petal[i].a(f5);
            this.leaf[i].a(f5);
        }

        for (int i = 0; i < stamens; i++)
        {
            this.stamen[i].a(f5);
            this.stamen2[i].a(f5);
        }

        this.head.a(f5);
        this.stem.a(f5);

        for (int i = 0; i < thorns; i++)
        {
            this.thorn[i].a(f5);
        }

        GL11.glPopMatrix();
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.head.f = 0.0F;
        this.head.g = (f4 / (180F / (float)Math.PI));
        float boff = this.sinage2;
        this.stem.g = this.head.g;
        this.stem.d = (boff * 0.5F);

        for (int i = 0; i < thorns; i++)
        {
            this.thorn[i].g = this.head.g;
            this.thorn[i].d = (boff * 0.5F);
        }

        for (int i = 0; i < petals; i++)
        {
            this.petal[i].f = (i % 2 == 0 ? -0.25F : -0.4125F);
            this.petal[i].f += this.sinage;
            this.petal[i].g = this.head.g;
            this.petal[i].g += this.pie / petals * i;
            this.leaf[i].f = (i % 2 == 0 ? 0.1F : 0.2F);
            this.leaf[i].f += this.sinage * 0.75F;
            this.leaf[i].g = (this.head.g + this.pie / petals / 2.0F);
            this.leaf[i].g += this.pie / petals * i;
            this.petal[i].d = boff;
            this.leaf[i].d = boff;
        }

        for (int i = 0; i < stamens; i++)
        {
            this.stamen[i].f = (0.2F + i / 15.0F);
            this.stamen[i].g = (this.head.g + 0.1F);
            this.stamen[i].g += this.pie / stamens * i;
            this.stamen[i].f += this.sinage * 0.4F;
            this.stamen2[i].f = (0.2F + i / 15.0F);
            this.stamen2[i].g = (this.head.g + 0.1F);
            this.stamen2[i].g += this.pie / stamens * i;
            this.stamen2[i].f += this.sinage * 0.4F;
            this.stamen[i].d = (boff + this.sinage * 2.0F);
            this.stamen2[i].d = (boff + this.sinage * 2.0F);
        }

        this.head.d = (boff + this.sinage * 2.0F);
    }
}

