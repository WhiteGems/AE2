package net.aetherteam.aether.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityProjectileSentry extends EntityThrowable
{
    public EntityLiving victim;
    public float renderYawOffset;
    public static int texfxindex = 94;

    public EntityProjectileSentry(World var1)
    {
        super(var1);
    }

    public EntityProjectileSentry(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
    }

    public EntityProjectileSentry(World var1, double var2, double var4, double var6, EntityLiving var8)
    {
        super(var1, var2, var4, var6);
    }

    public EntityProjectileSentry(World var1, EntityLiving var2)
    {
        super(var1, var2);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition var1)
    {
        if (var1.entityHit == null || var1.entityHit != this.getThrower())
        {
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 0.5F, false);

            if (var1.entityHit != null)
            {
                var1.entityHit.attackEntityFrom(DamageSource.generic.setExplosion(), 2);
            }

            this.setDead();
        }
    }
}
