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
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }
}
