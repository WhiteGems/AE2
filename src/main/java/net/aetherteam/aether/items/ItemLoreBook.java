package net.aetherteam.aether.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.aetherteam.aether.client.gui.GuiLore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemLoreBook extends ItemAether
{
    public static final String[] names = new String[] {"Golden Gummie Swet", "Blue Gummie Swet"};
    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public ItemLoreBook(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public void addCreativeItems(ArrayList var1)
    {
        var1.add(new ItemStack(this, 1, 0));
        var1.add(new ItemStack(this, 1, 1));
        var1.add(new ItemStack(this, 1, 2));
    }

    public int getColorFromDamage(int var1, int var2)
    {
        return var1 == 0 ? 8388479 : (var1 == 1 ? 16744319 : 8355839);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack var1)
    {
        int var2 = var1.getItemDamage();

        if (var2 > 2)
        {
            var2 = 2;
        }

        return super.getUnlocalizedName() + "." + var2;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        ModLoader.openGUI(var3, new GuiLore(var3.inventory, var3, var1.getItemDamage()));
        return var1;
    }
}
