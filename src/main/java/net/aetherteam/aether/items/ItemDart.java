package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemDart extends ItemAether
{
    public static final String[] dartNames = new String[]{"Golden Dart", "Poison Dart", "Enchanted Dart"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    protected ItemDart(int var1)
    {
        super(var1);
        this.setHasSubtypes(true);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int var1)
    {
        return this.icons[var1];
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public Icon getIconFromDamageForRenderPass(int var1, int var2)
    {
        return this.icons[var1];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = MathHelper.clamp_int(var1.getItemDamage(), 0, 2);
        return "Aether:" + dartNames[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister var1)
    {
        this.icons = new Icon[dartNames.length];

        for (int var2 = 0; var2 < dartNames.length; ++var2)
        {
            this.icons[var2] = var1.registerIcon("Aether:" + dartNames[var2]);
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 3; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }
}
