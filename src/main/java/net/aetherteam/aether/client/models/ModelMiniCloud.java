package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelMiniCloud extends ModelMinecart
{
    public bdi[] head;

    public ModelMiniCloud()
    {
        this(0.0F);
    }

    public ModelMiniCloud(float f)
    {
        this(f, 0.0F);
    }

    public ModelMiniCloud(float f, float f1)
    {
        this.head = new bdi[5];
        this.head[0] = new bdi(this, 0, 0);
        this.head[1] = new bdi(this, 36, 0);
        this.head[2] = new bdi(this, 36, 0);
        this.head[3] = new bdi(this, 36, 8);
        this.head[4] = new bdi(this, 36, 8);
        this.head[0].a(-4.5F, -4.5F, -4.5F, 9, 9, 9, f);
        this.head[0].a(0.0F, 0.0F + f1, 0.0F);
        this.head[1].a(-3.5F, -3.5F, -5.5F, 7, 7, 1, f);
        this.head[1].a(0.0F, 0.0F + f1, 0.0F);
        this.head[2].a(-3.5F, -3.5F, 4.5F, 7, 7, 1, f);
        this.head[2].a(0.0F, 0.0F + f1, 0.0F);
        this.head[3].a(-5.5F, -3.5F, -3.5F, 1, 7, 7, f);
        this.head[3].a(0.0F, 0.0F + f1, 0.0F);
        this.head[4].a(4.5F, -3.5F, -3.5F, 1, 7, 7, f);
        this.head[4].a(0.0F, 0.0F + f1, 0.0F);
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);

        for (int i = 0; i < 5; i++)
        {
            this.head[i].a(f5);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for (int i = 0; i < 5; i++)
        {
            this.head[i].g = (f3 / (180F / (float)Math.PI));
            this.head[i].f = (f4 / (180F / (float)Math.PI));
        }
    }
}

