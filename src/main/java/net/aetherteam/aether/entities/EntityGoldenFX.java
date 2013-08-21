package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityGoldenFX extends EntityPortalFX
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
        this.particleGreen = 0.74509805F;
        this.portalPosX = this.posX = d;
        this.portalPosY = this.posY = d1;
        this.portalPosZ = this.posZ = d2;
        this.rising = rise;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.rising)
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            float var1 = (float)this.particleAge / (float)this.particleMaxAge;
            float var2 = var1;
            var1 = -var1 + var1 * var1 * 2.0F;
            var1 = 1.0F - var1;
            this.posX = this.portalPosX + this.motionX * (double)var1;
            this.posY = this.portalPosY - 1.5D + this.motionY * (double)var1 + (double)(1.0F + var2);
            this.posZ = this.portalPosZ + this.motionZ * (double)var1;

            if (this.particleAge++ >= this.particleMaxAge)
            {
                this.setDead();
            }
        }
        else
        {
            super.onUpdate();
        }
    }
}
