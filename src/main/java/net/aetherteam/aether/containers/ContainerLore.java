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

    public ContainerLore(InventoryPlayer inventoryplayer, boolean b, EntityPlayer player)
    {
        super(inventoryplayer, b, player);
        Iterator var4 = this.inventorySlots.iterator();

        while (var4.hasNext())
        {
            Object var5 = var4.next();

            if (var5 instanceof Slot)
            {
                Slot slot = (Slot)var5;
                this.slotsToRemove.add(slot);
            }
        }

        this.inventorySlots.remove(0);
        int var7;

        for (var7 = 0; var7 < 3; ++var7)
        {
            for (int var8 = 0; var8 < 9; ++var8)
            {
                ((Slot)this.inventorySlots.get(var8 + (var7 + 1) * 9)).xDisplayPosition = 48 + var8 * 18;
                ((Slot)this.inventorySlots.get(var8 + (var7 + 1) * 9)).yDisplayPosition = 113 + var7 * 18;
            }
        }

        for (var7 = 0; var7 < 9; ++var7)
        {
            ((Slot)this.inventorySlots.get(var7)).xDisplayPosition = 48 + var7 * 18;
            ((Slot)this.inventorySlots.get(var7)).yDisplayPosition = 171;
        }

        this.loreSlot = new InventoryBasic("Lore Item", true, 1);
        this.addSlotToContainer(new Slot(this.loreSlot, 0, 82, 66));
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {}

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
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

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 0)
            {
                this.mergeItemStack(itemstack1, 10, 46, true);
            }
            else if (i >= 10 && i < 37)
            {
                this.mergeItemStack(itemstack1, 37, 46, false);
            }
            else if (i >= 37 && i < 46)
            {
                this.mergeItemStack(itemstack1, 10, 37, false);
            }
            else
            {
                this.mergeItemStack(itemstack1, 10, 46, false);
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onSlotChange(itemstack1, itemstack);
        }

        return itemstack;
    }
}
