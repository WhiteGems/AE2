package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemPiggieBank extends ItemAccessory
{
    protected ItemPiggieBank(int var1, int var2, int var3, int var4)
    {
        super(var1, var2, var3, var4);
        this.setMaxStackSize(1);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10)
    {
        PlayerBaseAetherServer var11 = Aether.getServerPlayer(var2);
        NBTTagCompound var12 = new NBTTagCompound();

        if (!var1.hasTagCompound())
        {
            var1.setTagCompound(var12);
            return false;
        }
        else
        {
            int var13 = var1.getTagCompound().getInteger("Coins");

            if (var2.isSneaking() && var11 != null)
            {
                if (var11.getCoins() > 0)
                {
                    var11.setCoinAmount(var11.getCoins() - 1);
                    var1.getTagCompound().setInteger("Coins", var13 + 1);
                }
            }
            else if (var13 > 0 && var11 != null)
            {
                var11.setCoinAmount(var11.getCoins() + 1);
                var1.getTagCompound().setInteger("Coins", var13 - 1);
            }

            return true;
        }
    }
}
