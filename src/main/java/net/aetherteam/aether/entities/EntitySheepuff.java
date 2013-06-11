package net.aetherteam.aether.entities;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySheepuff extends EntityAetherAnimal
{
    public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.975F, 0.85F, 0.6F}, {0.95F, 0.75F, 0.925F}, {0.8F, 0.85F, 0.975F}, {0.95F, 0.95F, 0.6F}, {0.75F, 0.9F, 0.55F}, {0.975F, 0.85F, 0.9F}, {0.65F, 0.65F, 0.65F}, {0.8F, 0.8F, 0.8F}, {0.65F, 0.8F, 0.85F}, {0.85F, 0.7F, 0.95F}, {0.6F, 0.7F, 0.9F}, {0.75F, 0.7F, 0.65F}, {0.7F, 0.75F, 0.6F}, {0.9F, 0.65F, 0.65F}, {0.55F, 0.55F, 0.55F}};
    private int amountEaten;

    public EntitySheepuff(World var1)
    {
        super(var1);
        this.texture = this.dir + "/mobs/sheepuff/sheepuff.png";
        this.setSize(0.9F, 1.3F);
        this.setFleeceColor(getRandomFleeceColor(this.rand));
        this.amountEaten = 0;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean var1, int var2)
    {
        if (!this.getSheared())
        {
            this.entityDropItem(new ItemStack(Block.cloth.blockID, 1 + this.rand.nextInt(2), this.getFleeceColor()), 0.0F);
        }
    }

    public EntityAnimal spawnBabyAnimal(EntityAnimal var1)
    {
        return new EntitySheepuff(this.worldObj);
    }

    public boolean isWheat(ItemStack var1)
    {
        return var1.itemID == Item.wheat.itemID;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer var1)
    {
        ItemStack var2 = var1.inventory.getCurrentItem();

        if (var2 != null && this.isWheat(var2))
        {
            return super.interact(var1);
        } else
        {
            int var3;

            if (var2 != null && var2.itemID == Item.shears.itemID && !this.getSheared())
            {
                if (!this.worldObj.isRemote)
                {
                    int var4;
                    EntityItem var5;

                    if (this.getPuffed())
                    {
                        this.setPuffed(false);
                        var3 = 2 + this.rand.nextInt(3);

                        for (var4 = 0; var4 < var3; ++var4)
                        {
                            var5 = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
                            var5.motionY += (double) (this.rand.nextFloat() * 0.05F);
                            var5.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                            var5.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        }
                    } else
                    {
                        this.setSheared(true);
                        var3 = 2 + this.rand.nextInt(3);

                        for (var4 = 0; var4 < var3; ++var4)
                        {
                            var5 = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
                            var5.motionY += (double) (this.rand.nextFloat() * 0.05F);
                            var5.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                            var5.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        }
                    }
                }

                var2.damageItem(1, var1);
            }

            if (var2 != null && var2.itemID == Item.dyePowder.itemID && !this.getSheared())
            {
                var3 = BlockCloth.getBlockFromDye(var2.getItemDamage());

                if (this.getFleeceColor() != var3)
                {
                    if (this.getPuffed() && var2.stackSize >= 2)
                    {
                        this.setFleeceColor(var3);
                        var2.stackSize -= 2;
                    } else if (!this.getPuffed())
                    {
                        this.setFleeceColor(var3);
                        --var2.stackSize;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        if (this.getPuffed())
        {
            this.motionY = 1.8D;
            this.motionX += this.rand.nextGaussian() * 0.5D;
            this.motionZ += this.rand.nextGaussian() * 0.5D;
        } else
        {
            this.motionY = 0.41999998688697815D;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.getPuffed())
        {
            this.fallDistance = 0.0F;

            if (this.motionY < -0.05D)
            {
                this.motionY = -0.05D;
            }
        }

        if (this.rand.nextInt(100) == 0)
        {
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID)
            {
                this.worldObj.setBlock(var1, var2 - 1, var3, Block.dirt.blockID);
                ++this.amountEaten;
            }
        }

        if (this.amountEaten == 5 && !this.getSheared() && !this.getPuffed())
        {
            this.setPuffed(true);
            this.amountEaten = 0;
        }

        if (this.amountEaten == 10 && this.getSheared() && !this.getPuffed())
        {
            this.setSheared(false);
            this.setFleeceColor(0);
            this.amountEaten = 0;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setBoolean("Sheared", this.getSheared());
        var1.setBoolean("Puffed", this.getPuffed());
        var1.setByte("Color", (byte) this.getFleeceColor());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.setSheared(var1.getBoolean("Sheared"));
        this.setPuffed(var1.getBoolean("Puffed"));
        this.setFleeceColor(var1.getByte("Color"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.sheep.say";
    }

    public int getFleeceColor()
    {
        return this.dataWatcher.getWatchableObjectByte(16) & 15;
    }

    public void setFleeceColor(int var1)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & 240 | var1 & 15)));
    }

    public boolean getSheared()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    public void setSheared(boolean var1)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 16)));
        } else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -17)));
        }
    }

    public boolean getPuffed()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 32) != 0;
    }

    public void setPuffed(boolean var1)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (var1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 32)));
        } else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -33)));
        }
    }

    public static int getRandomFleeceColor(Random var0)
    {
        int var1 = var0.nextInt(100);
        return var1 < 5 ? 3 : (var1 < 10 ? 9 : (var1 < 15 ? 5 : (var1 < 18 ? 6 : (var0.nextInt(500) != 0 ? 0 : 10))));
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityAgeable createChild(EntityAgeable var1)
    {
        return this;
    }
}
