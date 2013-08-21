package net.aetherteam.aether.entities.bosses;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFlyingMiniBoss extends EntityFlying implements IAetherBoss
{
    public EntityFlyingMiniBoss(World par1World)
    {
        super(par1World);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(200.0D);
        this.setEntityHealth(200.0F);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        DungeonHandler handler = DungeonHandler.instance();
        Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (dungeon != null && !dungeon.isActive())
        {
            this.setDead();
        }

        if (!this.worldObj.isRemote && (float)this.getHealthTracked() != this.func_110143_aJ())
        {
            this.setHealthTracked();
        }
    }

    public boolean attackEntityFrom(DamageSource src, int damage)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (dungeon != null && dungeon.hasQueuedParty())
        {
            Party party = dungeon.getQueuedParty();
            int players = dungeon.getQueuedMembers().size() + 1;
            float damageFactor = (float)(players - 1) * 0.075F;
            int newDamage = MathHelper.clamp_int((int)((float)damage - (float)damage * damageFactor), 1, damage);
            return super.attackEntityFrom(src, (float)newDamage);
        }
        else
        {
            return super.attackEntityFrom(src, (float)damage);
        }
    }

    public void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(26, Integer.valueOf((int)this.func_110143_aJ()));
    }

    public int getHealthTracked()
    {
        return this.dataWatcher.getWatchableObjectInt(26);
    }

    public void setHealthTracked()
    {
        this.dataWatcher.updateObject(26, Integer.valueOf((int)this.func_110143_aJ()));
    }

    public int getBossHP()
    {
        return this.getHealthTracked();
    }

    public int getBossMaxHP()
    {
        return 200;
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
