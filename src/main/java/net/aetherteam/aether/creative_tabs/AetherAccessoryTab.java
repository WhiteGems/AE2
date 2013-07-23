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

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return this.getTabLabel();
    }
}
