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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherFrozen
 * JD-Core Version:    0.6.2
 */