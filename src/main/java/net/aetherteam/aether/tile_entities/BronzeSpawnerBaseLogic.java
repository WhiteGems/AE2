package net.aetherteam.aether.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.WeightedRandomMinecart;
import net.minecraft.world.World;

class BronzeSpawnerBaseLogic extends MobSpawnerBaseLogic
{
    final TileEntityMobSpawner field_98295_a;
    private int field_98294_i;

    BronzeSpawnerBaseLogic(TileEntityMobSpawner par1TileEntityMobSpawner)
    {
        this.spawnCount = 9;
        this.field_98295_a = par1TileEntityMobSpawner;
    }

    public void func_98267_a(int par1)
    {
        this.field_98295_a.worldObj.addBlockEvent(this.field_98295_a.xCoord, this.field_98295_a.yCoord, this.field_98295_a.zCoord, Block.mobSpawner.blockID, par1, 0);
    }

    public World getSpawnerWorld()
    {
        return this.field_98295_a.worldObj;
    }

    public int getSpawnerX()
    {
        return this.field_98295_a.xCoord;
    }

    public int getSpawnerY()
    {
        return this.field_98295_a.yCoord;
    }

    public int getSpawnerZ()
    {
        return this.field_98295_a.zCoord;
    }

    public void setRandomMinecart(WeightedRandomMinecart par1WeightedRandomMinecart)
    {
        super.setRandomMinecart(par1WeightedRandomMinecart);

        if (getSpawnerWorld() != null)
        {
            getSpawnerWorld().markBlockForUpdate(this.field_98295_a.xCoord, this.field_98295_a.yCoord, this.field_98295_a.zCoord);
        }
    }
}

