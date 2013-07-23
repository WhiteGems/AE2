package net.aetherteam.aether.containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLore extends ContainerPlayer
{
    public List slotsToRemove = new ArrayList();
    public IInventory loreSlot;

    public ContainerLore(InventoryPlayer inventoryplayer, boolean b, EntityPlayer player)
    {
        super(inventoryplayer, b, player);

        for (Iterator i$ = this.inventorySlots.iterator(); i$.hasNext();)
        {
            Object slottemp = i$.next();

            if ((slottemp instanceof Slot))
            {
                Slot slot = (Slot)slottemp;
                this.slotsToRemove.add(slot);
            }
        }

        this.inventorySlots.remove(0);

        for (int var4 = 0; var4 < 3; var4++)
        {
            for (int var5 = 0; var5 < 9; var5++)
            {
                ((Slot)this.inventorySlots.get(var5 + (var4 + 1) * 9)).xDisplayPosition = (48 + var5 * 18);
                ((Slot)this.inventorySlots.get(var5 + (var4 + 1) * 9)).yDisplayPosition = (113 + var4 * 18);
            }
        }

        for (int var4 = 0; var4 < 9; var4++)
        {
            ((Slot)this.inventorySlots.get(var4)).xDisplayPosition = (48 + var4 * 18);
            ((Slot)this.inventorySlots.get(var4)).yDisplayPosition = 171;
        }

        this.loreSlot = new InventoryBasic("Lore Item", true, 1);
        addSlotToContainer(new Slot(this.loreSlot, 0, 82, 66));
        onCraftMatrixChanged(this.craftMatrix);
    }

    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
    }

    public void onCraftGuiClosed(EntityPlayer entityplayer)
    {
        super.onCraftGuiClosed(entityplayer);
        ItemStack itemstack = this.loreSlot.getStackInSlotOnClosing(0);

        if (itemstack != null)
        {
            entityplayer.dropPlayerItem(itemstack);
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public ItemStack getStackInSlot(int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if ((slot != null) && (slot.getHasStack()))
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 0)
            {
                mergeItemStack(itemstack1, 10, 46, true);
            }
            else if ((i >= 10) && (i < 37))
            {
                mergeItemStack(itemstack1, 37, 46, false);
            }
            else if ((i >= 37) && (i < 46))
            {
                mergeItemStack(itemstack1, 10, 37, false);
            }
            else
            {
                mergeItemStack(itemstack1, 10, 46, false);
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onSlotChange(itemstack1, itemstack);
            }
            else
            {
                return null;
            }
        }

        return itemstack;
    }
}

