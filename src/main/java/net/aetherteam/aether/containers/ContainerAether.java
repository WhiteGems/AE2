package net.aetherteam.aether.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;

public class ContainerAether extends ContainerPlayer
{
    public ContainerAether(InventoryPlayer var1, InventoryAether var2, EntityPlayer var3)
    {
        this(var1, var2, true, var3);
    }

    public ContainerAether(InventoryPlayer var1, InventoryAether var2, boolean var3, EntityPlayer var4)
    {
        super(var1, var3, var4);
        this.inventorySlots.clear();
        this.craftMatrix = new InventoryCrafting(this, 2, 2);
        this.craftResult = new InventoryCraftResult();
        this.isLocalWorld = var3;
        this.addSlotToContainer(new SlotCrafting(var1.player, this.craftMatrix, this.craftResult, 0, 134, 62));
        int var5;
        int var6;

        for (var5 = 0; var5 < 2; ++var5)
        {
            for (var6 = 0; var6 < 2; ++var6)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, var6 + var5 * 2, 125 + var6 * 18, 8 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 4; ++var5)
        {
            ;
        }

        for (var5 = 0; var5 < 3; ++var5)
        {
            for (var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(var1, var6 + (var5 + 1) * 9, 8 + var6 * 18, 84 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 9; ++var5)
        {
            this.addSlotToContainer(new Slot(var1, var5, 8 + var5 * 18, 142));
        }

        for (var5 = 1; var5 < 3; ++var5)
        {
            for (var6 = 0; var6 < 4; ++var6)
            {
                int var7 = 4 * (var5 - 1) + var6;
            }
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }
}
