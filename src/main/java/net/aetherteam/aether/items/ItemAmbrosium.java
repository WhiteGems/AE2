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

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize -= 1;
        entityplayer.heal(this.healAmount);
        return itemstack;
    }
}

