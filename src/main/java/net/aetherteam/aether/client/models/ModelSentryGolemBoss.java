package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.src.bdi;

public class ModelSentryGolemBoss extends ModelMinecart
{
    bdi body;
    bdi head;
    bdi bottom;
    bdi rightforearm;
    bdi leftarm;
    bdi rightarm;
    bdi leftforearm;

    public ModelSentryGolemBoss()
    {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.body = new bdi(this, 32, 4);
        this.body.a(0.0F, 0.0F, 0.0F, 18, 20, 18);
        this.body.a(-9.0F, -16.0F, -9.0F);
        this.body.b(128, 128);
        this.body.i = true;
        setRotation(this.body, 0.0F, 0.0F, 0.0F);
        this.head = new bdi(this, 42, 48);
        this.head.a(0.0F, 0.0F, 0.0F, 14, 8, 14);
        this.head.a(-7.0F, -24.0F, -7.0F);
        this.head.b(128, 128);
        this.head.i = true;
        setRotation(this.head, 0.0F, 0.0F, 0.0F);
        this.bottom = new bdi(this, 0, 42);
        this.bottom.a(0.0F, 0.0F, 0.0F, 14, 6, 14);
        this.bottom.a(-7.0F, 4.0F, -7.0F);
        this.bottom.b(128, 128);
        this.bottom.i = true;
        setRotation(this.bottom, 0.0F, 0.0F, 0.0F);
        this.rightforearm = new bdi(this, 0, 90);
        this.rightforearm.a(0.0F, 0.0F, 0.0F, 10, 18, 10);
        this.rightforearm.a(9.0F, -5.0F, -5.5F);
        this.rightforearm.b(128, 128);
        this.rightforearm.i = true;
        setRotation(this.rightforearm, -0.4363323F, 0.0F, 0.0F);
        this.leftarm = new bdi(this, 0, 0);
        this.leftarm.a(0.0F, 0.0F, 0.0F, 8, 15, 8);
        this.leftarm.a(-9.0F, -16.0F, 4.0F);
        this.leftarm.b(128, 128);
        this.leftarm.i = true;
        setRotation(this.leftarm, -((float)Math.PI * 2F / 9F), ((float)Math.PI / 2F), 0.0F);
        this.rightarm = new bdi(this, 0, 0);
        this.rightarm.a(0.0F, 0.0F, 0.0F, 8, 15, 8);
        this.rightarm.a(9.0F, -16.0F, -4.0F);
        this.rightarm.b(128, 128);
        this.rightarm.i = true;
        setRotation(this.rightarm, -((float)Math.PI * 2F / 9F), -((float)Math.PI / 2F), 0.0F);
        this.leftforearm = new bdi(this, 0, 62);
        this.leftforearm.a(0.0F, 0.0F, 0.0F, 10, 17, 10);
        this.leftforearm.a(-19.0F, -5.0F, -4.5F);
        this.leftforearm.b(128, 128);
        this.leftforearm.i = true;
        setRotation(this.leftforearm, -0.4363323F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.a(f5);
        this.head.a(f5);
        this.bottom.a(f5);
        this.rightforearm.a(f5);
        this.leftarm.a(f5);
        this.rightarm.a(f5);
        this.leftforearm.a(f5);
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
        this.leftforearm.f = (this.leftarm.f + f1);
        this.rightforearm.f = (this.rightarm.f + f1);
    }

    public void setLivingAnimations(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        EntitySentryGuardian var5 = (EntitySentryGuardian)par1EntityLiving;
        int var6 = var5.getAttackTimer();

        if (var6 != 0)
        {
            this.leftforearm.f = 5.0F;
            this.rightforearm.f = 5.0F;
        }
    }

    private float func_78172_a(float par1, float par2)
    {
        return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
    }
}

