package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDungeonHolystone extends ItemBlock
{
    public ItemBlockDungeonHolystone(int itemID)
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

        if (i >= 2)
        {
            return "Dungeon Mossy Holystone";
        }

        if (i >= 0)
        {
            return "Dungeon Holystone";
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

