package net.aetherteam.aether;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;

public class FrostDamageSource extends DamageSource
{
    protected FrostDamageSource()
    {
        super("Frost bite");
    }

    /**
     * Returns the message to be displayed on player death.
     */
    public String getDeathMessage(EntityLiving par1EntityLiving)
    {
        return par1EntityLiving.getEntityName() + "被冻伤";
    }

    public boolean isUnblockable()
    {
        return true;
    }

    /**
     * Returns true if the damage is fire based.
     */
    public boolean isFireDamage()
    {
        return true;
    }
}
