package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAmbrosium extends ItemAether
{
    private int healAmount;

    public ItemAmbrosium(int var1, int var2)
    {
        super(var1);
        this.healAmount = var2;
        this.maxStackSize = 64;
    }

    public int getHealAmount()
    {
        return this.healAmount;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        --var1.stackSize;
        var3.heal(this.healAmount);
        return var1;
    }
}
