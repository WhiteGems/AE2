package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenLiquids extends WorldGenerator
{
    private int liquidBlockId;

    public AetherGenLiquids(int i)
    {
        this.liquidBlockId = i;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if ((world.getBlockId(i, j + 1, k) != AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i, j + 1, k) >= 2))
        {
            return false;
        }

        if ((world.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i, j - 1, k) >= 2))
        {
            return false;
        }

        if ((world.getBlockId(i, j, k) != 0) && ((world.getBlockId(i, j, k) != AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i, j, k) >= 2)))
        {
            return false;
        }

        int l = 0;

        if ((world.getBlockId(i - 1, j, k) == AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i - 1, j, k) >= 2))
        {
            l++;
        }

        if ((world.getBlockId(i + 1, j, k) == AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i + 1, j, k) >= 2))
        {
            l++;
        }

        if ((world.getBlockId(i, j, k - 1) == AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i, j, k - 1) >= 2))
        {
            l++;
        }

        if ((world.getBlockId(i, j, k + 1) == AetherBlocks.Holystone.blockID) || (world.getBlockMetadata(i, j, k + 1) >= 2))
        {
            l++;
        }

        int i1 = 0;

        if (world.isAirBlock(i - 1, j, k))
        {
            i1++;
        }

        if (world.isAirBlock(i + 1, j, k))
        {
            i1++;
        }

        if (world.isAirBlock(i, j, k - 1))
        {
            i1++;
        }

        if (world.isAirBlock(i, j, k + 1))
        {
            i1++;
        }

        if ((l == 3) && (i1 == 1))
        {
            world.setBlock(i, j, k, this.liquidBlockId);
            world.scheduledUpdatesAreImmediate = true;
            Block.blocksList[this.liquidBlockId].updateTick(world, i, j, k, random);
            world.scheduledUpdatesAreImmediate = false;
        }

        return true;
    }
}

