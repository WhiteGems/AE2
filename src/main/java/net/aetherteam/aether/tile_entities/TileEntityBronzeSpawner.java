package net.aetherteam.aether.tile_entities;

import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityBronzeSpawner extends TileEntityMobSpawner
{
    private final MobSpawnerBaseLogic field_98050_a = new BronzeSpawnerBaseLogic(this);

    public TileEntityBronzeSpawner()
    {
        this.getSpawnerLogic().setMobID("Aerbunny");
    }
}
