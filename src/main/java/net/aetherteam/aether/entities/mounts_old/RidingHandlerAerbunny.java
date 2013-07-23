package net.aetherteam.aether.entities.mounts_old;

import net.aetherteam.aether.entities.ai.AIEntityAerbunnyHop;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class RidingHandlerAerbunny extends RidingHandler
{
    protected EntityLiving animal;

    public RidingHandlerAerbunny(EntityAerbunny var1)
    {
        super(var1);
        this.animal = (EntityLiving)this.mount;
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

            this.animal.tasks.taskEntries.clear();
        }

        super.update();
    }

    public void onUnMount()
    {
        this.animal.tasks.addTask(1, new EntityAISwimming(this.animal));
        this.animal.tasks.addTask(2, new EntityAIAvoidEntity((EntityAerbunny)this.animal, EntityPlayer.class, 16.0F, 2.6F, 2.8F));
        this.animal.tasks.addTask(3, new EntityAIWander((EntityAerbunny)this.animal, 2.5F));
        this.animal.tasks.addTask(4, new EntityAIWatchClosest(this.animal, EntityPlayer.class, 10.0F));
        this.animal.tasks.addTask(5, ((EntityAerbunny)this.animal).aiEatGrass);
        this.animal.tasks.addTask(6, new AIEntityAerbunnyHop((EntityAerbunny)this.animal));
        super.onUnMount();
    }

    public boolean shouldBeSitting()
    {
        return this.isBeingRidden();
    }

    public double getAnimalYOffset()
    {
        return 1.65D;
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
