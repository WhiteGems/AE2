package net.aetherteam.aether.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenClouds extends WorldGenerator
{
    BronzeDungeon dungeon;
    private int cloudBlockId;
    private int meta;
    private int numberOfBlocks;
    private boolean flat;

    public AetherGenClouds(int var1, int var2, int var3, boolean var4, BronzeDungeon var5)
    {
        this.dungeon = var5;
        this.cloudBlockId = var1;
        this.meta = var2;
        this.numberOfBlocks = var3;
        this.flat = var4;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        int var6 = var3;
        int var7 = var4;
        int var8 = var5;
        int var9 = var2.nextInt(3) - 1;
        int var10 = var2.nextInt(3) - 1;

        if (this.meta >= 3)
        {
            ;
        }

        for (int var11 = 0; var11 < this.numberOfBlocks; ++var11)
        {
            var6 += var2.nextInt(3) - 1 + var9;

            if (var2.nextBoolean() && !this.flat || this.flat && var2.nextInt(10) == 0)
            {
                var7 += var2.nextInt(3) - 1;
            }

            var8 += var2.nextInt(3) - 1 + var10;

            for (int var12 = var6; var12 < var6 + var2.nextInt(4) + 3 * (this.flat ? 3 : 1); ++var12)
            {
                for (int var13 = var7; var13 < var7 + var2.nextInt(1) + 2; ++var13)
                {
                    for (int var14 = var8; var14 < var8 + var2.nextInt(4) + 3 * (this.flat ? 3 : 1); ++var14)
                    {
                        if (var1.getBlockId(var12, var13, var14) == 0 && Math.abs(var12 - var6) + Math.abs(var13 - var7) + Math.abs(var14 - var8) < 4 * (this.flat ? 3 : 1) + var2.nextInt(2) && !this.dungeon.hasStructureAt(var12, var13, var14))
                        {
                            var1.setBlock(var12, var13, var14, this.cloudBlockId, this.meta, ChunkProviderAether.placementFlagType);
                        }
                    }
                }
            }
        }

        return true;
    }
}
