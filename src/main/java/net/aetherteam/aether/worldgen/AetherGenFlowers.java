package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.BlockAetherFlower;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenFlowers extends WorldGenerator
{
    private int plantBlockId;
    private int placementChance;

    public AetherGenFlowers(int i, int chance)
    {
        this.plantBlockId = i;
        this.placementChance = chance;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int l = 0; l < this.placementChance; l++)
        {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);

            if ((world.isAirBlock(i1, j1, k1)) && (((BlockAetherFlower)net.minecraft.block.Block.blocksList[this.plantBlockId]).canBlockStay(world, i1, j1, k1)))
            {
                world.setBlock(i1, j1, k1, this.plantBlockId);
            }
        }

        return true;
    }
}

