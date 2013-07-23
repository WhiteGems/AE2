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

    public InventoryAether(EntityPlayer var1)
    {
        this.player = var1;
        this.slots = new ItemStack[8];
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    public boolean isEmpty()
    {
        ItemStack[] var1 = this.slots;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3)
        {
            ItemStack var4 = var1[var3];

            if (var4 != null)
            {
                return false;
            }
        }

        return true;
    }

    public void closeChest() {}

    public void damageArmor(int var1)
    {
        for (int var2 = 0; var2 < this.slots.length; ++var2)
        {
            if (this.slots[var2] != null && this.slots[var2].getItem() instanceof IAetherAccessory)
            {
                this.slots[var2].damageItem(var1, this.player);

                if (this.slots[var2].stackSize == 0)
                {
                    this.slots[var2] = null;
                }
            }
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.slots[var1] != null)
        {
            ItemStack var3;

            if (this.slots[var1].stackSize <= var2)
            {
                var3 = this.slots[var1];
                this.slots[var1] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.slots[var1].splitStack(var2);

                if (this.slots[var1].stackSize == 0)
                {
                    this.slots[var1] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public void dropAllItems()
    {
        for (int var1 = 0; var1 < this.slots.length; ++var1)
        {
            if (this.slots[var1] != null)
            {
                this.player.dropPlayerItemWithRandomChoice(this.slots[var1], true);
                this.slots[var1] = null;
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
    public ItemStack getStackInSlot(int var1)
    {
        return this.slots[var1];
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
        int var1 = 0;
        int var2 = 0;
        int var3 = 0;
        ItemStack[] var4 = this.slots;
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            ItemStack var7 = var4[var6];

            if (var7 != null && var7.getItem() instanceof IAetherAccessory)
            {
                int var8 = var7.getMaxDamage();
                int var9 = var7.getItemDamageForDisplay();
                int var10 = var8 - var9;
                var2 += var10;
                var3 += var8;
                int var11 = ((ItemAccessory)var7.getItem()).damageReduceAmount;
                var1 += var11;
            }
        }

        if (var3 == 0)
        {
            return 0;
        }
        else
        {
            return (var1 - 1) * var2 / var3 + 1;
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

    public void readFromNBT(NBTTagList var1)
    {
        this.slots = new ItemStack[8];

        for (int var2 = 0; var2 < var1.tagCount(); ++var2)
        {
            NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

            if (var4 > 8 || !(var5.getItem() instanceof IAetherAccessory))
            {
                this.readOldFile(var1);
                return;
            }

            if (var5.getItem() != null && var4 < this.slots.length)
            {
                this.slots[var4] = var5;
            }
        }
    }

    public void readOldFile(NBTTagList var1)
    {
        for (int var2 = 0; var2 < var1.tagCount(); ++var2)
        {
            NBTTagCompound var3 = (NBTTagCompound)var1.tagAt(var2);
            int var4 = var3.getByte("Slot") & 255;
            ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

            if (var5.getItem() != null && var4 >= 104 && var4 < 112)
            {
                this.slots[var4 - 104] = var5;
            }
        }
    }

    public NBTTagList writeToNBT(NBTTagList var1)
    {
        for (int var2 = 0; var2 < this.slots.length; ++var2)
        {
            if (this.slots[var2] != null)
            {
                NBTTagCompound var3 = new NBTTagCompound();
                var3.setByte("Slot", (byte)var2);
                this.slots[var2].writeToNBT(var3);
                var1.appendTag(var3);
            }
        }

        return var1;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.slots[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
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
    public boolean isStackValidForSlot(int var1, ItemStack var2)
    {
        Slot var3 = (Slot)this.player.inventoryContainer.inventorySlots.get(var1);

        if (var3 != null && var3.getHasStack() && !(var3 instanceof SlotMoreArmor))
        {
            int var4;

            if (var2.getItem() instanceof ItemArmor && !((Slot)this.player.inventoryContainer.inventorySlots.get(5 + ((ItemArmor)var2.getItem()).armorType)).getHasStack())
            {
                var4 = 5 + ((ItemArmor)var2.getItem()).armorType;
            }
            else
            {
                if (var2.getItem() instanceof IAetherAccessory && this.slots[((ItemAccessory)var2.getItem()).getSlotType()[0]] == null)
                {
                    var4 = ((ItemAccessory)var2.getItem()).getSlotType()[0];
                    this.slots[var4] = var2;
                    var3.putStack((ItemStack)null);
                    var3.onSlotChanged();
                    this.onInventoryChanged();
                    return true;
                }

                if (var2.getItem() instanceof IAetherAccessory && this.slots[((ItemAccessory)var2.getItem()).getSlotType()[1]] == null)
                {
                    var4 = ((ItemAccessory)var2.getItem()).getSlotType()[1];
                    this.slots[var4] = var2;
                    var3.putStack((ItemStack)null);
                    var3.onSlotChanged();
                    this.onInventoryChanged();
                    return true;
                }
            }
        }

        return false;
    }
}
