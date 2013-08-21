package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAmbrosium extends ItemAether
{
    private int healAmount;

    public ItemAmbrosium(int i, int j)
    {
        super(i);
        this.healAmount = j;
        this.maxStackSize = 64;
    }

    public int getHealAmount()
    {
        return this.healAmount;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        --itemstack.stackSize;
        entityplayer.heal((float)this.healAmount);
        return itemstack;
    }
}
