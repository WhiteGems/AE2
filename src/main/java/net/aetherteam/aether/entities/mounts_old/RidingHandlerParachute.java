package net.aetherteam.aether.entities.mounts_old;

import net.aetherteam.aether.entities.EntityCloudParachute;

public class RidingHandlerParachute extends RidingHandler
{
    public RidingHandlerParachute(EntityCloudParachute var1)
    {
        super(var1);
    }

    public void update()
    {
        if (this.isBeingRidden())
        {
            this.rider.fallDistance = 0.0F;

            if (this.rider.motionY < -0.2D && !this.rider.isSneaking())
            {
                this.rider.motionY = -0.2D;
            }
        }

        super.update();
    }

    public boolean shouldBeSitting()
    {
        return this.isBeingRidden();
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
