package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherToolsTab extends CreativeTabs
{
    public AetherToolsTab()
    {
        super("AetherToolsTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.GravititePickaxe);
    }

    public String getTabLabel()
    {
        return "Aether Tools";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

