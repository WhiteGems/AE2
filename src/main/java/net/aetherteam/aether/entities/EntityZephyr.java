package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityZephyr extends EntityFlying
    implements IMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    public float sinage = 0.0F;

    private long checkTime = 0L;
    private final long checkTimeInterval = 3000L;
    private double checkX = 0.0D;
    private double checkY = 0.0D;
    private double checkZ = 0.0D;
    private final double minTraversalDist = 3.0D;
    private boolean isStuckWarning = false;
    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;

    public EntityZephyr(World world)
    {
        super(world);
        this.courseChangeCooldown = 0;
        this.targetedEntity = null;
        this.aggroCooldown = 0;
        this.prevAttackCounter = 0;
        this.attackCounter = 0;
        this.texture = (this.dir + "/mobs/zephyr/zephyr.png");
        setSize(4.0F, 4.0F);
    }

    public int getMaxHealth()
    {
        return 5;
    }

    protected void updateEntityActionState()
    {
        if ((this.posY < -2.0D) || (this.posY > 130.0D))
        {
            despawnEntity();
        }

        this.prevAttackCounter = this.attackCounter;
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

        if ((d3 < 4.0D) || (d3 > 30.0D))
        {
            this.waypointX = (this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = (this.posY + this.rand.nextFloat() * 2.0F * 16.0F);
            this.waypointZ = (this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            this.motionX += d / d3 * 0.1D;
            this.motionY += d1 / d3 * 0.1D;
            this.motionZ += d2 / d3 * 0.1D;
        }
        else
        {
            this.waypointX = (this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = (this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = (this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.courseChangeCooldown-- <= 0)
        {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
        }
    }

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (this.waypointX - this.posX) / d3;
        double d5 = (this.waypointY - this.posY) / d3;
        double d6 = (this.waypointZ - this.posZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; i < d3; i++)
        {
            axisalignedbb.offset(d4, d5, d6);

            if (this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0)
            {
                return false;
            }
        }

        return true;
    }

    protected String getLivingSound()
    {
        return "aemob.zephyr.say";
    }

    protected String getHurtSound()
    {
        return "aemob.zephyr.say";
    }

    protected String getDeathSound()
    {
        return "aemob.zephyr.say";
    }

    public boolean canDespawn()
    {
        return true;
    }

    protected float getSoundVolume()
    {
        return 3.0F;
    }

    private void checkForBeingStuck()
    {
        long curtime = System.currentTimeMillis();

        if (curtime > this.checkTime + 3000L)
        {
            double diffx = this.posX - this.checkX;
            double diffy = this.posY - this.checkY;
            double diffz = this.posZ - this.checkZ;
            double distanceTravelled = Math.sqrt(diffx * diffx + diffy * diffy + diffz * diffz);

            if (distanceTravelled < 3.0D)
            {
                if (!this.isStuckWarning)
                {
                    this.isStuckWarning = true;
                }
                else
                {
                    setDead();
                }
            }

            this.checkX = this.posX;
            this.checkY = this.posY;
            this.checkZ = this.posZ;
            this.checkTime = curtime;
        }
    }

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return (this.rand.nextInt(65) == 0) && (this.worldObj.checkNoEntityCollision(this.boundingBox)) && (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) && (!this.worldObj.isAnyLiquid(this.boundingBox)) && (this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID) && (this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID) && (this.worldObj.difficultySetting > 0);
    }

    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}

