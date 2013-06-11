package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPoisonCure extends ItemFood
{
    public ItemPoisonCure(int var1, int var2, boolean var3)
    {
        super(var1, var2, var3);
    }

    public ItemFood setIconName(String var1)
    {
        this.setUnlocalizedName("Aether:" + var1);
        return this;
    }

    public void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (!var2.isRemote)
        {
            var3.curePotionEffects(new ItemStack(Item.bucketMilk));
        }

        --var1.stackSize;
        var2.playSoundAtEntity(var3, "random.burp", 0.5F, var2.rand.nextFloat() * 0.1F + 0.9F);
    }
}
