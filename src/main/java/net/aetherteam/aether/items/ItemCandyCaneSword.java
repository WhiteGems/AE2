package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
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
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        if (entityliving.deathTime > 0)
        {
            return true;
        }
        else
        {
            if ((new Random()).nextBoolean() && entityliving1 != null && entityliving1 instanceof EntityPlayer && !entityliving1.worldObj.isRemote && entityliving.hurtTime > 0)
            {
                entityliving.dropItemWithOffset(AetherItems.CandyCane.itemID, 1, 0.0F);
            }

            itemstack.damageItem(1, entityliving1);
            return true;
        }
    }
}
