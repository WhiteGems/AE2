package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherMaterialsTab extends CreativeTabs
{
    public AetherMaterialsTab()
    {
        super("AetherMaterialsTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.SkyrootStick);
    }

    public String getTabLabel()
    {
        return "以太II|材料";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.creative_tabs.AetherMaterialsTab
 * JD-Core Version:    0.6.2
 */