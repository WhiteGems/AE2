package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenLiquids extends WorldGenerator
{
    private int liquidBlockId;

    public AetherGenLiquids(int var1)
    {
        this.liquidBlockId = var1;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        if (var1.getBlockId(var3, var4 + 1, var5) == AetherBlocks.Holystone.blockID && var1.getBlockMetadata(var3, var4 + 1, var5) < 2)
        {
            if (var1.getBlockId(var3, var4 - 1, var5) == AetherBlocks.Holystone.blockID && var1.getBlockMetadata(var3, var4 - 1, var5) < 2)
            {
                if (var1.getBlockId(var3, var4, var5) != 0 && (var1.getBlockId(var3, var4, var5) != AetherBlocks.Holystone.blockID || var1.getBlockMetadata(var3, var4, var5) >= 2))
                {
                    return false;
                }
                else
                {
                    int var6 = 0;

                    if (var1.getBlockId(var3 - 1, var4, var5) == AetherBlocks.Holystone.blockID || var1.getBlockMetadata(var3 - 1, var4, var5) >= 2)
                    {
                        ++var6;
                    }

                    if (var1.getBlockId(var3 + 1, var4, var5) == AetherBlocks.Holystone.blockID || var1.getBlockMetadata(var3 + 1, var4, var5) >= 2)
                    {
                        ++var6;
                    }

                    if (var1.getBlockId(var3, var4, var5 - 1) == AetherBlocks.Holystone.blockID || var1.getBlockMetadata(var3, var4, var5 - 1) >= 2)
                    {
                        ++var6;
                    }

                    if (var1.getBlockId(var3, var4, var5 + 1) == AetherBlocks.Holystone.blockID || var1.getBlockMetadata(var3, var4, var5 + 1) >= 2)
                    {
                        ++var6;
                    }

                    int var7 = 0;

                    if (var1.isAirBlock(var3 - 1, var4, var5))
                    {
                        ++var7;
                    }

                    if (var1.isAirBlock(var3 + 1, var4, var5))
                    {
                        ++var7;
                    }

                    if (var1.isAirBlock(var3, var4, var5 - 1))
                    {
                        ++var7;
                    }

                    if (var1.isAirBlock(var3, var4, var5 + 1))
                    {
                        ++var7;
                    }

                    if (var6 == 3 && var7 == 1)
                    {
                        var1.setBlock(var3, var4, var5, this.liquidBlockId);
                        var1.scheduledUpdatesAreImmediate = true;
                        Block.blocksList[this.liquidBlockId].updateTick(var1, var3, var4, var5, var2);
                        var1.scheduledUpdatesAreImmediate = false;
                    }

                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
