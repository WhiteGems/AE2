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
    public ItemCandyCaneSword(int var1, EnumToolMaterial var2)
    {
        super(var1, var2);
    }

    public Item setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        if ((new Random()).nextInt(25) == 0 && var3 != null && var3 instanceof EntityPlayer && (var2.hurtTime > 0 || var2.deathTime > 0))
        {
            var2.dropItemWithOffset(AetherItems.CandyCane.itemID, 1, 0.0F);
            var1.damageItem(1, var3);
        }

        var1.damageItem(1, var3);
        return true;
    }
}
