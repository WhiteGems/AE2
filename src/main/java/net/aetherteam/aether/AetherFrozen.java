package net.aetherteam.aether;

import net.minecraft.item.ItemStack;

public class AetherFrozen
{
    public ItemStack frozenFrom;
    public ItemStack frozenTo;
    public int frozenPowerNeeded;

    public AetherFrozen(ItemStack var1, ItemStack var2, int var3)
    {
        this.frozenFrom = var1;
        this.frozenTo = var2;
        this.frozenPowerNeeded = var3;
    }
}
