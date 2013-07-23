package net.aetherteam.aether.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.entities.ai.EntityAIEatAetherGrass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCloth;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySheepuff extends EntityAetherAnimal
{
    private EntityAIEatAetherGrass aiEatGrass = new EntityAIEatAetherGrass(this);
    private int sheepTimer;
    public static final float[][] fleeceColorTable = { { 1.0F, 1.0F, 1.0F }, { 0.975F, 0.85F, 0.6F }, { 0.95F, 0.75F, 0.925F }, { 0.8F, 0.85F, 0.975F }, { 0.95F, 0.95F, 0.6F }, { 0.75F, 0.9F, 0.55F }, { 0.975F, 0.85F, 0.9F }, { 0.65F, 0.65F, 0.65F }, { 0.8F, 0.8F, 0.8F }, { 0.65F, 0.8F, 0.85F }, { 0.85F, 0.7F, 0.95F }, { 0.6F, 0.7F, 0.9F }, { 0.75F, 0.7F, 0.65F }, { 0.7F, 0.75F, 0.6F }, { 0.9F, 0.65F, 0.65F }, { 0.55F, 0.55F, 0.55F } };
    private int amountEaten;

    public EntitySheepuff(World world)
    {
        super(world);
        this.texture = (this.dir + "/mobs/sheepuff/sheepuff.png");
        setSize(0.9F, 1.3F);
        setFleeceColor(getRandomFleeceColor(this.rand));
        float f = 0.23F;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        this.tasks.addTask(2, new EntityAIMate(this, f));
        this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.itemID, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0.25F));
        this.tasks.addTask(5, this.aiEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, f));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.amountEaten = 0;
    }

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

    protected void dropFewItems(boolean var1, int var2)
    {
        if (!getSheared())
        {
            entityDropItem(new ItemStack(Block.cloth.blockID, 1 + this.rand.nextInt(2), getFleeceColor()), 0.0F);
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

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if ((itemstack != null) && (isWheat(itemstack)))
        {
            return super.interact(entityplayer);
        }

        if ((itemstack != null) && (itemstack.itemID == Item.shears.itemID) && (!getSheared()))
        {
            if (!this.worldObj.isRemote)
            {
                if (getPuffed())
                {
                    setPuffed(false);
                    int i = 2 + this.rand.nextInt(3);

                    for (int j = 0; j < i; j++)
                    {
                        EntityItem entityitem = entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 1.0F);
                        entityitem.motionY += this.rand.nextFloat() * 0.05F;
                        entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                        entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                    }
                }
                else
                {
                    setSheared(true);
                    int i = 2 + this.rand.nextInt(3);

                    for (int j = 0; j < i; j++)
                    {
                        EntityItem entityitem = entityDropItem(new ItemStack(Block.cloth.blockID, 1, getFleeceColor()), 1.0F);
                        entityitem.motionY += this.rand.nextFloat() * 0.05F;
                        entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                        entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
                    }
                }
            }

            itemstack.damageItem(1, entityplayer);
        }

        if ((itemstack != null) && (itemstack.itemID == Item.dyePowder.itemID) && (!getSheared()))
        {
            int colour = BlockCloth.getBlockFromDye(itemstack.getItemDamage());

            if (getFleeceColor() != colour)
            {
                if ((getPuffed()) && (itemstack.stackSize >= 2))
                {
                    setFleeceColor(colour);
                    itemstack.stackSize -= 2;
                }
                else if (!getPuffed())
                {
                    setFleeceColor(colour);
                    itemstack.stackSize -= 1;
                }
            }
        }

        return false;
    }

    protected void jump()
    {
        if (getPuffed())
        {
            this.motionY = 1.8D;
            this.motionX += this.rand.nextGaussian() * 0.5D;
            this.motionZ += this.rand.nextGaussian() * 0.5D;
        }
        else
        {
            this.motionY = 0.4199999868869782D;
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
        return this.sheepTimer < 4 ? (this.sheepTimer - par1) / 4.0F : (this.sheepTimer >= 4) && (this.sheepTimer <= 36) ? 1.0F : this.sheepTimer <= 0 ? 0.0F : -(this.sheepTimer - 40 - par1) / 4.0F;
    }

    @SideOnly(Side.CLIENT)
    public float func_70890_k(float par1)
    {
        if ((this.sheepTimer > 4) && (this.sheepTimer <= 36))
        {
            float f1 = (this.sheepTimer - 4 - par1) / 32.0F;
            return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f1 * 28.700001F);
        }

        return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        if (getPuffed())
        {
            this.fallDistance = 0.0F;

            if (this.motionY < -0.05D)
            {
                this.motionY = -0.05D;
            }
        }

        if (this.onGround)
        {
            setPuffed(false);
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Sheared", getSheared());
        nbttagcompound.setBoolean("Puffed", getPuffed());
        nbttagcompound.setByte("Color", (byte)getFleeceColor());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        setSheared(nbttagcompound.getBoolean("Sheared"));
        setPuffed(nbttagcompound.getBoolean("Puffed"));
        setFleeceColor(nbttagcompound.getByte("Color"));
    }

    public void eatGrassBonus()
    {
        setSheared(false);
        setPuffed(true);
        jump();

        if (isChild())
        {
            int i = getGrowingAge() + 1200;

            if (i > 0)
            {
                i = 0;
            }

            setGrowingAge(i);
        }
    }

    protected String getLivingSound()
    {
        return "mob.sheep.say";
    }

    protected String getHurtSound()
    {
        return "mob.sheep.say";
    }

    protected String getDeathSound()
    {
        return "mob.sheep.say";
    }

    public int getFleeceColor()
    {
        return this.dataWatcher.getWatchableObjectByte(16) & 0xF;
    }

    public void setFleeceColor(int i)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 0xF0 | i & 0xF)));
    }

    public boolean getSheared()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x10) != 0;
    }

    public void setSheared(boolean flag)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);

        if (flag)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 0x10)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 0xFFFFFFEF)));
        }
    }

    public boolean getPuffed()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x20) != 0;
    }

    public void setPuffed(boolean flag)
    {
        byte byte0 = this.dataWatcher.getWatchableObjectByte(16);

        if (flag)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 0x20)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & 0xFFFFFFDF)));
        }
    }

    public static int getRandomFleeceColor(Random random)
    {
        int i = random.nextInt(100);

        if (i < 5)
        {
            return 3;
        }

        if (i < 10)
        {
            return 9;
        }

        if (i < 15)
        {
            return 5;
        }

        if (i < 18)
        {
            return 6;
        }

        return random.nextInt(500) != 0 ? 0 : 10;
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        return this;
    }
}

