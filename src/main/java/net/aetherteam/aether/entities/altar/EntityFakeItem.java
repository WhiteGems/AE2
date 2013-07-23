package net.aetherteam.aether.entities.altar;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.logging.ILogAgent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityFakeItem extends EntityItem
{
    public int a;
    public int b;
    private int health;
    public float c;

    public EntityFakeItem(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = ((float)(Math.random() * Math.PI * 2.0D));
        setSize(0.25F, 0.25F);
        this.yOffset = (this.height / 2.0F);
        setPosition(par2, par4, par6);
        this.rotationYaw = 0.0F;
    }

    public EntityFakeItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        this(par1World, par2, par4, par6);
        setEntityItemStack(par8ItemStack);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityFakeItem(World par1World)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = ((float)(Math.random() * Math.PI * 2.0D));
        setSize(0.25F, 0.25F);
        this.yOffset = (this.height / 2.0F);
    }

    protected void entityInit()
    {
        getDataWatcher().addObjectByDataType(10, 5);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.noClip = true;
        this.age += 1;
        ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

        if ((item != null) && (item.stackSize <= 0))
        {
            setDead();
        }
    }

    private void searchForOtherItemsNearby()
    {
        Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityFakeItem.class, this.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

        while (iterator.hasNext())
        {
            EntityFakeItem entityitem = (EntityFakeItem)iterator.next();
            combineItems(entityitem);
        }
    }

    public boolean combineItems(EntityFakeItem par1EntityItem)
    {
        if (par1EntityItem == this)
        {
            return false;
        }

        if ((par1EntityItem.isEntityAlive()) && (isEntityAlive()))
        {
            ItemStack itemstack = getEntityItem();
            ItemStack itemstack1 = par1EntityItem.getEntityItem();

            if (itemstack1.getItem() != itemstack.getItem())
            {
                return false;
            }

            if ((itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()))
            {
                return false;
            }

            if ((itemstack1.hasTagCompound()) && (!itemstack1.getTagCompound().equals(itemstack.getTagCompound())))
            {
                return false;
            }

            if ((itemstack1.getItem().getHasSubtypes()) && (itemstack1.getItemDamage() != itemstack.getItemDamage()))
            {
                return false;
            }

            if (itemstack1.stackSize < itemstack.stackSize)
            {
                return par1EntityItem.combineItems(this);
            }

            if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
            {
                return false;
            }

            itemstack1.stackSize += itemstack.stackSize;
            par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, this.delayBeforeCanPickup);
            par1EntityItem.age = Math.min(par1EntityItem.age, this.age);
            par1EntityItem.setEntityItemStack(itemstack1);
            setDead();
            return true;
        }

        return false;
    }

    public void setAgeToCreativeDespawnTime()
    {
        this.age = 4800;
    }

    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

    protected void dealFireDamage(int par1)
    {
        attackEntityFrom(DamageSource.inFire, par1);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }

        if ((getEntityItem() != null) && (getEntityItem().itemID == Item.netherStar.itemID) && (par1DamageSource.isExplosion()))
        {
            return false;
        }

        setBeenAttacked();
        this.health -= par2;

        if (this.health <= 0)
        {
            setDead();
        }

        return false;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)(byte)this.health);
        par1NBTTagCompound.setShort("Age", (short)this.age);

        if (getEntityItem() != null)
        {
            par1NBTTagCompound.setCompoundTag("Item", getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setDead();
        this.health = (par1NBTTagCompound.getShort("Health") & 0xFF);
        this.age = par1NBTTagCompound.getShort("Age");
        NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("Item");
        setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        ItemStack item = getDataWatcher().getWatchableObjectItemStack(10);

        if ((item == null) || (item.stackSize <= 0))
        {
            setDead();
        }
    }

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
    }

    public String getEntityName()
    {
        return StatCollector.translateToLocal("item." + getEntityItem().getItemName());
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    public void travelToDimension(int par1)
    {
        super.travelToDimension(par1);

        if (!this.worldObj.isRemote)
        {
            searchForOtherItemsNearby();
        }
    }

    public ItemStack getEntityItem()
    {
        ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);

        if (itemstack == null)
        {
            if (this.worldObj != null)
            {
                this.worldObj.X().logSevere("Item entity " + this.entityId + " has no item?!");
            }

            return new ItemStack(Block.stone);
        }

        return itemstack;
    }

    public void setEntityItemStack(ItemStack par1ItemStack)
    {
        getDataWatcher().updateObject(10, par1ItemStack);
        getDataWatcher().setObjectWatched(10);
    }
}

