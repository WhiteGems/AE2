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

    public EntityDartPoison(World world)
    {
        super(world);
    }

    public EntityDartPoison(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityDartPoison(World world, EntityLiving ent)
    {
        super(world, ent);
    }

    public void entityInit()
    {
        super.entityInit();
        this.item = new ItemStack(AetherItems.Dart, 1, 1);
        this.dmg = 1;
    }

    public boolean onHitTarget(Entity entity)
    {
        super.onHitTarget(entity);

        if ((entity instanceof EntityLiving))
        {
            EntityLiving ent = (EntityLiving)entity;

            if (!this.worldObj.isRemote)
            {
                ent.addPotionEffect(new PotionEffect(Potion.poison.id, 200, 0));
                ent.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 3));
                setDead();
            }
        }

        return true;
    }
}

