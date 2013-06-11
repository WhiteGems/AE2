package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherAccessoryTab extends CreativeTabs
{
    public AetherAccessoryTab()
    {
        super("AetherAccessoryTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.IronRing);
    }

    public String getTabLabel()
    {
        return "Aether Accessories";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.creative_tabs.AetherAccessoryTab
 * JD-Core Version:    0.6.2
 */