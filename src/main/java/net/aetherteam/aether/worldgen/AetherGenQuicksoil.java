package net.aetherteam.aether.worldgen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenQuicksoil extends WorldGenerator
{
    private int minableBlockId;

    public AetherGenQuicksoil(int var1)
    {
        this.minableBlockId = var1;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        for (int var6 = var3 - 3; var6 < var3 + 4; ++var6)
        {
            for (int var7 = var5 - 3; var7 < var5 + 4; ++var7)
            {
                if (var1.getBlockId(var6, var4, var7) == 0 && (var6 - var3) * (var6 - var3) + (var7 - var5) * (var7 - var5) < 12)
                {
                    var1.setBlock(var6, var4, var7, this.minableBlockId);
                }
            }
        }

        return true;
    }
}
