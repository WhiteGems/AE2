package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenBuildings extends WorldGenerator
{
    public int blockID1;
    public int blockID2;
    public int meta1;
    public int meta2;
    public int chance;
    public boolean replaceAir;
    public boolean replaceSolid;

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        return false;
    }

    public void setBlocks(int var1, int var2, int var3)
    {
        this.blockID1 = var1;
        this.blockID2 = var2;
        this.chance = var3;

        if (this.chance < 1)
        {
            this.chance = 1;
        }
    }

    public void setMetadata(int var1, int var2)
    {
        this.meta1 = var1;
        this.meta2 = var2;
    }

    public void addLineX(World var1, Random var2, int var3, int var4, int var5, int var6)
    {
        for (int var7 = var3; var7 < var3 + var6; ++var7)
        {
            if ((this.replaceAir || var1.getBlockId(var7, var4, var5) != 0) && (this.replaceSolid || var1.getBlockId(var7, var4, var5) == 0))
            {
                if (var2.nextInt(this.chance) == 0)
                {
                    var1.setBlock(var7, var4, var5, this.blockID2, this.meta2, 2);
                }
                else
                {
                    var1.setBlock(var7, var4, var5, this.blockID1, this.meta1, 2);
                }
            }
        }
    }

    public void addLineY(World var1, Random var2, int var3, int var4, int var5, int var6)
    {
        for (int var7 = var4; var7 < var4 + var6; ++var7)
        {
            if ((this.replaceAir || var1.getBlockId(var3, var7, var5) != 0) && (this.replaceSolid || var1.getBlockId(var3, var7, var5) == 0))
            {
                if (var2.nextInt(this.chance) == 0)
                {
                    var1.setBlock(var3, var7, var5, this.blockID2, this.meta2, 2);
                }
                else
                {
                    var1.setBlock(var3, var7, var5, this.blockID1, this.meta1, 2);
                }
            }
        }
    }

    public void addLineZ(World var1, Random var2, int var3, int var4, int var5, int var6)
    {
        for (int var7 = var5; var7 < var5 + var6; ++var7)
        {
            if ((this.replaceAir || var1.getBlockId(var3, var4, var7) != 0) && (this.replaceSolid || var1.getBlockId(var3, var4, var7) == 0))
            {
                if (var2.nextInt(this.chance) == 0)
                {
                    var1.setBlock(var3, var4, var7, this.blockID2, this.meta2, 2);
                }
                else
                {
                    var1.setBlock(var3, var4, var7, this.blockID1, this.meta1, 2);
                }
            }
        }
    }

    public void addPlaneX(World var1, Random var2, int var3, int var4, int var5, int var6, int var7)
    {
        for (int var8 = var4; var8 < var4 + var6; ++var8)
        {
            for (int var9 = var5; var9 < var5 + var7; ++var9)
            {
                if ((this.replaceAir || var1.getBlockId(var3, var8, var9) != 0) && (this.replaceSolid || var1.getBlockId(var3, var8, var9) == 0))
                {
                    if (var2.nextInt(this.chance) == 0)
                    {
                        var1.setBlock(var3, var8, var9, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        var1.setBlock(var3, var8, var9, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addPlaneY(World var1, Random var2, int var3, int var4, int var5, int var6, int var7)
    {
        for (int var8 = var3; var8 < var3 + var6; ++var8)
        {
            for (int var9 = var5; var9 < var5 + var7; ++var9)
            {
                if ((this.replaceAir || var1.getBlockId(var8, var4, var9) != 0) && (this.replaceSolid || var1.getBlockId(var8, var4, var9) == 0))
                {
                    if (var2.nextInt(this.chance) == 0)
                    {
                        var1.setBlock(var8, var4, var9, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        var1.setBlock(var8, var4, var9, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addPlaneZ(World var1, Random var2, int var3, int var4, int var5, int var6, int var7)
    {
        for (int var8 = var3; var8 < var3 + var6; ++var8)
        {
            for (int var9 = var4; var9 < var4 + var7; ++var9)
            {
                if ((this.replaceAir || var1.getBlockId(var8, var9, var5) != 0) && (this.replaceSolid || var1.getBlockId(var8, var9, var5) == 0))
                {
                    if (var2.nextInt(this.chance) == 0)
                    {
                        var1.setBlock(var8, var9, var5, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        var1.setBlock(var8, var9, var5, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addHollowBox(World var1, Random var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        int var9 = this.blockID1;
        int var10 = this.blockID2;
        this.setBlocks(0, 0, this.chance);
        this.addSolidBox(var1, var2, var3, var4, var5, var6, var7, var8);
        this.setBlocks(var9, var10, this.chance);
        this.addPlaneY(var1, var2, var3, var4, var5, var6, var8);
        this.addPlaneY(var1, var2, var3, var4 + var7 - 1, var5, var6, var8);
        this.addPlaneX(var1, var2, var3, var4, var5, var7, var8);
        this.addPlaneX(var1, var2, var3 + var6 - 1, var4, var5, var7, var8);
        this.addPlaneZ(var1, var2, var3, var4, var5, var6, var7);
        this.addPlaneZ(var1, var2, var3, var4, var5 + var8 - 1, var6, var7);
    }

    public void addSquareTube(World var1, Random var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
    {
        int var10 = this.blockID1;
        int var11 = this.blockID2;
        this.setBlocks(0, 0, this.chance);
        this.addSolidBox(var1, var2, var3, var4, var5, var6, var7, var8);
        this.setBlocks(var10, var11, this.chance);

        if (var9 == 0 || var9 == 2)
        {
            this.addPlaneY(var1, var2, var3, var4, var5, var6, var8);
            this.addPlaneY(var1, var2, var3, var4 + var7 - 1, var5, var6, var8);
        }

        if (var9 == 1 || var9 == 2)
        {
            this.addPlaneX(var1, var2, var3, var4, var5, var7, var8);
            this.addPlaneX(var1, var2, var3 + var6 - 1, var4, var5, var7, var8);
        }

        if (var9 == 0 || var9 == 1)
        {
            this.addPlaneZ(var1, var2, var3, var4, var5, var6, var7);
            this.addPlaneZ(var1, var2, var3, var4, var5 + var8 - 1, var6, var7);
        }
    }

    public void addSolidBox(World var1, Random var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        for (int var9 = var3; var9 < var3 + var6; ++var9)
        {
            for (int var10 = var4; var10 < var4 + var7; ++var10)
            {
                for (int var11 = var5; var11 < var5 + var8; ++var11)
                {
                    if ((this.replaceAir || var1.getBlockId(var9, var10, var11) != 0) && (this.replaceSolid || var1.getBlockId(var9, var10, var11) == 0))
                    {
                        if (var2.nextInt(this.chance) == 0)
                        {
                            var1.setBlock(var9, var10, var11, this.blockID2, this.meta2, 2);
                        }
                        else
                        {
                            var1.setBlock(var9, var10, var11, this.blockID1, this.meta1, 2);
                        }
                    }
                }
            }
        }
    }

    public boolean isBoxSolid(World var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        boolean var8 = true;

        for (int var9 = var2; var9 < var2 + var5; ++var9)
        {
            for (int var10 = var3; var10 < var3 + var6; ++var10)
            {
                for (int var11 = var4; var11 < var4 + var7; ++var11)
                {
                    if (var1.getBlockId(var9, var10, var11) == 0)
                    {
                        var8 = false;
                    }
                }
            }
        }

        return var8;
    }

    public boolean isBoxEmpty(World var1, int var2, int var3, int var4, int var5, int var6, int var7)
    {
        boolean var8 = true;

        for (int var9 = var2; var9 < var2 + var5; ++var9)
        {
            for (int var10 = var3; var10 < var3 + var6; ++var10)
            {
                for (int var11 = var4; var11 < var4 + var7; ++var11)
                {
                    if (var1.getBlockId(var9, var10, var11) != 0)
                    {
                        var8 = false;
                    }
                }
            }
        }

        return var8;
    }
}
