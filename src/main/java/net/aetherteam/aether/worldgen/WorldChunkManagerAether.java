package net.aetherteam.aether.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerAether extends WorldChunkManager
{
    private BiomeGenBase biomeGenerator;

    public WorldChunkManagerAether(double d)
    {
        this.biomeGenerator = Aether.biome;
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int i, int j, int k, List list)
    {
        return list.contains(this.biomeGenerator);
    }

    /**
     * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
     * Strongly favors positive y positions.
     */
    public ChunkPosition findBiomePosition(int i, int j, int k, List list, Random random)
    {
        return list.contains(this.biomeGenerator) ? new ChunkPosition(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1)) : null;
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l, boolean flag)
    {
        return this.loadBlockGeneratorData(abiomegenbase, i, j, k, l);
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    public BiomeGenBase getBiomeGenAt(int i, int j)
    {
        return this.biomeGenerator;
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] af, int i, int j, int k, int l)
    {
        if (af == null || af.length < k * l)
        {
            af = new float[k * l];
        }

        Arrays.fill(af, 0, k * l, 0.0F);
        return af;
    }

    /**
     * Returns a list of temperatures to use for the specified blocks.  Args: listToReuse, x, y, width, length
     */
    public float[] getTemperatures(float[] af, int i, int j, int k, int l)
    {
        if (af == null || af.length < k * l)
        {
            af = new float[k * l];
        }

        Arrays.fill(af, 0, k * l, 1.0F);
        return af;
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l)
    {
        if (abiomegenbase == null || abiomegenbase.length < k * l)
        {
            abiomegenbase = new BiomeGenBase[k * l];
        }

        Arrays.fill(abiomegenbase, 0, k * l, this.biomeGenerator);
        return abiomegenbase;
    }
}
