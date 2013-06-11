package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAerwhale extends EntityLiving implements IMob
{
    public String dir = "/net/aetherteam/aether/client/sprites";
    private int aboveBelow;
    private long checkTime = 0L;
    private final long checkTimeInterval = 3000L;
    private double checkX = 0.0D;
    private double checkY = 0.0D;
    private double checkZ = 0.0D;
    private final double minTraversalDist = 3.0D;
    private boolean isStuckWarning = false;
    public int courseChangeCooldown = 0;
    public double waypointX;
    public double waypointZ;
    private Entity targetedEntity = null;
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;
    public double motionYaw;
    public double motionPitch;

    public EntityAerwhale(World var1)
    {
        super(var1);
        this.isImmuneToFire = true;
        this.aggroCooldown = 0;
        this.prevAttackCounter = 0;
        this.attackCounter = 0;
        this.texture = this.dir + "/mobs/aerwhale/aerwhale.png";
        this.setSize(2.0F, 2.0F);
        this.moveSpeed = 0.5F;
        this.health = 20;
        this.rotationYaw = 360.0F * this.rand.nextFloat();
        this.rotationPitch = 90.0F * this.rand.nextFloat() - 45.0F;
        this.ignoreFrustumCheck = true;
        this.aboveBelow = this.rand.nextInt(2);
    }

    public int getMaxHealth()
    {
        return 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {}

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float var1, float var2)
    {
        if (this.isInWater())
        {
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.handleLavaMovement())
        {
            this.moveFlying(var1, var2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else
        {
            float var3 = 0.91F;

            if (this.onGround)
            {
                var3 = 0.54600006F;
                int var4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (var4 > 0)
                {
                    var3 = Block.blocksList[var4].slipperiness * 0.91F;
                }
            }

            float var10 = 0.16277136F / (var3 * var3 * var3);
            this.moveFlying(var1, var2, this.onGround ? 0.1F * var10 : 0.02F);
            var3 = 0.91F;

            if (this.onGround)
            {
                var3 = 0.54600006F;
                int var5 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (var5 > 0)
                {
                    var3 = Block.blocksList[var5].slipperiness * 0.91F;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) var3;
            this.motionY *= (double) var3;
            this.motionZ *= (double) var3;
        }

        this.prevLimbYaw = this.limbYaw;
        double var8 = this.posX - this.prevPosX;
        double var9 = this.posZ - this.prevPosZ;
        float var7 = MathHelper.sqrt_double(var8 * var8 + var9 * var9) * 4.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }

        this.limbYaw += (var7 - this.limbYaw) * 0.4F;
        this.limbSwing += this.limbYaw;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        this.rotationPitch = 0.0F;

        if (this.aboveBelow == 0)
        {
            this.setPosition(this.posX, 1.0D, this.posZ);
        } else
        {
            this.setPosition(this.posX, 128.0D, this.posZ);
        }

        double[] var1 = new double[]{this.openSpace(0.0F, 0.0F), this.openSpace(45.0F, 0.0F), this.openSpace(0.0F, 45.0F), this.openSpace(-45.0F, 0.0F), this.openSpace(0.0F, -45.0F)};
        int var2 = 0;
        int var3;

        for (var3 = 1; var3 < 5; ++var3)
        {
            if (var1[var3] > var1[var2])
            {
                var2 = var3;
            }
        }

        switch (var2)
        {
            case 0:
                if (var1[0] == 50.0D)
                {
                    this.motionYaw *= 0.8999999761581421D;
                    this.motionPitch *= 0.8999999761581421D;

                    if (this.posY > 100.0D)
                    {
                        this.motionPitch -= 2.0D;
                    }

                    if (this.posY < 20.0D)
                    {
                        this.motionPitch += 2.0D;
                    }
                } else
                {
                    this.rotationPitch = -this.rotationPitch;
                    this.rotationYaw = -this.rotationYaw;
                }

                break;

            case 1:
                this.motionYaw += 5.0D;
                break;

            case 2:
                this.motionPitch -= 5.0D;
                break;

            case 3:
                this.motionYaw -= 5.0D;
                break;

            case 4:
                this.motionPitch += 5.0D;
        }

        this.motionYaw += (double) (2.0F * this.rand.nextFloat() - 1.0F);
        this.motionPitch += (double) (2.0F * this.rand.nextFloat() - 1.0F);
        this.rotationPitch = (float) ((double) this.rotationPitch + 0.1D * this.motionPitch);
        this.rotationYaw = (float) ((double) this.rotationYaw + 0.1D * this.motionYaw);

        if (this.rotationPitch < -60.0F)
        {
            this.rotationPitch = -60.0F;
        }

        if (this.rotationPitch > 60.0F)
        {
            this.rotationPitch = 60.0F;
        }

        this.rotationPitch = (float) ((double) this.rotationPitch * 0.99D);
        this.motionX += 0.005D * Math.cos((double) this.rotationYaw / 180.0D * Math.PI) * Math.cos((double) this.rotationPitch / 180.0D * Math.PI);
        this.motionY += 0.005D * Math.sin((double) this.rotationPitch / 180.0D * Math.PI);
        this.motionZ += 0.005D * Math.sin((double) this.rotationYaw / 180.0D * Math.PI) * Math.cos((double) this.rotationPitch / 180.0D * Math.PI);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        var3 = MathHelper.floor_double(this.posX);
        int var4 = MathHelper.floor_double(this.boundingBox.minY);
        int var5 = MathHelper.floor_double(this.posZ);

        if (this.motionX > 0.0D && this.worldObj.getBlockId(var3 + 1, var4, var5) != 0)
        {
            this.motionX = -this.motionX;
            this.motionYaw -= 10.0D;
        } else if (this.motionX < 0.0D && this.worldObj.getBlockId(var3 - 1, var4, var5) != 0)
        {
            this.motionX = -this.motionX;
            this.motionYaw += 10.0D;
        }

        if (this.motionY > 0.0D && this.worldObj.getBlockId(var3, var4 + 1, var5) != 0)
        {
            this.motionY = -this.motionY;
            this.motionPitch -= 10.0D;
        } else if (this.motionY < 0.0D && this.worldObj.getBlockId(var3, var4 - 1, var5) != 0)
        {
            this.motionY = -this.motionY;
            this.motionPitch += 10.0D;
        }

        if (this.motionZ > 0.0D && this.worldObj.getBlockId(var3, var4, var5 + 1) != 0)
        {
            this.motionZ = -this.motionZ;
            this.motionYaw -= 10.0D;
        } else if (this.motionZ < 0.0D && this.worldObj.getBlockId(var3, var4, var5 - 1) != 0)
        {
            this.motionZ = -this.motionZ;
            this.motionYaw += 10.0D;
        }

        this.extinguish();
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.checkForBeingStuck();
    }

    public double getSpeed()
    {
        return Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
    }

    private double openSpace(float var1, float var2)
    {
        float var3 = this.rotationYaw + var1;
        float var4 = this.rotationYaw + var1;
        Vec3 var5 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        float var6 = MathHelper.cos(-var3 * 0.01745329F - (float) Math.PI);
        float var7 = MathHelper.sin(-var3 * 0.01745329F - (float) Math.PI);
        float var8 = MathHelper.cos(-var4 * 0.01745329F);
        float var9 = MathHelper.sin(-var4 * 0.01745329F);
        float var10 = var7 * var8;
        float var12 = var6 * var8;
        double var13 = 50.0D;
        Vec3 var15 = var5.addVector((double) var10 * var13, (double) var9 * var13, (double) var12 * var13);
        MovingObjectPosition var16 = this.worldObj.rayTraceBlocks_do(var5, var15, true);

        if (var16 == null)
        {
            return 50.0D;
        } else if (var16.typeOfHit == EnumMovingObjectType.TILE)
        {
            double var17 = (double) var16.blockX - this.posX;
            double var19 = (double) var16.blockY - this.posY;
            double var21 = (double) var16.blockZ - this.posZ;
            return Math.sqrt(var17 * var17 + var19 * var19 + var21 * var21);
        } else
        {
            return 50.0D;
        }
    }

    private void checkForBeingStuck()
    {
        long var1 = System.currentTimeMillis();

        if (var1 > this.checkTime + 3000L)
        {
            double var3 = this.posX - this.checkX;
            double var5 = this.posY - this.checkY;
            double var7 = this.posZ - this.checkZ;
            double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);

            if (var9 < 3.0D && !this.isStuckWarning)
            {
                this.isStuckWarning = true;
            }

            this.checkX = this.posX;
            this.checkY = this.posY;
            this.checkZ = this.posZ;
            this.checkTime = var1;
        }
    }

    private boolean isCourseTraversable(double var1, double var3, double var5, double var7)
    {
        double var9 = (this.waypointX - this.posX) / var7;
        double var11 = (this.waypointZ - this.posZ) / var7;
        AxisAlignedBB var13 = this.boundingBox.copy();

        for (int var14 = 1; (double) var14 < var7; ++var14)
        {
            var13.offset(var9, this.posY, var11);

            if (this.worldObj.getCollidingBoundingBoxes(this, var13).size() > 0)
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
        return "aemob.aerwhale.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aemob.aerwhale.die";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aemob.aerwhale.die";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 3.0F;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    public boolean canDespawn()
    {
        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {}

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(65) == 0 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(var1, var2 - 1, var3) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockId(var1, var2 - 1, var3) != AetherBlocks.Holystone.blockID;
    }
}
