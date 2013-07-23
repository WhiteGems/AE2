package net.aetherteam.aether.entities;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.aetherteam.aether.entities.mounts_old.RidingHandlerParachute;
import net.minecraft.block.material.Material;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityCloudParachute extends Entity
{
    public static Random ab;
    public RidingHandler ridinghandler;
    private static final double ANIM_RADIUS = 0.75D;

    public EntityCloudParachute(World world)
    {
        super(world);
        setSize(1.0F, 1.0F);
        Entity.rand = new Random();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.ridinghandler = new RidingHandlerParachute(this);
        this.ignoreFrustumCheck = true;
    }

    public EntityCloudParachute(World world, EntityPlayer player, boolean bool)
    {
        this(world);

        if (player == null)
        {
            return;
        }

        setPositionAndRotation(player.posX, player.posY, player.posZ, this.rotationYaw, this.rotationPitch);
        getRidingHandler().setRider(player);
        setColor(bool);
    }

    public static boolean entityHasRoomForCloud(World world, EntityLiving entityliving)
    {
        AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entityliving.posX - 0.5D, entityliving.boundingBox.minY - 1.0D, entityliving.posZ - 0.5D, entityliving.posX + 0.5D, entityliving.boundingBox.minY, entityliving.posZ + 0.5D);
        return (world.getCollidingBoundingBoxes(entityliving, boundingBox).size() == 0) && (!world.isAABBInMaterial(boundingBox, Material.water));
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

    public boolean isInRangeToRenderDist(double d)
    {
        if (getRidingHandler().getRider() != null)
        {
            return getRidingHandler().getRider().isInRangeToRenderDist(d);
        }

        return super.isInRangeToRenderDist(d);
    }

    public boolean canBeCollidedWith()
    {
        return false;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public void onEntityUpdate()
    {
        if ((getRidingHandler() != null) && (getRidingHandler().getRider() != null))
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY, this.posZ, Entity.rand, Double.valueOf(0.75D));
        }

        if (isCollided())
        {
            die();
        }

        super.onEntityUpdate();
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        if (super.interact(entityplayer))
        {
            return super.interact(entityplayer);
        }

        if ((this.riddenByEntity == null) && (!getRidingHandler().isBeingRidden()))
        {
            getRidingHandler().setRider(entityplayer);
        }
        else
        {
            getRidingHandler().onUnMount();
        }

        return true;
    }

    public void die()
    {
        getRidingHandler().onUnMount();

        for (int i = 0; i < 32; i++)
        {
            Aether.proxy.spawnCloudSmoke(this.worldObj, this.posX, this.posY, this.posZ, Entity.rand, Double.valueOf(0.75D));
        }

        this.isDead = true;
    }

    private boolean isCollided()
    {
        return (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0) || (this.worldObj.isAABBInMaterial(this.boundingBox, Material.water));
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
    }

    public RidingHandler getRidingHandler()
    {
        return this.ridinghandler;
    }

    protected void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound var1)
    {
    }
}

