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

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    public ItemAppleGold setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        this.setUnlocalizedName("aether:" + name);
        return this;
    }
}
