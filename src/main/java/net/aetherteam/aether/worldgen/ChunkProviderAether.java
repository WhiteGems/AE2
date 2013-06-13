package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
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

public class ChunkProviderAether implements IChunkProvider
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
    private double[] field_28079_r = new double[256];
    private double[] field_28078_s = new double[256];
    private double[] field_28077_t = new double[256];
    private MapGenBase mapGenCaves = new MapGenCaves();
    double[] field_28093_d;
    double[] field_28092_e;
    double[] field_28091_f;
    double[] field_28090_g;
    double[] field_28089_h;
    int[][] field_28088_i = new int[32][32];
    public byte topAetherBlock;
    public byte fillerAetherBlock;
    public static BronzeDungeon bronzeDungeon = new BronzeDungeon();
    public static int gumCount;
    public static int placementFlagType = 3;

    public ChunkProviderAether(World var1, long var2)
    {
        this.worldObj = var1;
        this.random = new Random(var2);
        this.noiseGenerator1 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator2 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator3 = new NoiseGeneratorOctaves(this.random, 8);
        this.noiseGenerator4 = new NoiseGeneratorOctaves(this.random, 4);
        this.noiseGenerator5 = new NoiseGeneratorOctaves(this.random, 4);
        this.noiseGenerator6 = new NoiseGeneratorOctaves(this.random, 10);
        this.noiseGenerator7 = new NoiseGeneratorOctaves(this.random, 16);
        this.noiseGenerator8 = new NoiseGeneratorOctaves(this.random, 8);
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int var1, int var2)
    {
        return true;
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5)
    {
        return null;
    }

    public void func_28071_a(int var1, int var2, byte[] var3)
    {
        byte var4 = 2;
        int var5 = var4 + 1;
        byte var6 = 33;
        int var7 = var4 + 1;
        this.field_28080_q = this.func_28073_a(this.field_28080_q, var1 * var4, 0, var2 * var4, var5, var6, var7);

        for (int var8 = 0; var8 < var4; ++var8)
        {
            for (int var9 = 0; var9 < var4; ++var9)
            {
                for (int var10 = 0; var10 < 32; ++var10)
                {
                    double var11 = 0.25D;
                    double var13 = this.field_28080_q[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var15 = this.field_28080_q[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var17 = this.field_28080_q[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var19 = this.field_28080_q[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var21 = (this.field_28080_q[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 1] - var13) * var11;
                    double var23 = (this.field_28080_q[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 1] - var15) * var11;
                    double var25 = (this.field_28080_q[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 1] - var17) * var11;
                    double var27 = (this.field_28080_q[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 1] - var19) * var11;

                    for (int var29 = 0; var29 < 4; ++var29)
                    {
                        double var30 = 0.125D;
                        double var32 = var13;
                        double var34 = var15;
                        double var36 = (var17 - var13) * var30;
                        double var38 = (var19 - var15) * var30;

                        for (int var40 = 0; var40 < 8; ++var40)
                        {
                            int var41 = var40 + var8 * 8 << 11 | 0 + var9 * 8 << 7 | var10 * 4 + var29;
                            short var42 = 128;
                            double var43 = 0.125D;
                            double var45 = var32;
                            double var47 = (var34 - var32) * var43;

                            for (int var49 = 0; var49 < 8; ++var49)
                            {
                                int var50 = 0;

                                if (var45 > 0.0D)
                                {
                                    var50 = AetherBlocks.Holystone.blockID;
                                }

                                var3[var41] = (byte) var50;
                                var41 += var42;
                                var45 += var47;
                            }

                            var32 += var36;
                            var34 += var38;
                        }

                        var13 += var21;
                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                    }
                }
            }
        }
    }

    public void func_28072_a(int var1, int var2, byte[] var3)
    {
        double var4 = 0.03125D;
        this.field_28079_r = this.noiseGenerator4.generateNoiseOctaves(this.field_28079_r, var1 * 16, var2 * 16, 0, 16, 16, 1, var4, var4, 1.0D);
        this.field_28078_s = this.noiseGenerator4.generateNoiseOctaves(this.field_28078_s, var1 * 16, 109, var2 * 16, 16, 1, 16, var4, 1.0D, var4);
        this.field_28077_t = this.noiseGenerator5.generateNoiseOctaves(this.field_28077_t, var1 * 16, var2 * 16, 0, 16, 16, 1, var4 * 2.0D, var4 * 2.0D, var4 * 2.0D);

        for (int var6 = 0; var6 < 16; ++var6)
        {
            for (int var7 = 0; var7 < 16; ++var7)
            {
                int var8 = (int) (this.field_28077_t[var6 + var7 * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
                int var9 = -1;
                this.topAetherBlock = (byte) AetherBlocks.AetherGrass.blockID;
                this.fillerAetherBlock = (byte) AetherBlocks.AetherDirt.blockID;
                byte var10 = this.topAetherBlock;
                byte var11 = this.fillerAetherBlock;
                byte var12 = (byte) AetherBlocks.Holystone.blockID;

                if (var10 < 0)
                {
                    var10 = (byte) (var10 + 0);
                }

                if (var11 < 0)
                {
                    var11 = (byte) (var11 + 0);
                }

                if (var12 < 0)
                {
                    var12 = (byte) (var12 + 0);
                }

                for (int var13 = 127; var13 >= 0; --var13)
                {
                    int var14 = (var7 * 16 + var6) * 128 + var13;
                    byte var15 = var3[var14];

                    if (var15 == 0)
                    {
                        var9 = -1;
                    } else if (var15 == var12)
                    {
                        if (var9 == -1)
                        {
                            if (var8 <= 0)
                            {
                                var10 = 0;
                                var11 = var12;
                            }

                            var9 = var8;

                            if (var13 >= 0)
                            {
                                var3[var14] = var10;
                            } else
                            {
                                var3[var14] = var11;
                            }
                        } else if (var9 > 0)
                        {
                            --var9;
                            var3[var14] = var11;
                        }
                    }
                }
            }
        }
    }

    private double[] func_28073_a(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        if (var1 == null)
        {
            var1 = new double[var5 * var6 * var7];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.field_28090_g = this.noiseGenerator6.generateNoiseOctaves(this.field_28090_g, var2, var4, var5, var7, 1.121D, 1.121D, 0.5D);
        this.field_28089_h = this.noiseGenerator7.generateNoiseOctaves(this.field_28089_h, var2, var4, var5, var7, 200.0D, 200.0D, 0.5D);
        var8 *= 2.0D;
        this.field_28093_d = this.noiseGenerator3.generateNoiseOctaves(this.field_28093_d, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.field_28092_e = this.noiseGenerator1.generateNoiseOctaves(this.field_28092_e, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        this.field_28091_f = this.noiseGenerator2.generateNoiseOctaves(this.field_28091_f, var2, var3, var4, var5, var6, var7, var8, var10, var8);
        int var12 = 0;
        int var13 = 0;
        int var14 = 16 / var5;

        for (int var15 = 0; var15 < var5; ++var15)
        {
            int var16 = var15 * var14 + var14 / 2;

            for (int var17 = 0; var17 < var7; ++var17)
            {
                int var18 = var17 * var14 + var14 / 2;
                double var19 = 1.0D;
                var19 *= var19;
                var19 *= var19;
                var19 = 1.0D - var19;
                double var21 = (this.field_28090_g[var13] + 256.0D) / 512.0D;
                var21 *= var19;

                if (var21 > 1.0D)
                {
                    var21 = 1.0D;
                }

                double var23 = this.field_28089_h[var13] / 8000.0D;

                if (var23 < 0.0D)
                {
                    var23 = -var23 * 0.3D;
                }

                var23 = var23 * 3.0D - 2.0D;

                if (var23 > 1.0D)
                {
                    var23 = 1.0D;
                }

                var23 /= 8.0D;
                var23 = 0.0D;

                if (var21 < 0.0D)
                {
                    var21 = 0.0D;
                }

                var21 += 0.5D;
                var23 = var23 * (double) var6 / 16.0D;
                ++var13;
                double var25 = (double) var6 / 2.0D;

                for (int var27 = 0; var27 < var6; ++var27)
                {
                    double var28 = 0.0D;
                    double var30 = ((double) var27 - var25) * 8.0D / var21;

                    if (var30 < 0.0D)
                    {
                        var30 *= -1.0D;
                    }

                    double var32 = this.field_28092_e[var12] / 512.0D;
                    double var34 = this.field_28091_f[var12] / 512.0D;
                    double var36 = (this.field_28093_d[var12] / 10.0D + 1.0D) / 2.0D;

                    if (var36 < 0.0D)
                    {
                        var28 = var32;
                    } else if (var36 > 1.0D)
                    {
                        var28 = var34;
                    } else
                    {
                        var28 = var32 + (var34 - var32) * var36;
                    }

                    var28 -= 8.0D;
                    byte var38 = 32;
                    double var39;

                    if (var27 > var6 - var38)
                    {
                        var39 = (double) ((float) (var27 - (var6 - var38)) / ((float) var38 - 1.0F));
                        var28 = var28 * (1.0D - var39) + -30.0D * var39;
                    }

                    var38 = 8;

                    if (var27 < var38)
                    {
                        var39 = (double) ((float) (var38 - var27) / ((float) var38 - 1.0F));
                        var28 = var28 * (1.0D - var39) + -30.0D * var39;
                    }

                    var1[var12] = var28;
                    ++var12;
                }
            }
        }

        return var1;
    }

    public void recreateStructures(int var1, int var2) {}

    @Override
    public void func_104112_b()
    {
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4)
    {
        WorldChunkManager var5 = this.worldObj.getWorldChunkManager();
        BiomeGenBase var6 = var5.getBiomeGenAt(var2 >> 4, var4 >> 4);
        return var6 == null ? null : var6.getSpawnableList(var1);
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int var1, int var2)
    {
        return this.provideChunk(var1, var2);
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider var1, int var2, int var3)
    {
        int var4 = var2 * 16;
        int var5 = var3 * 16;
        int var8;
        int var7;
        int var6;

        if (this.random.nextInt(50) == 0)
        {
            var6 = var4 + this.random.nextInt(4);
            var7 = this.random.nextInt(32);
            var8 = var5 + this.random.nextInt(4);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 6, 4, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(50) == 0)
        {
            var6 = var4 + this.random.nextInt(4);
            var7 = this.random.nextInt(32);
            var8 = var5 + this.random.nextInt(4);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 5, 4, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(50) == 0)
        {
            var6 = var4 + this.random.nextInt(4);
            var7 = this.random.nextInt(32);
            var8 = var5 + this.random.nextInt(4);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 4, 4, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(50) == 0)
        {
            var6 = var4 + this.random.nextInt(4);
            var7 = this.random.nextInt(32);
            var8 = var5 + this.random.nextInt(4);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 3, 4, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(13) == 0)
        {
            var6 = var4 + this.random.nextInt(16);
            var7 = this.random.nextInt(65) + 32;
            var8 = var5 + this.random.nextInt(16);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 3, 8, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(50) == 0)
        {
            var6 = var4 + this.random.nextInt(16);
            var7 = this.random.nextInt(32) + 96;
            var8 = var5 + this.random.nextInt(16);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 2, 4, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(13) == 0)
        {
            var6 = var4 + this.random.nextInt(16);
            var7 = this.random.nextInt(65) + 32;
            var8 = var5 + this.random.nextInt(16);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 1, 8, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(7) == 0)
        {
            var6 = var4 + this.random.nextInt(16);
            var7 = this.random.nextInt(65) + 32;
            var8 = var5 + this.random.nextInt(16);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 0, 16, false, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        if (this.random.nextInt(25) == 0)
        {
            var6 = var4 + this.random.nextInt(16);
            var7 = this.random.nextInt(32);
            var8 = var5 + this.random.nextInt(16);
            (new AetherGenClouds(AetherBlocks.Aercloud.blockID, 0, 64, true, bronzeDungeon)).generate(this.worldObj, this.random, var6, var7, var8);
        }

        double var23 = 0.03125D;
        this.field_28077_t = this.noiseGenerator5.generateNoiseOctaves(this.field_28077_t, var2, var3, 0, 16, 16, 1, var23 * 2.0D, var23 * 2.0D, var23 * 2.0D);
        bronzeDungeon.generateStructuresInChunk(this.worldObj, this.random, var2, var3, this.field_28077_t);
        BiomeGenBase var22 = Aether.biome;
        BlockSand.fallInstantly = true;
        this.random.setSeed(this.worldObj.getSeed());
        long var9 = this.random.nextLong() / 2L * 2L + 1L;
        long var11 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long) var2 * var9 + (long) var3 * var11 ^ this.worldObj.getSeed());
        var23 = 0.125D;
        int var13;
        int var15;
        int var14;

        if (this.random.nextInt(32) == 0)
        {
            var13 = var4 + this.random.nextInt(16);
            var14 = this.random.nextInt(64) + 32;
            var15 = var5 + this.random.nextInt(16);
            (new GenerateFloatingTree()).generate(this.worldObj, this.random, var13, var14, var15);
        }

        if (gumCount < 800)
        {
            ++gumCount;
        }

        int var16;

        for (var13 = 0; var13 < 20; ++var13)
        {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.AetherDirt.blockID, 32)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        for (var13 = 0; var13 < 6; ++var13)
        {
            var14 = var4 + this.random.nextInt(16) + 8;
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16) + 8;
            (new AetherGenFlowers(AetherBlocks.WhiteFlower.blockID, 64)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        for (var13 = 0; var13 < 6; ++var13)
        {
            if (this.random.nextInt(2) == 0)
            {
                var14 = var4 + this.random.nextInt(16) + 8;
                var15 = this.random.nextInt(128);
                var16 = var5 + this.random.nextInt(16) + 8;
                (new AetherGenFlowers(AetherBlocks.PurpleFlower.blockID, 64)).generate(this.worldObj, this.random, var14, var15, var16);
            }
        }

        for (var13 = 0; var13 < 10; ++var13)
        {
            int var10000 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.Icestone.blockID, 10)).generate(this.worldObj, this.random, var2, var15, var16);
        }

        for (var13 = 0; var13 < 20; ++var13)
        {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.AmbrosiumOre.blockID, 16)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        for (var13 = 0; var13 < 15; ++var13)
        {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(64);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.ZaniteOre.blockID, 8)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        for (var13 = 0; var13 < 6; ++var13)
        {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(32);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.GravititeOre.blockID, 4)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        for (var13 = 0; var13 < 4; ++var13)
        {
            var14 = var4 + this.random.nextInt(16);
            var15 = this.random.nextInt(128);
            var16 = var5 + this.random.nextInt(16);
            (new AetherGenMinable(AetherBlocks.ContinuumOre.blockID, 4)).generate(this.worldObj, this.random, var14, var15, var16);
        }

        if (this.random.nextInt(5) == 0)
        {
            for (var13 = var4; var13 < var4 + 16; ++var13)
            {
                for (var14 = var5; var14 < var5 + 16; ++var14)
                {
                    for (var15 = 0; var15 < 48; ++var15)
                    {
                        if (this.worldObj.getBlockId(var13, var15, var14) == 0 && this.worldObj.getBlockId(var13, var15 + 1, var14) == AetherBlocks.AetherGrass.blockID && this.worldObj.getBlockId(var13, var15 + 2, var14) == 0)
                        {
                            (new AetherGenQuicksoil(AetherBlocks.Quicksoil.blockID)).generate(this.worldObj, this.random, var13, var15, var14);
                            var15 = 128;
                        }
                    }
                }
            }
        }

        var23 = 0.5D;
        byte var20 = 3;
        byte var21 = 4;
        int var17;
        WorldGenerator var18;

        for (var15 = 0; var15 < var20; ++var15)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = var5 + this.random.nextInt(16) + 8;
            var18 = var22.getRandomWorldGenForTrees(this.random);
            var18.setScale(1.0D, 1.0D, 1.0D);
            var18.generate(this.worldObj, this.random, var16, this.worldObj.getHeightValue(var16, var17), var17);
        }

        for (var15 = 0; var15 < var21; ++var15)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = var5 + this.random.nextInt(16) + 8;
            var18 = var22.getRandomWorldGenForGrass(this.random);
            var18.setScale(1.0D, 1.0D, 1.0D);
            var18.generate(this.worldObj, this.random, var16, this.worldObj.getHeightValue(var16, var17), var17);
        }

        int var19;

        for (var15 = 0; var15 < 2; ++var15)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(128);
            var19 = var5 + this.random.nextInt(16) + 8;
            (new AetherGenFlowers(AetherBlocks.BerryBush.blockID, 32)).generate(this.worldObj, this.random, var16, var17, var19);
        }

        for (var15 = 0; var15 < 2; ++var15)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(128);
            var19 = var5 + this.random.nextInt(16) + 8;
            (new AetherGenOrangeFruit(16)).generate(this.worldObj, this.random, var16, var17, var19);
        }

        for (var15 = 0; var15 < 50; ++var15)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(this.random.nextInt(120) + 8);
            var19 = var5 + this.random.nextInt(16) + 8;
            (new AetherGenLiquids(Block.waterMoving.blockID)).generate(this.worldObj, this.random, var16, var17, var19);
        }

        SpawnerAnimals.performWorldGenSpawning(this.worldObj, var22, var4 + 8, var5 + 8, 16, 16, this.random);
        BlockSand.fallInstantly = false;
    }

    public Chunk prepareChunk(int var1, int var2)
    {
        return this.provideChunk(var1, var2);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int var1, int var2)
    {
        this.random.setSeed((long) var1 * 341873128712L + (long) var2 * 132897987541L);
        byte[] var3 = new byte[32768];
        this.func_28071_a(var1, var2, var3);
        this.func_28072_a(var1, var2, var3);
        this.mapGenCaves.generate(this, this.worldObj, var1, var2, var3);
        bronzeDungeon.generate(this, this.worldObj, var1, var2, var3);
        AetherChunk var4 = new AetherChunk(this.worldObj, var3, var1, var2);
        var4.generateSkylightMap();
        return var4;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean var1, IProgressUpdate var2)
    {
        return true;
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }
}
