package net.aetherteam.aether;

import net.minecraft.item.ItemStack;

public class AetherEnchantment
{
    public ItemStack enchantFrom;
    public ItemStack enchantTo;
    public int enchantAmbrosiumNeeded;
    public boolean limitStackToOne;

    public AetherEnchantment(ItemStack from, ItemStack to, int i)
    {
        this(from, to, i, false);
    }

    public AetherEnchantment(ItemStack from, ItemStack to, int i, boolean limit)
    {
        this.enchantFrom = from;
        this.enchantTo = to;
        this.enchantAmbrosiumNeeded = i;
        this.limitStackToOne = limit;
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.AetherEnchantment
 * JD-Core Version:    0.6.2
 */