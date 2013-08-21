package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAetherLog extends ItemBlock
{
    public ItemBlockAetherLog(int itemID)
    {
        super(itemID);
        this.setHasSubtypes(true);
    }

    public Item setIconName(String name)
    {
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
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

        return this.getUnlocalizedName() + i;
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int damage)
    {
        return damage <= 1 ? 1 : 3;
    }
}
