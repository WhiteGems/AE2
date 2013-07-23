package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class ItemPigSlayer extends ItemSword
{
    Random rand = new Random();

    public ItemPigSlayer(int var1)
    {
        super(var1, EnumToolMaterial.IRON);
        this.setMaxDamage(0);
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
        if (var2 != null && var3 != null)
        {
            String var4 = EntityList.getEntityString(var2);

            if (var4 != null && !var4.equals("") && (var4.toLowerCase().contains("pig") || var4.toLowerCase().contains("phyg")))
            {
                if (var2.getHealth() > 0)
                {
                    var2.setEntityHealth(1);
                    var2.hurtTime = 0;
                    var2.attackEntityFrom(DamageSource.causeMobDamage(var3), 9999);
                }

                int var5;

                for (var5 = 0; var5 < 20; ++var5)
                {
                    double var6 = this.rand.nextGaussian() * 0.02D;
                    double var8 = this.rand.nextGaussian() * 0.02D;
                    double var10 = this.rand.nextGaussian() * 0.02D;
                    double var12 = 5.0D;
                    var2.worldObj.spawnParticle("flame", var2.posX + (double)(this.rand.nextFloat() * var2.width * 2.0F) - (double)var2.width - var6 * var12, var2.posY + (double)(this.rand.nextFloat() * var2.height) - var8 * var12, var2.posZ + (double)(this.rand.nextFloat() * var2.width * 2.0F) - (double)var2.width - var10 * var12, var6, var8, var10);
                }

                boolean var14 = false;

                if (var3 instanceof EntityPlayer)
                {
                    var5 = EnchantmentHelper.getLootingModifier(var3);
                }

                var2.isDead = true;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean onBlockDestroyed(ItemStack var1, int var2, int var3, int var4, int var5, EntityLiving var6)
    {
        return true;
    }
}
