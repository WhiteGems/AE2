package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.entities.EntityLightningKnife;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLightningKnife extends ItemAether
{
    public ItemLightningKnife(int i)
    {
        super(i);
        this.maxStackSize = 16;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize -= 1;
        world.playSoundAtEntity(entityplayer, "mob.aether.dartshoot", 2.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityLightningKnife(world, entityplayer));
        }

        return itemstack;
    }
}

