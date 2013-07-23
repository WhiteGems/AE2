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
    public static final String[] names = { "Golden Gummie Swet", "Blue Gummie Swet" };

    @SideOnly(Side.CLIENT)
    private Icon[] icons;

    public ItemLoreBook(int i)
    {
        super(i);
        this.maxStackSize = 1;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public void addCreativeItems(ArrayList itemList)
    {
        itemList.add(new ItemStack(this, 1, 0));
        itemList.add(new ItemStack(this, 1, 1));
        itemList.add(new ItemStack(this, 1, 2));
    }

    public int getColorFromDamage(int i, int j)
    {
        if (i == 0)
        {
            return 8388479;
        }

        if (i == 1)
        {
            return 16744319;
        }

        return 8355839;
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();

        if (i > 2)
        {
            i = 2;
        }

        return super.getUnlocalizedName() + "." + i;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        ModLoader.openGUI(entityplayer, new GuiLore(entityplayer.inventory, entityplayer, itemstack.getItemDamage()));
        return itemstack;
    }
}

