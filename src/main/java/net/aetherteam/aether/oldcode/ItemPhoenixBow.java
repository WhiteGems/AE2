package net.aetherteam.aether.oldcode;

import net.aetherteam.aether.items.ItemAether;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPhoenixBow extends ItemAether
{
    public ItemPhoenixBow(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (var3.inventory.consumeInventoryItem(Item.arrow.itemID))
        {
            var2.playSoundAtEntity(var3, "mob.ghast.fireball", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!var2.isRemote)
            {
                ;
            }
        }

        return var1;
    }
}
