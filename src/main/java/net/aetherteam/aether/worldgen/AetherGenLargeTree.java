package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenLargeTree extends WorldGenerator
{
    private int leafBlock;
    private int logBlock;
    private int logMetadata;

    public AetherGenLargeTree(int var1, int var2, int var3)
    {
        this.leafBlock = var1;
        this.logBlock = var2;
        this.logMetadata = var3;
    }

    public boolean branch(World var1, Random var2, int var3, int var4, int var5, int var6)
    {
        int var7 = var2.nextInt(3) - 1;
        int var8 = var6;
        int var9 = var2.nextInt(3) - 1;
        int var10 = var3;
        int var11 = var5;

        for (int var12 = 0; var12 < 2; ++var12)
        {
            var3 += var7;
            var4 += var8;
            var5 += var9;
            var10 -= var7;
            var11 -= var9;

            if (var1.getBlockId(var3, var4, var5) == this.leafBlock)
            {
                var1.setBlock(var3, var4, var5, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
                var1.setBlock(var10, var4, var11, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
            }
        }

        return true;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var3, var4 - 1, var5) != AetherBlocks.AetherGrass.blockID && var1.getBlockId(var3, var4 - 1, var5) != AetherBlocks.AetherDirt.blockID)
        {
            return false;
        }
        else
        {
            byte var6 = 11;
            int var7;

            for (var7 = var3 - 3; var7 < var3 + 5; ++var7)
            {
                for (int var8 = var4 + 5; var8 < var4 + 13; ++var8)
                {
                    for (int var9 = var5 - 3; var9 < var5 + 5; ++var9)
                    {
                        if ((var7 - var3) * (var7 - var3) + (var8 - var4 - 8) * (var8 - var4 - 8) + (var9 - var5) * (var9 - var5) < 12 + var2.nextInt(5) && var1.getBlockId(var7, var8, var9) == 0)
                        {
                            var1.setBlock(var7, var8, var9, this.leafBlock);
                        }
                    }
                }
            }

            for (var7 = 0; var7 < var6 - 2; ++var7)
            {
                if (var7 > 4)
                {
                    this.branch(var1, var2, var3, var4 + var7, var5, var7 / 4 - 1);
                }

                var1.setBlock(var3, var4 + var7, var5, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
            }

            return true;
        }
    }
}
