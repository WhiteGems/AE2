package net.aetherteam.aether.entities.ai;

import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.World;

public class AIEntityArrowAttackSentry extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final EntitySentryGolem field_82641_b;
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

    public AIEntityArrowAttackSentry(EntitySentryGolem par1IRangedAttackMob, float par2, int par3, float par4)
    {
        if (!(par1IRangedAttackMob instanceof EntityLiving))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }

        this.field_82641_b = par1IRangedAttackMob;
        this.entityHost = par1IRangedAttackMob;
        this.entityMoveSpeed = par2;
        this.maxRangedAttackTime = par3;
        this.maxAttackRange = (par4 * par4);
        this.minAttackRange = 49.0F;
        this.runRange = 16.0F;
        setMutexBits(3);
    }

    public boolean shouldExecute()
    {
        EntityLiving var1 = this.entityHost.getAttackTarget();

        if (var1 == null)
        {
            return false;
        }

        this.attackTarget = var1;
        return true;
    }

    public boolean continueExecuting()
    {
        return (shouldExecute()) || (!this.entityHost.getNavigator().noPath());
    }

    public void resetTask()
    {
        this.attackTarget = null;
        this.seeTimer = 0;
    }

    public void updateTask()
    {
        double distances = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
        boolean canSee = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (canSee)
        {
            this.seeTimer += 1;
        }
        else
        {
            this.seeTimer = 0;
        }

        if ((distances <= this.runRange) && (this.seeTimer >= 20))
        {
            this.running = true;
        }

        if ((this.running) && (distances <= this.minAttackRange) && (this.seeTimer >= 20))
        {
            Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom((EntityCreature)this.entityHost, 16, 7, this.entityHost.worldObj.U().getVecFromPool(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ));

            if (var2 != null)
            {
                this.i = var2.xCoord;
                this.j = var2.yCoord;
                this.k = var2.zCoord;
            }

            this.entityHost.getNavigator().tryMoveToXYZ(this.i, this.j, this.k, this.entityMoveSpeed);
        }
        else if ((distances <= this.maxAttackRange) && (this.seeTimer >= 20))
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
        this.field_82641_b.setFire(this.rangedAttackTime);

        if ((this.rangedAttackTime > 60) ||
                (this.rangedAttackTime <= 30))
        {
            this.field_82641_b.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 20)
        {
            this.field_82641_b.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 10)
        {
            this.field_82641_b.setHandState((byte)1);
        }

        if (this.rangedAttackTime <= 0)
        {
            if ((distances <= this.maxAttackRange) && (canSee))
            {
                this.field_82641_b.attackEntityWithRangedAttack(this.attackTarget, 1.0F);
                this.rangedAttackTime = this.maxRangedAttackTime;
                this.field_82641_b.setHandState((byte)2);
            }
        }
    }
}

