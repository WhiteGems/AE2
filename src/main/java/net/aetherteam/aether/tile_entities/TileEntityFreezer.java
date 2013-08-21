package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.AetherFrozen;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFreezer extends TileEntity implements IInventory
{
    private static List<AetherFrozen> frozen = new ArrayList();
    private ItemStack[] frozenItemStacks = new ItemStack[3];
    public int frozenProgress = 0;
    public int frozenPowerRemaining = 0;
    public int frozenTimeForItem = 0;
    private AetherFrozen currentFrozen;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.frozenItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.frozenItemStacks[i];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.frozenItemStacks[i] != null)
        {
            ItemStack itemstack1;

            if (this.frozenItemStacks[i].stackSize <= j)
            {
                itemstack1 = this.frozenItemStacks[i];
                this.frozenItemStacks[i] = null;
                return itemstack1;
            }
            else
            {
                itemstack1 = this.frozenItemStacks[i].splitStack(j);

                if (this.frozenItemStacks[i].stackSize == 0)
                {
                    this.frozenItemStacks[i] = null;
                }

                return itemstack1;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.frozenItemStacks[par1] != null)
        {
            ItemStack var2 = this.frozenItemStacks[par1];
            this.frozenItemStacks[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.frozenItemStacks[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Freezer";
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.frozenItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < this.frozenItemStacks.length)
            {
                this.frozenItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.frozenProgress = nbttagcompound.getShort("BurnTime");
        this.frozenTimeForItem = nbttagcompound.getShort("CookTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.frozenProgress);
        nbttagcompound.setShort("CookTime", (short)this.frozenTimeForItem);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.frozenItemStacks.length; ++i)
        {
            if (this.frozenItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.frozenItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i)
    {
        return this.frozenTimeForItem == 0 ? 0 : this.frozenProgress * i / this.frozenTimeForItem;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        return this.frozenPowerRemaining * i / 500;
    }

    public boolean isBurning()
    {
        return this.frozenPowerRemaining > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (this.frozenPowerRemaining > 0)
        {
            --this.frozenPowerRemaining;

            if (this.currentFrozen != null)
            {
                ++this.frozenProgress;
            }
        }

        if (this.currentFrozen != null && (this.frozenItemStacks[0] == null || this.frozenItemStacks[0].itemID != this.currentFrozen.frozenFrom.itemID))
        {
            this.currentFrozen = null;
            this.frozenProgress = 0;
        }

        if (this.currentFrozen != null && this.frozenProgress >= this.currentFrozen.frozenPowerNeeded)
        {
            if (!this.worldObj.isRemote)
            {
                if (this.frozenItemStacks[2] == null)
                {
                    this.setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), 1, this.currentFrozen.frozenTo.getItemDamage()));
                }
                else
                {
                    this.setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), this.getStackInSlot(2).stackSize + 1, this.currentFrozen.frozenTo.getItemDamage()));
                }

                if (this.getStackInSlot(0).itemID != Item.bucketWater.itemID && this.getStackInSlot(0).itemID != Item.bucketLava.itemID)
                {
                    if (this.getStackInSlot(0).itemID == AetherItems.SkyrootBucket.itemID)
                    {
                        this.setInventorySlotContents(0, new ItemStack(AetherItems.SkyrootBucket));
                    }
                    else
                    {
                        this.decrStackSize(0, 1);
                    }
                }
                else
                {
                    this.setInventorySlotContents(0, new ItemStack(Item.bucketEmpty));
                }
            }

            this.frozenProgress = 0;
            this.currentFrozen = null;
            this.frozenTimeForItem = 0;
        }

        if (this.frozenPowerRemaining <= 0 && this.currentFrozen != null && this.getStackInSlot(1) != null && this.getStackInSlot(1).itemID == AetherBlocks.Icestone.blockID)
        {
            this.frozenPowerRemaining += 500;

            if (!this.worldObj.isRemote)
            {
                this.decrStackSize(1, 1);
            }
        }

        if (this.currentFrozen == null)
        {
            ItemStack itemstack = this.getStackInSlot(0);

            for (int i = 0; i < frozen.size(); ++i)
            {
                if (itemstack != null && frozen.get(i) != null && itemstack.itemID == ((AetherFrozen)frozen.get(i)).frozenFrom.itemID && itemstack.getItemDamage() == ((AetherFrozen)frozen.get(i)).frozenFrom.getItemDamage())
                {
                    if (this.frozenItemStacks[2] == null)
                    {
                        this.currentFrozen = (AetherFrozen)frozen.get(i);
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    }
                    else if (this.frozenItemStacks[2].itemID == ((AetherFrozen)frozen.get(i)).frozenTo.itemID && ((AetherFrozen)frozen.get(i)).frozenTo.getItem().getItemStackLimit() > this.frozenItemStacks[2].stackSize)
                    {
                        this.currentFrozen = (AetherFrozen)frozen.get(i);
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    }
                }
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public static void addFreezable(ItemStack from, ItemStack to, int i)
    {
        frozen.add(new AetherFrozen(from, to, i));
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        this.readFromNBT(pkt.customParam1);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return false;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
