package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.bosses.EntitySliderHostMimic;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;

public class ModelSliderMimicHost extends ModelMinecart
{
    bdi body3;
    bdi frontLeftLeg;
    bdi frontRightLeg;
    bdi backLeftLeg;
    bdi backRightLeg;
    bdi body;
    bdi body2;
    bdi body5;
    bdi body4;
    bdi body6;
    bdi fakeBody;

    public ModelSliderMimicHost()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.fakeBody = new bdi(this, 0, 0);
        this.fakeBody.a(0.0F, 0.0F, 0.0F, 32, 32, 32);
        this.fakeBody.a(-16.0F, -8.0F, -16.0F);
        this.fakeBody.b(128, 64);
        this.fakeBody.i = true;
        setRotation(this.fakeBody, 0.0F, 0.0F, 0.0F);
        this.body3 = new bdi(this, 21, 11);
        this.body3.a(22.0F, 2.0F, 0.0F, 11, 11, 32);
        this.body3.a(-17.0F, -10.0F, -16.0F);
        this.body3.b(128, 64);
        this.body3.i = true;
        this.frontLeftLeg = new bdi(this, 0, 0);
        this.frontLeftLeg.a(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.frontLeftLeg.a(10.0F, 11.0F, -16.0F);
        this.frontLeftLeg.b(128, 64);
        this.frontLeftLeg.i = true;
        setRotation(this.frontLeftLeg, 0.0F, 0.0F, 0.0F);
        this.frontRightLeg = new bdi(this, 0, 0);
        this.frontRightLeg.a(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.frontRightLeg.a(-16.0F, 11.0F, -16.0F);
        this.frontRightLeg.b(128, 64);
        this.frontRightLeg.i = true;
        setRotation(this.frontRightLeg, 0.0F, 0.0F, 0.0F);
        this.backLeftLeg = new bdi(this, 0, 0);
        this.backLeftLeg.a(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.backLeftLeg.a(10.0F, 11.0F, 10.0F);
        this.backLeftLeg.b(128, 64);
        this.backLeftLeg.i = true;
        setRotation(this.backLeftLeg, 0.0F, 0.0F, 0.0F);
        this.backRightLeg = new bdi(this, 0, 0);
        this.backRightLeg.a(0.0F, 0.0F, 0.0F, 6, 13, 6);
        this.backRightLeg.a(-16.0F, 11.0F, 10.0F);
        this.backRightLeg.b(128, 64);
        this.backRightLeg.i = true;
        setRotation(this.backRightLeg, 0.0F, 0.0F, 0.0F);
        this.body = new bdi(this, 0, 0);
        this.body.a(0.0F, 2.0F, 0.0F, 32, 10, 32);
        this.body.a(-16.0F, -20.0F, -16.0F);
        this.body.b(128, 64);
        this.body.i = true;
        setRotation(this.body, 0.0F, 0.0F, 0.0F);
        this.body2 = new bdi(this, 58, 25);
        this.body2.a(0.0F, 2.0F, 0.0F, 13, 11, 18);
        this.body2.a(-6.0F, -10.0F, -2.0F);
        this.body2.b(128, 64);
        this.body2.i = true;
        setRotation(this.body2, 0.0F, 0.0F, 0.0F);
        this.body5 = new bdi(this, 106, 42);
        this.body5.a(0.0F, 2.0F, 0.0F, 10, 11, 1);
        this.body5.a(-16.0F, -10.0F, 15.0F);
        this.body5.b(128, 64);
        this.body5.i = true;
        setRotation(this.body5, 0.0F, 0.0F, 0.0F);
        this.body4 = new bdi(this, 0, 22);
        this.body4.a(0.0F, 2.0F, 0.0F, 32, 10, 32);
        this.body4.a(-16.0F, 1.0F, -16.0F);
        this.body4.b(128, 64);
        this.body4.i = true;
        setRotation(this.body4, 0.0F, 0.0F, 0.0F);
        this.body6 = new bdi(this, 1, 11);
        this.body6.a(0.0F, 2.0F, 0.0F, 10, 11, 31);
        this.body6.a(-16.0F, -10.0F, -16.0F);
        this.body6.b(128, 64);
        this.body6.i = true;
        setRotation(this.body6, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        if (((entity instanceof EntitySliderHostMimic)) && (!((EntitySliderHostMimic)entity).isAwake()))
        {
            this.fakeBody.a(f5);
        }
        else
        {
            this.body3.a(f5);
            this.frontLeftLeg.a(f5);
            this.frontRightLeg.a(f5);
            this.backLeftLeg.a(f5);
            this.backRightLeg.a(f5);
            this.body.a(f5);
            this.body2.a(f5);
            this.body5.a(f5);
            this.body4.a(f5);
            this.body6.a(f5);
        }
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
        this.frontRightLeg.f = (MathHelper.cos(f * 0.6662F) * 1.4F * f1);
        this.frontLeftLeg.f = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1);
        this.frontRightLeg.g = 0.0F;
        this.frontLeftLeg.g = 0.0F;
        this.backRightLeg.f = (MathHelper.cos(f * 0.6662F) * 1.4F * f1);
        this.backLeftLeg.f = (MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1);
        this.backRightLeg.g = 0.0F;
        this.backLeftLeg.g = 0.0F;
    }
}

