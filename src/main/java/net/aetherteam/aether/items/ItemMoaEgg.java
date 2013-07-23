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
        setHasSubtypes(true);
    }

    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < AetherMoaColour.colours.size(); var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    public int getColorFromItemStack(ItemStack stack, int damage)
    {
        return AetherMoaColour.getColour(stack.getItemDamage()).colour;
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getItemDamage();

        if (i > AetherMoaColour.colours.size() - 1)
        {
            i = AetherMoaColour.colours.size() - 1;
        }

        return getUnlocalizedName() + i;
    }
}

