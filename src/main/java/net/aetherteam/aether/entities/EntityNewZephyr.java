package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
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

    public EntityNewZephyr(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/newzephyr/zephyr.png";
        this.setSize(2.0F, 1.0F);
        this.xpov = this.rand.nextBoolean() ? -1 : 1;
        this.zpov = this.rand.nextBoolean() ? -1 : 1;
    }

    public int getMaxHealth()
    {
        return 5;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.targetedEntity != null)
        {
            double var1 = this.targetedEntity.posX - this.posX;
            double var3 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
            double var5 = this.targetedEntity.posZ - this.posZ;
            this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var1, var5)) * 180.0F / (float)Math.PI;
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
            AxisAlignedBB var1 = AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(15.0D, 200.0D, 15.0D);
            List var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var1);

            for (int var3 = 0; var3 < var2.size(); ++var3)
            {
                if (var2.size() > 0 && !((EntityPlayer)var2.get(var3)).capabilities.isCreativeMode)
                {
                    this.targetedEntity = (EntityPlayer)var2.get(var3);
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
        double var1;
        double var3;
        double var5;
        double var7;
        double var9;
        double var11;
        double var13;
        double var15;

        if (!this.divepointSet)
        {
            var1 = (double)this.rand.nextFloat() * 2.0D;
            var3 = (double)MathHelper.sqrt_double(9.0D - var1 * var1);
            var5 = (double)this.rand.nextFloat() * var3;
            var7 = (double)MathHelper.sqrt_double(var3 * var3 - var5 * var5);
            this.waypointX = this.targetedEntity.posX + var5 * (double)this.xpov;
            this.waypointY = this.targetedEntity.posY + var1;
            this.waypointZ = this.targetedEntity.posZ + var7 * (double)this.zpov;
            this.targetX = this.targetedEntity.posX;
            this.targetY = this.targetedEntity.posY;
            this.targetZ = this.targetedEntity.posZ;
            this.divepointSet = true;
            var9 = this.waypointX - this.posX;
            var11 = this.waypointY - this.posY;
            var13 = this.waypointZ - this.posZ;
            var15 = (double)MathHelper.sqrt_double(var9 * var9 + var11 * var11 + var13 * var13);

            if (!this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var15))
            {
                this.divepointSet = false;
            }
        }
        else
        {
            var1 = this.waypointX - this.posX;
            var3 = this.waypointY - this.posY;
            var5 = this.waypointZ - this.posZ;
            var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);

            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7))
            {
                float var25 = 0.5F;
                this.motionX = var1 / var7 * (double)var25;
                this.motionY = var3 / var7 * (double)var25;
                this.motionZ = var5 / var7 * (double)var25;
            }
            else
            {
                this.divepointSet = false;
                this.dive = false;
                this.updateTime = -1;
            }

            if (var7 < 1.0D)
            {
                this.divepointSet = false;
                this.dive = false;
                this.updateTime = -1;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
                var9 = this.targetX - this.posX;
                var11 = this.targetY - (this.posY + (double)(this.height / 2.0F));
                var13 = this.targetZ - this.posZ;
                var15 = (double)MathHelper.sqrt_double(var9 * var9 + var11 * var11 + var13 * var13);
                Object var17 = null;

                for (int var18 = 0; var18 < 5; ++var18)
                {
                    Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY + 0.5D, this.posZ, new Random(), 0.5D, var9 / var15, var11 / var15, var13 / var15);
                }

                List var26 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(var9 / var15, var11 / var15, var13 / var15).expand(1.0D, 4.0D, 1.0D));

                for (int var19 = 0; var19 < var26.size(); ++var19)
                {
                    Entity var20 = (Entity)var26.get(var19);
                    System.out.println(var20);
                    double var21 = var20.posX - this.posX;
                    double var23;

                    for (var23 = var20.posZ - this.posZ; var21 * var21 + var23 * var23 < 1.0E-4D; var23 = (Math.random() - Math.random()) * 0.01D)
                    {
                        var21 = (Math.random() - Math.random()) * 0.01D;
                    }

                    var20.velocityChanged = true;
                    var20.motionX = 0.3D * var21;
                    var20.motionY = 1.0D;
                    var20.motionZ = 0.3D * var23;
                }

                new EntityZephyrSnowball(this.worldObj, this, var9, var11, var13);
            }
        }
    }

    private void orbitPlayer()
    {
        double var1 = this.waypointX - this.posX;
        double var3 = this.waypointY - this.posY;
        double var5 = this.waypointZ - this.posZ;
        double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        double var9 = this.targetedEntity.getDistanceSqToEntity(this);
        double var11 = this.targetedEntity.posX - this.posX;
        double var13 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double var15 = this.targetedEntity.posZ - this.posZ;
        this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var11, var15)) * 180.0F / (float)Math.PI;

        if (this.updateTime-- < 0)
        {
            this.updateTime = this.rand.nextInt(5) + 40;
            double var17 = (double)this.rand.nextFloat() * 10.0D;
            double var19 = (double)MathHelper.sqrt_double(196.0D - var17 * var17);
            double var21 = (double)this.rand.nextFloat() * var19;
            double var23 = (double)MathHelper.sqrt_double(var19 * var19 - var21 * var21);

            if (this.rand.nextInt(8) == 0)
            {
                this.xpov = this.rand.nextBoolean() ? -1 : 1;
                this.zpov = this.rand.nextBoolean() ? -1 : 1;
            }

            this.waypointX = this.targetedEntity.posX + var21 * (double)this.xpov;
            this.waypointY = this.targetedEntity.posY + var17;
            this.waypointZ = this.targetedEntity.posZ + var23 * (double)this.zpov;
        }

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7))
        {
            float var25 = 0.5F;
            this.motionX = var1 / var7 * (double)var25;
            this.motionY = var3 / var7 * (double)var25;
            this.motionZ = var5 / var7 * (double)var25;
        }

        if (var7 < 1.0D)
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
    public void moveEntityWithHeading(float var1, float var2)
    {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    private void randomMovement()
    {
        double var1 = this.waypointX - this.posX;
        double var3 = this.waypointY - this.posY;
        double var5 = this.waypointZ - this.posZ;
        double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);

        if (var7 < 4.0D || var7 > 40.0D)
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 30.0F);
        }

        if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7))
        {
            float var9 = 0.5F;
            this.motionX = var1 / var7 * (double)var9;
            this.motionY = var3 / var7 * (double)var9;
            this.motionZ = var5 / var7 * (double)var9;
        }
        else
        {
            this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }
    }

    private boolean isCourseTraversable(double var1, double var3, double var5, double var7)
    {
        double var9 = (this.waypointX - this.posX) / var7;
        double var11 = (this.waypointY - this.posY) / var7;
        double var13 = (this.waypointZ - this.posZ) / var7;
        AxisAlignedBB var15 = this.boundingBox.copy();

        for (int var16 = 1; (double)var16 < var7; ++var16)
        {
            var15.offset(var9, var11, var13);

            if (this.worldObj.getCollidingBoundingBoxes(this, var15).size() > 0)
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
        return "aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.zephyr.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.zephyr.say";
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
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(65) == 0 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(var1, var2 - 1, var3) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }
}
