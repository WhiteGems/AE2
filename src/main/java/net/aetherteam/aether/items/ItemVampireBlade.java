package net.aetherteam.aether.items;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemVampireBlade extends ItemSword
{
    private float weaponDamage;
    private static Random random = new Random();

    public ItemVampireBlade(int i)
    {
        super(i, EnumToolMaterial.EMERALD);
        this.maxStackSize = 1;
        this.setMaxDamage(EnumToolMaterial.EMERALD.getMaxUses());
        this.weaponDamage = 4.0F + EnumToolMaterial.EMERALD.getDamageVsEntity() * 2.0F;
    }

    public float func_82803_g()
    {
        return this.weaponDamage;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 1.5F;
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
        EntityPlayer player = (EntityPlayer)entityliving1;

        if (Aether.getServerPlayer(player) == null)
        {
            return true;
        }
        else
        {
            if (player.func_110143_aJ() < player.func_110138_aP() && entityliving.hurtTime > 0 && entityliving.deathTime <= 0)
            {
                player.heal(1.0F);
            }

            itemstack.damageItem(1, entityliving1);
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

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        itemstack.damageItem(2, entityliving);
        return true;
    }
}
