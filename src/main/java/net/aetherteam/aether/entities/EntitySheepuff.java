package net.aetherteam.aether.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.entities.ai.EntityAIEatAetherGrass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
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
    private EntityAIEatAetherGrass aiEatGrass = new EntityAIEatAetherGrass(this);
    private int sheepTimer;
    public static final float[][] fleeceColorTable = new float[][] {{1.0F, 1.0F, 1.0F}, {0.975F, 0.85F, 0.6F}, {0.95F, 0.75F, 0.925F}, {0.8F, 0.85F, 0.975F}, {0.95F, 0.95F, 0.6F}, {0.75F, 0.9F, 0.55F}, {0.975F, 0.85F, 0.9F}, {0.65F, 0.65F, 0.65F}, {0.8F, 0.8F, 0.8F}, {0.65F, 0.8F, 0.85F}, {0.85F, 0.7F, 0.95F}, {0.6F, 0.7F, 0.9F}, {0.75F, 0.7F, 0.65F}, {0.7F, 0.75F, 0.6F}, {0.9F, 0.65F, 0.65F}, {0.55F, 0.55F, 0.55F}};
    private int amountEaten;

    public EntitySheepuff(World world)
    {
        super(world);
        this.setSize(0.9F, 1.3F);
        this.setFleeceColor(getRandomFleeceColor(this.rand));
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        this.setEntityHealth(20.0F);
        float f = 0.23F;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.3799999952316284D));
        this.tasks.addTask(2, new EntityAIMate(this, (double)f));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25D, Item.wheat.itemID, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0.25D));
        this.tasks.addTask(5, this.aiEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, (double)f));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.amountEaten = 0;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    protected void updateAITasks()
    {
        this.sheepTimer = this.aiEatGrass.getEatGrassTick();
        super.updateAITasks();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
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

    public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntitySheepuff(this.worldObj);
    }

    public boolean isWheat(ItemStack itemstack)
    {
        return itemstack.itemID == Item.wheat.itemID;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && this.isWheat(itemstack))
        {
            return super.interact(entityplayer);
        }
        else
        {
            int colour;

            if (itemstack != null && itemstack.itemID == Item.shears.itemID && !this.getSheared())
            {
                if (!this.worldObj.isRemote)
                {
                    int j;
                    EntityItem entityitem;

                    if (this.getPuffed())
                    {
                        this.setPuffed(false);
                        colour = 2 + this.rand.nextInt(3);

                        for (j = 0; j < colour; ++j)
                        {
                            entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
                            entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
                            entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                            entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        }
                    }
                    else
                    {
                        this.setSheared(true);
                        colour = 2 + this.rand.nextInt(3);

                        for (j = 0; j < colour; ++j)
                        {
                            entityitem = this.entityDropItem(new ItemStack(Block.cloth.blockID, 1, this.getFleeceColor()), 1.0F);
                            entityitem.motionY += (double)(this.rand.nextFloat() * 0.05F);
                            entityitem.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                            entityitem.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                        }
                    }
                }

                itemstack.damageItem(1, entityplayer);
            }

            if (itemstack != null && itemstack.itemID == Item.dyePowder.itemID && !this.getSheared())
            {
                colour = BlockColored.getBlockFromDye(itemstack.getItemDamage());

                if (this.getFleeceColor() != colour)
                {
                    if (this.getPuffed() && itemstack.stackSize >= 2)
                    {
                        this.setFleeceColor(colour);
                        itemstack.stackSize -= 2;
                    }
                    else if (!this.getPuffed())
                    {
                        this.setFleeceColor(colour);
                        --itemstack.stackSize;
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
        }
        else
        {
            this.motionY = 0.41999998688697815D;
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 10)
        {
            this.sheepTimer = 40;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    @SideOnly(Side.CLIENT)
    public float func_70894_j(float par1)
    {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float)this.sheepTimer - par1) / 4.0F : -((float)(this.sheepTimer - 40) - par1) / 4.0F));
    }

    @SideOnly(Side.CLIENT)
    public float func_70890_k(float par1)
    {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36)
        {
            float f1 = ((float)(this.sheepTimer - 4) - par1) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f1 * 28.7F);
        }
        else
        {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        if (this.getPuffed())
        {
            this.fallDistance = 0.0F;

            if (this.motionY < -0.05D)
            {
                this.motionY = -0.05D;
            }
        }

        if (this.onGround)
        {
            this.setPuffed(false);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Sheared", this.getSheared());
        nbttagcompound.setBoolean("Puffed", this.getPuffed());
        nbttagcompound.setByte("Color", (byte)this.getFleeceColor());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        this.setSheared(nbttagcompound.getBoolean("Sheared"));
        this.setPuffed(nbttagcompound.getBoolean("Puffed"));
        this.setFleeceColor(nbttagcompound.getByte("Color"));
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
        this.setSheared(false);
        this.setPuffed(true);
        this.jump();

        if (this.isChild())
        {
            int i = this.getGrowingAge() + 1200;

            if (i > 0)
            {
                i = 0;
            }

            this.setGrowingAge(i);
        }
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

    public void setFleeceColor(int i)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 240 | i & 15)));
    }

    public boolean getSheared()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
    }

    public void setSheared(boolean flag)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);

        if (flag)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 16)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -17)));
        }
    }

    public boolean getPuffed()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 32) != 0;
    }

    public void setPuffed(boolean flag)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);

        if (flag)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 32)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -33)));
        }
    }

    public static int getRandomFleeceColor(Random random)
    {
        int i = random.nextInt(100);
        return i < 5 ? 3 : (i < 10 ? 9 : (i < 15 ? 5 : (i < 18 ? 6 : (random.nextInt(500) != 0 ? 0 : 10))));
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return this;
    }
}
