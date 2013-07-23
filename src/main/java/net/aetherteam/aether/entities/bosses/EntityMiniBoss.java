package net.aetherteam.aether.entities.bosses;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.enums.EnumBossType;
import net.aetherteam.aether.interfaces.IAetherBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMiniBoss extends EntityBossMob
    implements IAetherBoss
{
    protected EntityLiving boss;
    private double spawnX;
    private double spawnY;
    private double spawnZ;

    public EntityMiniBoss(World par1World)
    {
        super(par1World);
    }

    public void onUpdate()
    {
        super.onUpdate();
        Side side = FMLCommonHandler.instance().getEffectiveSide();

        if (side.isServer())
        {
            Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

            if ((dungeon != null) && (!dungeon.isActive()) && (!(this.boss instanceof EntityLabyrinthEye)))
            {
                setDead();
            }
        }
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

    public int getMaxHealth()
    {
        return 200;
    }
}

