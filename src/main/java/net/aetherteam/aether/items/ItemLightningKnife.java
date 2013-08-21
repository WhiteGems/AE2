package net.aetherteam.aether.items;

import net.aetherteam.aether.entities.EntityLightningKnife;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLightningKnife extends ItemAether
{
    public ItemLightningKnife(int i)
    {
        super(i);
        this.maxStackSize = 16;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        --itemstack.stackSize;
        world.playSoundAtEntity(entityplayer, "random.drr", 2.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityLightningKnife(world, entityplayer));
        }

        return itemstack;
    }
}
