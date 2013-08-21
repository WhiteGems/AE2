package net.aetherteam.aether.items;

import net.minecraft.entity.EntityLivingBase;
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
        this.field_111218_cA = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitentity, EntityLivingBase player)
    {
        if (player != null && player instanceof EntityPlayer && (hitentity.hurtTime > 0 || hitentity.deathTime > 0))
        {
            hitentity.addVelocity(0.0D, 1.0D, 0.0D);
            itemstack.damageItem(1, player);
        }

        return true;
    }
}
