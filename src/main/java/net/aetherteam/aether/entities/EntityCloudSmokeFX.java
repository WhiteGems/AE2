package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCloudSmokeFX extends EntityNoteFX
{
    float smokeParticleScale;

    public EntityCloudSmokeFX(World world, double x, double y, double z, double initialMotionX, double initialMotionY, double intialMotionZ, float size, float red, float blue, float green)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.1000000014901161D;
        this.motionY *= 0.1000000014901161D;
        this.motionZ *= 0.1000000014901161D;
        this.motionX += initialMotionX;
        this.motionY += initialMotionY;
        this.motionZ += intialMotionZ;
        this.particleRed = red;
        this.particleBlue = blue;
        this.particleGreen = green;
        this.particleScale *= 0.75F;
        this.particleScale *= size;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.2D)));
        this.particleMaxAge = ((int)(this.particleMaxAge * size));
        this.noClip = true;
    }

    public void a(Rect2i tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (this.particleAge + f) / this.particleMaxAge * 32.0F;

        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        this.particleScale = (this.smokeParticleScale * f6);
        super.a(tessellator, f, f1, f2, f3, f4, f5);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            setDead();
        }

        setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.004D;
        moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}

