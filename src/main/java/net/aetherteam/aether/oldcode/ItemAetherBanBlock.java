package net.aetherteam.aether.oldcode;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAetherBanBlock extends ItemBlock
{
    private int dimensionToBan = 3;

    public ItemAetherBanBlock(int par1)
    {
        super(par1);
    }

    public ItemAetherBanBlock(int par1, int dimensionToBan)
    {
        this(par1);
        this.dimensionToBan = dimensionToBan;
    }

    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par2EntityPlayer.dimension != this.dimensionToBan)
        {
            return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
        }

        return false;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

