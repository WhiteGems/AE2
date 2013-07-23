package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.src.bdi;

public class ModelMimic extends ModelMinecart
{
    bdi box;
    bdi boxLid;
    bdi leftLeg;
    bdi rightLeg;

    public ModelMimic()
    {
        this.box = new bdi(this, 0, 0);
        this.box.a(-8.0F, 0.0F, -8.0F, 16, 10, 16);
        this.box.a(0.0F, 0.0F, 0.0F);
        this.boxLid = new bdi(this, 16, 10);
        this.boxLid.a(0.0F, 0.0F, 0.0F, 16, 6, 16);
        this.boxLid.a(-8.0F, 0.0F, 8.0F);
        this.leftLeg = new bdi(this, 0, 0);
        this.leftLeg.a(-3.0F, 0.0F, -3.0F, 6, 15, 6);
        this.leftLeg.a(-4.0F, 9.0F, 0.0F);
        this.rightLeg = new bdi(this, 0, 0);
        this.rightLeg.a(-3.0F, 0.0F, -3.0F, 6, 15, 6);
        this.rightLeg.a(4.0F, 9.0F, 0.0F);
    }

    public void render1(float f, float f1, float f2, float f3, float f4, float f5, EntityMimic mimic)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, mimic);
        this.boxLid.f = ((float)Math.PI - mimic.mouth);
        this.rightLeg.f = mimic.legs;
        this.leftLeg.f = (-mimic.legs);
        this.box.a(f5);
    }

    public void render2(float f, float f1, float f2, float f3, float f4, float f5, EntityMimic mimic)
    {
        this.boxLid.a(f5);
        this.leftLeg.a(f5);
        this.rightLeg.a(f5);
    }
}

