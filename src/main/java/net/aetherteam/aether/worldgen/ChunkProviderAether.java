package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ChunkProviderAether
    implements IChunkProvider
{
    private Random random;
    private NoiseGeneratorOctaves noiseGenerator1;
    private NoiseGeneratorOctaves noiseGenerator2;
    private NoiseGeneratorOctaves noiseGenerator3;
    private NoiseGeneratorOctaves noiseGenerator4;
    private NoiseGeneratorOctaves noiseGenerator5;
    public NoiseGeneratorOctaves noiseGenerator6;
    public NoiseGeneratorOctaves noiseGenerator7;
    public NoiseGeneratorOctaves noiseGenerator8;
    private World worldObj;
    private double[] field_28080_q;
    private double[] field_28079_r;
    private double[] field_28078_s;
    private double[] field_28077_t;
    private MapGenBase mapGenCaves;
    double[] field_28093_d;
    double[] field_28092_e;
    double[] field_28091_f;
    double[] field_28090_g;
    double[] field_28089_h;
    int[][] field_28088_i;
    public byte topAetherBlock;
    public byte fillerAetherBlock;
    public static BronzeDungeon bronzeDungeon = new BronzeDungeon();
    public static int gumCount;
    public static int placementFlagType = 2;

    public ChunkProviderAether(World world, long l)
    {
        this.field_28079_r = new double[256];
        this.field_28078_s = new double[256];
        this.field_28077_t = new double[256];
        this.mapGenCaves = new MapGenCaves();
        this.field_28088_i = new int[32][32];
        this.worldObj = world;
        this.random = new Random(l);
        this.noiseGenerator1 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator2 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator3 = new NoiseGeneratorOctaves(this.random, 8);
        this.noiseGenerator4 = new NoiseGeneratorOctaves(this.random, 4);
        this.noiseGenerator5 = new NoiseGeneratorOctaves(this.random, 4);
        this.noiseGenerator6 = new NoiseGeneratorOctaves(this.random, 10);
        this.noiseGenerator7 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator8 = new NoiseGeneratorOctaves(this.random, 8);
    }

    public boolean d()
    {
        return true;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5)
    {
        return null;
    }

    public void func_28071_a(int i, int j, byte[] abyte0)
    {
        byte byte0 = 2;
        int k = byte0 + 1;
        byte byte1 = 33;
        int l = byte0 + 1;
        this.field_28080_q = func_28073_a(this.field_28080_q, i * byte0, 0, j * byte0, k, byte1, l);

        for (int i1 = 0; i1 < byte0; i1++)
            for (int j1 = 0; j1 < byte0; j1++)
                for (int k1 = 0; k1 < 32; k1++)
                {
                    double d = 0.25D;
                    double d1 = this.field_28080_q[(((i1 + 0) * l + j1 + 0) * byte1 + k1 + 0)];
                    double d2 = this.field_28080_q[(((i1 + 0) * l + j1 + 1) * byte1 + k1 + 0)];
                    double d3 = this.field_28080_q[(((i1 + 1) * l + j1 + 0) * byte1 + k1 + 0)];
                    double d4 = this.field_28080_q[(((i1 + 1) * l + j1 + 1) * byte1 + k1 + 0)];
                    double d5 = (this.field_28080_q[(((i1 + 0) * l + j1 + 0) * byte1 + k1 + 1)] - d1) * d;
                    double d6 = (this.field_28080_q[(((i1 + 0) * l + j1 + 1) * byte1 + k1 + 1)] - d2) * d;
                    double d7 = (this.field_28080_q[(((i1 + 1) * l + j1 + 0) * byte1 + k1 + 1)] - d3) * d;
                    double d8 = (this.field_28080_q[(((i1 + 1) * l + j1 + 1) * byte1 + k1 + 1)] - d4) * d;

                    for (int l1 = 0; l1 < 4; l1++)
                    {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 8; i2++)
                        {
                            int j2 = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
                            char c = '';
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int k2 = 0; k2 < 8; k2++)
                            {
                                int l2 = 0;

                                if (d15 > 0.0D)
                                {
                                    l2 = AetherBlocks.Holystone.blockID;
                                }

                                abyte0[j2] = ((byte)l2);
                                j2 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
    }

    public void func_28072_a(int i, int j, byte[] abyte0)
    {
        double d = 0.03125D;
        this.field_28079_r = this.noiseGenerator4.generateNoiseOctaves(this.field_28079_r, i * 16, j * 16, 0, 16, 16, 1, d, d, 1.0D);
        this.field_28078_s = this.noiseGenerator4.generateNoiseOctaves(this.field_28078_s, i * 16, 109, j * 16, 16, 1, 16, d, 1.0D, d);
        this.field_28077_t = this.noiseGenerator5.generateNoiseOctaves(this.field_28077_t, i * 16, j * 16, 0, 16, 16, 1, d * 2.0D, d * 2.0D, d * 2.0D);

        for (int k = 0; k < 16; k++)
            for (int l = 0; l < 16; l++)
            {
                int i1 = (int)(this.field_28077_t[(k + l * 16)] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int j1 = -1;
                this.topAetherBlock = ((byte)AetherBlocks.AetherGrass.blockID);
                this.fillerAetherBlock = ((byte)AetherBlocks.AetherDirt.blockID);
                byte byte0 = this.topAetherBlock;
                byte byte1 = this.fillerAetherBlock;
                byte stone = (byte)AetherBlocks.Holystone.blockID;

                if (byte0 < 0)
                {
                    byte0 = (byte)(byte0 + 0);
                }

                if (byte1 < 0)
                {
                    byte1 = (byte)(byte1 + 0);
                }

                if (stone < 0)
                {
                    stone = (byte)(stone + 0);
                }

                for (int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    byte byte2 = abyte0[l1];

                    if (byte2 == 0)
                    {
                        j1 = -1;
                    }
                    else if (byte2 == stone)
                    {
                        if (j1 == -1)
                        {
                            if (i1 <= 0)
                            {
                                byte0 = 0;
                                byte1 = stone;
                            }

                            j1 = i1;

                            if (k1 >= 0)
                            {
                                abyte0[l1] = byte0;
                            }
                            else
                            {
                                abyte0[l1] = byte1;
                            }
                        }
                        else if (j1 > 0)
                        {
                            j1--;
                            abyte0[l1] = byte1;
                        }
                    }
                }
            }
    }

    private double[] func_28073_a(double[] ad, int i, int j, int k, int l, int i1, int j1)
    {
        if (ad == null)
        {
            ad = new double[l * i1 * j1];
        }

        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        this.field_28090_g = this.noiseGenerator6.generateNoiseOctaves(this.field_28090_g, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        this.field_28089_h = this.noiseGenerator7.generateNoiseOctaves(this.field_28089_h, i, k, l, j1, 200.0D, 200.0D, 0.5D);
        d *= 2.0D;
        this.field_28093_d = this.noiseGenerator3.generateNoiseOctaves(this.field_28093_d, i, j, k, l, i1, j1, d / 80.0D, d1 / 160.0D, d / 80.0D);
        this.field_28092_e = this.noiseGenerator1.generateNoiseOctaves(this.field_28092_e, i, j, k, l, i1, j1, d, d1, d);
        this.field_28091_f = this.noiseGenerator2.generateNoiseOctaves(this.field_28091_f, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / l;

        for (int j2 = 0; j2 < l; j2++)
        {
            int k2 = j2 * i2 + i2 / 2;

            for (int l2 = 0; l2 < j1; l2++)
            {
                int i3 = l2 * i2 + i2 / 2;
                double d4 = 1.0D;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (this.field_28090_g[l1] + 256.0D) / 512.0D;
                d5 *= d4;

                if (d5 > 1.0D)
                {
                    d5 = 1.0D;
                }

                double d6 = this.field_28089_h[l1] / 8000.0D;

                if (d6 < 0.0D)
                {
                    d6 = -d6 * 0.3D;
                }

                d6 = d6 * 3.0D - 2.0D;

                if (d6 > 1.0D)
                {
                    d6 = 1.0D;
                }

                d6 /= 8.0D;
                d6 = 0.0D;

                if (d5 < 0.0D)
                {
                    d5 = 0.0D;
                }

                d5 += 0.5D;
                d6 = d6 * i1 / 16.0D;
                l1++;
                double d7 = i1 / 2.0D;

                for (int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (j3 - d7) * 8.0D / d5;

                    if (d9 < 0.0D)
                    {
                        d9 *= -1.0D;
                    }

                    double d10 = this.field_28092_e[k1] / 512.0D;
                    double d11 = this.field_28091_f[k1] / 512.0D;
                    double d12 = (this.field_28093_d[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d12 < 0.0D)
                    {
                        d8 = d10;
                    }
                    else if (d12 > 1.0D)
                    {
                        d8 = d11;
                    }
                    else
                    {
                        d8 = d10 + (d11 - d10) * d12;
                    }

                    d8 -= 8.0D;
                    int k3 = 32;

                    if (j3 > i1 - k3)
                    {
                        double d13 = (j3 - (i1 - k3)) / (k3 - 1.0F);
                        d8 = d8 * (1.0D - d13) + -30.0D * d13;
                    }

                    k3 = 8;

                    if (j3 < k3)
                    {
                        double d14 = (k3 - j3) / (k3 - 1.0F);
                        d8 = d8 * (1.0D - d14) + -30.0D * d14;
                    }

                    ad[k1] = d8;
                    k1++;
                }
            }
        }

        return ad;
    }

    public void recreateStructures(int i, int j)
    {
    }

    public int f()
    {
        return 0;
    }

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        WorldChunkManager worldchunkmanager = this.worldObj.u();
        BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i >> 4, k >> 4);

        if (biomegenbase == null)
        {
            return null;
        }

        return biomegenbase.getSpawnableList(enumcreaturetype);
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public String e()
    {
        return "RandomLevelSource";
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        int k = i * 16;
        int l = j * 16;

        if (this.random.nextInt(50) == 0)
        {
            int x = k + this.random.nextInt(4);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(4);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 6, 4, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(50) == 0)
        {
            int x = k + this.random.nextInt(4);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(4);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 5, 4, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(50) == 0)
        {
            int x = k + this.random.nextInt(4);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(4);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 4, 4, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(50) == 0)
        {
            int x = k + this.random.nextInt(4);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(4);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 3, 4, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(13) == 0)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(65) + 32;
            int z = l + this.random.nextInt(16);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 3, 8, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(50) == 0)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(32) + 96;
            int z = l + this.random.nextInt(16);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 2, 4, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(13) == 0)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(65) + 32;
            int z = l + this.random.nextInt(16);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 1, 8, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(7) == 0)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(65) + 32;
            int z = l + this.random.nextInt(16);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 0, 16, false, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(25) == 0)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(16);
            new AetherGenClouds(AetherBlocks.Aercloud.blockID, 0, 64, true, bronzeDungeon).generate(this.worldObj, this.random, x, y, z);
        }

        double d = 0.03125D;
        this.field_28077_t = this.noiseGenerator5.generateNoiseOctaves(this.field_28077_t, i, j, 0, 16, 16, 1, d, d, d);
        bronzeDungeon.generateStructuresInChunk(this.worldObj, this.random, i, j, this.field_28077_t);
        BiomeGenBase biomegenbase = Aether.biome;
        net.minecraft.block.BlockSand.fallInstantly = true;
        this.random.setSeed(this.worldObj.getTotalWorldTime());
        long l1 = this.random.nextLong() / 2L * 2L + 1L;
        long l2 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed(i * l1 + j * l2 ^ this.worldObj.getTotalWorldTime());
        d = 0.125D;

        for (int n = 0; n < 20; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.AetherDirt.blockID, 32).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 6; n++)
        {
            int x = k + this.random.nextInt(16) + 8;
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16) + 8;
            new AetherGenFlowers(AetherBlocks.WhiteFlower.blockID, 64).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 6; n++)
        {
            if (this.random.nextInt(2) == 0)
            {
                int x = k + this.random.nextInt(16) + 8;
                int y = this.random.nextInt(128);
                int z = l + this.random.nextInt(16) + 8;
                new AetherGenFlowers(AetherBlocks.PurpleFlower.blockID, 64).generate(this.worldObj, this.random, x, y, z);
            }
        }

        for (int n = 0; n < 10; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.Icestone.blockID, 10).generate(this.worldObj, this.random, i, y, z);
        }

        for (int n = 0; n < 20; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.AmbrosiumOre.blockID, 16).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 15; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(64);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.ZaniteOre.blockID, 8).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 6; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(32);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.GravititeOre.blockID, 4).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 4; n++)
        {
            int x = k + this.random.nextInt(16);
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16);
            new AetherGenMinable(AetherBlocks.ContinuumOre.blockID, 4).generate(this.worldObj, this.random, x, y, z);
        }

        if (this.random.nextInt(5) == 0)
        {
            for (int x = k; x < k + 16; x++)
            {
                for (int z = l; z < l + 16; z++)
                {
                    for (int n = 0; n < 48; n++)
                    {
                        if ((this.worldObj.getBlockId(x, n, z) == 0) && (this.worldObj.getBlockId(x, n + 1, z) == AetherBlocks.AetherGrass.blockID) && (this.worldObj.getBlockId(x, n + 2, z) == 0))
                        {
                            new AetherGenQuicksoil(AetherBlocks.Quicksoil.blockID).generate(this.worldObj, this.random, x, n, z);
                            n = 128;
                        }
                    }
                }
            }
        }

        d = 0.5D;
        int numberofgrassgen = 4;
        int numberoftreegen = 3;

        for (int i11 = 0; i11 < numberoftreegen; i11++)
        {
            int k15 = k + this.random.nextInt(16) + 8;
            int j18 = l + this.random.nextInt(16) + 8;
            WorldGenerator worldgenerator = biomegenbase.getRandomWorldGenForTrees(this.random);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(this.worldObj, this.random, k15, this.worldObj.getHeightValue(k15, j18), j18);
        }

        for (int i11 = 0; i11 < numberofgrassgen; i11++)
        {
            int k15 = k + this.random.nextInt(16) + 8;
            int j18 = l + this.random.nextInt(16) + 8;
            WorldGenerator worldgenerator = biomegenbase.getRandomWorldGenForGrass(this.random);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(this.worldObj, this.random, k15, this.worldObj.getHeightValue(k15, j18), j18);
        }

        for (int n = 0; n < 2; n++)
        {
            int x = k + this.random.nextInt(16) + 8;
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16) + 8;
            new AetherGenFlowers(AetherBlocks.BerryBush.blockID, 32).generate(this.worldObj, this.random, x, y, z);
        }

        for (int n = 0; n < 2; n++)
        {
            int x = k + this.random.nextInt(16) + 8;
            int y = this.random.nextInt(128);
            int z = l + this.random.nextInt(16) + 8;
            new AetherGenOrangeFruit(16).generate(this.worldObj, this.random, x, y, z);
        }

        for (int k17 = 0; k17 < 50; k17++)
        {
            int j20 = k + this.random.nextInt(16) + 8;
            int l21 = this.random.nextInt(this.random.nextInt(120) + 8);
            int l22 = l + this.random.nextInt(16) + 8;
            new AetherGenLiquids(Block.waterMoving.blockID).generate(this.worldObj, this.random, j20, l21, l22);
        }

        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.random);
        net.minecraft.block.BlockSand.fallInstantly = false;
    }

    public Chunk prepareChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        this.random.setSeed(i * 341873128712L + j * 132897987541L);
        byte[] abyte0 = new byte[32768];
        func_28071_a(i, j, abyte0);
        func_28072_a(i, j, abyte0);
        this.mapGenCaves.generate(this, this.worldObj, i, j, abyte0);
        bronzeDungeon.generate(this, this.worldObj, i, j, abyte0);
        Chunk chunk = new Chunk(this.worldObj, abyte0, i, j);
        chunk.generateSkylightMap();
        return chunk;
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        return true;
    }

    public boolean canSave()
    {
        return false;
    }

    public void b()
    {
    }
}

