package net.aetherteam.aether.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class EntityPoisonNeedle extends EntityDartPoison
{
    public EntityPoisonNeedle(World var1)
    {
        super(var1);
    }

    public EntityPoisonNeedle(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
    }

    public EntityPoisonNeedle(World var1, EntityLiving var2)
    {
        super(var1, var2);
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
