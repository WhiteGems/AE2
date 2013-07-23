package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHolystoneAxe extends ItemAxe
{
    private static Random random = new Random();

    protected ItemHolystoneAxe(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    public boolean onBlockDestroyed(ItemStack var1, World var2, int var3, int var4, int var5, int var6, EntityLiving var7)
    {
        if (random.nextInt(50) == 0)
        {
            var7.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
        }

        return super.onBlockDestroyed(var1, var2, var3, var4, var5, var6, var7);
    }
}
