package net.aetherteam.aether.items;

import net.minecraft.item.Item;

public class ItemAether extends Item
{
    protected ItemAether(int var1)
    {
        super(var1);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
