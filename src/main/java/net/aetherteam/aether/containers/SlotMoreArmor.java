package net.aetherteam.aether.containers;

import net.aetherteam.aether.interfaces.IAetherAccessory;
import net.aetherteam.aether.items.ItemAccessory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SlotMoreArmor extends Slot
{
    final int armorType;
    public int iconIndex = 0;

    public SlotMoreArmor(Container var1, IInventory var2, int var3, int var4, int var5, int var6)
    {
        super(var2, var3, var4, var5);
        this.armorType = var6;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 1;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack var1)
    {
        return var1.getItem() instanceof IAetherAccessory ? ((ItemAccessory) var1.getItem()).isTypeValid(this.armorType) : false;
    }

    public void setBackgroundIconIndex(int var1)
    {
        this.iconIndex = var1;
    }

    /**
     * Returns the icon index on items.png that is used as background image of the slot.
     */
    public Icon getBackgroundIconIndex()
    {
        switch (this.armorType)
        {
            case 4:
                return ItemAccessory.pendantSlot;

            case 5:
                return ItemAccessory.capeSlot;

            case 6:
                return ItemAccessory.shieldSlot;

            case 7:
                return ItemAccessory.miscSlot;

            case 8:
                return ItemAccessory.ringSlot;

            case 9:
                return ItemAccessory.ringSlot;

            case 10:
                return ItemAccessory.gloveSlot;

            case 11:
                return ItemAccessory.miscSlot;

            default:
                return super.getBackgroundIconIndex();
        }
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged()
    {
        super.onSlotChanged();
    }
}
