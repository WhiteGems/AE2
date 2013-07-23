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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityIncubator extends TileEntity implements IInventory, ISidedInventory
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
    public ItemStack getStackInSlot(int var1)
    {
        return this.IncubatorItemStacks[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.IncubatorItemStacks[var1] != null)
        {
            ItemStack var3;

            if (this.IncubatorItemStacks[var1].stackSize <= var2)
            {
                var3 = this.IncubatorItemStacks[var1];
                this.IncubatorItemStacks[var1] = null;
                return var3;
            }
            else
            {
                var3 = this.IncubatorItemStacks[var1].splitStack(var2);

                if (this.IncubatorItemStacks[var1].stackSize == 0)
                {
                    this.IncubatorItemStacks[var1] = null;
                }

                return var3;
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
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.IncubatorItemStacks[var1] != null)
        {
            ItemStack var2 = this.IncubatorItemStacks[var1];
            this.IncubatorItemStacks[var1] = null;
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
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.IncubatorItemStacks[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
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
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("Items");
        this.IncubatorItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.IncubatorItemStacks.length)
            {
                this.IncubatorItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.progress = var1.getShort("BurnTime");
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setShort("BurnTime", (short)this.progress);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.IncubatorItemStacks.length; ++var3)
        {
            if (this.IncubatorItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.IncubatorItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);
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
    public int getCookProgressScaled(int var1)
    {
        return this.progress * var1 / this.ticksRequired;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int var1)
    {
        return this.torchPower * var1 / 500;
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
                EntityMoa var1 = new EntityMoa(this.worldObj, true, false, false, AetherMoaColour.getColour(this.IncubatorItemStacks[1].getItemDamage()), this.playerUsing);
                var1.setPosition((double)this.xCoord + 0.5D, (double)this.yCoord + 1.5D, (double)this.zCoord + 0.5D);
                this.worldObj.spawnEntityInWorld(var1);
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
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        this.playerUsing = var1;
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public int getStartInventorySide(ForgeDirection var1)
    {
        return var1 == ForgeDirection.DOWN ? 1 : (var1 == ForgeDirection.UP ? 0 : 2);
    }

    public int getSizeInventorySide(ForgeDirection var1)
    {
        return 1;
    }

    public void onDataPacket(INetworkManager var1, Packet132TileEntityData var2)
    {
        this.readFromNBT(var2.customParam1);
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
    public boolean isStackValidForSlot(int var1, ItemStack var2)
    {
        return false;
    }
}
