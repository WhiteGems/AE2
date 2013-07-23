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

    protected ItemHolystoneAxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, int i, int j, int k, int l, EntityLiving entityliving)
    {
        if (random.nextInt(50) == 0)
        {
            entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
        }

        return super.onBlockDestroyed(itemstack, world, i, j, k, l, entityliving);
    }
}

