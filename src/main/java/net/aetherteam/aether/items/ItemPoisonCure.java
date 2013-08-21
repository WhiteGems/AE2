package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPoisonCure extends ItemFood
{
    public ItemPoisonCure(int i, int j, boolean flag)
    {
        super(i, j, flag);
    }

    public ItemFood setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return (ItemFood)this.setUnlocalizedName("aether:" + name);
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        --itemstack.stackSize;

        if (!world.isRemote)
        {
            entityplayer.curePotionEffects(new ItemStack(Item.bucketMilk));
        }

        entityplayer.getFoodStats().addStats(this);
        world.playSoundAtEntity(entityplayer, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(itemstack, world, entityplayer);
        return itemstack;
    }
}
