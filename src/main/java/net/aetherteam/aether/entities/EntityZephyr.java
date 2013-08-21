package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityZephyr extends EntityFlying implements IMob
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
    public int courseChangeCooldown = 0;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity = null;
    private int aggroCooldown = 0;
    public int prevAttackCounter = 0;
    public int attackCounter = 0;

    public EntityZephyr(World world)
    {
        super(world);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(5.0D);
        this.setEntityHealth(5.0F);
        this.setSize(4.0F, 4.0F);
    }

    protected void updateEntityActionState()
    {
        if (this.posY < -2.0D || this.posY > 130.0D)
        {
            this.despawnEntity();
        }

        this.prevAttackCounter = this.attackCounter;
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

        if (d3 < 4.0D || d3 > 30.0D)
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.posY + (double)(this.rand.nextFloat() * 2.0F * 16.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            this.motionX += d / d3 * 0.1D;
            this.motionY += d1 / d3 * 0.1D;
            this.motionZ += d2 / d3 * 0.1D;
        }
        else
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
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

        for (int i = 1; (double)i < d3; ++i)
        {
            axisalignedbb.offset(d4, d5, d6);

            if (this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "aether:aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.zephyr.say";
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return true;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
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
                    this.setDead();
                }
            }

            this.checkX = this.posX;
            this.checkY = this.posY;
            this.checkZ = this.posZ;
            this.checkTime = curtime;
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(65) == 0 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}
