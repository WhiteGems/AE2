package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityNewZephyr extends EntityFlying implements IMob
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
    public int courseChangeCooldown = 0;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity = null;
    private int aggroCooldown = 0;
    public int prevAttackCounter = 0;
    public int attackCounter = 0;
    public float sinage;

    public EntityNewZephyr(World world)
    {
        super(world);
        this.setSize(2.0F, 1.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(5.0D);
        this.setEntityHealth(5.0F);
        this.xpov = this.rand.nextBoolean() ? -1 : 1;
        this.zpov = this.rand.nextBoolean() ? -1 : 1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.targetedEntity != null)
        {
            double d5 = this.targetedEntity.posX - this.posX;
            double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
            double d7 = this.targetedEntity.posZ - this.posZ;
            this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
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
        if (this.targetedEntity != null && this.targetedEntity.isDead)
        {
            this.targetedEntity = null;
        }

        if (this.targetedEntity == null || this.aggroCooldown-- <= 0)
        {
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(15.0D, 200.0D, 15.0D);
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);

            for (int i = 0; i < list.size(); ++i)
            {
                if (list.size() > 0 && !((EntityPlayer)list.get(i)).capabilities.isCreativeMode && Aether.getServerPlayer((EntityPlayer)list.get(i)).getZephyrCoolDown() == 0)
                {
                    this.targetedEntity = (EntityPlayer)list.get(i);
                    Aether.getServerPlayer((EntityPlayer)list.get(i)).setZephyrCooldown();
                    System.out.println("Zephyr Targeting !!!");
                }
            }

            if (this.targetedEntity != null)
            {
                this.aggroCooldown = 20;
            }
        }

        if (this.posY < -2.0D || this.posY > 130.0D)
        {
            this.despawnEntity();
        }

        if (this.targetedEntity == null)
        {
            this.randomMovement();
        }
        else if (this.dive)
        {
            this.dive();
        }
        else
        {
            this.orbitPlayer();
        }

        this.prevAttackCounter = this.attackCounter;
    }

    private void dive()
    {
        double d;
        double d1;
        double d2;
        double d3;
        double d5;
        double d6;
        double d7;
        double range;

        if (!this.divepointSet)
        {
            d = (double)this.rand.nextFloat() * 2.0D;
            d1 = (double)MathHelper.sqrt_double(9.0D - d * d);
            d2 = (double)this.rand.nextFloat() * d1;
            d3 = (double)MathHelper.sqrt_double(d1 * d1 - d2 * d2);
            this.waypointX = this.targetedEntity.posX + d2 * (double)this.xpov;
            this.waypointY = this.targetedEntity.posY + d;
            this.waypointZ = this.targetedEntity.posZ + d3 * (double)this.zpov;
            this.targetX = this.targetedEntity.posX;
            this.targetY = this.targetedEntity.posY;
            this.targetZ = this.targetedEntity.posZ;
            this.divepointSet = true;
            d5 = this.waypointX - this.posX;
            d6 = this.waypointY - this.posY;
            d7 = this.waypointZ - this.posZ;
            range = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);

            if (!this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, range))
            {
                this.divepointSet = false;
            }
        }
        else
        {
            d = this.waypointX - this.posX;
            d1 = this.waypointY - this.posY;
            d2 = this.waypointZ - this.posZ;
            d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
            {
                float var25 = 0.5F;
                this.motionX = d / d3 * (double)var25;
                this.motionY = d1 / d3 * (double)var25;
                this.motionZ = d2 / d3 * (double)var25;
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
                d5 = this.targetX - this.posX;
                d6 = this.targetY - (this.posY + (double)(this.height / 2.0F));
                d7 = this.targetZ - this.posZ;
                range = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);
                Object entity = null;

                for (int list = 0; list < 5; ++list)
                {
                    Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, new Random(), 0.5D, d5 / range, d6 / range, d7 / range);
                }

                List var26 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(d5 / range, d6 / range, d7 / range).expand(1.0D, 4.0D, 1.0D));

                for (int snowball = 0; snowball < var26.size(); ++snowball)
                {
                    Entity entity1 = (Entity)var26.get(snowball);
                    System.out.println(entity1);
                    double knockx = entity1.posX - this.posX;
                    double knockz;

                    for (knockz = entity1.posZ - this.posZ; knockx * knockx + knockz * knockz < 1.0E-4D; knockz = (Math.random() - Math.random()) * 0.01D)
                    {
                        knockx = (Math.random() - Math.random()) * 0.01D;
                    }

                    entity1.velocityChanged = true;
                    entity1.motionX = 0.3D * knockx;
                    entity1.motionY = 1.0D;
                    entity1.motionZ = 0.3D * knockz;
                }

                new EntityZephyrSnowball(this.worldObj, this, d5, d6, d7);
            }
        }
    }

    private void orbitPlayer()
    {
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        double d4 = this.targetedEntity.getDistanceSqToEntity(this);
        double d5 = this.targetedEntity.posX - this.posX;
        double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double d7 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;

        if (this.updateTime-- < 0)
        {
            this.updateTime = this.rand.nextInt(5) + 40;
            double Speed = (double)this.rand.nextFloat() * 10.0D;
            double distance = (double)MathHelper.sqrt_double(196.0D - Speed * Speed);
            double x = (double)this.rand.nextFloat() * distance;
            double y = (double)MathHelper.sqrt_double(distance * distance - x * x);

            if (this.rand.nextInt(8) == 0)
            {
                this.xpov = this.rand.nextBoolean() ? -1 : 1;
                this.zpov = this.rand.nextBoolean() ? -1 : 1;
            }

            this.waypointX = this.targetedEntity.posX + x * (double)this.xpov;
            this.waypointY = this.targetedEntity.posY + Speed;
            this.waypointZ = this.targetedEntity.posZ + y * (double)this.zpov;
        }

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float var25 = 0.5F;
            this.motionX = d / d3 * (double)var25;
            this.motionY = d1 / d3 * (double)var25;
            this.motionZ = d2 / d3 * (double)var25;
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

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float par1, float par2)
    {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    private void randomMovement()
    {
        double d = this.waypointX - this.posX;
        double d1 = this.waypointY - this.posY;
        double d2 = this.waypointZ - this.posZ;
        double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);

        if (d3 < 4.0D || d3 > 40.0D)
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
        }

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
        {
            float Speed = 0.5F;
            this.motionX = d / d3 * (double)Speed;
            this.motionY = d1 / d3 * (double)Speed;
            this.motionZ = d2 / d3 * (double)Speed;
        }
        else
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
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
