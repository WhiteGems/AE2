package net.aetherteam.aether.items;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

public class ItemHealingStone extends ItemAppleGold
{
    public ItemHealingStone(int var1, int var2, float var3, boolean var4)
    {
        super(var1, var2, var3, var4);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
    }

    public ItemAppleGold setIconName(String var1)
    {
        this.setUnlocalizedName("Aether:" + var1);
        return this;
    }
}
