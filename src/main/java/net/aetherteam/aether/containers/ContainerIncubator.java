package net.aetherteam.aether.containers;

import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.aetherteam.aether.tile_entities.TileEntityIncubatorSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerIncubator extends Container
{
    private TileEntityIncubator Incubator;
    private int cookTime = 0;
    private int burnTime = 0;
    private int itemBurnTime = 0;

    public ContainerIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator)
    {
        this.Incubator = tileentityIncubator;
        this.addSlotToContainer(new TileEntityIncubatorSlot(tileentityIncubator, 1, 73, 17));
        this.addSlotToContainer(new Slot(tileentityIncubator, 0, 73, 53));
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.Incubator.isUseableByPlayer(entityplayer);
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

            if (i > 1)
            {
                if (!this.mergeItemStack(itemstack1, 0, 2, true))
                {
                    return null;
                }
            }
            else
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
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

            slot.onPickupFromSlot(entityplayer, itemstack1);
        }

        return itemstack;
    }
}
