package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

public class ItemAetherKey extends ItemAether
{
    public static final String[] keyNames = new String[]{"Bronze Key", "Silver Key", "Golden Key", "Guardian Key", "Host Key", "Cog Key"};
    public static final int bronzeKey = 0;
    public static final int silverKey = 1;
    public static final int goldenKey = 2;
    public static final int guardianKey = 3;
    public static final int hostKey = 4;
    public static final int cogKey = 5;
    @SideOnly(Side.CLIENT)
    private Icon[] keyIcons;

    protected ItemAetherKey(int var1)
    {
        super(var1);
        this.setHasSubtypes(true);
        this.maxStackSize = 1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 6; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister var1)
    {
        this.keyIcons = new Icon[keyNames.length];

        for (int var2 = 0; var2 < keyNames.length; ++var2)
        {
            this.keyIcons[var2] = var1.registerIcon("Aether:" + keyNames[var2]);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int var1)
    {
        return this.keyIcons[var1];
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = MathHelper.clamp_int(var1.getItemDamage(), 0, 5);
        return "Aether:" + keyNames[var2];
    }
}
