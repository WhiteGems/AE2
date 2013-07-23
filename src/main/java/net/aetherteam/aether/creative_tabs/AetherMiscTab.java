package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherMiscTab extends CreativeTabs
{
    public AetherMiscTab()
    {
        super("AetherMiscTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.ShardOfLife);
    }

    public String getTabLabel()
    {
        return "Aether Miscellaneous";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

