package net.aetherteam.aether.items;

import net.aetherteam.aether.entities.EntityLightningKnife;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLightningKnife extends ItemAether
{
    public ItemLightningKnife(int var1)
    {
        super(var1);
        this.maxStackSize = 16;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
        --var1.stackSize;
        var2.playSoundAtEntity(var3, "mob.aether.dartshoot", 2.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!var2.isRemote)
        {
            var2.spawnEntityInWorld(new EntityLightningKnife(var2, var3));
        }

        return var1;
    }
}
