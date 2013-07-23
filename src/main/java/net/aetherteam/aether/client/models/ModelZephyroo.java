package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntityZephyroo;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelZephyroo extends ModelMinecart
{
    bdi LeftHand;
    bdi LeftArm;
    bdi LeftFoot;
    bdi LeftLeg;
    bdi LeftHip;
    bdi LeftShoulder;
    bdi TailBottom;
    bdi Pouch;
    bdi Snout;
    bdi RightHip;
    bdi RightLeg;
    bdi RightFoot;
    bdi RightShoulder;
    bdi RightArm;
    bdi RightHand;
    bdi TailTop;
    bdi EarLeft;
    bdi Neck;
    bdi EarRight;
    bdi Head;
    bdi Body;

    public ModelZephyroo()
    {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.LeftHand = new bdi(this, 50, 50);
        this.LeftHand.a(0.0F, 0.0F, 0.0F, 2, 1, 4);
        this.LeftHand.a(5.0F, 10.5F, -10.5F);
        this.LeftHand.b(128, 64);
        this.LeftHand.i = true;
        setRotation(this.LeftHand, -0.3665191F, 0.0F, 0.0F);
        this.LeftArm = new bdi(this, 40, 38);
        this.LeftArm.a(0.0F, 0.0F, 0.0F, 2, 9, 2);
        this.LeftArm.a(5.0F, 3.0F, -7.0F);
        this.LeftArm.b(128, 64);
        this.LeftArm.i = true;
        setRotation(this.LeftArm, -0.4363323F, 0.0F, 0.0F);
        this.LeftFoot = new bdi(this, 29, 19);
        this.LeftFoot.a(0.0F, 8.0F, -7.0F, 3, 2, 6);
        this.LeftFoot.a(3.0F, 14.0F, 1.0F);
        this.LeftFoot.b(128, 64);
        this.LeftFoot.i = true;
        setRotation(this.LeftFoot, 0.0F, 0.0F, 0.0F);
        this.LeftLeg = new bdi(this, 0, 0);
        this.LeftLeg.a(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.LeftLeg.a(3.0F, 14.0F, 1.0F);
        this.LeftLeg.b(128, 64);
        this.LeftLeg.i = true;
        setRotation(this.LeftLeg, -0.4363323F, 0.0F, 0.0F);
        this.LeftHip = new bdi(this, 0, 41);
        this.LeftHip.a(0.0F, 0.0F, 0.0F, 2, 7, 7);
        this.LeftHip.a(4.0F, 8.0F, 0.5F);
        this.LeftHip.b(128, 64);
        this.LeftHip.i = true;
        setRotation(this.LeftHip, 0.034907F, 0.0F, 0.0F);
        this.LeftShoulder = new bdi(this, 40, 49);
        this.LeftShoulder.a(0.0F, 0.0F, 0.0F, 2, 3, 3);
        this.LeftShoulder.a(5.0F, 2.0F, -7.5F);
        this.LeftShoulder.b(128, 64);
        this.LeftShoulder.i = true;
        setRotation(this.LeftShoulder, 0.034907F, 0.0F, 0.0F);
        this.TailBottom = new bdi(this, 44, 25);
        this.TailBottom.a(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.TailBottom.a(-1.5F, 21.0F, 15.0F);
        this.TailBottom.b(128, 64);
        this.TailBottom.i = true;
        setRotation(this.TailBottom, 1.32645F, 0.0F, 0.0F);
        this.Pouch = new bdi(this, 13, 0);
        this.Pouch.a(0.0F, 0.0F, 0.0F, 9, 2, 8);
        this.Pouch.a(-4.5F, 10.0F, -4.0F);
        this.Pouch.b(128, 64);
        this.Pouch.i = true;
        setRotation(this.Pouch, -((float)Math.PI / 4F), 0.0F, 0.0F);
        this.Snout = new bdi(this, 0, 22);
        this.Snout.a(0.0F, 0.0F, 0.0F, 4, 4, 9);
        this.Snout.a(-2.0F, -2.0F, -14.5F);
        this.Snout.b(128, 64);
        this.Snout.i = true;
        setRotation(this.Snout, 0.349066F, 0.0F, 0.0F);
        this.RightHip = new bdi(this, 0, 41);
        this.RightHip.a(0.0F, 0.0F, 0.0F, 2, 7, 7);
        this.RightHip.a(-6.0F, 8.0F, 0.5F);
        this.RightHip.b(128, 64);
        this.RightHip.i = true;
        setRotation(this.RightHip, 0.034907F, 0.0F, 0.0F);
        this.RightLeg = new bdi(this, 0, 0);
        this.RightLeg.a(0.0F, 0.0F, 0.0F, 3, 9, 3);
        this.RightLeg.a(-6.0F, 14.0F, 1.0F);
        this.RightLeg.b(128, 64);
        this.RightLeg.i = true;
        setRotation(this.RightLeg, -0.4363323F, 0.0F, 0.0F);
        this.RightFoot = new bdi(this, 29, 19);
        this.RightFoot.a(0.0F, 8.0F, -7.0F, 3, 2, 6);
        this.RightFoot.a(-6.0F, 14.0F, 1.0F);
        this.RightFoot.b(128, 64);
        this.RightFoot.i = true;
        setRotation(this.RightFoot, 0.0F, 0.0F, 0.0F);
        this.RightShoulder = new bdi(this, 40, 49);
        this.RightShoulder.a(0.0F, 0.0F, 0.0F, 2, 3, 3);
        this.RightShoulder.a(-7.0F, 2.0F, -7.5F);
        this.RightShoulder.b(128, 64);
        this.RightShoulder.i = true;
        setRotation(this.RightShoulder, 0.034907F, 0.0F, 0.0F);
        this.RightArm = new bdi(this, 40, 38);
        this.RightArm.a(0.0F, 0.0F, 0.0F, 2, 9, 2);
        this.RightArm.a(-7.0F, 3.0F, -7.0F);
        this.RightArm.b(128, 64);
        this.RightArm.i = true;
        setRotation(this.RightArm, -0.4363323F, 0.0F, 0.0F);
        this.RightHand = new bdi(this, 50, 50);
        this.RightHand.a(0.0F, 0.0F, 0.0F, 2, 1, 4);
        this.RightHand.a(-7.0F, 10.5F, -10.5F);
        this.RightHand.b(128, 64);
        this.RightHand.i = true;
        setRotation(this.RightHand, -0.3665191F, 0.0F, 0.0F);
        this.TailTop = new bdi(this, 48, 37);
        this.TailTop.a(0.0F, 0.0F, 0.0F, 3, 10, 3);
        this.TailTop.a(-1.5F, 14.0F, 8.0F);
        this.TailTop.b(128, 64);
        this.TailTop.i = true;
        setRotation(this.TailTop, 0.802852F, 0.0F, 0.0F);
        this.EarLeft = new bdi(this, 0, 41);
        this.EarLeft.a(0.0F, 0.0F, 0.0F, 1, 5, 2);
        this.EarLeft.a(2.0F, -10.5F, -10.0F);
        this.EarLeft.b(128, 64);
        this.EarLeft.i = true;
        setRotation(this.EarLeft, 0.0F, 0.0F, 0.244346F);
        this.Neck = new bdi(this, 0, 14);
        this.Neck.a(0.0F, 0.0F, 0.0F, 4, 3, 5);
        this.Neck.a(-2.0F, 0.0F, -9.5F);
        this.Neck.b(128, 64);
        this.Neck.i = true;
        setRotation(this.Neck, 0.6108652F, 0.0F, 0.0F);
        this.EarRight = new bdi(this, 0, 41);
        this.EarRight.a(0.0F, 0.0F, 0.0F, 1, 5, 2);
        this.EarRight.a(-3.0F, -10.0F, -10.0F);
        this.EarRight.b(128, 64);
        this.EarRight.i = true;
        setRotation(this.EarRight, 0.0F, 0.0F, -0.261799F);
        this.Head = new bdi(this, 26, 27);
        this.Head.a(0.0F, 0.0F, 0.0F, 4, 3, 5);
        this.Head.a(-2.0F, -6.0F, -11.0F);
        this.Head.b(128, 64);
        this.Head.i = true;
        setRotation(this.Head, 0.0F, 0.0F, 0.0F);
        this.Body = new bdi(this, 0, 35);
        this.Body.a(0.0F, 0.0F, 0.0F, 10, 9, 20);
        this.Body.a(-5.0F, -2.0F, -4.0F);
        this.Body.b(128, 64);
        this.Body.i = true;
        setRotation(this.Body, -((float)Math.PI / 4F), 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.LeftHand.a(f5);
        this.LeftArm.a(f5);
        this.LeftFoot.a(f5);
        this.LeftLeg.a(f5);
        this.LeftHip.a(f5);
        this.LeftShoulder.a(f5);
        this.TailBottom.a(f5);
        this.Pouch.a(f5);
        this.Snout.a(f5);
        this.RightHip.a(f5);
        this.RightLeg.a(f5);
        this.RightFoot.a(f5);
        this.RightShoulder.a(f5);
        this.RightArm.a(f5);
        this.RightHand.a(f5);
        this.TailTop.a(f5);
        this.EarLeft.a(f5);
        this.Neck.a(f5);
        this.EarRight.a(f5);
        this.Head.a(f5);
        this.Body.a(f5);
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

        if ((entity.motionX != 0.0D) && (entity.motionZ != 0.0D))
        {
            this.RightLeg.f = ((float)Math.cos(((EntityZephyroo)entity).getTimeTilJump() * 0.5F));
            this.LeftLeg.f = ((float)Math.cos(((EntityZephyroo)entity).getTimeTilJump() * 0.5F));
            this.RightFoot.f = (this.RightLeg.f + 0.25F);
            this.LeftFoot.f = (this.RightLeg.f + 0.25F);
        }
        else
        {
            this.RightLeg.f = 0.0F;
            this.LeftLeg.f = 0.0F;
            this.RightFoot.f = (this.RightLeg.f + 0.25F);
            this.LeftFoot.f = (this.RightLeg.f + 0.25F);
        }
    }
}

