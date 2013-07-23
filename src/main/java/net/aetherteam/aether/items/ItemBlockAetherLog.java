package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAetherLog extends ItemBlock
{
    public ItemBlockAetherLog(int itemID)
    {
        super(itemID);
        setHasSubtypes(true);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();

        if (i > 2)
        {
            i = 2;
        }

        if (i == 1)
        {
            i = 0;
        }

        return getUnlocalizedName() + i;
    }

    public int getMetadata(int damage)
    {
        if (damage <= 1)
        {
            return 1;
        }

        return 3;
    }
}

