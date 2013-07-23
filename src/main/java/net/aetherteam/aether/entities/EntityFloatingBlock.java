package net.aetherteam.aether.entities;

import java.util.List;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.blocks.BlockFloating;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFloatingBlock extends Entity
{
    public int flytime;

    public EntityFloatingBlock(World world)
    {
        super(world);
        this.flytime = 0;
    }

    public EntityFloatingBlock(World world, double d, double d1, double d2, int id, int meta)
    {
        super(world);
        this.flytime = 0;
        setBlockID(id);
        setMetadata(meta);
        this.preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
        this.yOffset = (this.height / 2.0F);
        setPosition(d, d1, d2);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = d;
        this.prevPosY = d1;
        this.prevPosZ = d2;
    }

    public EntityFloatingBlock(World world, double d, double d1, double d2, int id)
    {
        this(world, d, d1, d2, id, 0);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Integer.valueOf(0));
        this.dataWatcher.addObject(17, Integer.valueOf(0));
    }

    public int getBlockID()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    public void setBlockID(int blockID)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(blockID));
    }

    public int getMetadata()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setMetadata(int meta)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(meta));
    }

    public void setDead()
    {
        super.setDead();
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void onUpdate()
    {
        if (getBlockID() == 0)
        {
            setDead();
            return;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.flytime += 1;
        this.motionY += 0.04D;
        moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);

        if ((this.worldObj.getBlockId(i, j, k) == getBlockID()) || ((this.worldObj.getBlockId(i, j, k) == AetherBlocks.AetherGrass.blockID) && (getBlockID() == AetherBlocks.AetherDirt.blockID)))
        {
            this.worldObj.setBlock(i, j, k, 0);
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 1.0D, 0.0D));

        for (int n = 0; n < list.size(); n++)
        {
            if (((list.get(n) instanceof EntityFallingSand)) && (Block.blocksList[getBlockID()].canPlaceBlockAt(this.worldObj, i, j, k)))
            {
                this.worldObj.setBlock(i, j, k, getBlockID(), getMetadata(), 2);
                setDead();
            }
        }

        if ((this.isCollidedVertically) && (!this.onGround))
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
            setDead();

            if ((!Block.blocksList[getBlockID()].canPlaceBlockAt(this.worldObj, i, j, k)) || (BlockFloating.canFallAbove(this.worldObj, i, j + 1, k)) || (!this.worldObj.setBlock(i, j, k, getBlockID(), getMetadata(), 2)))
            {
                ItemStack stack = new ItemStack(getBlockID(), 1, getMetadata());

                if (!this.worldObj.isRemote)
                {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
                }
            }
        }
        else if (this.flytime > 100)
        {
            ItemStack stack = new ItemStack(getBlockID(), 1, getMetadata());

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
            }

            setDead();
        }
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("blockID", getBlockID());
        nbttagcompound.setInteger("metadata", getMetadata());
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setBlockID(nbttagcompound.getInteger("blockID"));
        setMetadata(nbttagcompound.getInteger("metadata"));
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public World getWorld()
    {
        return this.worldObj;
    }
}

