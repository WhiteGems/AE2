package net.aetherteam.aether.items;

import java.util.List;

import net.aetherteam.aether.AetherMoaColour;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemMoaEgg extends ItemAether
{
    protected ItemMoaEgg(int var1)
    {
        super(var1);
        this.setHasSubtypes(true);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < AetherMoaColour.colours.size(); ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }

    public int getColorFromItemStack(ItemStack var1, int var2)
    {
        return AetherMoaColour.getColour(var1.getItemDamage()).colour;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = var1.getItemDamage();

        if (var2 > AetherMoaColour.colours.size() - 1)
        {
            var2 = AetherMoaColour.colours.size() - 1;
        }

        return this.getUnlocalizedName() + var2;
    }
}
