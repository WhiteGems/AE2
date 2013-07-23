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

    public ContainerPlayerAether(InventoryPlayer var1, InventoryAether var2, boolean var3, EntityPlayer var4, AetherCommonPlayerHandler var5)
    {
        super(var1, var3, var4);
        this.player = var4;
        Iterator var6 = this.inventorySlots.iterator();

        while (var6.hasNext())
        {
            Object var7 = var6.next();

            if (var7 instanceof Slot)
            {
                Slot var8 = (Slot)var7;

                switch (var8.slotNumber)
                {
                    case 0:
                        var8.yDisplayPosition += 26;
                        var8.xDisplayPosition -= 10;
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        var8.yDisplayPosition -= 18;
                        var8.xDisplayPosition += 37;
                        break;

                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        var8.xDisplayPosition += 54;
                }
            }
        }

        for (int var10 = 1; var10 < 3; ++var10)
        {
            for (int var11 = 0; var11 < 4; ++var11)
            {
                int var12 = 4 * (var10 - 1) + var11;
                this.addSlotToContainer(new SlotMoreArmor(this, var2, var12, 62 + var10 * 18, 8 + var11 * 18, var12 + 4));
            }
        }

        this.inv = var2;
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2)
    {
        Slot var3 = (Slot)this.inventorySlots.get(var2);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            this.inv.isStackValidForSlot(var2, var4);
        }

        return super.transferStackInSlot(var1, var2);
    }
}
