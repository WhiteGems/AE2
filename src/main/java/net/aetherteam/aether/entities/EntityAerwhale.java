package net.aetherteam.aether.entities;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    private boolean hasSpawnedDisk;
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

    public EntityAerwhale(World world)
    {
        super(world);
        this.isImmuneToFire = true;
        this.aggroCooldown = 0;
        this.prevAttackCounter = 0;
        this.attackCounter = 0;
        this.setSize(2.0F, 2.0F);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        this.setEntityHealth(20.0F);
        this.rotationYaw = 360.0F * this.rand.nextFloat();
        this.rotationPitch = 90.0F * this.rand.nextFloat() - 45.0F;
        this.ignoreFrustumCheck = true;
        this.aboveBelow = this.rand.nextInt(2);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (!this.hasSpawnedDisk && this.worldObj.rand.nextInt(10000) == 1)
        {
            this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(AetherItems.AerwhaleMusicDisk)));
            this.hasSpawnedDisk = true;
        }
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float par1, float par2)
    {
        if (this.isInWater())
        {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }
        else if (this.handleLavaMovement())
        {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }
        else
        {
            float d0 = 0.91F;

            if (this.onGround)
            {
                d0 = 0.54600006F;
                int f3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (f3 > 0)
                {
                    d0 = Block.blocksList[f3].slipperiness * 0.91F;
                }
            }

            float f31 = 0.16277136F / (d0 * d0 * d0);
            this.moveFlying(par1, par2, this.onGround ? 0.1F * f31 : 0.02F);
            d0 = 0.91F;

            if (this.onGround)
            {
                d0 = 0.54600006F;
                int d1 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

                if (d1 > 0)
                {
                    d0 = Block.blocksList[d1].slipperiness * 0.91F;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)d0;
            this.motionY *= (double)d0;
            this.motionZ *= (double)d0;
        }

        this.prevLimbYaw = this.limbYaw;
        double d01 = this.posX - this.prevPosX;
        double d11 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt_double(d01 * d01 + d11 * d11) * 4.0F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        this.limbYaw += (f4 - this.limbYaw) * 0.4F;
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
        }
        else
        {
            this.setPosition(this.posX, 128.0D, this.posZ);
        }

        double[] distances = new double[] {this.openSpace(0.0F, 0.0F), this.openSpace(45.0F, 0.0F), this.openSpace(0.0F, 45.0F), this.openSpace(-45.0F, 0.0F), this.openSpace(0.0F, -45.0F)};
        int longest = 0;
        int i;

        for (i = 1; i < 5; ++i)
        {
            if (distances[i] > distances[longest])
            {
                longest = i;
            }
        }

        switch (longest)
        {
            case 0:
                if (distances[0] == 50.0D)
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
                }
                else
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

        this.motionYaw += (double)(2.0F * this.rand.nextFloat() - 1.0F);
        this.motionPitch += (double)(2.0F * this.rand.nextFloat() - 1.0F);
        this.rotationPitch = (float)((double)this.rotationPitch + 0.1D * this.motionPitch);
        this.rotationYaw = (float)((double)this.rotationYaw + 0.1D * this.motionYaw);

        if (this.rotationPitch < -60.0F)
        {
            this.rotationPitch = -60.0F;
        }

        if (this.rotationPitch > 60.0F)
        {
            this.rotationPitch = 60.0F;
        }

        this.rotationPitch = (float)((double)this.rotationPitch * 0.99D);
        this.motionX += 0.005D * Math.cos((double)this.rotationYaw / 180.0D * Math.PI) * Math.cos((double)this.rotationPitch / 180.0D * Math.PI);
        this.motionY += 0.005D * Math.sin((double)this.rotationPitch / 180.0D * Math.PI);
        this.motionZ += 0.005D * Math.sin((double)this.rotationYaw / 180.0D * Math.PI) * Math.cos((double)this.rotationPitch / 180.0D * Math.PI);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);

        if (this.motionX > 0.0D && this.worldObj.getBlockId(i + 1, j, k) != 0)
        {
            this.motionX = -this.motionX;
            this.motionYaw -= 10.0D;
        }
        else if (this.motionX < 0.0D && this.worldObj.getBlockId(i - 1, j, k) != 0)
        {
            this.motionX = -this.motionX;
            this.motionYaw += 10.0D;
        }

        if (this.motionY > 0.0D && this.worldObj.getBlockId(i, j + 1, k) != 0)
        {
            this.motionY = -this.motionY;
            this.motionPitch -= 10.0D;
        }
        else if (this.motionY < 0.0D && this.worldObj.getBlockId(i, j - 1, k) != 0)
        {
            this.motionY = -this.motionY;
            this.motionPitch += 10.0D;
        }

        if (this.motionZ > 0.0D && this.worldObj.getBlockId(i, j, k + 1) != 0)
        {
            this.motionZ = -this.motionZ;
            this.motionYaw -= 10.0D;
        }
        else if (this.motionZ < 0.0D && this.worldObj.getBlockId(i, j, k - 1) != 0)
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

    private double openSpace(float rotationYawOffset, float rotationPitchOffset)
    {
        float yaw = this.rotationYaw + rotationYawOffset;
        float pitch = this.rotationYaw + rotationYawOffset;
        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        float f3 = MathHelper.cos(-yaw * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-yaw * 0.01745329F - (float)Math.PI);
        float f5 = MathHelper.cos(-pitch * 0.01745329F);
        float f6 = MathHelper.sin(-pitch * 0.01745329F);
        float f7 = f4 * f5;
        float f9 = f3 * f5;
        double d3 = 50.0D;
        Vec3 vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = this.worldObj.clip(vec3d, vec3d1);

        if (movingobjectposition == null)
        {
            return 50.0D;
        }
        else if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            double i = (double)movingobjectposition.blockX - this.posX;
            double j = (double)movingobjectposition.blockY - this.posY;
            double k = (double)movingobjectposition.blockZ - this.posZ;
            return Math.sqrt(i * i + j * j + k * k);
        }
        else
        {
            return 50.0D;
        }
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

            if (distanceTravelled < 3.0D && !this.isStuckWarning)
            {
                this.isStuckWarning = true;
            }

            this.checkX = this.posX;
            this.checkY = this.posY;
            this.checkZ = this.posZ;
            this.checkTime = curtime;
        }
    }

    private boolean isCourseTraversable(double d, double d1, double d2, double d3)
    {
        double d4 = (this.waypointX - this.posX) / d3;
        double d6 = (this.waypointZ - this.posZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; (double)i < d3; ++i)
        {
            axisalignedbb.offset(d4, this.posY, d6);

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
        return "aether:aemob.aerwhale.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "aether:aemob.aerwhale.die";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "aether:aemob.aerwhale.die";
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
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        return this.rand.nextInt(65) == 0 && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID;
    }
}
