package net.aetherteam.aether.items;

import net.minecraft.item.Item;

public class ItemAether extends Item
{
    protected ItemAether(int par1)
    {
        super(par1);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

