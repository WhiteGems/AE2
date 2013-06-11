package net.aetherteam.aether.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAetherGrass extends ItemBlock
{
    public ItemBlockAetherGrass(int var1)
    {
        super(var1);
        this.setHasSubtypes(true);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = var1.getItemDamage();

        if (var2 == 1)
        {
            var2 = 1;
        }

        if (var2 == 0)
        {
            var2 = 0;
        }

        return this.getUnlocalizedName() + var2;
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int var1)
    {
        return var1 == 1 ? 1 : 0;
    }
}
