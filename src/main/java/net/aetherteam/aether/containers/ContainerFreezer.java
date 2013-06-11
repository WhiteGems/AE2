package net.aetherteam.aether.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
    private int cookTime = 0;
    private int burnTime = 0;
    private int itemBurnTime = 0;

    public ContainerFreezer(InventoryPlayer var1, TileEntityFreezer var2)
    {
        this.freezer = var2;
        this.addSlotToContainer(new Slot(var2, 0, 56, 17));
        this.addSlotToContainer(new Slot(var2, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(var1.player, var2, 2, 116, 35));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(var1, var3, 8 + var3 * 18, 142));
        }
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting) this.crafters.get(var1);

            if (this.cookTime != this.freezer.frozenTimeForItem)
            {
                var2.sendProgressBarUpdate(this, 0, this.freezer.frozenTimeForItem);
            }

            if (this.burnTime != this.freezer.frozenProgress)
            {
                var2.sendProgressBarUpdate(this, 1, this.freezer.frozenProgress);
            }

            if (this.itemBurnTime != this.freezer.frozenPowerRemaining)
            {
                var2.sendProgressBarUpdate(this, 2, this.freezer.frozenPowerRemaining);
            }
        }

        this.cookTime = this.freezer.frozenTimeForItem;
        this.burnTime = this.freezer.frozenProgress;
        this.itemBurnTime = this.freezer.frozenPowerRemaining;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int var1, int var2)
    {
        if (var1 == 0)
        {
            this.freezer.frozenTimeForItem = var2;
        }

        if (var1 == 1)
        {
            this.freezer.frozenProgress = var2;
        }

        if (var1 == 2)
        {
            this.freezer.frozenPowerRemaining = var2;
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.freezer.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot) this.inventorySlots.get(var2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (var2 == 2)
            {
                if (!this.mergeItemStack(var5, 3, 39, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            } else if (var2 != 1 && var2 != 0)
            {
                if (var2 >= 3 && var2 < 30)
                {
                    this.mergeItemStack(var5, 30, 39, false);
                } else if (var2 >= 30 && var2 < 39 && !this.mergeItemStack(var5, 3, 30, false))
                {
                    this.mergeItemStack(var5, 3, 30, false);
                }
            } else if (!this.mergeItemStack(var5, 3, 39, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack) null);
            } else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(var1, var5);
        }

        return var3;
    }
}
