package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemShardOfLife extends ItemAether
{
    public ItemShardOfLife(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (Aether.getServerPlayer(var3) == null)
        {
            return var1;
        }
        else if (Aether.getServerPlayer(var3).maxHealth >= 40)
        {
            Aether.proxy.displayMessage(var3, "You cannot add anymore hearts.");
            return var1;
        }
        else
        {
            Aether.getServerPlayer(var3).increaseMaxHP(2);
            --var1.stackSize;
            return var1;
        }
    }
}
