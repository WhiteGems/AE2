package net.aetherteam.aether.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemSkyrootSword extends ItemSword
{
    public ItemSkyrootSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

