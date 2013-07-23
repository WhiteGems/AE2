package net.aetherteam.aether.entities;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityPoisonNeedle extends EntityDartPoison
{
    public EntityPoisonNeedle(World world)
    {
        super(world);
    }

    public EntityPoisonNeedle(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityPoisonNeedle(World world, EntityLiving ent)
    {
        super(world, ent);
    }

    public void entityInit()
    {
        super.entityInit();
        this.dmg = 0;
        this.speed = 1.5F;
        this.curvature = 0.03F;
    }

    public boolean onHitBlock()
    {
        this.curvature = 0.03F;
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return this.victim == null;
    }
}

