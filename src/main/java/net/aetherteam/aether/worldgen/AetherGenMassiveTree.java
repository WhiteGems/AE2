package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenMassiveTree extends WorldGenerator
{
    int leaves;
    int randHeight;
    boolean branches;

    public AetherGenMassiveTree(int leafID, int heightWeight, boolean branchFlag)
    {
        this.leaves = leafID;
        this.randHeight = heightWeight;
        this.branches = branchFlag;
    }

    public boolean generate(World world, Random random, int x, int y, int z)
    {
        boolean cangen = true;
        int height = random.nextInt(this.randHeight) + (this.branches == true ? 8 : 4);

        for (int x1 = x - 3; x1 < x + 3; x1++)
        {
            for (int y1 = y + 1; y1 < y + (height + 2); y1++)
            {
                for (int z1 = z - 3; z1 < z + 3; z1++)
                {
                    if (world.getBlockId(x1, y1, z1) != 0)
                    {
                        cangen = false;
                    }
                }
            }
        }

        if ((y + (height + 2) <= world.getActualHeight()) && (cangen))
        {
            int y1 = world.getBlockId(x, y - 1, z);

            if ((y1 != AetherBlocks.AetherGrass.blockID) && (y1 != AetherBlocks.AetherDirt.blockID))
            {
                return false;
            }

            world.setBlock(x, y - 1, z, AetherBlocks.AetherDirt.blockID);

            for (int y2 = y; y2 <= y + height; y2++)
            {
                world.setBlock(x, y2, z, AetherBlocks.AetherLog.blockID);
            }

            if (this.branches)
            {
                world.setBlock(x + 1, y, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x + 1, y + 1, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x + 2, y, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x - 1, y, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x - 1, y + 1, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x - 2, y, z, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y, z + 1, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y + 1, z + 1, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y, z + 2, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y, z - 1, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y + 1, z - 1, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x, y, z - 2, AetherBlocks.SkyrootLogWall.blockID);
                world.setBlock(x + 1, y - 1, z, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x + 2, y - 1, z, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x - 1, y - 1, z, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x - 2, y - 1, z, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x, y - 1, z + 1, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x, y - 1, z + 2, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x, y - 1, z - 1, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x, y - 1, z - 2, AetherBlocks.AetherGrass.blockID);
                world.setBlock(x + 1, y - 2, z, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x + 2, y - 2, z, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x - 1, y - 2, z, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x - 2, y - 2, z, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x, y - 2, z + 1, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x, y - 2, z + 2, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x, y - 2, z - 1, AetherBlocks.AetherDirt.blockID);
                world.setBlock(x, y - 2, z - 2, AetherBlocks.AetherDirt.blockID);
            }

            setBlockAirCheck(world, x, y + (height + 1), z, this.leaves);
            setBlockAirCheck(world, x, y + (height + 2), z, this.leaves);

            for (int y2 = y + 2; y2 <= y + (height + 1); y2++)
            {
                setBlockAirCheck(world, x + 1, y2, z, this.leaves);
                setBlockAirCheck(world, x - 1, y2, z, this.leaves);
                setBlockAirCheck(world, x, y2, z + 1, this.leaves);
                setBlockAirCheck(world, x, y2, z - 1, this.leaves);
            }

            for (int y2 = y + 3; y2 <= y + height; y2 += 2)
            {
                setBlockAirCheck(world, x + 1, y2, z + 1, this.leaves);
                setBlockAirCheck(world, x - 1, y2, z - 1, this.leaves);
                setBlockAirCheck(world, x + 1, y2, z - 1, this.leaves);
                setBlockAirCheck(world, x - 1, y2, z + 1, this.leaves);
                setBlockAirCheck(world, x + 2, y2, z, this.leaves);
                setBlockAirCheck(world, x - 2, y2, z, this.leaves);
                setBlockAirCheck(world, x, y2, z + 2, this.leaves);
                setBlockAirCheck(world, x, y2, z - 2, this.leaves);
            }

            if (this.branches)
            {
                int side = random.nextInt(3);

                for (int y2 = y + (random.nextInt(2) + 3); y2 <= y + height - 2; y2 += 1 + random.nextInt(3))
                {
                    int branchLength = random.nextInt(4) + 1;

                    switch (side)
                    {
                        case 0:
                            for (int x2 = x; x2 <= x + branchLength; x2++)
                            {
                                world.setBlock(x2, y2, z, AetherBlocks.AetherLog.blockID);
                                setBlockAirCheck(world, x2, y2 + 1, z, this.leaves);
                                setBlockAirCheck(world, x2, y2 - 1, z, this.leaves);
                                setBlockAirCheck(world, x2 + 1, y2, z + 1, this.leaves);
                                setBlockAirCheck(world, x2 - 1, y2, z - 1, this.leaves);
                            }

                            world.setBlock(x + (branchLength + 1), y2 + 1, z, AetherBlocks.AetherLog.blockID);
                            world.setBlock(x + (branchLength + 2), y2 + 2, z, AetherBlocks.AetherLog.blockID);
                            world.setBlock(x + (branchLength + 2), y2 + 3, z, AetherBlocks.AetherLog.blockID);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 2, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 3, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 4, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 3), y2 + 2, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 3), y2 + 3, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 2, z - 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 3, z - 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 2, z + 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 3, z + 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 1, z - 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 2, z - 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 1, z + 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2 + 2, z + 1, this.leaves);
                            setBlockAirCheck(world, x + branchLength, y2, z - 1, this.leaves);
                            setBlockAirCheck(world, x + branchLength, y2 + 1, z - 1, this.leaves);
                            setBlockAirCheck(world, x + branchLength, y2, z + 1, this.leaves);
                            setBlockAirCheck(world, x + branchLength, y2 + 1, z + 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength - 1), y2, z - 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength - 1), y2, z + 1, this.leaves);
                            setBlockAirCheck(world, x + (branchLength - 2), y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength - 1), y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x + branchLength, y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 1), y2, z, this.leaves);
                            setBlockAirCheck(world, x + (branchLength + 2), y2 + 1, z, this.leaves);
                            break;

                        case 1:
                            for (int x3 = x; x3 >= x - branchLength; x3--)
                            {
                                world.setBlock(x3, y2, z, AetherBlocks.AetherLog.blockID);
                                setBlockAirCheck(world, x3, y2 + 1, z, this.leaves);
                                setBlockAirCheck(world, x3, y2 - 1, z, this.leaves);
                                setBlockAirCheck(world, x3 + 1, y2, z + 1, this.leaves);
                                setBlockAirCheck(world, x3 - 1, y2, z - 1, this.leaves);
                            }

                            world.setBlock(x - (branchLength + 1), y2 + 1, z, AetherBlocks.AetherLog.blockID);
                            world.setBlock(x - (branchLength + 2), y2 + 2, z, AetherBlocks.AetherLog.blockID);
                            world.setBlock(x - (branchLength + 2), y2 + 3, z, AetherBlocks.AetherLog.blockID);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 2, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 3, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 4, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 3), y2 + 2, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 3), y2 + 3, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 2, z - 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 3, z - 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 2, z + 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 3, z + 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 1, z - 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 2, z - 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 1, z + 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2 + 2, z + 1, this.leaves);
                            setBlockAirCheck(world, x - branchLength, y2, z - 1, this.leaves);
                            setBlockAirCheck(world, x - branchLength, y2 + 1, z - 1, this.leaves);
                            setBlockAirCheck(world, x - branchLength, y2, z + 1, this.leaves);
                            setBlockAirCheck(world, x - branchLength, y2 + 1, z + 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength - 1), y2, z - 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength - 1), y2, z + 1, this.leaves);
                            setBlockAirCheck(world, x - (branchLength - 2), y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength - 1), y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x - branchLength, y2 - 1, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 1), y2, z, this.leaves);
                            setBlockAirCheck(world, x - (branchLength + 2), y2 + 1, z, this.leaves);
                            break;

                        case 2:
                            for (int z2 = z; z2 <= z + branchLength; z2++)
                            {
                                world.setBlock(x, y2, z2, AetherBlocks.AetherLog.blockID);
                                setBlockAirCheck(world, x, y2 + 1, z2, this.leaves);
                                setBlockAirCheck(world, x, y2 - 1, z2, this.leaves);
                                setBlockAirCheck(world, x + 1, y2, z2, this.leaves);
                                setBlockAirCheck(world, x - 1, y2, z2, this.leaves);
                            }

                            world.setBlock(x, y2 + 1, z + (branchLength + 1), AetherBlocks.AetherLog.blockID);
                            world.setBlock(x, y2 + 2, z + (branchLength + 2), AetherBlocks.AetherLog.blockID);
                            world.setBlock(x, y2 + 3, z + (branchLength + 2), AetherBlocks.AetherLog.blockID);
                            setBlockAirCheck(world, x, y2 + 2, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 3, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 4, z + (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x, y2 + 2, z + (branchLength + 3), this.leaves);
                            setBlockAirCheck(world, x, y2 + 3, z + (branchLength + 3), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 2, z + (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 3, z + (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 2, z + (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 3, z + (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 1, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 2, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 1, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 2, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x - 1, y2, z + branchLength, this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 1, z + branchLength, this.leaves);
                            setBlockAirCheck(world, x + 1, y2, z + branchLength, this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 1, z + branchLength, this.leaves);
                            setBlockAirCheck(world, x - 1, y2, z + (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2, z + (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z + (branchLength - 2), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z + (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z + branchLength, this.leaves);
                            setBlockAirCheck(world, x, y2, z + (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 1, z + (branchLength + 2), this.leaves);
                            break;

                        case 3:
                            for (int z3 = z; z3 >= z - branchLength; z3--)
                            {
                                world.setBlock(x, y2, z3, AetherBlocks.AetherLog.blockID);
                                setBlockAirCheck(world, x, y2 + 1, z3, this.leaves);
                                setBlockAirCheck(world, x, y2 - 1, z3, this.leaves);
                                setBlockAirCheck(world, x + 1, y2, z3, this.leaves);
                                setBlockAirCheck(world, x - 1, y2, z3, this.leaves);
                            }

                            world.setBlock(x, y2 + 1, z - (branchLength + 1), AetherBlocks.AetherLog.blockID);
                            world.setBlock(x, y2 + 2, z - (branchLength + 2), AetherBlocks.AetherLog.blockID);
                            world.setBlock(x, y2 + 3, z - (branchLength + 2), AetherBlocks.AetherLog.blockID);
                            setBlockAirCheck(world, x, y2 + 2, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 3, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 4, z - (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x, y2 + 2, z - (branchLength + 3), this.leaves);
                            setBlockAirCheck(world, x, y2 + 3, z - (branchLength + 3), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 2, z - (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 3, z - (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 2, z - (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 3, z - (branchLength + 2), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 1, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 2, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 1, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 2, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x - 1, y2, z - branchLength, this.leaves);
                            setBlockAirCheck(world, x - 1, y2 + 1, z - branchLength, this.leaves);
                            setBlockAirCheck(world, x + 1, y2, z - branchLength, this.leaves);
                            setBlockAirCheck(world, x + 1, y2 + 1, z - branchLength, this.leaves);
                            setBlockAirCheck(world, x - 1, y2, z - (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x + 1, y2, z - (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z - (branchLength - 2), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z - (branchLength - 1), this.leaves);
                            setBlockAirCheck(world, x, y2 - 1, z - branchLength, this.leaves);
                            setBlockAirCheck(world, x, y2, z - (branchLength + 1), this.leaves);
                            setBlockAirCheck(world, x, y2 + 1, z - (branchLength + 2), this.leaves);
                    }

                    side++;

                    if (side > 3)
                    {
                        side = 0;
                    }
                }
            }

            return true;
        }

        return false;
    }

    public void setBlockAirCheck(World world, int x, int y, int z, int blockID)
    {
        if (world.getBlockId(x, y, z) == 0)
        {
            world.setBlock(x, y, z, blockID);
        }
    }
}

