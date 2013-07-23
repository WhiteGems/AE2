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

    public int getSlotStackLimit()
    {
        return 1;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if ((itemstack.getItem() instanceof IAetherAccessory))
        {
            return ((ItemAccessory)itemstack.getItem()).isTypeValid(this.armorType);
        }

        return false;
    }

    public void setBackgroundIconIndex(int index)
    {
        this.iconIndex = index;
    }

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
        }

        return super.getBackgroundIconIndex();
    }

    public void onSlotChanged()
    {
        super.onSlotChanged();
    }
}

