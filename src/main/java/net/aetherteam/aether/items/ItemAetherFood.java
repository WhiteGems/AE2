package net.aetherteam.aether.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemAetherFood extends ItemFood
{
    public ItemAetherFood(int var1, int var2, boolean var3)
    {
        super(var1, var2, var3);
    }

    public ItemAetherFood(int var1, int var2, int var3, int var4, boolean var5)
    {
        super(var1, var2, var5);
    }

    protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3)
    {
        if (!var2.isRemote && var1.itemID == AetherItems.Strawberry.itemID)
        {
            var3.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, 1));
            var3.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, 1));
        }
        else
        {
            super.onFoodEaten(var1, var2, var3);
        }
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
