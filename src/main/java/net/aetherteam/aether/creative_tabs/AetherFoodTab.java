package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherFoodTab extends CreativeTabs
{
    public AetherFoodTab()
    {
        super("AetherFoodTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.BlueBerry);
    }

    public String getTabLabel()
    {
        return "Aether Food";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

