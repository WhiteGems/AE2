package net.aetherteam.aether.worldgen;

import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GenerateFloatingTree extends WorldGenerator
{
    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        int var7;

        for (var7 = var3 - 6; var7 < var3 + 6 + 6; ++var7)
        {
            for (int var8 = var4 - 6; var8 < var4 + 11 + 6; ++var8)
            {
                for (int var9 = var5 - 6; var9 < var5 + 6 + 6; ++var9)
                {
                    if (var1.getBlockId(var7, var8, var9) != 0)
                    {
                        var6 = false;
                    }
                }
            }
        }

        if (var4 + 11 <= var1.getHeight() && var6)
        {
            var1.setBlock(var3, var4, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4, var5 + 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4, var5 - 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 + 1, var4, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 - 1, var4, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 1, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 1, var5 + 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 1, var5 + 2, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 1, var5 - 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 1, var5 - 2, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 + 1, var4 + 1, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 + 2, var4 + 1, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 - 1, var4 + 1, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 - 2, var4 + 1, var5, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 + 1, var4 + 1, var5 + 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 + 1, var4 + 1, var5 - 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 - 1, var4 + 1, var5 - 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3 - 1, var4 + 1, var5 + 1, AetherBlocks.Holystone.blockID);
            var1.setBlock(var3, var4 + 2, var5, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3, var4 + 2, var5 + 1, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3, var4 + 2, var5 + 2, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3, var4 + 2, var5 - 1, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3, var4 + 2, var5 - 2, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 + 1, var4 + 2, var5, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 + 2, var4 + 2, var5, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 - 1, var4 + 2, var5, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 - 2, var4 + 2, var5, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 + 1, var4 + 2, var5 + 1, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 + 1, var4 + 2, var5 - 1, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 - 1, var4 + 2, var5 - 1, AetherBlocks.AetherGrass.blockID);
            var1.setBlock(var3 - 1, var4 + 2, var5 + 1, AetherBlocks.AetherGrass.blockID);

            for (var7 = var4 + 3; var7 <= var4 + 9; ++var7)
            {
                var1.setBlock(var3, var7, var5, AetherBlocks.AetherLog.blockID);
            }

            var1.setBlock(var3, var4 + 10, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 5, var5, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3 - 1, var4 + 5, var5, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 5, var5 + 1, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 5, var5 - 1, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 5, var5 - 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 5, var5 + 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 2, var4 + 5, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 2, var4 + 5, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 5, var5 - 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 5, var5 - 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 5, var5 + 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 5, var5 + 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 2, var4 + 5, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 2, var4 + 5, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 2, var4 + 5, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 2, var4 + 5, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 5, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 5, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 5, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 5, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 6, var5 - 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 6, var5 + 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 2, var4 + 6, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 2, var4 + 6, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 6, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 6, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 6, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 6, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 6, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 6, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 6, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 6, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 7, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 7, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 7, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 7, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 8, var5, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3 - 1, var4 + 8, var5, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 8, var5 + 1, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 8, var5 - 1, AetherBlocks.AetherLog.blockID);
            var1.setBlock(var3, var4 + 8, var5 - 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 8, var5 + 2, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 2, var4 + 8, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 2, var4 + 8, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 8, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 8, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 8, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 8, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 + 1, var4 + 9, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3 - 1, var4 + 9, var5, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 9, var5 + 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            var1.setBlock(var3, var4 + 9, var5 - 1, AetherBlocks.CrystalLeaves.blockID, var2.nextInt(2), ChunkProviderAether.placementFlagType);
            return true;
        } else
        {
            return false;
        }
    }
}
