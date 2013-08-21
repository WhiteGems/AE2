package net.aetherteam.aether.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EntityDamageSource;

public class CustomDamageSource extends EntityDamageSource
{
    private Entity indirectEntity;
    private String deathMessage;

    public CustomDamageSource(String par1Str, Entity par2Entity, Entity par3Entity)
    {
        super(par1Str, par2Entity);
        this.indirectEntity = par3Entity;
    }

    public CustomDamageSource setDeathMessage(String message)
    {
        this.deathMessage = message;
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

    public String getDeathMessage(EntityLiving entity)
    {
        return entity.getEntityName() + this.deathMessage;
    }
}
