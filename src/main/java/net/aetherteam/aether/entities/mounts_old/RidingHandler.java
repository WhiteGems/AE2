package net.aetherteam.aether.entities.mounts_old;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class RidingHandler
{
    protected Entity mount;
    protected EntityLiving rider;

    public RidingHandler(Entity var1)
    {
        this.mount = var1;
    }

    public void setRider(EntityPlayer var1)
    {
        if (!(this.rider instanceof EntityPlayer) && Aether.getPlayerBase(var1).riddenBy == null)
        {
            Aether.getPlayerBase(var1).rideEntity(this.mount, this);
            this.rider = var1;
            this.onMount();
        }
    }

    public boolean isBeingRidden()
    {
        return this.rider != null;
    }

    public boolean jump(AetherCommonPlayerHandler var1, EntityPlayer var2)
    {
        return true;
    }

    public void update()
    {
        if (this.isBeingRidden())
        {
            this.mount.setPositionAndRotation(this.rider.posX, this.rider.posY, this.rider.posZ, this.rider.rotationYaw, this.rider.rotationPitch);
            this.mount.motionY = this.rider.posY - this.mount.posY - 1.63D;
            this.mount.motionX = this.rider.posX - this.mount.posX;
            this.mount.motionZ = this.rider.posZ - this.mount.posZ;
            this.rider.shouldRiderSit();
        }
    }

    public float getSpeedModifier()
    {
        return -1.0F;
    }

    public void onMount() {}

    public void onUnMount()
    {
        if (this.rider instanceof EntityPlayer)
        {
            Aether.getPlayerBase((EntityPlayer) this.rider).rideEntity((Entity) null, this);
        }

        this.rider = null;
    }

    public EntityLiving getRider()
    {
        return this.rider;
    }

    public Entity getMount()
    {
        return this.mount;
    }

    public boolean shouldBeSitting()
    {
        return false;
    }

    public boolean animateSitting()
    {
        return true;
    }

    public boolean sprinting()
    {
        return true;
    }

    public double getRiderYOffset()
    {
        return 0.0D;
    }

    public double getAnimalYOffset()
    {
        return 0.0D;
    }
}
