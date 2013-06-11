package net.aetherteam.aether.entities;

import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.entities.mounts_old.RidingHandlerParachute;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityCloudParachute extends Entity
{
    public static Random rand;
    public RidingHandler ridinghandler;
    private static final double ANIM_RADIUS = 0.75D;

    public EntityCloudParachute(World var1)
    {
        super(var1);
        this.setSize(1.0F, 1.0F);
        rand = new Random();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.ridinghandler = new RidingHandlerParachute(this);
        this.ignoreFrustumCheck = true;
    }

    public EntityCloudParachute(World var1, EntityPlayer var2, boolean var3)
    {
        this(var1);

        if (var2 != null)
        {
            this.setPositionAndRotation(var2.posX, var2.posY, var2.posZ, this.rotationYaw, this.rotationPitch);
            this.getRidingHandler().setRider(var2);
            this.setColor(var3);
        }
    }

    public static boolean entityHasRoomForCloud(World var0, EntityLiving var1)
    {
        AxisAlignedBB var2 = AxisAlignedBB.getBoundingBox(var1.posX - 0.5D, var1.boundingBox.minY - 1.0D, var1.posZ - 0.5D, var1.posX + 0.5D, var1.boundingBox.minY, var1.posZ + 0.5D);
        return var0.getCollidingBoundingBoxes(var1, var2).size() == 0 && !var0.isAABBInMaterial(var2, Material.water);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    public boolean getColor()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setColor(boolean var1)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var1 ? 1 : 0)));
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double var1)
    {
        return this.getRidingHandler().getRider() != null ? this.getRidingHandler().getRider().isInRangeToRenderDist(var1) : super.isInRangeToRenderDist(var1);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        if (this.getRidingHandler() != null && this.getRidingHandler().getRider() != null)
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY, this.posZ, rand, Double.valueOf(0.75D));
        }

        if (this.isCollided())
        {
            this.die();
        }

        super.onEntityUpdate();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        if (super.interact(var1))
        {
            return super.interact(var1);
        } else
        {
            if (this.riddenByEntity == null && !this.getRidingHandler().isBeingRidden())
            {
                this.getRidingHandler().setRider(var1);
            } else
            {
                this.getRidingHandler().onUnMount();
            }

            return true;
        }
    }

    public void die()
    {
        this.getRidingHandler().onUnMount();

        for (int var1 = 0; var1 < 32; ++var1)
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY, this.posZ, rand, Double.valueOf(0.75D));
        }

        this.isDead = true;
    }

    private boolean isCollided()
    {
        return this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0 || this.worldObj.isAABBInMaterial(this.boundingBox, Material.water);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1)
    {}

    public RidingHandler getRidingHandler()
    {
        return this.ridinghandler;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound var1)
    {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound var1)
    {}
}
