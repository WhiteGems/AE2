package net.aetherteam.aether.entities.mounts_old;

import java.util.List;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.aetherteam.aether.entities.mounts.EntityFlyingCow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class RidingHandlerFlyingCow extends RidingHandler
{
    protected EntityLiving animal = (EntityLiving)this.mount;

    public RidingHandlerFlyingCow(EntityFlyingCow flyingCow)
    {
        super(flyingCow);
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

            this.animal.tasks.taskEntries.clear();
            this.rider.stepHeight = 1.0F;
        }

        super.update();
    }

    public float getSpeedModifier()
    {
        return 1.35F;
    }

    public void onUnMount()
    {
        this.rider.stepHeight = 0.5F;
        this.animal.tasks.addTask(0, new EntityAISwimming(this.animal));
        this.animal.tasks.addTask(1, new EntityAIPanic((EntityCreature)this.animal, 0.38F));
        this.animal.tasks.addTask(2, new EntityAIWander((EntityCreature)this.animal, 0.3F));
        this.animal.tasks.addTask(4, new EntityAIWatchClosest(this.animal, EntityPlayer.class, 6.0F));
        this.animal.tasks.addTask(5, new EntityAILookIdle(this.animal));
        super.onUnMount();
    }

    public boolean jump(AetherCommonPlayerHandler aetherCommonPlayerHandler, EntityPlayer player)
    {
        if (isBeingRidden())
        {
            this.rider.motionY = 1.600000023841858D;
        }

        return false;
    }

    public boolean shouldBeSitting()
    {
        return isBeingRidden();
    }

    public double getRiderYOffset()
    {
        return 0.8D;
    }
}

