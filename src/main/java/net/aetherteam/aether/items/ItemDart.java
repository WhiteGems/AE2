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
    public static final String[] dartNames = { "Golden Dart", "Poison Dart", "Enchanted Dart" };

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    protected ItemDart(int itemID)
    {
        super(itemID);
        setHasSubtypes(true);
    }

    public Icon getIconFromDamage(int damage)
    {
        return this.icons[damage];
    }

    public Icon getIconFromDamageForRenderPass(int damage, int par2)
    {
        return this.icons[damage];
    }

    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 2);
        return "Aether:" + dartNames[var2];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons = new Icon[dartNames.length];

        for (int i = 0; i < dartNames.length; i++)
        {
            this.icons[i] = par1IconRegister.registerIcon("Aether:" + dartNames[i]);
        }
    }

    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 3; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}

