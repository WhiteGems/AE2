package net.aetherteam.aether.containers;

import net.aetherteam.aether.tile_entities.TileEntityEnchanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerEnchanter extends Container
{
    private TileEntityEnchanter enchanter;
    private int cookTime = 0;
    private int burnTime = 0;
    private int itemBurnTime = 0;

    public ContainerEnchanter(InventoryPlayer inventoryplayer, TileEntityEnchanter tileentityenchanter)
    {
        this.enchanter = tileentityenchanter;
        this.addSlotToContainer(new Slot(tileentityenchanter, 0, 56, 17));
        this.addSlotToContainer(new Slot(tileentityenchanter, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(inventoryplayer.player, tileentityenchanter, 2, 116, 35));
        int j;

        for (j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventoryplayer, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (i != 1 && i != 0)
            {
                if (i >= 3 && i < 30)
                {
                    this.mergeItemStack(itemstack1, 30, 39, false);
                }
                else if (i >= 30 && i < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    this.mergeItemStack(itemstack1, 3, 30, false);
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
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
        }

        return itemstack;
    }

    public void updateProgressBar(int i, int j)
    {
        if (i == 0)
        {
            this.enchanter.enchantTimeForItem = j;
        }

        if (i == 1)
        {
            this.enchanter.enchantProgress = j;
        }

        if (i == 2)
        {
            this.enchanter.enchantPowerRemaining = j;
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.enchanter.isUseableByPlayer(entityplayer);
    }
}
