package net.aetherteam.aether.entities;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityDartPoison extends EntityDartGolden
{
    public EntityLiving victim;
    public int poisonTime;

    public EntityDartPoison(World var1)
    {
        super(var1);
    }

    public EntityDartPoison(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
    }

    public EntityDartPoison(World var1, EntityLiving var2)
    {
        super(var1, var2);
    }

    public void entityInit()
    {
        super.entityInit();
        this.item = new ItemStack(AetherItems.Dart, 1, 1);
        this.dmg = 1;
    }

    public boolean onHitTarget(Entity var1)
    {
        super.onHitTarget(var1);

        if (var1 instanceof EntityLiving)
        {
            EntityLiving var2 = (EntityLiving)var1;

            if (!this.worldObj.isRemote)
            {
                var2.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
                var2.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 3));
                this.setDead();
            }
        }

        return true;
    }
}
