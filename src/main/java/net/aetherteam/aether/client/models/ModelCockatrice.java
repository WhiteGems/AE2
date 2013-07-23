package net.aetherteam.aether.client.models;

import java.util.Random;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;

public class ModelCockatrice extends ModelMinecart
{
    public bdi head;
    public bdi body;
    public bdi legs;
    public bdi legs2;
    public bdi wings;
    public bdi wings2;
    public bdi jaw;
    public bdi neck;
    public bdi feather1;
    public bdi feather2;
    public bdi feather3;
    public Random random;

    public ModelCockatrice()
    {
        byte byte0 = 16;
        this.random = new Random();
        this.head = new bdi(this, 0, 13);
        this.head.a(-2.0F, -4.0F, -6.0F, 4, 4, 8, 0.0F);
        this.head.a(0.0F, -8 + byte0, -4.0F);
        this.jaw = new bdi(this, 24, 13);
        this.jaw.a(-2.0F, -1.0F, -6.0F, 4, 1, 8, -0.1F);
        this.jaw.a(0.0F, -8 + byte0, -4.0F);
        this.body = new bdi(this, 0, 0);
        this.body.a(-3.0F, -3.0F, 0.0F, 6, 8, 5, 0.0F);
        this.body.a(0.0F, 0 + byte0, 0.0F);
        this.legs = new bdi(this, 22, 0);
        this.legs.a(-1.0F, -1.0F, -1.0F, 2, 9, 2);
        this.legs.a(-2.0F, 0 + byte0, 1.0F);
        this.legs2 = new bdi(this, 22, 0);
        this.legs2.a(-1.0F, -1.0F, -1.0F, 2, 9, 2);
        this.legs2.a(2.0F, 0 + byte0, 1.0F);
        this.wings = new bdi(this, 52, 0);
        this.wings.a(-1.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings.a(-3.0F, -4 + byte0, 0.0F);
        this.wings2 = new bdi(this, 52, 0);
        this.wings2.a(0.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings2.a(3.0F, -4 + byte0, 0.0F);
        this.neck = new bdi(this, 44, 0);
        this.neck.a(-1.0F, -6.0F, -1.0F, 2, 6, 2);
        this.neck.a(0.0F, -2 + byte0, -4.0F);
        this.feather1 = new bdi(this, 30, 0);
        this.feather1.a(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather1.a(0.0F, 1 + byte0, 1.0F);
        this.feather2 = new bdi(this, 30, 0);
        this.feather2.a(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather2.a(0.0F, 1 + byte0, 1.0F);
        this.feather3 = new bdi(this, 30, 0);
        this.feather3.a(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather3.a(0.0F, 1 + byte0, 1.0F);
        this.feather1.d += 0.5F;
        this.feather2.d += 0.5F;
        this.feather3.d += 0.5F;
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        this.head.a(f5);
        this.jaw.a(f5);
        this.body.a(f5);
        this.legs.a(f5);
        this.legs2.a(f5);
        this.wings.a(f5);
        this.wings2.a(f5);
        this.neck.a(f5);
        this.feather1.a(f5);
        this.feather2.a(f5);
        this.feather3.a(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (float)Math.PI;
        this.head.f = (f4 / (180F / (float)Math.PI));
        this.head.g = (f3 / (180F / (float)Math.PI));
        this.jaw.f = this.head.f;
        this.jaw.g = this.head.g;
        this.body.f = ((float)Math.PI / 2F);
        this.legs.f = (MathHelper.cos(f * 0.6662F) * 1.4F * f1);
        this.legs2.f = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1);

        if (f2 > 0.001F)
        {
            this.wings.e = -1.0F;
            this.wings2.e = -1.0F;
            this.wings.d = 12.0F;
            this.wings2.d = 12.0F;
            this.wings.f = 0.0F;
            this.wings2.f = 0.0F;
            this.wings.h = f2;
            this.wings2.h = (-f2);
            this.legs.f = 0.6F;
            this.legs2.f = 0.6F;
        }
        else
        {
            this.wings.e = -3.0F;
            this.wings2.e = -3.0F;
            this.wings.d = 14.0F;
            this.wings2.d = 14.0F;
            this.wings.f = (f6 / 2.0F);
            this.wings2.f = (f6 / 2.0F);
            this.wings.h = 0.0F;
            this.wings2.h = 0.0F;
        }

        this.feather1.g = -0.375F;
        this.feather2.g = 0.0F;
        this.feather3.g = 0.375F;
        this.feather1.f = 0.25F;
        this.feather2.f = 0.25F;
        this.feather3.f = 0.25F;
        this.neck.f = 0.0F;
        this.neck.g = this.head.g;
        this.jaw.f += 0.35F;
    }
}

