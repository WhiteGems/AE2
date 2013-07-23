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

    public EntityFloatingBlock(World var1)
    {
        super(var1);
        this.flytime = 0;
    }

    public EntityFloatingBlock(World var1, double var2, double var4, double var6, int var8, int var9)
    {
        super(var1);
        this.flytime = 0;
        this.setBlockID(var8);
        this.setMetadata(var9);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(var2, var4, var6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = var2;
        this.prevPosY = var4;
        this.prevPosZ = var6;
    }

    public EntityFloatingBlock(World var1, double var2, double var4, double var6, int var8)
    {
        this(var1, var2, var4, var6, var8, 0);
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

    public void setBlockID(int var1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(var1));
    }

    public int getMetadata()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setMetadata(int var1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(var1));
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
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (this.worldObj.getBlockId(var1, var2, var3) == this.getBlockID() || this.worldObj.getBlockId(var1, var2, var3) == AetherBlocks.AetherGrass.blockID && this.getBlockID() == AetherBlocks.AetherDirt.blockID)
            {
                this.worldObj.setBlock(var1, var2, var3, 0);
            }

            List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 1.0D, 0.0D));

            for (int var5 = 0; var5 < var4.size(); ++var5)
            {
                if (var4.get(var5) instanceof EntityFallingSand && Block.blocksList[this.getBlockID()].canPlaceBlockAt(this.worldObj, var1, var2, var3))
                {
                    this.worldObj.setBlock(var1, var2, var3, this.getBlockID(), this.getMetadata(), 2);
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

                if (!Block.blocksList[this.getBlockID()].canPlaceBlockAt(this.worldObj, var1, var2, var3) || BlockFloating.canFallAbove(this.worldObj, var1, var2 + 1, var3) || !this.worldObj.setBlock(var1, var2, var3, this.getBlockID(), this.getMetadata(), 2))
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
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("blockID", this.getBlockID());
        var1.setInteger("metadata", this.getMetadata());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.setBlockID(var1.getInteger("blockID"));
        this.setMetadata(var1.getInteger("metadata"));
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
