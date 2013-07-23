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

    public boolean areBiomesViable(int i, int j, int k, List list)
    {
        return list.contains(this.biomeGenerator);
    }

    public ChunkPosition findBiomePosition(int i, int j, int k, List list, Random random)
    {
        if (list.contains(this.biomeGenerator))
        {
            return new ChunkPosition(i - k + random.nextInt(k * 2 + 1), 0, j - k + random.nextInt(k * 2 + 1));
        }

        return null;
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l, boolean flag)
    {
        return loadBlockGeneratorData(abiomegenbase, i, j, k, l);
    }

    public BiomeGenBase getBiomeGenAt(int i, int j)
    {
        return this.biomeGenerator;
    }

    public float[] getRainfall(float[] af, int i, int j, int k, int l)
    {
        if ((af == null) || (af.length < k * l))
        {
            af = new float[k * l];
        }

        Arrays.fill(af, 0, k * l, 0.0F);
        return af;
    }

    public float[] getTemperatures(float[] af, int i, int j, int k, int l)
    {
        if ((af == null) || (af.length < k * l))
        {
            af = new float[k * l];
        }

        Arrays.fill(af, 0, k * l, 1.0F);
        return af;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l)
    {
        if ((abiomegenbase == null) || (abiomegenbase.length < k * l))
        {
            abiomegenbase = new BiomeGenBase[k * l];
        }

        Arrays.fill(abiomegenbase, 0, k * l, this.biomeGenerator);
        return abiomegenbase;
    }
}

