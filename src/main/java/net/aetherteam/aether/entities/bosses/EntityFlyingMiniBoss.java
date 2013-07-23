package net.aetherteam.aether.entities.bosses;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.aetherteam.aether.party.Party;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFlyingMiniBoss extends EntityFlying implements IAetherBoss
{
    public EntityFlyingMiniBoss(World var1)
    {
        super(var1);
    }

    public int getMaxHealth()
    {
        return 200;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        DungeonHandler var1 = DungeonHandler.instance();
        Dungeon var2 = var1.getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (var2 != null && !var2.isActive())
        {
            this.setDead();
        }

        if (!this.worldObj.isRemote && this.getHealthTracked() != this.health)
        {
            this.setHealthTracked();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        Dungeon var3 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

        if (var3 != null && var3.hasQueuedParty())
        {
            Party var4 = var3.getQueuedParty();
            int var5 = var3.getQueuedMembers().size() + 1;
            float var6 = (float)(var5 - 1) * 0.075F;
            int var7 = MathHelper.clamp_int((int)((float)var2 - (float)var2 * var6), 1, var2);
            return super.attackEntityFrom(var1, var7);
        }
        else
        {
            return super.attackEntityFrom(var1, var2);
        }
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
        return this.getHealthTracked();
    }

    public int getBossMaxHP()
    {
        return this.getMaxHealth();
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
