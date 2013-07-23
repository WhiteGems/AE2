package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityGoldenFX extends EntityCloudFX
{
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    private boolean rising;

    public EntityGoldenFX(World world, double d, double d1, double d2, double d3, double d4, double d5, boolean rise)
    {
        super(world, d, d1, d2, d3, d4, d5);
        this.particleBlue = 0.0F;
        this.particleRed = 0.976F;
        this.particleGreen = 0.7450981F;
        this.portalPosX = (this.posX = d);
        this.portalPosY = (this.posY = d1);
        this.portalPosZ = (this.posZ = d2);
        this.rising = rise;
    }

    public void onUpdate()
    {
        if (this.rising)
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            float var1 = this.particleAge / this.particleMaxAge;
            float var2 = var1;
            var1 = -var1 + var1 * var1 * 2.0F;
            var1 = 1.0F - var1;
            this.posX = (this.portalPosX + this.motionX * var1);
            this.posY = (this.portalPosY - 1.5D + this.motionY * var1 + (1.0F + var2));
            this.posZ = (this.portalPosZ + this.motionZ * var1);

            if (this.particleAge++ >= this.particleMaxAge)
            {
                setDead();
            }
        }
        else
        {
            super.onUpdate();
        }
    }
}

