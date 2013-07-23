package net.aetherteam.aether.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class AetherWorldType extends WorldType
{
    public AetherWorldType(int par1, String par2Str)
    {
        super(par1, par2Str, 0);
        this.biomesForWorldType[0] = new BiomeGenAether();
    }

    public double getHorizon(World world)
    {
        return 0.0D;
    }

    public boolean hasVoidParticles(boolean var1)
    {
        return false;
    }

    public double voidFadeMagnitude()
    {
        return 1.0D;
    }

    public String getWorldTypeName()
    {
        return "Aether";
    }

    public String getTranslateName()
    {
        return "Aether";
    }

    public WorldChunkManager getChunkManager(World world)
    {
        return new WorldChunkManagerAether(0.5D);
    }

    public IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkProviderAether(world, world.getTotalWorldTime());
    }
}

