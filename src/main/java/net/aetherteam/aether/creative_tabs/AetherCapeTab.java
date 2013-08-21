package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherCapeTab extends CreativeTabs
{
    public AetherCapeTab()
    {
        super("AetherCapeTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherItems.SwetCape);
    }

    public String getTabLabel()
    {
        return "以太II|披风";
    }

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return this.getTabLabel();
    }
}
