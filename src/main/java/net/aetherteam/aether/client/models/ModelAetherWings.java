package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelAetherWings extends ModelGhast
{
    public bdi wingLeft;
    public bdi wingRight;
    public float sinage;
    public boolean gonRound;

    public ModelAetherWings()
    {
        this(0.0F);
    }

    public ModelAetherWings(float f)
    {
        this(f, 0.0F);
    }

    public ModelAetherWings(float f, float f1)
    {
        this.l = 0;
        this.m = 0;
        this.n = false;
        this.wingLeft = new bdi(this, 24, 31);
        this.wingLeft.a(0.0F, -7.0F, 1.0F, 19, 8, 0, f);
        this.wingLeft.a(0.5F, 5.0F + f1, 2.625F);
        this.wingRight = new bdi(this, 24, 31);
        this.wingRight.i = true;
        this.wingRight.a(-19.0F, -7.0F, 1.0F, 19, 8, 0, f);
        this.wingRight.a(-0.5F, 5.0F + f1, 2.625F);
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, e);
        this.wingLeft.b(f5);
        this.wingRight.b(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        this.wingLeft.g = -0.2F;
        this.wingRight.g = 0.2F;
        this.wingLeft.h = -0.125F;
        this.wingRight.h = 0.125F;
        bdi tmp40_37 = this.wingLeft;
        tmp40_37.g = ((float)(tmp40_37.g + Math.sin(this.sinage) / 6.0D));
        bdi tmp66_63 = this.wingRight;
        tmp66_63.g = ((float)(tmp66_63.g - Math.sin(this.sinage) / 6.0D));
        bdi tmp92_89 = this.wingLeft;
        tmp92_89.h = ((float)(tmp92_89.h + Math.cos(this.sinage) / (this.gonRound ? 8.0F : 3.0F)));
        bdi tmp130_127 = this.wingRight;
        tmp130_127.h = ((float)(tmp130_127.h - Math.cos(this.sinage) / (this.gonRound ? 8.0F : 3.0F)));
    }
}

