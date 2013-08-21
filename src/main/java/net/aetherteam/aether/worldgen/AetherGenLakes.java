package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenLakes extends WorldGenerator
{
    private int blockIndex;

    public AetherGenLakes(int i)
    {
        this.blockIndex = i;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        i -= 8;

        for (k -= 8; j > 0 && world.isAirBlock(i, j, k); --j)
        {
            ;
        }

        j -= 4;
        boolean[] aflag = new boolean[2048];
        int l = random.nextInt(4) + 4;
        int i2;

        for (i2 = 0; i2 < l; ++i2)
        {
            double i3 = random.nextDouble() * 6.0D + 3.0D;
            double flag1 = random.nextDouble() * 4.0D + 2.0D;
            double d2 = random.nextDouble() * 6.0D + 3.0D;
            double d3 = random.nextDouble() * (16.0D - i3 - 2.0D) + 1.0D + i3 / 2.0D;
            double d4 = random.nextDouble() * (8.0D - flag1 - 4.0D) + 2.0D + flag1 / 2.0D;
            double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

            for (int j4 = 1; j4 < 15; ++j4)
            {
                for (int k4 = 1; k4 < 15; ++k4)
                {
                    for (int l4 = 1; l4 < 7; ++l4)
                    {
                        double d6 = ((double)j4 - d3) / (i3 / 2.0D);
                        double d7 = ((double)l4 - d4) / (flag1 / 2.0D);
                        double d8 = ((double)k4 - d5) / (d2 / 2.0D);
                        double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                        if (d9 < 1.0D)
                        {
                            aflag[(j4 * 16 + k4) * 8 + l4] = true;
                        }
                    }
                }
            }
        }

        int var32;
        boolean var33;
        int i4;

        for (i2 = 0; i2 < 16; ++i2)
        {
            for (var32 = 0; var32 < 16; ++var32)
            {
                for (i4 = 0; i4 < 8; ++i4)
                {
                    var33 = !aflag[(i2 * 16 + var32) * 8 + i4] && (i2 < 15 && aflag[((i2 + 1) * 16 + var32) * 8 + i4] || i2 > 0 && aflag[((i2 - 1) * 16 + var32) * 8 + i4] || var32 < 15 && aflag[(i2 * 16 + var32 + 1) * 8 + i4] || var32 > 0 && aflag[(i2 * 16 + (var32 - 1)) * 8 + i4] || i4 < 7 && aflag[(i2 * 16 + var32) * 8 + i4 + 1] || i4 > 0 && aflag[(i2 * 16 + var32) * 8 + (i4 - 1)]);

                    if (var33)
                    {
                        Material material = world.getBlockMaterial(i + i2, j + i4, k + var32);

                        if (i4 >= 4 && material.isLiquid())
                        {
                            return false;
                        }

                        if (i4 < 4 && !material.isSolid() && world.getBlockId(i + i2, j + i4, k + var32) != this.blockIndex)
                        {
                            return false;
                        }
                    }
                }
            }
        }

        for (i2 = 0; i2 < 16; ++i2)
        {
            for (var32 = 0; var32 < 16; ++var32)
            {
                for (i4 = 0; i4 < 8; ++i4)
                {
                    if (aflag[(i2 * 16 + var32) * 8 + i4])
                    {
                        world.setBlock(i + i2, j + i4, k + var32, i4 < 4 ? this.blockIndex : 0);
                    }
                }
            }
        }

        for (i2 = 0; i2 < 16; ++i2)
        {
            for (var32 = 0; var32 < 16; ++var32)
            {
                for (i4 = 4; i4 < 8; ++i4)
                {
                    if (aflag[(i2 * 16 + var32) * 8 + i4] && world.getBlockId(i + i2, j + i4 - 1, k + var32) == AetherBlocks.AetherDirt.blockID && world.getSavedLightValue(EnumSkyBlock.Sky, i + i2, j + i4, k + var32) > 0)
                    {
                        world.setBlock(i + i2, j + i4 - 1, k + var32, AetherBlocks.AetherGrass.blockID);
                    }
                }
            }
        }

        if (Block.blocksList[this.blockIndex].blockMaterial == Material.lava)
        {
            for (i2 = 0; i2 < 16; ++i2)
            {
                for (var32 = 0; var32 < 16; ++var32)
                {
                    for (i4 = 0; i4 < 8; ++i4)
                    {
                        var33 = !aflag[(i2 * 16 + var32) * 8 + i4] && (i2 < 15 && aflag[((i2 + 1) * 16 + var32) * 8 + i4] || i2 > 0 && aflag[((i2 - 1) * 16 + var32) * 8 + i4] || var32 < 15 && aflag[(i2 * 16 + var32 + 1) * 8 + i4] || var32 > 0 && aflag[(i2 * 16 + (var32 - 1)) * 8 + i4] || i4 < 7 && aflag[(i2 * 16 + var32) * 8 + i4 + 1] || i4 > 0 && aflag[(i2 * 16 + var32) * 8 + (i4 - 1)]);

                        if (var33 && (i4 < 4 || random.nextInt(2) != 0) && world.getBlockMaterial(i + i2, j + i4, k + var32).isSolid())
                        {
                            world.setBlock(i + i2, j + i4, k + var32, AetherBlocks.Holystone.blockID, 0, ChunkProviderAether.placementFlagType);
                        }
                    }
                }
            }
        }

        return true;
    }
}
