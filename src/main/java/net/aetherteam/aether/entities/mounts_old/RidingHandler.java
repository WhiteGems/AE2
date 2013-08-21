package net.aetherteam.aether.entities.mounts_old;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class RidingHandler
{
    protected Entity mount;
    protected EntityLivingBase rider;

    public RidingHandler(Entity entity)
    {
        this.mount = entity;
    }

    public void setRider(EntityPlayer player)
    {
        if (!(this.rider instanceof EntityPlayer) && Aether.getPlayerBase(player).riddenBy == null)
        {
            Aether.getPlayerBase(player).rideEntity(this.mount, this);
            this.rider = player;
            this.onMount();
        }
    }

    public boolean isBeingRidden()
    {
        return this.rider != null;
    }

    public boolean jump(AetherCommonPlayerHandler aetherCommonPlayerHandler, EntityPlayer player)
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
            Aether.getPlayerBase((EntityPlayer)this.rider).rideEntity((Entity)null, this);
        }

        this.rider = null;
    }

    public EntityLivingBase getRider()
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
