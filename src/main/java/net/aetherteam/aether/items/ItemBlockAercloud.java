package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAercloud extends ItemBlock
{
    public ItemBlockAercloud(int itemID)
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

        if (i > 5)
        {
            i = 5;
        }

        return getUnlocalizedName() + i;
    }

    public int getMetadata(int damage)
    {
        return damage;
    }
}

