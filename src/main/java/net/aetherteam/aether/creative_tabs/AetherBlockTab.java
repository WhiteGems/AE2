package net.aetherteam.aether.creative_tabs;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class AetherBlockTab extends CreativeTabs
{
    public AetherBlockTab()
    {
        super("AetherBlockTab");
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AetherBlocks.AetherGrass);
    }

    public String getTabLabel()
    {
        return "Aether Blocks";
    }

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return this.getTabLabel();
    }
}
