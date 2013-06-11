package net.aetherteam.aether.worldgen;

import java.util.Random;

import net.aetherteam.aether.blocks.BlockAetherFlower;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenFlowers extends WorldGenerator
{
    private int plantBlockId;
    private int placementChance;

    public AetherGenFlowers(int var1, int var2)
    {
        this.plantBlockId = var1;
        this.placementChance = var2;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        for (int var6 = 0; var6 < this.placementChance; ++var6)
        {
            int var7 = var3 + var2.nextInt(8) - var2.nextInt(8);
            int var8 = var4 + var2.nextInt(4) - var2.nextInt(4);
            int var9 = var5 + var2.nextInt(8) - var2.nextInt(8);

            if (var1.isAirBlock(var7, var8, var9) && ((BlockAetherFlower) Block.blocksList[this.plantBlockId]).canBlockStay(var1, var7, var8, var9))
            {
                var1.setBlock(var7, var8, var9, this.plantBlockId);
            }
        }

        return true;
    }
}
