package net.aetherteam.aether.containers;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.util.Collections;
import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.items.ItemAccessory;
import net.aetherteam.aether.packets.AetherPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class InventoryAether
    implements IInventory
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
        for (ItemStack stack : this.slots)
        {
            if (stack != null)
            {
                return false;
            }
        }

        return true;
    }

    public void closeChest()
    {
    }

    public void damageArmor(int i)
    {
        for (int j = 0; j < this.slots.length; j++)
        {
            if ((this.slots[j] != null) && ((this.slots[j].getItem() instanceof IAetherAccessory)))
            {
                this.slots[j].damageItem(i, this.player);

                if (this.slots[j].stackSize == 0)
                {
                    this.slots[j] = null;
                }
            }
        }
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (this.slots[i] != null)
        {
            if (this.slots[i].stackSize <= j)
            {
                ItemStack itemstack = this.slots[i];
                this.slots[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = this.slots[i].splitStack(j);

            if (this.slots[i].stackSize == 0)
            {
                this.slots[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }

        return null;
    }

    public void dropAllItems()
    {
        for (int j = 0; j < this.slots.length; j++)
        {
            if (this.slots[j] != null)
            {
                this.player.dropPlayerItemWithRandomChoice(this.slots[j], true);
                this.slots[j] = null;
            }
        }

        onInventoryChanged();
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public String getInvName()
    {
        return "Aether Slots";
    }

    public int getSizeInventory()
    {
        return 8;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.slots[i];
    }

    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }

    public int getTotalArmorValue()
    {
        int i = 0;
        int j = 0;
        int k = 0;

        for (ItemStack slot : this.slots)
        {
            if ((slot != null) && ((slot.getItem() instanceof IAetherAccessory)))
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

        return (i - 1) * j / k + 1;
    }

    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    public void onInventoryChanged()
    {
        if (!this.player.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllPlayers(AetherPacketHandler.sendAccessoryChange(Aether.getServerPlayer((EntityPlayerMP)this.player).inv.writeToNBT(new NBTTagList()), false, true, Collections.singleton(this.player.username), (byte)1));
        }
    }

    public void openChest()
    {
    }

    public void readFromNBT(NBTTagList nbttaglist)
    {
        this.slots = new ItemStack[8];

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if ((j > 8) || (!(itemstack.getItem() instanceof IAetherAccessory)))
            {
                readOldFile(nbttaglist);
                return;
            }

            if (itemstack.getItem() != null)
            {
                if (j < this.slots.length)
                {
                    this.slots[j] = itemstack;
                }
            }
        }
    }

    public void readOldFile(NBTTagList nbttaglist)
    {
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack.getItem() != null)
            {
                if ((j >= 104) && (j < 112))
                {
                    this.slots[(j - 104)] = itemstack;
                }
            }
        }
    }

    public NBTTagList writeToNBT(NBTTagList nbttaglist)
    {
        for (int j = 0; j < this.slots.length; j++)
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

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.slots[i] = itemstack;

        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public boolean isStackValidForSlot(int slotIndex, ItemStack itemstack)
    {
        Slot slot = (Slot)this.player.inventoryContainer.inventorySlots.get(slotIndex);

        if ((slot != null) && (slot.getHasStack()) && (!(slot instanceof SlotMoreArmor)))
        {
            int var6;

            if (((itemstack.getItem() instanceof ItemArmor)) && (!((Slot)this.player.inventoryContainer.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack()))
            {
                var6 = 5 + ((ItemArmor)itemstack.getItem()).armorType;
            }
            else
            {
                if (((itemstack.getItem() instanceof IAetherAccessory)) && (this.slots[((ItemAccessory)itemstack.getItem()).getSlotType()[0]] == null))
                {
                    int type = ((ItemAccessory)itemstack.getItem()).getSlotType()[0];
                    this.slots[type] = itemstack;
                    slot.putStack((ItemStack)null);
                    slot.onSlotChanged();
                    onInventoryChanged();
                    return true;
                }

                if (((itemstack.getItem() instanceof IAetherAccessory)) && (this.slots[((ItemAccessory)itemstack.getItem()).getSlotType()[1]] == null))
                {
                    int type = ((ItemAccessory)itemstack.getItem()).getSlotType()[1];
                    this.slots[type] = itemstack;
                    slot.putStack((ItemStack)null);
                    slot.onSlotChanged();
                    onInventoryChanged();
                    return true;
                }
            }
        }

        return false;
    }
}

