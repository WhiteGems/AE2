package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemCandyCaneSword extends ItemSword
{
    public ItemCandyCaneSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        if ((new Random().nextInt(25) == 0) &&
                (entityliving1 != null) && ((entityliving1 instanceof EntityPlayer)) && (
                    (entityliving.hurtTime > 0) || (entityliving.deathTime > 0)))
        {
            entityliving.dropItemWithOffset(AetherItems.CandyCane.itemID, 1, 0.0F);
            itemstack.damageItem(1, entityliving1);
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }
}

