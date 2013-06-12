package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherArmourTab extends CreativeTabs
{
    public AetherArmourTab()
    {
        super("AetherArmourTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.ValkyrieChestplate);
    }

    public String getTabLabel()
    {
        return "以太II|装甲";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.creative_tabs.AetherArmourTab
 * JD-Core Version:    0.6.2
 */