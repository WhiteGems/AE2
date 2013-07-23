package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBronzeDoor extends ItemBlock
{
    public ItemBlockBronzeDoor(int itemID)
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

        if (i <= 0)
        {
            return "Bronze Door";
        }

        if (i > 0)
        {
            return "Bronze Door Lock";
        }

        return getUnlocalizedName() + i;
    }

    public int getMetadata(int damage)
    {
        if (damage <= 0)
        {
            return 0;
        }

        return 1;
    }
}

