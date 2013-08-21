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

    public SlotMoreArmor(Container containerplayer, IInventory iinventory, int i, int j, int k, int l)
    {
        super(iinventory, i, j, k);
        this.armorType = l;
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
    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack.getItem() instanceof IAetherAccessory ? ((ItemAccessory)itemstack.getItem()).isTypeValid(this.armorType) : false;
    }

    public void setBackgroundIconIndex(int index)
    {
        this.iconIndex = index;
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
