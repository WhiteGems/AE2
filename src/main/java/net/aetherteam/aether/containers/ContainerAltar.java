package net.aetherteam.aether.containers;

import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerAltar extends Container
{
    private TileEntityAltar altar;
    private int cookTime = 0;
    private int burnTime = 0;
    private int itemBurnTime = 0;

    public ContainerAltar(InventoryPlayer var1, TileEntityAltar var2)
    {
        this.altar = var2;
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
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(var2);

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
            }
            else if (var2 != 1 && var2 != 0)
            {
                if (var2 >= 3 && var2 < 30)
                {
                    this.mergeItemStack(var5, 30, 39, false);
                }
                else if (var2 >= 30 && var2 < 39 && !this.mergeItemStack(var5, 3, 30, false))
                {
                    this.mergeItemStack(var5, 3, 30, false);
                }
            }
            else if (!this.mergeItemStack(var5, 3, 39, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }
        }

        return var3;
    }

    public void updateProgressBar(int var1, int var2)
    {
        if (var1 == 0)
        {
            this.altar.enchantTimeForItem = var2;
        }

        if (var1 == 1)
        {
            this.altar.enchantProgress = var2;
        }

        if (var1 == 2)
        {
            this.altar.enchantPowerRemaining = var2;
        }
    }

    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.altar.isUseableByPlayer(var1);
    }
}
