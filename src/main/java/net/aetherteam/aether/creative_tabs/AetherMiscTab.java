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
        return "以太II|杂项";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.creative_tabs.AetherMiscTab
 * JD-Core Version:    0.6.2
 */