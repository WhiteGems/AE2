package net.aetherteam.aether.containers;

import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.aetherteam.aether.client.gui.GuiAetherContainerCreative;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerAetherCreative extends Container
{
    public List itemList = new ArrayList();
    public EntityPlayer player;
    public InventoryAether inv;

    public ContainerAetherCreative(EntityPlayer par1EntityPlayer)
    {
        this.player = par1EntityPlayer;
        InventoryPlayer var2 = par1EntityPlayer.inventory;

        if (!this.player.worldObj.isRemote)
        {
            this.inv = Aether.getServerPlayer(par1EntityPlayer).inv;
        }

        for (int var3 = 0; var3 < 5; var3++)
        {
            for (int var4 = 0; var4 < 9; var4++)
            {
                addSlotToContainer(new Slot(GuiAetherContainerCreative.getInventory(), var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; var3++)
        {
            addSlotToContainer(new Slot(var2, var3, 9 + var3 * 18, 112));
        }

        for (var3 = 0; var3 < 8; var3++);

        scrollTo(0.0F);
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);
    }

    public void scrollTo(float par1)
    {
        int var2 = this.itemList.size() / 9 - 5 + 1;
        int var3 = (int)(par1 * var2 + 0.5D);

        if (var3 < 0)
        {
            var3 = 0;
        }

        for (int var4 = 0; var4 < 5; var4++)
        {
            for (int var5 = 0; var5 < 9; var5++)
            {
                int var6 = var5 + (var4 + var3) * 9;

                if ((var6 >= 0) && (var6 < this.itemList.size()))
                {
                    GuiAetherContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
                }
                else
                {
                    GuiAetherContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, (ItemStack)null);
                }
            }
        }
    }

    public boolean hasMoreThan1PageOfItemsInList()
    {
        return this.itemList.size() > 45;
    }

    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        if ((par2 >= this.inventorySlots.size() - 9) && (par2 < this.inventorySlots.size()))
        {
            Slot var3 = (Slot)this.inventorySlots.get(par2);

            if ((var3 != null) && (var3.getHasStack()))
            {
                var3.putStack((ItemStack)null);
            }
        }

        return null;
    }
}

