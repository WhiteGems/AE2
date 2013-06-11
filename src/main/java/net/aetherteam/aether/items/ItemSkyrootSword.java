package net.aetherteam.aether.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemSkyrootSword extends ItemSword
{
    public ItemSkyrootSword(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
