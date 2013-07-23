package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelSentryGolem extends ModelGhast
{
    bdi LowerBody = new bdi(this);
    bdi LeftArmHand;
    bdi RightArmHand;
    bdi SentryHead;
    bdi SentryBody;
    public boolean isDefault = false;
    public byte armState = 2;

    float armAngle = 0.0F;

    float[] armsAngles = { 1.0F, 1.0F, 0.5F, 0.5F };

    public ModelSentryGolem()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.c = new bdi(this, 0, 0);
        this.c.a(-5.5F, -10.0F, -4.5F, 11, 8, 9);
        this.c.a(0.0F, -2.0F, 0.0F);
        this.c.b(128, 64);
        this.c.i = true;
        setRotation(this.c, 0.0F, 0.0F, 0.0F);
        this.i = new bdi(this, 42, 47);
        this.i.a(-3.3F, 0.0F, -3.0F, 5, 11, 6);
        this.i.a(4.0F, 13.0F, 0.0F);
        this.i.b(128, 64);
        this.i.i = true;
        setRotation(this.i, 0.0F, 0.0F, 0.0F);
        this.g = new bdi(this, 40, 3);
        this.g.a(3.0F, -1.0F, -3.0F, 4, 8, 6);
        this.g.a(8.0F, 0.0F, 0.0F);
        this.g.b(128, 64);
        this.g.i = true;
        setRotation(this.g, 0.0F, 0.0F, 0.0F);
        this.LowerBody = new bdi(this, 0, 50);
        this.LowerBody.a(-6.0F, 6.0F, -4.5F, 12, 5, 9);
        this.LowerBody.a(0.0F, 2.0F, 0.0F);
        this.LowerBody.b(128, 64);
        this.LowerBody.i = true;
        setRotation(this.LowerBody, 0.0F, 0.0F, 0.0F);
        this.h.i = true;
        this.h = new bdi(this, 42, 47);
        this.h.a(-1.633333F, 0.0F, -3.0F, 5, 11, 6);
        this.h.a(-4.0F, 13.0F, 0.0F);
        this.h.b(128, 64);
        this.h.i = true;
        setRotation(this.h, 0.0F, 0.0F, 0.0F);
        this.h.i = false;
        this.e = new bdi(this, 0, 17);
        this.e.a(-8.0F, -4.0F, -5.5F, 16, 10, 11);
        this.e.a(0.0F, 2.0F, 0.0F);
        this.e.b(128, 64);
        this.e.i = true;
        setRotation(this.e, 0.0F, 0.0F, 0.0F);
        this.RightArmHand = new bdi(this, 54, 17);
        this.RightArmHand.a(-5.0F, 6.0F, -3.5F, 5, 6, 7);
        this.RightArmHand.a(-8.0F, 0.0F, 0.0F);
        this.RightArmHand.b(128, 64);
        this.RightArmHand.i = true;
        setRotation(this.RightArmHand, 0.0F, 0.0F, 0.0F);
        this.RightArmHand.i = false;
        this.f.i = true;
        this.f = new bdi(this, 40, 3);
        this.f.a(-7.0F, -1.0F, -3.0F, 4, 8, 6);
        this.f.a(-8.0F, 0.0F, 0.0F);
        this.f.b(128, 64);
        this.f.i = true;
        setRotation(this.f, 0.0F, 0.0F, 0.0F);
        this.f.i = false;
        this.LeftArmHand = new bdi(this, 54, 17);
        this.LeftArmHand.a(0.0F, 6.0F, -3.5F, 5, 6, 7);
        this.LeftArmHand.a(8.0F, 0.0F, 0.0F);
        this.LeftArmHand.b(128, 64);
        this.LeftArmHand.i = true;
        setRotation(this.LeftArmHand, 0.0F, 0.0F, 0.0F);
        this.SentryBody = new bdi(this, 64, 48);
        this.SentryBody.a(-5.5F, -8.0F, -4.5F, 8, 8, 8);
        this.SentryBody.a(1.5F, 4.0F, -12.0F);
        this.SentryBody.b(128, 64);
        this.SentryBody.i = true;
        setRotation(this.SentryBody, 0.0F, 0.0F, 0.0F);
        this.SentryHead = new bdi(this, 64, 48);
        this.SentryHead.a(-5.5F, -8.0F, -4.5F, 8, 8, 8);
        this.SentryHead.a(1.5F, -11.0F, 0.0F);
        this.SentryHead.b(128, 64);
        this.SentryHead.i = true;
        setRotation(this.SentryHead, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.e.a(f5);
        this.i.a(f5);
        this.g.a(f5);
        this.LowerBody.a(f5);
        this.h.a(f5);
        this.e.a(f5);
        this.RightArmHand.a(f5);
        this.f.a(f5);
        this.LeftArmHand.a(f5);
        this.SentryHead.a(f5);
        this.SentryBody.a(f5);
        this.SentryHead.k = true;
        this.SentryBody.k = true;
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

        if ((entity instanceof EntitySentryGolem))
        {
            EntitySentryGolem entityS = (EntitySentryGolem)entity;
            this.armState = entityS.getHandState();

            if (entityS.progress < this.armsAngles[this.armState])
            {
                entityS.progress += 0.02F;
            }

            if (entityS.progress > this.armsAngles[this.armState])
            {
                entityS.progress -= 0.02F;
            }

            this.f.f = (-3.0F * entityS.progress);
            this.g.f = (-3.0F * entityS.progress);
            this.f.g -= 0.3F * entityS.progress;
            this.g.g += 0.3F * entityS.progress;
            this.f.h += 0.3F * entityS.progress;
            this.g.h -= 0.3F * entityS.progress;
        }

        this.RightArmHand.f = this.f.f;
        this.RightArmHand.g = this.f.g;
        this.RightArmHand.h = this.f.h;
        this.LeftArmHand.f = this.g.f;
        this.LeftArmHand.g = this.g.g;
        this.LeftArmHand.h = this.g.h;
        this.LowerBody.f = this.e.f;
        this.LowerBody.g = this.e.g;
        this.LowerBody.h = this.e.h;
    }
}

