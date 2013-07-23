package net.aetherteam.aether.entities.ai;

import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class AIEntityArrowAttackSentry extends EntityAIBase
{
    private final EntityLiving entityHost;

    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final EntitySentryGolem rangedAttackEntityHost;
    private EntityLiving attackTarget;
    private int rangedAttackTime = 0;
    private float entityMoveSpeed;
    private int seeTimer = 0;
    private int maxRangedAttackTime;
    private float maxAttackRange;
    private float minAttackRange;
    private float runRange;
    double i = 0.0D;
    double j = 0.0D;
    double k = 0.0D;
    boolean running = false;

    public AIEntityArrowAttackSentry(EntitySentryGolem var1, float var2, int var3, float var4)
    {
        if (!(var1 instanceof EntityLiving))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = var1;
            this.entityHost = var1;
            this.entityMoveSpeed = var2;
            this.maxRangedAttackTime = var3;
            this.maxAttackRange = var4 * var4;
            this.minAttackRange = 49.0F;
            this.runRange = 16.0F;
            this.setMutexBits(3);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving var1 = this.entityHost.getAttackTarget();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = var1;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.attackTarget = null;
        this.seeTimer = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        double var1 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
        boolean var3 = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (var3)
        {
            ++this.seeTimer;
        }
        else
        {
            this.seeTimer = 0;
        }

        if (var1 <= (double)this.runRange && this.seeTimer >= 20)
        {
            this.running = true;
        }

        if (this.running && var1 <= (double)this.minAttackRange && this.seeTimer >= 20)
        {
            Vec3 var4 = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature)this.entityHost, 16, 7, this.entityHost.worldObj.getWorldVec3Pool().getVecFromPool(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ));

            if (var4 != null)
            {
                this.i = var4.xCoord;
                this.j = var4.yCoord;
                this.k = var4.zCoord;
            }

            this.entityHost.getNavigator().tryMoveToXYZ(this.i, this.j, this.k, this.entityMoveSpeed);
        }
        else if (var1 <= (double)this.maxAttackRange && this.seeTimer >= 20)
        {
            this.running = false;
            this.entityHost.getNavigator().clearPathEntity();
        }
        else
        {
            this.running = false;
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
        this.rangedAttackEntityHost.setFire(this.rangedAttackTime);

        if (this.rangedAttackTime <= 60)
        {
            ;
        }

        if (this.rangedAttackTime <= 30)
        {
            this.rangedAttackEntityHost.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 20)
        {
            this.rangedAttackEntityHost.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 10)
        {
            this.rangedAttackEntityHost.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 0 && var1 <= (double)this.maxAttackRange && var3)
        {
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, 1.0F);
            this.rangedAttackTime = this.maxRangedAttackTime;
            this.rangedAttackEntityHost.setHandState((byte)2);
        }
    }
}
