package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenMassiveTree extends WorldGenerator
{
    int leaves;
    int randHeight;
    boolean branches;

    public AetherGenMassiveTree(int var1, int var2, boolean var3)
    {
        this.leaves = var1;
        this.randHeight = var2;
        this.branches = var3;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        int var7 = var2.nextInt(this.randHeight) + (this.branches ? 8 : 4);
        int var8;
        int var9;
        int var10;

        for (var8 = var3 - 3; var8 < var3 + 3; ++var8)
        {
            for (var9 = var4 + 1; var9 < var4 + var7 + 2; ++var9)
            {
                for (var10 = var5 - 3; var10 < var5 + 3; ++var10)
                {
                    if (var1.getBlockId(var8, var9, var10) != 0)
                    {
                        var6 = false;
                    }
                }
            }
        }

        if (var4 + var7 + 2 <= var1.getHeight() && var6)
        {
            var8 = var1.getBlockId(var3, var4 - 1, var5);

            if (var8 != AetherBlocks.AetherGrass.blockID && var8 != AetherBlocks.AetherDirt.blockID)
            {
                return false;
            }
            else
            {
                var1.setBlock(var3, var4 - 1, var5, AetherBlocks.AetherDirt.blockID);

                for (var9 = var4; var9 <= var4 + var7; ++var9)
                {
                    var1.setBlock(var3, var9, var5, AetherBlocks.AetherLog.blockID);
                }

                if (this.branches)
                {
                    var1.setBlock(var3 + 1, var4, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 + 1, var4 + 1, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 + 2, var4, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 - 1, var4, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 - 1, var4 + 1, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 - 2, var4, var5, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4, var5 + 1, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4 + 1, var5 + 1, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4, var5 + 2, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4, var5 - 1, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4 + 1, var5 - 1, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3, var4, var5 - 2, AetherBlocks.SkyrootLogWall.blockID);
                    var1.setBlock(var3 + 1, var4 - 1, var5, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3 + 2, var4 - 1, var5, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3 - 1, var4 - 1, var5, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3 - 2, var4 - 1, var5, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3, var4 - 1, var5 + 1, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3, var4 - 1, var5 + 2, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3, var4 - 1, var5 - 1, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3, var4 - 1, var5 - 2, AetherBlocks.AetherGrass.blockID);
                    var1.setBlock(var3 + 1, var4 - 2, var5, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3 + 2, var4 - 2, var5, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3 - 1, var4 - 2, var5, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3 - 2, var4 - 2, var5, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3, var4 - 2, var5 + 1, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3, var4 - 2, var5 + 2, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3, var4 - 2, var5 - 1, AetherBlocks.AetherDirt.blockID);
                    var1.setBlock(var3, var4 - 2, var5 - 2, AetherBlocks.AetherDirt.blockID);
                }

                this.setBlockAirCheck(var1, var3, var4 + var7 + 1, var5, this.leaves);
                this.setBlockAirCheck(var1, var3, var4 + var7 + 2, var5, this.leaves);

                for (var9 = var4 + 2; var9 <= var4 + var7 + 1; ++var9)
                {
                    this.setBlockAirCheck(var1, var3 + 1, var9, var5, this.leaves);
                    this.setBlockAirCheck(var1, var3 - 1, var9, var5, this.leaves);
                    this.setBlockAirCheck(var1, var3, var9, var5 + 1, this.leaves);
                    this.setBlockAirCheck(var1, var3, var9, var5 - 1, this.leaves);
                }

                for (var9 = var4 + 3; var9 <= var4 + var7; var9 += 2)
                {
                    this.setBlockAirCheck(var1, var3 + 1, var9, var5 + 1, this.leaves);
                    this.setBlockAirCheck(var1, var3 - 1, var9, var5 - 1, this.leaves);
                    this.setBlockAirCheck(var1, var3 + 1, var9, var5 - 1, this.leaves);
                    this.setBlockAirCheck(var1, var3 - 1, var9, var5 + 1, this.leaves);
                    this.setBlockAirCheck(var1, var3 + 2, var9, var5, this.leaves);
                    this.setBlockAirCheck(var1, var3 - 2, var9, var5, this.leaves);
                    this.setBlockAirCheck(var1, var3, var9, var5 + 2, this.leaves);
                    this.setBlockAirCheck(var1, var3, var9, var5 - 2, this.leaves);
                }

                if (this.branches)
                {
                    var9 = var2.nextInt(3);

                    for (var10 = var4 + var2.nextInt(2) + 3; var10 <= var4 + var7 - 2; var10 += 1 + var2.nextInt(3))
                    {
                        int var11 = var2.nextInt(4) + 1;
                        int var12;

                        switch (var9)
                        {
                            case 0:
                                for (var12 = var3; var12 <= var3 + var11; ++var12)
                                {
                                    var1.setBlock(var12, var10, var5, AetherBlocks.AetherLog.blockID);
                                    this.setBlockAirCheck(var1, var12, var10 + 1, var5, this.leaves);
                                    this.setBlockAirCheck(var1, var12, var10 - 1, var5, this.leaves);
                                    this.setBlockAirCheck(var1, var12 + 1, var10, var5 + 1, this.leaves);
                                    this.setBlockAirCheck(var1, var12 - 1, var10, var5 - 1, this.leaves);
                                }

                                var1.setBlock(var3 + var11 + 1, var10 + 1, var5, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3 + var11 + 2, var10 + 2, var5, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3 + var11 + 2, var10 + 3, var5, AetherBlocks.AetherLog.blockID);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 2, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 3, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 4, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 3, var10 + 2, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 3, var10 + 3, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 2, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 3, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 2, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 3, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 1, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 2, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 1, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10 + 2, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11, var10, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11, var10 + 1, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11, var10, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11, var10 + 1, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + (var11 - 1), var10, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + (var11 - 1), var10, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + (var11 - 2), var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + (var11 - 1), var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11, var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 1, var10, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 + var11 + 2, var10 + 1, var5, this.leaves);
                                break;

                            case 1:
                                for (var12 = var3; var12 >= var3 - var11; --var12)
                                {
                                    var1.setBlock(var12, var10, var5, AetherBlocks.AetherLog.blockID);
                                    this.setBlockAirCheck(var1, var12, var10 + 1, var5, this.leaves);
                                    this.setBlockAirCheck(var1, var12, var10 - 1, var5, this.leaves);
                                    this.setBlockAirCheck(var1, var12 + 1, var10, var5 + 1, this.leaves);
                                    this.setBlockAirCheck(var1, var12 - 1, var10, var5 - 1, this.leaves);
                                }

                                var1.setBlock(var3 - (var11 + 1), var10 + 1, var5, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3 - (var11 + 2), var10 + 2, var5, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3 - (var11 + 2), var10 + 3, var5, AetherBlocks.AetherLog.blockID);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 2, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 3, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 4, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 3), var10 + 2, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 3), var10 + 3, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 2, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 3, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 2, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 3, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 1, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 2, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 1, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10 + 2, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - var11, var10, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - var11, var10 + 1, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - var11, var10, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - var11, var10 + 1, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 - 1), var10, var5 - 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 - 1), var10, var5 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 - 2), var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 - 1), var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - var11, var10 - 1, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 1), var10, var5, this.leaves);
                                this.setBlockAirCheck(var1, var3 - (var11 + 2), var10 + 1, var5, this.leaves);
                                break;

                            case 2:
                                for (var12 = var5; var12 <= var5 + var11; ++var12)
                                {
                                    var1.setBlock(var3, var10, var12, AetherBlocks.AetherLog.blockID);
                                    this.setBlockAirCheck(var1, var3, var10 + 1, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3, var10 - 1, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3 + 1, var10, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3 - 1, var10, var12, this.leaves);
                                }

                                var1.setBlock(var3, var10 + 1, var5 + var11 + 1, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3, var10 + 2, var5 + var11 + 2, AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3, var10 + 3, var5 + var11 + 2, AetherBlocks.AetherLog.blockID);
                                this.setBlockAirCheck(var1, var3, var10 + 2, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 3, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 4, var5 + var11 + 2, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 2, var5 + var11 + 3, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 3, var5 + var11 + 3, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 2, var5 + var11 + 2, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 3, var5 + var11 + 2, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 2, var5 + var11 + 2, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 3, var5 + var11 + 2, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 1, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 2, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 1, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 2, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10, var5 + var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 1, var5 + var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10, var5 + var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 1, var5 + var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10, var5 + (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10, var5 + (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 + (var11 - 2), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 + (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 + var11, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10, var5 + var11 + 1, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 1, var5 + var11 + 2, this.leaves);
                                break;

                            case 3:
                                for (var12 = var5; var12 >= var5 - var11; --var12)
                                {
                                    var1.setBlock(var3, var10, var12, AetherBlocks.AetherLog.blockID);
                                    this.setBlockAirCheck(var1, var3, var10 + 1, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3, var10 - 1, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3 + 1, var10, var12, this.leaves);
                                    this.setBlockAirCheck(var1, var3 - 1, var10, var12, this.leaves);
                                }

                                var1.setBlock(var3, var10 + 1, var5 - (var11 + 1), AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3, var10 + 2, var5 - (var11 + 2), AetherBlocks.AetherLog.blockID);
                                var1.setBlock(var3, var10 + 3, var5 - (var11 + 2), AetherBlocks.AetherLog.blockID);
                                this.setBlockAirCheck(var1, var3, var10 + 2, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 3, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 4, var5 - (var11 + 2), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 2, var5 - (var11 + 3), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 3, var5 - (var11 + 3), this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 2, var5 - (var11 + 2), this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 3, var5 - (var11 + 2), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 2, var5 - (var11 + 2), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 3, var5 - (var11 + 2), this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 1, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 2, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 1, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 2, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10, var5 - var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10 + 1, var5 - var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10, var5 - var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10 + 1, var5 - var11, this.leaves);
                                this.setBlockAirCheck(var1, var3 - 1, var10, var5 - (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3 + 1, var10, var5 - (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 - (var11 - 2), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 - (var11 - 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 - 1, var5 - var11, this.leaves);
                                this.setBlockAirCheck(var1, var3, var10, var5 - (var11 + 1), this.leaves);
                                this.setBlockAirCheck(var1, var3, var10 + 1, var5 - (var11 + 2), this.leaves);
                        }

                        ++var9;

                        if (var9 > 3)
                        {
                            var9 = 0;
                        }
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void setBlockAirCheck(World var1, int var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var2, var3, var4) == 0)
        {
            var1.setBlock(var2, var3, var4, var5);
        }
    }
}
