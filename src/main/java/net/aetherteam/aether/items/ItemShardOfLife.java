package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemShardOfLife extends ItemAether
{
    public ItemShardOfLife(int i)
    {
        super(i);
        this.maxStackSize = 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (Aether.getServerPlayer(player) == null)
        {
            return itemstack;
        }
        else
        {
            Aether.getServerPlayer(player).increaseMaxHP(2);
            --itemstack.stackSize;
            return itemstack;
        }
    }
}
