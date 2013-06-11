package net.aetherteam.aether.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EntityDamageSource;

public class CustomDamageSource extends EntityDamageSource
{
    private Entity indirectEntity;
    private String deathMessage;

    public CustomDamageSource(String var1, Entity var2, Entity var3)
    {
        super(var1, var2);
        this.indirectEntity = var3;
    }

    public CustomDamageSource setDeathMessage(String var1)
    {
        this.deathMessage = var1;
        return this;
    }

    public Entity getSourceOfDamage()
    {
        return this.damageSourceEntity;
    }

    public Entity getEntity()
    {
        return this.indirectEntity;
    }

    /**
     * Returns the message to be displayed on player death.
     */
    public String getDeathMessage(EntityLiving var1)
    {
        return var1.getEntityName() + this.deathMessage;
    }
}
