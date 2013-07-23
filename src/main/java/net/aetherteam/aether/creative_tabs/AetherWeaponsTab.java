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

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return this.getTabLabel();
    }
}
