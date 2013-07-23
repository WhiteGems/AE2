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
    public static final String[] keyNames = { "Bronze Key", "Silver Key", "Golden Key", "Guardian Key", "Host Key", "Cog Key" };
    public static final int bronzeKey = 0;
    public static final int silverKey = 1;
    public static final int goldenKey = 2;
    public static final int guardianKey = 3;
    public static final int hostKey = 4;
    public static final int cogKey = 5;

    @SideOnly(Side.CLIENT)
    private Icon[] keyIcons;

    protected ItemAetherKey(int itemID)
    {
        super(itemID);
        setHasSubtypes(true);
        this.maxStackSize = 1;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 6; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.keyIcons = new Icon[keyNames.length];

        for (int i = 0; i < keyNames.length; i++)
        {
            this.keyIcons[i] = par1IconRegister.registerIcon("Aether:" + keyNames[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage)
    {
        return this.keyIcons[damage];
    }

    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 5);
        return "Aether:" + keyNames[var2];
    }
}

