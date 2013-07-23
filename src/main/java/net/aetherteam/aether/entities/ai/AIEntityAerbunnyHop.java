package net.aetherteam.aether.entities.ai;

import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityJumpHelper;

public class AIEntityAerbunnyHop extends EntityAIBase
{
    private EntityAerbunny theEntity;

    public AIEntityAerbunnyHop(EntityAerbunny entityAerbunny)
    {
        setMutexBits(8);
        this.theEntity = entityAerbunny;
    }

    public boolean shouldExecute()
    {
        return (this.theEntity.motionZ > 0.0D) || (this.theEntity.motionX > 0.0D) || (this.theEntity.onGround);
    }

    public void updateTask()
    {
        this.theEntity.getJumpHelper().setJumping();
    }
}

