package net.aetherteam.aether;

import net.minecraft.item.ItemStack;

public class AetherEnchantment
{
    public ItemStack enchantFrom;
    public ItemStack enchantTo;
    public int enchantAmbrosiumNeeded;
    public boolean limitStackToOne;

    public AetherEnchantment(ItemStack var1, ItemStack var2, int var3)
    {
        this(var1, var2, var3, false);
    }

    public AetherEnchantment(ItemStack var1, ItemStack var2, int var3, boolean var4)
    {
        this.enchantFrom = var1;
        this.enchantTo = var2;
        this.enchantAmbrosiumNeeded = var3;
        this.limitStackToOne = var4;
    }
}
