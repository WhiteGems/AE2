package net.aetherteam.aether.entities;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityNewZephyr extends EntityFlying
    implements IMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";

    boolean divepointSet = false;
    int xpov = 1;
    int zpov = 1;
    private double targetX;
    private double targetY;
    private double targetZ;
    int updateTime = 0;
    private boolean dive;
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
    public float sinage;

    public EntityNewZephyr(World world)
    {
        super(world);
        this.courseChangeCooldown = 0;
        this.targetedEntity = null;
        this.aggroCooldown = 0;
        this.prevAttackCounter = 0;
        this.attackCounter = 0;
        this.texture = (this.dir + "/mobs/newzephyr/zephyr.png");
        setSize(2.0F, 1.0F);
        this.xpov = (this.rand.nextBoolean() ? -1 : 1);
        this.zpov = (this.rand.nextBoolean() ? -1 : 1);
    }

    public int getMaxHealth()
    {
        return 5;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.targetedEntity != null)
        {
            double d5 = this.targetedEntity.posX - this.posX;
            double d6 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
            double d7 = this.targetedEntity.posZ - this.posZ;
            this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(d5, d7) * 180.0F / (float)Math.PI);
        }
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.hurtTime > 0)
        {
            this.sinage += 0.9F;
        }
        else
        {
            this.sinage += 0.2F;
        }

        if (this.sinage > ((float)Math.PI * 2F))
        {
            this.sinage -= ((float)Math.PI * 2F);
        }
    }

    protected void updateEntityActionState()
    {
        if ((this.targetedEntity != null) && (this.targetedEntity.isDead))
        {
            this.targetedEntity = null;
        }

        if ((this.targetedEntity == null) || (this.aggroCooldown-- <= 0))
        {
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(15.0D, 200.0D, 15.0D);
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);

            for (int i = 0; i < list.size(); i++)
            {
                if ((list.size() > 0) &&
                        (!((EntityPlayer)list.get(i)).capabilities.isCreativeMode))
                {
                    this.targetedEntity = ((EntityPlayer)list.get(i));
                }
            }

            if (this.targetedEntity != null)
            {
                this.aggroCooldown = 20;
            }
        }

        if ((this.posY < -2.0D) || (this.posY > 130.0D))
        {
            despawnEntity();
        }

        if (this.targetedEntity == null)
        {
            randomMovement();
        }
        else if (this.dive)
        {
            dive();
        }
        else
        {
            orbitPlayer();
        }

        this.prevAttackCounter = this.attackCounter;
    }

    private void dive()
    {
        EntityZephyrSnowball snowball;

        if (!this.divepointSet)
        {
            double randomHeight = this.rand.nextFloat() * 2.0D;
            double distance = MathHelper.sqrt_double(9.0D - randomHeight * randomHeight);
            double x = this.rand.nextFloat() * distance;
            double y = MathHelper.sqrt_double(distance * distance - x * x);
            this.waypointX = (this.targetedEntity.posX + x * this.xpov);
            this.waypointY = (this.targetedEntity.posY + randomHeight);
            this.waypointZ = (this.targetedEntity.posZ + y * this.zpov);
            this.targetX = this.targetedEntity.posX;
            this.targetY = this.targetedEntity.posY;
            this.targetZ = this.targetedEntity.posZ;
            this.divepointSet = true;
            double d = this.waypointX - this.posX;
            double d1 = this.waypointY - this.posY;
            double d2 = this.waypointZ - this.posZ;
            double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

            if (!isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
            {
                this.divepointSet = false;
            }
        }
        else
        {
            double d = this.waypointX - this.posX;
            double d1 = this.waypointY - this.posY;
            double d2 = this.waypointZ - this.posZ;
            double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

            if (isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
            {
                float Speed = 0.5F;
                this.motionX = (d / d3 * Speed);
                this.motionY = (d1 / d3 * Speed);
                this.motionZ = (d2 / d3 * Speed);
            }
            else
            {
                this.divepointSet = false;
                this.dive = false;
                this.updateTime = -1;
            }

            if (d3 < 1.0D)
            {
                this.divepointSet = false;
                this.dive = false;
                this.updateTime = -1;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                double d5 = this.targetX - this.posX;
                double d6 = this.targetY - (this.posY + this.height / 2.0F);
                double d7 = this.targetZ - this.posZ;
                double range = MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);
                Entity entity = null;

                for (int multiply = 0; multiply < 5; multiply++)
                {
                    Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, new Random(), 0.5D, d5 / range, d6 / range, d7 / range);
                }

                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(d5 / range, d6 / range, d7 / range).expand(1.0D, 4.0D, 1.0D));

                for (int l = 0; l < list.size(); l++)
                {
                    Entity entity1 = (Entity)list.get(l);
                    System.out.println(entity1);
                    double knockx = entity1.posX - this.posX;

                    for (double knockz = entity1.posZ - this.posZ; knockx * knockx + knockz * knockz < 0.0001D; knockz = (Math.random() - Math.random()) * 0.01D)
                    {
                        knockx = (Math.random() - Math.random()) * 0.01D;
                    }

                    entity1.velocityChanged = true;
                    entity1.motionX = (0.3D * knockx);
                    entity1.motionY = 1.0D;
                    entity1.motionZ = (0.3D * knockz);
                }

                snowball = new EntityZephyrSnowball(this.worldObj, this, d5, d6, d7);
            }
        }
    }

    private void orbitPlayer()
    {
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        double d4 = this.targetedEntity.getDistanceSqToEntity(this);
        double d5 = this.targetedEntity.posX - this.posX;
        double d6 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
        double d7 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(d5, d7) * 180.0F / (float)Math.PI);

        if (this.updateTime-- < 0)
        {
            this.updateTime = (this.rand.nextInt(5) + 40);
            double randomHeight = this.rand.nextFloat() * 10.0D;
            double distance = MathHelper.sqrt_double(196.0D - randomHeight * randomHeight);
            double x = this.rand.nextFloat() * distance;
            double y = MathHelper.sqrt_double(distance * distance - x * x);

            if (this.rand.nextInt(8) == 0)
            {
                this.xpov = (this.rand.nextBoolean() ? -1 : 1);
                this.zpov = (this.rand.nextBoolean() ? -1 : 1);
            }

            this.waypointX = (this.targetedEntity.posX + x * this.xpov);
            this.waypointY = (this.targetedEntity.posY + randomHeight);
            this.waypointZ = (this.targetedEntity.posZ + y * this.zpov);
        }

        if (isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float Speed = 0.5F;
            this.motionX = (d / d3 * Speed);
            this.motionY = (d1 / d3 * Speed);
            this.motionZ = (d2 / d3 * Speed);
        }

        if (d3 < 1.0D)
        {
            if (this.rand.nextInt(6) == 0)
            {
                this.dive = true;
            }

            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }
    }

    public void moveEntityWithHeading(float par1, float par2)
    {
        moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    private void randomMovement()
    {
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

        if ((d3 < 4.0D) || (d3 > 40.0D))
        {
            this.waypointX = (this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointY = (this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointZ = (this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
        }

        if (isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float Speed = 0.5F;
            this.motionX = (d / d3 * Speed);
            this.motionY = (d1 / d3 * Speed);
            this.motionZ = (d2 / d3 * Speed);
        }
        else
        {
            this.waypointX = (this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = (this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = (this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
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

