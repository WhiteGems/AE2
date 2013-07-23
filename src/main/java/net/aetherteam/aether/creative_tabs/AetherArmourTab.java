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
        return "Aether Armour";
    }

    public String getTranslatedTabLabel()
    {
        return getTabLabel();
    }
}

