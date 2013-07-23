package net.aetherteam.aether.containers;

import java.util.Iterator;
import java.util.List;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPlayerAether extends ContainerPlayer
{
    public InventoryAether inv;
    public EntityPlayer player;

    public ContainerPlayerAether(InventoryPlayer inventory, InventoryAether inventoryMoreSlots, boolean b, EntityPlayer player, AetherCommonPlayerHandler aetherCommonPlayerHandler)
    {
        super(inventory, b, player);
        this.player = player;

        for (Iterator i$ = this.inventorySlots.iterator(); i$.hasNext();)
        {
            Object slottemp = i$.next();

            if ((slottemp instanceof Slot))
            {
                Slot slot = (Slot)slottemp;

                switch (slot.slotNumber)
                {
                    case 0:
                        slot.yDisplayPosition += 26;
                        slot.xDisplayPosition -= 10;
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        slot.yDisplayPosition -= 18;
                        slot.xDisplayPosition += 37;
                        break;

                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        slot.xDisplayPosition += 54;
                }
            }
        }

        for (int i = 1; i < 3; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                int armorType = 4 * (i - 1) + j;
                int slotId = armorType;
                addSlotToContainer(new SlotMoreArmor(this, inventoryMoreSlots, slotId, 62 + i * 18, 8 + j * 18, armorType + 4));
            }
        }

        this.inv = inventoryMoreSlots;
        onCraftMatrixChanged(this.craftMatrix);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if ((slot != null) && (slot.getHasStack()))
        {
            ItemStack stackInHand = slot.getStack();
            this.inv.isStackValidForSlot(slotIndex, stackInHand);
        }

        return super.transferStackInSlot(player, slotIndex);
    }
}

