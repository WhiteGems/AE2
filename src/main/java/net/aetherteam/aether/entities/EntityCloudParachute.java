package net.aetherteam.aether.entities;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.entities.mounts_old.RidingHandlerParachute;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityCloudParachute extends EntityLiving
{
    public static Random rand;
    public RidingHandler ridinghandler;
    private static final double ANIM_RADIUS = 0.75D;

    public EntityCloudParachute(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
        rand = new Random();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.ridinghandler = new RidingHandlerParachute(this);
        this.ignoreFrustumCheck = true;
    }

    public EntityCloudParachute(World world, EntityPlayer player, boolean bool)
    {
        this(world);

        if (player != null)
        {
            this.setPositionAndRotation(player.posX, player.posY, player.posZ, this.rotationYaw, this.rotationPitch);
            this.getRidingHandler().setRider(player);
            this.setColor(bool);
        }
    }

    public static boolean entityHasRoomForCloud(World world, EntityLiving entityliving)
    {
        AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entityliving.posX - 0.5D, entityliving.boundingBox.minY - 1.0D, entityliving.posZ - 0.5D, entityliving.posX + 0.5D, entityliving.boundingBox.minY, entityliving.posZ + 0.5D);
        return world.getCollidingBoundingBoxes(entityliving, boundingBox).size() == 0 && !world.isAABBInMaterial(boundingBox, Material.water);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    public boolean getColor()
    {
        return this.dataWatcher.getWatchableObjectByte(16) == 1;
    }

    public void setColor(boolean b)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b ? 1 : 0)));
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        return this.getRidingHandler().getRider() != null ? this.getRidingHandler().getRider().isInRangeToRenderDist(d) : super.isInRangeToRenderDist(d);
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
    public boolean interact(EntityPlayer entityplayer)
    {
        if (super.interact(entityplayer))
        {
            return super.interact(entityplayer);
        }
        else
        {
            if (this.riddenByEntity == null && !this.getRidingHandler().isBeingRidden())
            {
                this.getRidingHandler().setRider(entityplayer);
            }
            else
            {
                this.getRidingHandler().onUnMount();
            }

            return true;
        }
    }

    public void die()
    {
        this.getRidingHandler().onUnMount();

        for (int i = 0; i < 32; ++i)
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
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    public RidingHandler getRidingHandler()
    {
        return this.ridinghandler;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1) {}
}
