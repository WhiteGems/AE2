package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockChristmasLeaves extends ItemBlock
{
    public ItemBlockChristmasLeaves(int i)
    {
        super(i);
        setHasSubtypes(true);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();

        if (i > 1)
        {
            i = 1;
        }

        return getUnlocalizedName() + i;
    }

    public int getMetadata(int damage)
    {
        return damage;
    }
}

