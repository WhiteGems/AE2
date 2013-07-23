package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenLargeTree extends WorldGenerator
{
    private int leafBlock;
    private int logBlock;
    private int logMetadata;

    public AetherGenLargeTree(int leafID, int logID, int logMeta)
    {
        this.leafBlock = leafID;
        this.logBlock = logID;
        this.logMetadata = logMeta;
    }

    public boolean branch(World world, Random random, int i, int j, int k, int slant)
    {
        int directionX = random.nextInt(3) - 1;
        int directionY = slant;
        int directionZ = random.nextInt(3) - 1;
        int x = i;
        int z = k;

        for (int n = 0; n < 2; n++)
        {
            i += directionX;
            j += directionY;
            k += directionZ;
            x -= directionX;
            z -= directionZ;

            if (world.getBlockId(i, j, k) == this.leafBlock)
            {
                world.setBlock(i, j, k, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
                world.setBlock(x, j, z, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
            }
        }

        return true;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        if ((world.getBlockId(i, j - 1, k) != AetherBlocks.AetherGrass.blockID) && (world.getBlockId(i, j - 1, k) != AetherBlocks.AetherDirt.blockID))
        {
            return false;
        }

        int height = 11;

        for (int x = i - 3; x < i + 5; x++)
        {
            for (int y = j + 5; y < j + 13; y++)
            {
                for (int z = k - 3; z < k + 5; z++)
                {
                    if (((x - i) * (x - i) + (y - j - 8) * (y - j - 8) + (z - k) * (z - k) < 12 + random.nextInt(5)) && (world.getBlockId(x, y, z) == 0))
                    {
                        world.setBlock(x, y, z, this.leafBlock);
                    }
                }
            }
        }

        for (int n = 0; n < height - 2; n++)
        {
            if (n > 4)
            {
                branch(world, random, i, j + n, k, n / 4 - 1);
            }

            world.setBlock(i, j + n, k, this.logBlock, this.logMetadata, ChunkProviderAether.placementFlagType);
        }

        return true;
    }
}

