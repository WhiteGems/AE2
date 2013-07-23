package net.aetherteam.aether.containers;

import java.util.List;
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
    private int cookTime;
    private int burnTime;
    private int itemBurnTime;

    public ContainerIncubator(InventoryPlayer inventoryplayer, TileEntityIncubator tileentityIncubator)
    {
        this.cookTime = 0;
        this.burnTime = 0;
        this.itemBurnTime = 0;
        this.Incubator = tileentityIncubator;
        addSlotToContainer(new TileEntityIncubatorSlot(tileentityIncubator, 1, 73, 17));
        addSlotToContainer(new Slot(tileentityIncubator, 0, 73, 53));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.Incubator.isUseableByPlayer(entityplayer);
    }

    public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if ((slot != null) && (slot.getHasStack()))
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if ((i != 1) && (i != 0))
            {
                if ((i >= 3) && (i < 30))
                {
                    mergeItemStack(itemstack1, 30, 39, false);
                }
                else if ((i >= 30) && (i < 39) && (!mergeItemStack(itemstack1, 3, 30, false)))
                {
                    mergeItemStack(itemstack1, 3, 30, false);
                }
            }
            else if (!mergeItemStack(itemstack1, 3, 39, false))
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

            slot.onPickupFromSlot(entityplayer, itemstack1);
        }

        return itemstack;
    }
}

