package net.aetherteam.aether.tile_entities;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class TileEntityIncubatorSlot extends Slot
{
    public TileEntityIncubatorSlot(IInventory inv, int slot, int x, int y)
    {
        super(inv, slot, x, y);
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
