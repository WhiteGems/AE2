package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class ItemPigSlayer extends ItemSword
{
    Random rand = new Random();

    public ItemPigSlayer(int i)
    {
        super(i, EnumToolMaterial.IRON);
        this.setMaxDamage(0);
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
        if (entityliving != null && entityliving1 != null)
        {
            String s = EntityList.getEntityString(entityliving);

            if (s != null && !s.equals("") && (s.toLowerCase().contains("pig") || s.toLowerCase().contains("phyg")))
            {
                if (entityliving.func_110143_aJ() > 0.0F)
                {
                    entityliving.setEntityHealth(1.0F);
                    entityliving.hurtTime = 0;
                    entityliving.attackEntityFrom(DamageSource.causeMobDamage(entityliving1), 9999.0F);
                }

                int lootingModifier;

                for (lootingModifier = 0; lootingModifier < 20; ++lootingModifier)
                {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    double d3 = 5.0D;
                    entityliving.worldObj.spawnParticle("flame", entityliving.posX + (double)(this.rand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width - d * d3, entityliving.posY + (double)(this.rand.nextFloat() * entityliving.height) - d1 * d3, entityliving.posZ + (double)(this.rand.nextFloat() * entityliving.width * 2.0F) - (double)entityliving.width - d2 * d3, d, d1, d2);
                }

                boolean var14 = false;

                if (entityliving1 instanceof EntityPlayer)
                {
                    lootingModifier = EnchantmentHelper.getLootingModifier(entityliving1);
                }

                entityliving.isDead = true;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        return true;
    }
}
