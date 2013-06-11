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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityFreezer extends TileEntity implements IInventory, ISidedInventory
{
    private static List frozen = new ArrayList();
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
    public ItemStack getStackInSlot(int var1)
    {
        return this.frozenItemStacks[var1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.frozenItemStacks[var1] != null)
        {
            ItemStack var3;

            if (this.frozenItemStacks[var1].stackSize <= var2)
            {
                var3 = this.frozenItemStacks[var1];
                this.frozenItemStacks[var1] = null;
                return var3;
            } else
            {
                var3 = this.frozenItemStacks[var1].splitStack(var2);

                if (this.frozenItemStacks[var1].stackSize == 0)
                {
                    this.frozenItemStacks[var1] = null;
                }

                return var3;
            }
        } else
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
        if (this.frozenItemStacks[var1] != null)
        {
            ItemStack var2 = this.frozenItemStacks[var1];
            this.frozenItemStacks[var1] = null;
            return var2;
        } else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.frozenItemStacks[var1] = var2;

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
        return "Freezer";
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);
        NBTTagList var2 = var1.getTagList("Items");
        this.frozenItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.frozenItemStacks.length)
            {
                this.frozenItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.frozenProgress = var1.getShort("BurnTime");
        this.frozenTimeForItem = var1.getShort("CookTime");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
        var1.setShort("BurnTime", (short) this.frozenProgress);
        var1.setShort("CookTime", (short) this.frozenTimeForItem);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.frozenItemStacks.length; ++var3)
        {
            if (this.frozenItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.frozenItemStacks[var3].writeToNBT(var4);
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
        return this.frozenTimeForItem == 0 ? 0 : this.frozenProgress * var1 / this.frozenTimeForItem;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int var1)
    {
        return this.frozenPowerRemaining * var1 / 500;
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
                } else
                {
                    this.setInventorySlotContents(2, new ItemStack(this.currentFrozen.frozenTo.getItem(), this.getStackInSlot(2).stackSize + 1, this.currentFrozen.frozenTo.getItemDamage()));
                }

                if (this.getStackInSlot(0).itemID != Item.bucketWater.itemID && this.getStackInSlot(0).itemID != Item.bucketLava.itemID)
                {
                    if (this.getStackInSlot(0).itemID == AetherItems.SkyrootBucket.itemID)
                    {
                        this.setInventorySlotContents(0, new ItemStack(AetherItems.SkyrootBucket));
                    } else
                    {
                        this.decrStackSize(0, 1);
                    }
                } else
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
            ItemStack var1 = this.getStackInSlot(0);

            for (int var2 = 0; var2 < frozen.size(); ++var2)
            {
                if (var1 != null && frozen.get(var2) != null && var1.itemID == ((AetherFrozen) frozen.get(var2)).frozenFrom.itemID && var1.getItemDamage() == ((AetherFrozen) frozen.get(var2)).frozenFrom.getItemDamage())
                {
                    if (this.frozenItemStacks[2] == null)
                    {
                        this.currentFrozen = (AetherFrozen) frozen.get(var2);
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    } else if (this.frozenItemStacks[2].itemID == ((AetherFrozen) frozen.get(var2)).frozenTo.itemID && ((AetherFrozen) frozen.get(var2)).frozenTo.getItem().getItemStackLimit() > this.frozenItemStacks[2].stackSize)
                    {
                        this.currentFrozen = (AetherFrozen) frozen.get(var2);
                        this.frozenTimeForItem = this.currentFrozen.frozenPowerNeeded;
                    }
                }
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    public static void addFreezable(ItemStack var0, ItemStack var1, int var2)
    {
        frozen.add(new AetherFrozen(var0, var1, var2));
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
