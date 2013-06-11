package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherWeaponsTab extends CreativeTabs
{
    public AetherWeaponsTab()
    {
        super("AetherWeaponsTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.GravititeSword);
    }

    public String getTabLabel()
    {
        return "Aether Weapons";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.creative_tabs.AetherWeaponsTab
 * JD-Core Version:    0.6.2
 */