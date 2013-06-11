package net.aetherteam.aether.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.client.gui.GuiAetherContainerCreative;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

@SideOnly(Side.CLIENT)
public class SlotAetherCreativeInventory extends Slot
{
    private final Slot theSlot;
    final GuiAetherContainerCreative theCreativeInventory;

    public SlotAetherCreativeInventory(GuiAetherContainerCreative var1, Slot var2, int var3)
    {
        super(var2.inventory, var3, 0, 0);
        this.theCreativeInventory = var1;
        this.theSlot = var2;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack var1)
    {
        return this.theSlot.isItemValid(var1);
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    public ItemStack getStack()
    {
        return this.theSlot.getStack();
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack()
    {
        return this.theSlot.getHasStack();
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack var1)
    {
        this.theSlot.putStack(var1);
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged()
    {
        this.theSlot.onSlotChanged();
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return this.theSlot.getSlotStackLimit();
    }

    /**
     * Returns the icon index on items.png that is used as background image of the slot.
     */
    public Icon getBackgroundIconIndex()
    {
        return this.theSlot.getBackgroundIconIndex();
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int var1)
    {
        return this.theSlot.decrStackSize(var1);
    }

    /**
     * returns true if this slot is in par2 of par1
     */
    public boolean isSlotInInventory(IInventory var1, int var2)
    {
        return this.theSlot.isSlotInInventory(var1, var2);
    }

    public static Slot func_75240_a(SlotAetherCreativeInventory var0)
    {
        return var0.theSlot;
    }
}
