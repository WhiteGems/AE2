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

        for (k -= 8; (j > 0) && (world.isAirBlock(i, j, k)); j--);

        j -= 4;
        boolean[] aflag = new boolean[2048];
        int l = random.nextInt(4) + 4;

        for (int i1 = 0; i1 < l; i1++)
        {
            double d = random.nextDouble() * 6.0D + 3.0D;
            double d1 = random.nextDouble() * 4.0D + 2.0D;
            double d2 = random.nextDouble() * 6.0D + 3.0D;
            double d3 = random.nextDouble() * (16.0D - d - 2.0D) + 1.0D + d / 2.0D;
            double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
            double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

            for (int j4 = 1; j4 < 15; j4++)
            {
                for (int k4 = 1; k4 < 15; k4++)
                {
                    for (int l4 = 1; l4 < 7; l4++)
                    {
                        double d6 = (j4 - d3) / (d / 2.0D);
                        double d7 = (l4 - d4) / (d1 / 2.0D);
                        double d8 = (k4 - d5) / (d2 / 2.0D);
                        double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                        if (d9 < 1.0D)
                        {
                            aflag[((j4 * 16 + k4) * 8 + l4)] = true;
                        }
                    }
                }
            }
        }

        for (int j1 = 0; j1 < 16; j1++)
        {
            for (int j2 = 0; j2 < 16; j2++)
            {
                for (int j3 = 0; j3 < 8; j3++)
                {
                    boolean flag = (aflag[((j1 * 16 + j2) * 8 + j3)] == 0) && (((j1 < 15) && (aflag[(((j1 + 1) * 16 + j2) * 8 + j3)] != 0)) || ((j1 > 0) && (aflag[(((j1 - 1) * 16 + j2) * 8 + j3)] != 0)) || ((j2 < 15) && (aflag[((j1 * 16 + (j2 + 1)) * 8 + j3)] != 0)) || ((j2 > 0) && (aflag[((j1 * 16 + (j2 - 1)) * 8 + j3)] != 0)) || ((j3 < 7) && (aflag[((j1 * 16 + j2) * 8 + (j3 + 1))] != 0)) || ((j3 > 0) && (aflag[((j1 * 16 + j2) * 8 + (j3 - 1))] != 0)));

                    if (flag)
                    {
                        Material material = world.getBlockMaterial(i + j1, j + j3, k + j2);

                        if ((j3 >= 4) && (material.isLiquid()))
                        {
                            return false;
                        }

                        if ((j3 < 4) && (!material.isSolid()) && (world.getBlockId(i + j1, j + j3, k + j2) != this.blockIndex))
                        {
                            return false;
                        }
                    }
                }
            }
        }

        for (int k1 = 0; k1 < 16; k1++)
        {
            for (int k2 = 0; k2 < 16; k2++)
            {
                for (int k3 = 0; k3 < 8; k3++)
                {
                    if (aflag[((k1 * 16 + k2) * 8 + k3)] != 0)
                    {
                        world.setBlock(i + k1, j + k3, k + k2, k3 < 4 ? this.blockIndex : 0);
                    }
                }
            }
        }

        for (int l1 = 0; l1 < 16; l1++)
        {
            for (int l2 = 0; l2 < 16; l2++)
            {
                for (int l3 = 4; l3 < 8; l3++)
                {
                    if ((aflag[((l1 * 16 + l2) * 8 + l3)] != 0) && (world.getBlockId(i + l1, j + l3 - 1, k + l2) == AetherBlocks.AetherDirt.blockID) && (world.getSavedLightValue(EnumSkyBlock.Sky, i + l1, j + l3, k + l2) > 0))
                    {
                        world.setBlock(i + l1, j + l3 - 1, k + l2, AetherBlocks.AetherGrass.blockID);
                    }
                }
            }
        }

        if (Block.blocksList[this.blockIndex].blockMaterial == Material.lava)
        {
            for (int i2 = 0; i2 < 16; i2++)
            {
                for (int i3 = 0; i3 < 16; i3++)
                {
                    for (int i4 = 0; i4 < 8; i4++)
                    {
                        boolean flag1 = (aflag[((i2 * 16 + i3) * 8 + i4)] == 0) && (((i2 < 15) && (aflag[(((i2 + 1) * 16 + i3) * 8 + i4)] != 0)) || ((i2 > 0) && (aflag[(((i2 - 1) * 16 + i3) * 8 + i4)] != 0)) || ((i3 < 15) && (aflag[((i2 * 16 + (i3 + 1)) * 8 + i4)] != 0)) || ((i3 > 0) && (aflag[((i2 * 16 + (i3 - 1)) * 8 + i4)] != 0)) || ((i4 < 7) && (aflag[((i2 * 16 + i3) * 8 + (i4 + 1))] != 0)) || ((i4 > 0) && (aflag[((i2 * 16 + i3) * 8 + (i4 - 1))] != 0)));

                        if ((flag1) && ((i4 < 4) || (random.nextInt(2) != 0)) && (world.getBlockMaterial(i + i2, j + i4, k + i3).isSolid()))
                        {
                            world.setBlock(i + i2, j + i4, k + i3, AetherBlocks.Holystone.blockID, 0, ChunkProviderAether.placementFlagType);
                        }
                    }
                }
            }
        }

        return true;
    }
}

