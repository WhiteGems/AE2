package net.aetherteam.aether.items;

import java.util.List;
import net.aetherteam.aether.AetherMoaColour;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemMoaEgg extends ItemAether
{
    protected ItemMoaEgg(int itemID)
    {
        super(itemID);
        this.setHasSubtypes(true);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < AetherMoaColour.colours.size(); ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    public int getColorFromItemStack(ItemStack stack, int damage)
    {
        return AetherMoaColour.getColour(stack.getItemDamage()).colour;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getItemDamage();

        if (i > AetherMoaColour.colours.size() - 1)
        {
            i = AetherMoaColour.colours.size() - 1;
        }

        return this.getUnlocalizedName() + i;
    }
}
