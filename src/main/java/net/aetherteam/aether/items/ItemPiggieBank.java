package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerAetherServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemPiggieBank extends ItemAccessory
{
    protected ItemPiggieBank(int par1, int par2, int par3, int par4)
    {
        super(par1, par2, par3, par4);
        this.setMaxStackSize(1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        PlayerAetherServer player = Aether.getServerPlayer(par2EntityPlayer);
        NBTTagCompound nbt = new NBTTagCompound();

        if (!par1ItemStack.hasTagCompound())
        {
            par1ItemStack.setTagCompound(nbt);
            return false;
        }
        else
        {
            int coins = par1ItemStack.getTagCompound().getInteger("Coins");

            if (par2EntityPlayer.isSneaking() && player != null)
            {
                if (player.getCoins() > 0)
                {
                    player.setCoinAmount(player.getCoins() - 1);
                    par1ItemStack.getTagCompound().setInteger("Coins", coins + 1);
                }
            }
            else if (coins > 0 && player != null)
            {
                player.setCoinAmount(player.getCoins() + 1);
                par1ItemStack.getTagCompound().setInteger("Coins", coins - 1);
            }

            return true;
        }
    }
}
