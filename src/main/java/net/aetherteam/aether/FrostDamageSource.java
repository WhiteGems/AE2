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
        return par1EntityLiving.getEntityName() + "被冻伤";
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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.FrostDamageSource
 * JD-Core Version:    0.6.2
 */
