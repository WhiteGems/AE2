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

