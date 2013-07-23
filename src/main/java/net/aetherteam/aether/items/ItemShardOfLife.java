package net.aetherteam.aether.items;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.PlayerBaseAetherServer;
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

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        if (Aether.getServerPlayer(player) == null)
        {
            return itemstack;
        }

        if (Aether.getServerPlayer(player).maxHealth >= 40)
        {
            Aether.proxy.displayMessage(player, "You cannot add anymore hearts.");
            return itemstack;
        }

        Aether.getServerPlayer(player).increaseMaxHP(2);
        itemstack.stackSize -= 1;
        return itemstack;
    }
}

