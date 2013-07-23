package net.aetherteam.aether.entities.bosses;

import java.util.ArrayList;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFlyingMiniBoss extends EntityFlying
    implements IAetherBoss
{
    public EntityFlyingMiniBoss(World par1World)
    {
        super(par1World);
    }

    public int getMaxHealth()
    {
        return 200;
    }

    public void onUpdate()
    {
        super.onUpdate();
        DungeonHandler handler = DungeonHandler.instance();
        Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if ((dungeon != null) && (!dungeon.isActive()))
        {
            setDead();
        }

        if ((!this.worldObj.isRemote) && (getHealthTracked() != this.health))
        {
            setHealthTracked();
        }
    }

    public boolean attackEntityFrom(DamageSource src, int damage)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if ((dungeon != null) && (dungeon.hasQueuedParty()))
        {
            Party party = dungeon.getQueuedParty();
            int players = dungeon.getQueuedMembers().size() + 1;
            float damageFactor = (players - 1) * 0.075F;
            int newDamage = MathHelper.clamp_int((int)(damage - damage * damageFactor), 1, damage);
            return super.attackEntityFrom(src, newDamage);
        }

        if (!super.attackEntityFrom(src, damage))
        {
            return false;
        }

        return true;
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(26, Integer.valueOf(this.health));
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(26, Integer.valueOf(this.health));
    }

    public int getBossHP()
    {
        return getHealthTracked();
    }

    public int getBossMaxHP()
    {
        return getMaxHealth();
    }

    public int getBossEntityID()
    {
        return this.entityId;
    }

    public String getBossTitle()
    {
        return "";
    }

    public Entity getBossEntity()
    {
        return this;
    }

    public int getBossStage()
    {
        return 0;
    }

    public EnumBossType getBossType()
    {
        return EnumBossType.MINI;
    }
}

