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
    public ItemAetherFood(int i, int j, boolean flag)
    {
        super(i, j, flag);
    }

    public ItemAetherFood(int i, int j, int k, int l, boolean flag)
    {
        super(i, j, flag);
    }

    protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if ((!par2World.isRemote) && (par1ItemStack.itemID == AetherItems.Strawberry.itemID))
        {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, 1));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, 1));
        }
        else
        {
            super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        }
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

