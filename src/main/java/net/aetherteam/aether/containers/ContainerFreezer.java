package net.aetherteam.aether.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.aetherteam.aether.tile_entities.TileEntityFreezer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerFreezer extends Container
{
    private TileEntityFreezer freezer;
    private int cookTime;
    private int burnTime;
    private int itemBurnTime;

    public ContainerFreezer(InventoryPlayer inventoryplayer, TileEntityFreezer tileentityfreezer)
    {
        this.cookTime = 0;
        this.burnTime = 0;
        this.itemBurnTime = 0;
        this.freezer = tileentityfreezer;
        addSlotToContainer(new Slot(tileentityfreezer, 0, 56, 17));
        addSlotToContainer(new Slot(tileentityfreezer, 1, 56, 53));
        addSlotToContainer(new SlotFurnace(inventoryplayer.player, tileentityfreezer, 2, 116, 35));

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

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.cookTime != this.freezer.frozenTimeForItem)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.freezer.frozenTimeForItem);
            }

            if (this.burnTime != this.freezer.frozenProgress)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.freezer.frozenProgress);
            }

            if (this.itemBurnTime != this.freezer.frozenPowerRemaining)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.freezer.frozenPowerRemaining);
            }
        }

        this.cookTime = this.freezer.frozenTimeForItem;
        this.burnTime = this.freezer.frozenProgress;
        this.itemBurnTime = this.freezer.frozenPowerRemaining;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int j)
    {
        if (i == 0)
        {
            this.freezer.frozenTimeForItem = j;
        }

        if (i == 1)
        {
            this.freezer.frozenProgress = j;
        }

        if (i == 2)
        {
            this.freezer.frozenPowerRemaining = j;
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.freezer.isUseableByPlayer(entityplayer);
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

