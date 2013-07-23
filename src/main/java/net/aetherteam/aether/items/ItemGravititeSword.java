package net.aetherteam.aether.items;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemGravititeSword extends ItemSword
{
    public ItemGravititeSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving hitentity, EntityLiving player)
    {
        if ((player != null) && ((player instanceof EntityPlayer)))
        {
            if ((hitentity.hurtTime > 0) || (hitentity.deathTime > 0))
            {
                hitentity.addVelocity(0.0D, 1.0D, 0.0D);
                itemstack.damageItem(1, player);
            }
        }

        return true;
    }
}

