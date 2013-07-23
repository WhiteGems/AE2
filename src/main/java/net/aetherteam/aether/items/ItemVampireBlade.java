package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemVampireBlade extends ItemSword
{
    private int weaponDamage;
    private static Random random = new Random();

    public ItemVampireBlade(int var1)
    {
        super(var1, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        this.setMaxDamage(EnumToolMaterial.EMERALD.getMaxUses());
        this.weaponDamage = 4 + EnumToolMaterial.EMERALD.getDamageVsEntity() * 2;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity var1)
    {
        return this.weaponDamage;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack var1, Block var2)
    {
        return 1.5F;
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
        EntityPlayer var4 = (EntityPlayer)var3;

        if (Aether.getServerPlayer(var4) == null)
        {
            return true;
        }
        else
        {
            if (var4.getHealth() < Aether.getServerPlayer(var4).maxHealth && var2.hurtTime > 0 && var2.deathTime <= 0)
            {
                var4.heal(1);
            }

            var1.damageItem(1, var3);
            return true;
        }
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    public boolean onBlockDestroyed(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6)
    {
        var1.damageItem(2, var6);
        return true;
    }
}
