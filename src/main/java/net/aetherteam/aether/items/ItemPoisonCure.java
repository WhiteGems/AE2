package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

public class ItemPoisonCure extends ItemFood
{
    public ItemPoisonCure(int i, int j, boolean flag)
    {
        super(i, j, flag);
    }

    public ItemFood setIconName(String name)
    {
        setUnlocalizedName("Aether:" + name);
        return this;
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        itemstack.stackSize -= 1;

        if (!world.isRemote)
        {
            entityplayer.curePotionEffects(new ItemStack(Item.bucketMilk));
        }

        entityplayer.cn().addStats(this);
        world.playSoundAtEntity(entityplayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        onFoodEaten(itemstack, world, entityplayer);
        return itemstack;
    }
}

