package net.aetherteam.aether.tile_entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.AetherMoaColour;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityIncubator extends TileEntity implements IInventory
{
    private ItemStack[] IncubatorItemStacks = new ItemStack[2];
    public int torchPower;
    public int progress = 0;
    public int ticksRequired = 6000;
    public EntityPlayer playerUsing;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.IncubatorItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.IncubatorItemStacks[i];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.IncubatorItemStacks[i] != null)
        {
            ItemStack itemstack1;

            if (this.IncubatorItemStacks[i].stackSize <= j)
            {
                itemstack1 = this.IncubatorItemStacks[i];
                this.IncubatorItemStacks[i] = null;
                return itemstack1;
            }
            else
            {
                itemstack1 = this.IncubatorItemStacks[i].splitStack(j);

                if (this.IncubatorItemStacks[i].stackSize == 0)
                {
                    this.IncubatorItemStacks[i] = null;
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
        if (this.IncubatorItemStacks[par1] != null)
        {
            ItemStack var2 = this.IncubatorItemStacks[par1];
            this.IncubatorItemStacks[par1] = null;
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
        this.IncubatorItemStacks[i] = itemstack;

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
        return "Incubator";
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.IncubatorItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < this.IncubatorItemStacks.length)
            {
                this.IncubatorItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.progress = nbttagcompound.getShort("BurnTime");
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)this.progress);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.IncubatorItemStacks.length; ++i)
        {
            if (this.IncubatorItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.IncubatorItemStacks[i].writeToNBT(nbttagcompound1);
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
        return this.progress * i / this.ticksRequired;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int i)
    {
        return this.torchPower * i / 500;
    }

    public boolean isBurning()
    {
        return this.torchPower > 0;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        if (this.torchPower > 0)
        {
            --this.torchPower;

            if (this.getStackInSlot(1) != null)
            {
                ++this.progress;
            }
        }

        if (this.IncubatorItemStacks[1] == null || this.IncubatorItemStacks[1].itemID != AetherItems.MoaEgg.itemID)
        {
            this.progress = 0;
        }

        if (this.progress >= this.ticksRequired)
        {
            if (this.IncubatorItemStacks[1] != null && !this.worldObj.isRemote)
            {
                EntityMoa moa = new EntityMoa(this.worldObj, true, false, false, AetherMoaColour.getColour(this.IncubatorItemStacks[1].getItemDamage()), this.playerUsing);
                moa.setPosition((double)this.xCoord + 0.5D, (double)this.yCoord + 1.5D, (double)this.zCoord + 0.5D);
                this.worldObj.spawnEntityInWorld(moa);
            }

            if (!this.worldObj.isRemote)
            {
                this.decrStackSize(1, 1);
            }

            this.progress = 0;
        }

        if (this.torchPower <= 0 && this.IncubatorItemStacks[1] != null && this.IncubatorItemStacks[1].itemID == AetherItems.MoaEgg.itemID && this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == AetherBlocks.AmbrosiumTorch.blockID)
        {
            this.torchPower += 1000;

            if (!this.worldObj.isRemote)
            {
                this.decrStackSize(0, 1);
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        this.playerUsing = par1EntityPlayer;
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
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
