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

    public WorldChunkManagerAether(double var1)
    {
        this.biomeGenerator = Aether.biome;
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int var1, int var2, int var3, List var4)
    {
        return var4.contains(this.biomeGenerator);
    }

    /**
     * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
     * Strongly favors positive y positions.
     */
    public ChunkPosition findBiomePosition(int var1, int var2, int var3, List var4, Random var5)
    {
        return var4.contains(this.biomeGenerator) ? new ChunkPosition(var1 - var3 + var5.nextInt(var3 * 2 + 1), 0, var2 - var3 + var5.nextInt(var3 * 2 + 1)) : null;
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] var1, int var2, int var3, int var4, int var5, boolean var6)
    {
        return this.loadBlockGeneratorData(var1, var2, var3, var4, var5);
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    public BiomeGenBase getBiomeGenAt(int var1, int var2)
    {
        return this.biomeGenerator;
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] var1, int var2, int var3, int var4, int var5)
    {
        if (var1 == null || var1.length < var4 * var5)
        {
            var1 = new float[var4 * var5];
        }

        Arrays.fill(var1, 0, var4 * var5, 0.0F);
        return var1;
    }

    /**
     * Returns a list of temperatures to use for the specified blocks.  Args: listToReuse, x, y, width, length
     */
    public float[] getTemperatures(float[] var1, int var2, int var3, int var4, int var5)
    {
        if (var1 == null || var1.length < var4 * var5)
        {
            var1 = new float[var4 * var5];
        }

        Arrays.fill(var1, 0, var4 * var5, 1.0F);
        return var1;
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] var1, int var2, int var3, int var4, int var5)
    {
        if (var1 == null || var1.length < var4 * var5)
        {
            var1 = new BiomeGenBase[var4 * var5];
        }

        Arrays.fill(var1, 0, var4 * var5, this.biomeGenerator);
        return var1;
    }
}
