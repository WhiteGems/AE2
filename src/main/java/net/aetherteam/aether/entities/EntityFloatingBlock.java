package net.aetherteam.aether.entities;

import java.util.List;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.blocks.BlockFloating;
import net.minecraft.block.Block;
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
        this.setBlockID(id);
        this.setMetadata(meta);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(d, d1, d2);
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

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
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

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.getBlockID() == 0)
        {
            this.setDead();
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.flytime;
            this.motionY += 0.04D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);

            if (this.worldObj.getBlockId(i, j, k) == this.getBlockID() || this.worldObj.getBlockId(i, j, k) == AetherBlocks.AetherGrass.blockID && this.getBlockID() == AetherBlocks.AetherDirt.blockID)
            {
                this.worldObj.setBlock(i, j, k, 0);
            }

            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 1.0D, 0.0D));

            for (int stack = 0; stack < list.size(); ++stack)
            {
                if (list.get(stack) instanceof EntityFallingSand && Block.blocksList[this.getBlockID()].canPlaceBlockAt(this.worldObj, i, j, k))
                {
                    this.worldObj.setBlock(i, j, k, this.getBlockID(), this.getMetadata(), 2);
                    this.setDead();
                }
            }

            ItemStack var6;

            if (this.isCollidedVertically && !this.onGround)
            {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
                this.setDead();

                if (!Block.blocksList[this.getBlockID()].canPlaceBlockAt(this.worldObj, i, j, k) || BlockFloating.canFallAbove(this.worldObj, i, j + 1, k) || !this.worldObj.setBlock(i, j, k, this.getBlockID(), this.getMetadata(), 2))
                {
                    var6 = new ItemStack(this.getBlockID(), 1, this.getMetadata());

                    if (!this.worldObj.isRemote)
                    {
                        this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, var6));
                    }
                }
            }
            else if (this.flytime > 100)
            {
                var6 = new ItemStack(this.getBlockID(), 1, this.getMetadata());

                if (!this.worldObj.isRemote)
                {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, var6));
                }

                this.setDead();
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("blockID", this.getBlockID());
        nbttagcompound.setInteger("metadata", this.getMetadata());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.setBlockID(nbttagcompound.getInteger("blockID"));
        this.setMetadata(nbttagcompound.getInteger("metadata"));
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
