package net.aetherteam.aether.items;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

public class ItemHealingStone extends ItemAppleGold
{
    public ItemHealingStone(int i, int j, float f, boolean flag)
    {
        super(i, j, f, flag);
    }

    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    public ItemAppleGold setIconName(String name)
    {
        setUnlocalizedName("Aether:" + name);
        return this;
    }
}

