package net.aetherteam.aether.entities.ai;

import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.minecraft.entity.ai.EntityAIBase;

public class AIEntityAerbunnyHop extends EntityAIBase
{
    private EntityAerbunny theEntity;

    public AIEntityAerbunnyHop(EntityAerbunny var1)
    {
        this.setMutexBits(8);
        this.theEntity = var1;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theEntity.motionZ > 0.0D || this.theEntity.motionX > 0.0D || this.theEntity.onGround;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theEntity.getJumpHelper().setJumping();
    }
}
