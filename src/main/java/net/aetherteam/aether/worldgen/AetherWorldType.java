package net.aetherteam.aether.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class AetherWorldType extends WorldType
{
    public AetherWorldType(int var1, String var2)
    {
        super(var1, var2, 0);
        this.biomesForWorldType[0] = new BiomeGenAether();
    }

    public double getHorizon(World var1)
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

    /**
     * Gets the translation key for the name of this world type.
     */
    public String getTranslateName()
    {
        return "Aether";
    }

    public WorldChunkManager getChunkManager(World var1)
    {
        return new WorldChunkManagerAether(0.5D);
    }

    public IChunkProvider getChunkGenerator(World var1, String var2)
    {
        return new ChunkProviderAether(var1, var1.getSeed());
    }
}
