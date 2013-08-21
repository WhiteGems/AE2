package net.aetherteam.aether.entities.altar;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityFakeItem extends EntityItem
{
    /**
     * The age of this EntityItem (used to animate it up and down as well as expire it)
     */
    public int age;
    public int delayBeforeCanPickup;

    /** The health of this EntityItem. (For example, damage for tools) */
    private int health;

    /** The EntityItem's random initial float height. */
    public float hoverStart;

    public EntityFakeItem(World par1World, double par2, double par4, double par6)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.rotationYaw = 0.0F;
    }

    public EntityFakeItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack)
    {
        this(par1World, par2, par4, par6);
        this.setEntityItemStack(par8ItemStack);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public EntityFakeItem(World par1World)
    {
        super(par1World);
        this.age = 0;
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit()
    {
        this.getDataWatcher().addObjectByDataType(10, 5);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.noClip = true;
        ++this.age;
        ItemStack item = this.getDataWatcher().getWatchableObjectItemStack(10);

        if (item != null && item.stackSize <= 0)
        {
            this.setDead();
        }
    }

    /**
     * Looks for other itemstacks nearby and tries to stack them together
     */
    private void searchForOtherItemsNearby()
    {
        Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityFakeItem.class, this.boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

        while (iterator.hasNext())
        {
            EntityFakeItem entityitem = (EntityFakeItem)iterator.next();
            this.combineItems(entityitem);
        }
    }

    public boolean combineItems(EntityFakeItem par1EntityItem)
    {
        if (par1EntityItem == this)
        {
            return false;
        }
        else if (par1EntityItem.isEntityAlive() && this.isEntityAlive())
        {
            ItemStack itemstack = this.getEntityItem();
            ItemStack itemstack1 = par1EntityItem.getEntityItem();

            if (itemstack1.getItem() != itemstack.getItem())
            {
                return false;
            }
            else if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound())
            {
                return false;
            }
            else if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
            {
                return false;
            }
            else if (itemstack1.getItem().getHasSubtypes() && itemstack1.getItemDamage() != itemstack.getItemDamage())
            {
                return false;
            }
            else if (itemstack1.stackSize < itemstack.stackSize)
            {
                return par1EntityItem.combineItems(this);
            }
            else if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
            {
                return false;
            }
            else
            {
                itemstack1.stackSize += itemstack.stackSize;
                par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, this.delayBeforeCanPickup);
                par1EntityItem.age = Math.min(par1EntityItem.age, this.age);
                par1EntityItem.setEntityItemStack(itemstack1);
                this.setDead();
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * sets the age of the item so that it'll despawn one minute after it has been dropped (instead of five). Used when
     * items are dropped from players in creative mode
     */
    public void setAgeToCreativeDespawnTime()
    {
        this.age = 4800;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int par1)
    {
        this.attackEntityFrom(DamageSource.inFire, par1);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (this.getEntityItem() != null && this.getEntityItem().itemID == Item.netherStar.itemID && par1DamageSource.isExplosion())
        {
            return false;
        }
        else
        {
            this.setBeenAttacked();
            this.health -= par2;

            if (this.health <= 0)
            {
                this.setDead();
            }

            return false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)((byte)this.health));
        par1NBTTagCompound.setShort("Age", (short)this.age);

        if (this.getEntityItem() != null)
        {
            par1NBTTagCompound.setCompoundTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.setDead();
        this.health = par1NBTTagCompound.getShort("Health") & 255;
        this.age = par1NBTTagCompound.getShort("Age");
        NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("Item");
        this.setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        ItemStack item = this.getDataWatcher().getWatchableObjectItemStack(10);

        if (item == null || item.stackSize <= 0)
        {
            this.setDead();
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {}

    /**
     * Gets the username of the entity.
     */
    public String getEntityName()
    {
        return StatCollector.translateToLocal("item." + this.getEntityItem().getItemName());
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return false;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int par1)
    {
        super.travelToDimension(par1);

        if (!this.worldObj.isRemote)
        {
            this.searchForOtherItemsNearby();
        }
    }

    /**
     * Returns the ItemStack corresponding to the Entity (Note: if no item exists, will log an error but still return an
     * ItemStack containing Block.stone)
     */
    public ItemStack getEntityItem()
    {
        ItemStack itemstack = this.getDataWatcher().getWatchableObjectItemStack(10);

        if (itemstack == null)
        {
            if (this.worldObj != null)
            {
                this.worldObj.getWorldLogAgent().logSevere("Item entity " + this.entityId + " has no item?!");
            }

            return new ItemStack(Block.stone);
        }
        else
        {
            return itemstack;
        }
    }

    /**
     * Sets the ItemStack for this entity
     */
    public void setEntityItemStack(ItemStack par1ItemStack)
    {
        this.getDataWatcher().updateObject(10, par1ItemStack);
        this.getDataWatcher().setObjectWatched(10);
    }
}
