package net.aetherteam.aether;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;

public class FrostDamageSource extends DamageSource
{
    protected FrostDamageSource()
    {
        super("Frost bite");
    }

    public String getDeathMessage(EntityLiving par1EntityLiving)
    {
        return par1EntityLiving.getEntityName() + " was frost bitten";
    }

    public boolean isUnblockable()
    {
        return true;
    }

    public boolean isFireDamage()
    {
        return true;
    }
}

