package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemHolystoneSword extends ItemSword
{
    public ItemHolystoneSword(int itemID, EnumToolMaterial mat)
    {
        super(itemID, mat);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        if ((new Random()).nextInt(20) == 0 && entityliving1 != null && entityliving1 instanceof EntityPlayer && entityliving.hurtTime > 0 && entityliving.deathTime <= 0)
        {
            if (!entityliving.worldObj.isRemote)
            {
                entityliving.dropItemWithOffset(AetherItems.AmbrosiumShard.itemID, 1, 0.0F);
            }

            itemstack.damageItem(1, entityliving1);
        }

        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public Item setIconName(String name)
    {
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }
}
