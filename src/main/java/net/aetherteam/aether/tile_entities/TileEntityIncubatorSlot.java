package net.aetherteam.aether.tile_entities;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class TileEntityIncubatorSlot extends Slot
{
    public TileEntityIncubatorSlot(IInventory var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }
}
