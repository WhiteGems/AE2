package net.aetherteam.aether.containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLore extends ContainerPlayer
{
    public List slotsToRemove = new ArrayList();
    public IInventory loreSlot;

    public ContainerLore(InventoryPlayer var1, boolean var2, EntityPlayer var3)
    {
        super(var1, var2, var3);
        Iterator var4 = this.inventorySlots.iterator();

        while (var4.hasNext())
        {
            Object var5 = var4.next();

            if (var5 instanceof Slot)
            {
                Slot var6 = (Slot) var5;
                this.slotsToRemove.add(var6);
            }
        }

        this.inventorySlots.remove(0);
        int var7;

        for (var7 = 0; var7 < 3; ++var7)
        {
            for (int var8 = 0; var8 < 9; ++var8)
            {
                ((Slot) this.inventorySlots.get(var8 + (var7 + 1) * 9)).xDisplayPosition = 48 + var8 * 18;
                ((Slot) this.inventorySlots.get(var8 + (var7 + 1) * 9)).yDisplayPosition = 113 + var7 * 18;
            }
        }

        for (var7 = 0; var7 < 9; ++var7)
        {
            ((Slot) this.inventorySlots.get(var7)).xDisplayPosition = 48 + var7 * 18;
            ((Slot) this.inventorySlots.get(var7)).yDisplayPosition = 171;
        }

        this.loreSlot = new InventoryBasic("Lore Item", true, 1);
        this.addSlotToContainer(new Slot(this.loreSlot, 0, 82, 66));
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    protected void retrySlotClick(int var1, int var2, boolean var3, EntityPlayer var4) {}

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer var1)
    {
        super.onCraftGuiClosed(var1);
        ItemStack var2 = this.loreSlot.getStackInSlotOnClosing(0);

        if (var2 != null)
        {
            var1.dropPlayerItem(var2);
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    public ItemStack getStackInSlot(int var1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot) this.inventorySlots.get(var1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (var1 == 0)
            {
                this.mergeItemStack(var4, 10, 46, true);
            } else if (var1 >= 10 && var1 < 37)
            {
                this.mergeItemStack(var4, 37, 46, false);
            } else if (var1 >= 37 && var1 < 46)
            {
                this.mergeItemStack(var4, 10, 37, false);
            } else
            {
                this.mergeItemStack(var4, 10, 46, false);
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack) null);
            } else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onSlotChange(var4, var2);
        }

        return var2;
    }
}
