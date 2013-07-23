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
        return "Aether Materials";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

