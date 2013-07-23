package net.aetherteam.aether.items;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemPigSlayer extends ItemSword
{
    Random rand;

    public ItemPigSlayer(int i)
    {
        super(i, EnumToolMaterial.IRON);
        this.rand = new Random();
        setMaxDamage(0);
    }

    public Item setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1)
    {
        if ((entityliving == null) || (entityliving1 == null))
        {
            return false;
        }

        String s = EntityList.getEntityString(entityliving);

        if ((s != null) && (!s.equals("")) && ((s.toLowerCase().contains("pig")) || (s.toLowerCase().contains("phyg"))))
        {
            if (entityliving.getHealth() > 0)
            {
                entityliving.setEntityHealth(1);
                entityliving.hurtTime = 0;
                entityliving.attackEntityFrom(DamageSource.causeMobDamage(entityliving1), 9999);
            }

            for (int j = 0; j < 20; j++)
            {
                double d = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d3 = 5.0D;
                entityliving.worldObj.spawnParticle("flame", entityliving.posX + this.rand.nextFloat() * entityliving.width * 2.0F - entityliving.width - d * d3, entityliving.posY + this.rand.nextFloat() * entityliving.height - d1 * d3, entityliving.posZ + this.rand.nextFloat() * entityliving.width * 2.0F - entityliving.width - d2 * d3, d, d1, d2);
            }

            int lootingModifier = 0;

            if ((entityliving1 instanceof EntityPlayer))
            {
                lootingModifier = EnchantmentHelper.getLootingModifier(entityliving1);
            }

            entityliving.isDead = true;
        }

        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving)
    {
        return true;
    }
}

