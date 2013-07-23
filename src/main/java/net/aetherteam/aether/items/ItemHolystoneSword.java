package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class ItemHolystoneSword extends ItemSword
{
    public ItemHolystoneSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        if (new Random().nextInt(20) == 0)
        {
            if ((entityliving1 != null) && ((entityliving1 instanceof EntityPlayer)))
            {
                if ((entityliving.hurtTime > 0) && (entityliving.deathTime <= 0))
                {
                    if (!entityliving.worldObj.isRemote)
                    {
                        entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
                    }

                    itemstack.damageItem(1, entityliving1);
                }
            }
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

