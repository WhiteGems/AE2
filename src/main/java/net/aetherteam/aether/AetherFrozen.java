package net.aetherteam.aether;

import net.minecraft.item.ItemStack;

public class AetherFrozen
{
    public ItemStack frozenFrom;
    public ItemStack frozenTo;
    public int frozenPowerNeeded;

    public AetherFrozen(ItemStack from, ItemStack to, int i)
    {
        this.frozenFrom = from;
        this.frozenTo = to;
        this.frozenPowerNeeded = i;
    }
}
