package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class ItemAetherFood extends ItemFood
{
    public ItemAetherFood(int var1, int var2, boolean var3)
    {
        super(var1, var2, var3);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
