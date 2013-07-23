package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelCog extends ModelMinecart
{
    bdi c11;
    bdi c21;
    bdi c31;
    bdi c41;
    bdi c51;
    bdi c61;
    bdi c71;
    bdi c81;
    bdi t11;
    bdi t21;
    bdi t31;
    bdi t41;
    bdi t51;
    bdi t61;
    bdi t71;
    bdi t81;
    bdi t91;
    bdi t111;
    bdi t101;
    bdi t121;

    public ModelCog()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.c11 = new bdi(this, 44, 0);
        this.c11.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c11.a(0.0F, 0.0F, 0.0F);
        this.c11.b(64, 32);
        this.c11.i = true;
        setRotation(this.c11, 0.0F, 0.0F, 0.0F);
        this.c21 = new bdi(this, 44, 0);
        this.c21.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c21.a(0.0F, 0.0F, 0.0F);
        this.c21.b(64, 32);
        this.c21.i = true;
        setRotation(this.c21, 0.0F, 0.0F, ((float)Math.PI / 4F));
        this.c31 = new bdi(this, 44, 0);
        this.c31.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c31.a(0.0F, 0.0F, 0.0F);
        this.c31.b(64, 32);
        this.c31.i = true;
        setRotation(this.c31, 0.0F, 0.0F, ((float)Math.PI / 2F));
        this.c41 = new bdi(this, 44, 0);
        this.c41.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c41.a(0.0F, 0.0F, 0.0F);
        this.c41.b(64, 32);
        this.c41.i = true;
        setRotation(this.c41, 0.0F, 0.0F, 2.356194F);
        this.c51 = new bdi(this, 44, 0);
        this.c51.a(-3.0F, 3.0F, -2.0F, 6, 4, 4);
        this.c51.a(0.0F, 0.0F, 0.0F);
        this.c51.b(64, 32);
        this.c51.i = true;
        setRotation(this.c51, 0.0F, 0.0F, 0.0F);
        this.c61 = new bdi(this, 44, 0);
        this.c61.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c61.a(0.0F, 0.0F, 0.0F);
        this.c61.b(64, 32);
        this.c61.i = true;
        setRotation(this.c61, 0.0F, 0.0F, -2.356194F);
        this.c71 = new bdi(this, 44, 0);
        this.c71.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c71.a(0.0F, 0.0F, 0.0F);
        this.c71.b(64, 32);
        this.c71.i = true;
        setRotation(this.c71, 0.0F, 0.0F, -((float)Math.PI / 2F));
        this.c81 = new bdi(this, 44, 0);
        this.c81.a(-3.0F, -7.0F, -2.0F, 6, 4, 4);
        this.c81.a(0.0F, 0.0F, 0.0F);
        this.c81.b(64, 32);
        this.c81.i = true;
        setRotation(this.c81, 0.0F, 0.0F, -((float)Math.PI / 4F));
        this.t11 = new bdi(this, 0, 0);
        this.t11.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t11.a(0.0F, 0.0F, 0.0F);
        this.t11.b(64, 32);
        this.t11.i = true;
        setRotation(this.t11, 0.0F, 0.0F, 0.5235988F);
        this.t21 = new bdi(this, 0, 0);
        this.t21.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t21.a(0.0F, 0.0F, 0.0F);
        this.t21.b(64, 32);
        this.t21.i = true;
        setRotation(this.t21, 0.0F, 0.0F, 1.047198F);
        this.t31 = new bdi(this, 0, 0);
        this.t31.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t31.a(0.0F, 0.0F, 0.0F);
        this.t31.b(64, 32);
        this.t31.i = true;
        setRotation(this.t31, 0.0F, 0.0F, ((float)Math.PI / 2F));
        this.t41 = new bdi(this, 0, 0);
        this.t41.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t41.a(0.0F, 0.0F, 0.0F);
        this.t41.b(64, 32);
        this.t41.i = true;
        setRotation(this.t41, 0.0F, 0.0F, 2.094395F);
        this.t51 = new bdi(this, 0, 0);
        this.t51.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t51.a(0.0F, 0.0F, 0.0F);
        this.t51.b(64, 32);
        this.t51.i = true;
        setRotation(this.t51, 0.0F, 0.0F, 2.617994F);
        this.t61 = new bdi(this, 0, 0);
        this.t61.a(-1.0F, 7.0F, -2.0F, 2, 2, 4);
        this.t61.a(0.0F, 0.0F, 0.0F);
        this.t61.b(64, 32);
        this.t61.i = true;
        setRotation(this.t61, 0.0F, 0.0F, 0.0F);
        this.t71 = new bdi(this, 0, 0);
        this.t71.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t71.a(0.0F, 0.0F, 0.0F);
        this.t71.b(64, 32);
        this.t71.i = true;
        setRotation(this.t71, 0.0F, 0.0F, -2.617994F);
        this.t81 = new bdi(this, 0, 0);
        this.t81.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t81.a(0.0F, 0.0F, 0.0F);
        this.t81.b(64, 32);
        this.t81.i = true;
        setRotation(this.t81, 0.0F, 0.0F, -2.094395F);
        this.t91 = new bdi(this, 0, 0);
        this.t91.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t91.a(0.0F, 0.0F, 0.0F);
        this.t91.b(64, 32);
        this.t91.i = true;
        setRotation(this.t91, 0.0F, 0.0F, -((float)Math.PI / 2F));
        this.t111 = new bdi(this, 0, 0);
        this.t111.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t111.a(0.0F, 0.0F, 0.0F);
        this.t111.b(64, 32);
        this.t111.i = true;
        setRotation(this.t111, 0.0F, 0.0F, -0.5235988F);
        this.t101 = new bdi(this, 0, 0);
        this.t101.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t101.a(0.0F, 0.0F, 0.0F);
        this.t101.b(64, 32);
        this.t101.i = true;
        setRotation(this.t101, 0.0F, 0.0F, -1.047198F);
        this.t121 = new bdi(this, 0, 0);
        this.t121.a(-1.0F, -9.0F, -2.0F, 2, 2, 4);
        this.t121.a(0.0F, 0.0F, 0.0F);
        this.t121.b(64, 32);
        this.t121.i = true;
        setRotation(this.t121, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.c11.a(f5);
        this.c21.a(f5);
        this.c31.a(f5);
        this.c41.a(f5);
        this.c51.a(f5);
        this.c61.a(f5);
        this.c71.a(f5);
        this.c81.a(f5);
        this.t11.a(f5);
        this.t21.a(f5);
        this.t31.a(f5);
        this.t41.a(f5);
        this.t51.a(f5);
        this.t61.a(f5);
        this.t71.a(f5);
        this.t81.a(f5);
        this.t91.a(f5);
        this.t111.a(f5);
        this.t101.a(f5);
        this.t121.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }
}

