package net.aetherteam.aether.entities.mounts_old;

import net.aetherteam.aether.entities.EntityCloudParachute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class RidingHandlerParachute extends RidingHandler
{
    public RidingHandlerParachute(EntityCloudParachute entityParachute)
    {
        super(entityParachute);
    }

    public void update()
    {
        if (isBeingRidden())
        {
            this.rider.fallDistance = 0.0F;

            if ((this.rider.motionY < -0.2D) && (!this.rider.isSneaking()))
            {
                this.rider.motionY = -0.2D;
            }
        }

        super.update();
    }

    public boolean shouldBeSitting()
    {
        return isBeingRidden();
    }

    public boolean animateSitting()
    {
        return false;
    }

    public boolean sprinting()
    {
        return false;
    }
}

