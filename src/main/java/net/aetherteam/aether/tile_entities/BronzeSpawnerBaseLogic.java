package net.aetherteam.aether.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.WeightedRandomMinecart;
import net.minecraft.world.World;

class BronzeSpawnerBaseLogic extends MobSpawnerBaseLogic
{
    /** The mob spawner we deal with */
    final TileEntityMobSpawner mobSpawnerEntity;

    /** A counter for spawn tries. */
    private int spawnCount = 9;

    BronzeSpawnerBaseLogic(TileEntityMobSpawner var1)
    {
        this.mobSpawnerEntity = var1;
    }

    public void func_98267_a(int var1)
    {
        this.mobSpawnerEntity.worldObj.addBlockEvent(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord, Block.mobSpawner.blockID, var1, 0);
    }

    public World getSpawnerWorld()
    {
        return this.mobSpawnerEntity.worldObj;
    }

    public int getSpawnerX()
    {
        return this.mobSpawnerEntity.xCoord;
    }

    public int getSpawnerY()
    {
        return this.mobSpawnerEntity.yCoord;
    }

    public int getSpawnerZ()
    {
        return this.mobSpawnerEntity.zCoord;
    }

    public void setRandomMinecart(WeightedRandomMinecart var1)
    {
        super.setRandomMinecart(var1);

        if (this.getSpawnerWorld() != null)
        {
            this.getSpawnerWorld().markBlockForUpdate(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord);
        }
    }
}
