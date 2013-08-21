package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenQuicksoil extends WorldGenerator
{
    private int minableBlockId;

    public AetherGenQuicksoil(int i)
    {
        this.minableBlockId = i;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        for (int x = i - 3; x < i + 4; ++x)
        {
            for (int z = k - 3; z < k + 4; ++z)
            {
                if (world.getBlockId(x, j, z) == 0 && (x - i) * (x - i) + (z - k) * (z - k) < 12)
                {
                    world.setBlock(x, j, z, this.minableBlockId);
                }
            }
        }

        return true;
    }
}
