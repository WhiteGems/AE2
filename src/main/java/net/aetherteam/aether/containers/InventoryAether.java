package net.aetherteam.aether.containers;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.Collections;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryAether implements IInventory
{
    public ItemStack[] slots;
    public EntityPlayer player;

    public InventoryAether(EntityPlayer entityplayer)
    {
        this.player = entityplayer;
        this.slots = new ItemStack[8];
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public boolean isEmpty()
    {
        ItemStack[] arr$ = this.slots;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            ItemStack stack = arr$[i$];

            if (stack != null)
            {
                return false;
            }
        }

        return true;
    }

    public void closeChest() {}

    public void damageArmor(int i)
    {
        for (int j = 0; j < this.slots.length; ++j)
        {
            if (this.slots[j] != null && this.slots[j].getItem() instanceof IAetherAccessory)
            {
                this.slots[j].damageItem(i, this.player);

                if (this.slots[j].stackSize == 0)
                {
                    this.slots[j] = null;
                }
            }
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.slots[i] != null)
        {
            ItemStack itemstack1;

            if (this.slots[i].stackSize <= j)
            {
                itemstack1 = this.slots[i];
                this.slots[i] = null;
                this.onInventoryChanged();
                return itemstack1;
            }
            else
            {
                itemstack1 = this.slots[i].splitStack(j);

                if (this.slots[i].stackSize == 0)
                {
                    this.slots[i] = null;
                }

                this.onInventoryChanged();
                return itemstack1;
            }
        }
        else
        {
            return null;
        }
    }

    public void dropAllItems()
    {
        if (!this.player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
        {
            for (int j = 0; j < this.slots.length; ++j)
            {
                if (this.slots[j] != null)
                {
                    this.player.dropPlayerItemWithRandomChoice(this.slots[j], true);
                    this.slots[j] = null;
                }
            }
        }

        this.onInventoryChanged();
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "Aether Slots";
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 8;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int i)
    {
        return this.slots[i];
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }

    public int getTotalArmorValue()
    {
        int i = 0;
        int j = 0;
        int k = 0;
        ItemStack[] arr$ = this.slots;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            ItemStack slot = arr$[i$];

            if (slot != null && slot.getItem() instanceof IAetherAccessory)
            {
                int i1 = slot.getMaxDamage();
                int j1 = slot.getItemDamageForDisplay();
                int k1 = i1 - j1;
                j += k1;
                k += i1;
                int l1 = ((ItemAccessory)slot.getItem()).damageReduceAmount;
                i += l1;
            }
        }

        if (k == 0)
        {
            return 0;
        }
        else
        {
            return (i - 1) * j / k + 1;
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        if (!this.player.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)this.player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(this.player.username), (byte)1));
        }
    }

    public void openChest() {}

    public void readFromNBT(NBTTagList nbttaglist)
    {
        this.slots = new ItemStack[8];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (j > 8 || !(itemstack.getItem() instanceof IAetherAccessory))
            {
                this.readOldFile(nbttaglist);
                return;
            }

            if (itemstack.getItem() != null && j < this.slots.length)
            {
                this.slots[j] = itemstack;
            }
        }
    }

    public void readOldFile(NBTTagList nbttaglist)
    {
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack.getItem() != null && j >= 104 && j < 112)
            {
                this.slots[j - 104] = itemstack;
            }
        }
    }

    public NBTTagList writeToNBT(NBTTagList nbttaglist)
    {
        for (int j = 0; j < this.slots.length; ++j)
        {
            if (this.slots[j] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)j);
                this.slots[j].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        return nbttaglist;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.slots[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
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
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack)
    {
        Slot slot = (Slot)this.player.inventoryContainer.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack() && !(slot instanceof SlotMoreArmor))
        {
            int type;

            if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.player.inventoryContainer.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
            {
                type = 5 + ((ItemArmor)itemstack.getItem()).armorType;
            }
            else
            {
                if (itemstack.getItem() instanceof IAetherAccessory && this.slots[((ItemAccessory)itemstack.getItem()).getSlotType()[0]] == null)
                {
                    type = ((ItemAccessory)itemstack.getItem()).getSlotType()[0];
                    this.slots[type] = itemstack;
                    slot.putStack((ItemStack)null);
                    slot.onSlotChanged();
                    this.onInventoryChanged();
                    return true;
                }

                if (itemstack.getItem() instanceof IAetherAccessory && this.slots[((ItemAccessory)itemstack.getItem()).getSlotType()[1]] == null)
                {
                    type = ((ItemAccessory)itemstack.getItem()).getSlotType()[1];
                    this.slots[type] = itemstack;
                    slot.putStack((ItemStack)null);
                    slot.onSlotChanged();
                    this.onInventoryChanged();
                    return true;
                }
            }
        }

        return false;
    }
}
