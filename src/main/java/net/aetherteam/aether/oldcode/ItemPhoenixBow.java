package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.aetherteam.aether.items.ItemAether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPhoenixBow extends ItemAether
{
    public ItemPhoenixBow(int i)
    {
        super(i);
        this.maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.inventory.consumeInventoryItem(Item.arrow.itemID))
        {
            world.playSoundAtEntity(entityplayer, "mob.ghast.fireball", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));

            if (world.isRemote);
        }

        return itemstack;
    }
}

