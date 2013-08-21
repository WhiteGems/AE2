package net.aetherteam.aether.containers;

import java.util.Iterator;
import net.aetherteam.aether.AetherCommonPlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
        Iterator i = this.inventorySlots.iterator();

        while (i.hasNext())
        {
            Object j = i.next();

            if (j instanceof Slot)
            {
                Slot armorType = (Slot)j;

                switch (armorType.slotNumber)
                {
                    case 0:
                        armorType.yDisplayPosition += 26;
                        armorType.xDisplayPosition -= 10;
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        armorType.yDisplayPosition -= 18;
                        armorType.xDisplayPosition += 37;
                        break;

                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        armorType.xDisplayPosition += 54;
                }
            }
        }

        for (int var10 = 1; var10 < 3; ++var10)
        {
            for (int var11 = 0; var11 < 4; ++var11)
            {
                int var12 = 4 * (var10 - 1) + var11;
                this.addSlotToContainer(new SlotMoreArmor(this, inventoryMoreSlots, var12, 62 + var10 * 18, 8 + var11 * 18, var12 + 4));
            }
        }

        this.inv = inventoryMoreSlots;
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stackInHand = slot.getStack();
            this.inv.isItemValidForSlot(slotIndex, stackInHand);
        }

        return super.transferStackInSlot(player, slotIndex);
    }
}
