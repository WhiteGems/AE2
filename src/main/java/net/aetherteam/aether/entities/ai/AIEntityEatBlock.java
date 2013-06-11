package net.aetherteam.aether.entities.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class AIEntityEatBlock extends EntityAIBase
{
    private EntityLiving theEntity;
    private World theWorld;
    int eatGrassTick = 0;
    Block blocktoEat;
    Block blocktoReplace;

    public AIEntityEatBlock(EntityLiving var1, Block var2, Block var3)
    {
        this.blocktoEat = var2;
        this.blocktoReplace = var3;
        this.theEntity = var1;
        this.theWorld = var1.worldObj;
        this.setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theEntity.getRNG().nextInt(this.theEntity.isChild() ? 50 : 1000) != 0)
        {
            return false;
        } else
        {
            int var1 = MathHelper.floor_double(this.theEntity.posX);
            int var2 = MathHelper.floor_double(this.theEntity.posY);
            int var3 = MathHelper.floor_double(this.theEntity.posZ);
            return this.theWorld.getBlockId(var1, var2, var3) == Block.tallGrass.blockID && this.theWorld.getBlockMetadata(var1, var2, var3) == 1 ? true : this.theWorld.getBlockId(var1, var2 - 1, var3) == this.blocktoEat.blockID;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.eatGrassTick = 40;
        this.theWorld.setEntityState(this.theEntity, (byte) 10);
        this.theEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.eatGrassTick = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.eatGrassTick > 0;
    }

    public int getEatGrassTick()
    {
        return this.eatGrassTick;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.eatGrassTick = Math.max(0, this.eatGrassTick - 1);

        if (this.eatGrassTick == 4)
        {
            int var1 = MathHelper.floor_double(this.theEntity.posX);
            int var2 = MathHelper.floor_double(this.theEntity.posY);
            int var3 = MathHelper.floor_double(this.theEntity.posZ);

            if (this.theWorld.getBlockId(var1, var2, var3) == Block.tallGrass.blockID)
            {
                this.theWorld.playAuxSFX(2001, var1, var2, var3, Block.tallGrass.blockID + 4096);
                this.theWorld.setBlock(var1, var2, var3, 0);
                this.theEntity.eatGrassBonus();
            } else if (this.theWorld.getBlockId(var1, var2 - 1, var3) == this.blocktoEat.blockID)
            {
                this.theWorld.playAuxSFX(2001, var1, var2 - 1, var3, this.blocktoEat.blockID);
                this.theWorld.setBlock(var1, var2 - 1, var3, this.blocktoReplace.blockID);
                this.theEntity.eatGrassBonus();
            }
        }
    }
}
