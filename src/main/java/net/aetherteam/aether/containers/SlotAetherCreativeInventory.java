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

    public SlotAetherCreativeInventory(GuiAetherContainerCreative par1GuiContainerCreative, Slot par2Slot, int par3)
    {
        super(par2Slot.inventory, par3, 0, 0);
        this.theCreativeInventory = par1GuiContainerCreative;
        this.theSlot = par2Slot;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return this.theSlot.isItemValid(par1ItemStack);
    }

    public ItemStack getStack()
    {
        return this.theSlot.getStack();
    }

    public boolean getHasStack()
    {
        return this.theSlot.getHasStack();
    }

    public void putStack(ItemStack par1ItemStack)
    {
        this.theSlot.putStack(par1ItemStack);
    }

    public void onSlotChanged()
    {
        this.theSlot.onSlotChanged();
    }

    public int getSlotStackLimit()
    {
        return this.theSlot.getSlotStackLimit();
    }

    public Icon getBackgroundIconIndex()
    {
        return this.theSlot.getBackgroundIconIndex();
    }

    public ItemStack decrStackSize(int par1)
    {
        return this.theSlot.decrStackSize(par1);
    }

    public boolean isSlotInInventory(IInventory par1IInventory, int par2)
    {
        return this.theSlot.isSlotInInventory(par1IInventory, par2);
    }

    public static Slot func_75240_a(SlotAetherCreativeInventory par0SlotCreativeInventory)
    {
        return par0SlotCreativeInventory.theSlot;
    }
}

