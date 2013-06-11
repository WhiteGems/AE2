package net.aetherteam.aether.worldgen;

import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.blocks.BlockAetherFlower;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenOrangeFruit extends WorldGenerator
{
    private int placementChance;

    public AetherGenOrangeFruit(int var1)
    {
        this.placementChance = var1;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        for (int var6 = 0; var6 < this.placementChance; ++var6)
        {
            int var7 = var3 + var2.nextInt(8) - var2.nextInt(8);
            int var8 = var4 + var2.nextInt(4) - var2.nextInt(4);
            int var9 = var5 + var2.nextInt(8) - var2.nextInt(8);

            if (var1.isAirBlock(var7, var8, var9) && ((BlockAetherFlower) Block.blocksList[AetherBlocks.BlockOrangeTree.blockID]).canBlockStay(var1, var7, var8, var9) && var1.getBlockId(var7, var8 + 1, var9) == 0)
            {
                var1.setBlock(var7, var8, var9, AetherBlocks.BlockOrangeTree.blockID, 0, ChunkProviderAether.placementFlagType);
            }
        }

        return true;
    }
}
