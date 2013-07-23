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

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        return false;
    }

    public void setBlocks(int i, int j, int k)
    {
        this.blockID1 = i;
        this.blockID2 = j;
        this.chance = k;

        if (this.chance < 1)
        {
            this.chance = 1;
        }
    }

    public void setMetadata(int i, int j)
    {
        this.meta1 = i;
        this.meta2 = j;
    }

    public void addLineX(World world, Random random, int i, int j, int k, int length)
    {
        for (int x = i; x < i + length; x++)
        {
            if ((this.replaceAir) || (world.getBlockId(x, j, k) != 0))
            {
                if ((this.replaceSolid) || (world.getBlockId(x, j, k) == 0))
                {
                    if (random.nextInt(this.chance) == 0)
                    {
                        world.setBlock(x, j, k, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        world.setBlock(x, j, k, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addLineY(World world, Random random, int i, int j, int k, int length)
    {
        for (int y = j; y < j + length; y++)
        {
            if ((this.replaceAir) || (world.getBlockId(i, y, k) != 0))
            {
                if ((this.replaceSolid) || (world.getBlockId(i, y, k) == 0))
                {
                    if (random.nextInt(this.chance) == 0)
                    {
                        world.setBlock(i, y, k, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        world.setBlock(i, y, k, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addLineZ(World world, Random random, int i, int j, int k, int length)
    {
        for (int z = k; z < k + length; z++)
        {
            if ((this.replaceAir) || (world.getBlockId(i, j, z) != 0))
            {
                if ((this.replaceSolid) || (world.getBlockId(i, j, z) == 0))
                {
                    if (random.nextInt(this.chance) == 0)
                    {
                        world.setBlock(i, j, z, this.blockID2, this.meta2, 2);
                    }
                    else
                    {
                        world.setBlock(i, j, z, this.blockID1, this.meta1, 2);
                    }
                }
            }
        }
    }

    public void addPlaneX(World world, Random random, int i, int j, int k, int dj, int dk)
    {
        for (int y = j; y < j + dj; y++)
        {
            for (int z = k; z < k + dk; z++)
            {
                if ((this.replaceAir) || (world.getBlockId(i, y, z) != 0))
                {
                    if ((this.replaceSolid) || (world.getBlockId(i, y, z) == 0))
                    {
                        if (random.nextInt(this.chance) == 0)
                        {
                            world.setBlock(i, y, z, this.blockID2, this.meta2, 2);
                        }
                        else
                        {
                            world.setBlock(i, y, z, this.blockID1, this.meta1, 2);
                        }
                    }
                }
            }
        }
    }

    public void addPlaneY(World world, Random random, int i, int j, int k, int di, int dk)
    {
        for (int x = i; x < i + di; x++)
        {
            for (int z = k; z < k + dk; z++)
            {
                if ((this.replaceAir) || (world.getBlockId(x, j, z) != 0))
                {
                    if ((this.replaceSolid) || (world.getBlockId(x, j, z) == 0))
                    {
                        if (random.nextInt(this.chance) == 0)
                        {
                            world.setBlock(x, j, z, this.blockID2, this.meta2, 2);
                        }
                        else
                        {
                            world.setBlock(x, j, z, this.blockID1, this.meta1, 2);
                        }
                    }
                }
            }
        }
    }

    public void addPlaneZ(World world, Random random, int i, int j, int k, int di, int dj)
    {
        for (int x = i; x < i + di; x++)
        {
            for (int y = j; y < j + dj; y++)
            {
                if ((this.replaceAir) || (world.getBlockId(x, y, k) != 0))
                {
                    if ((this.replaceSolid) || (world.getBlockId(x, y, k) == 0))
                    {
                        if (random.nextInt(this.chance) == 0)
                        {
                            world.setBlock(x, y, k, this.blockID2, this.meta2, 2);
                        }
                        else
                        {
                            world.setBlock(x, y, k, this.blockID1, this.meta1, 2);
                        }
                    }
                }
            }
        }
    }

    public void addHollowBox(World world, Random random, int i, int j, int k, int di, int dj, int dk)
    {
        int temp1 = this.blockID1;
        int temp2 = this.blockID2;
        setBlocks(0, 0, this.chance);
        addSolidBox(world, random, i, j, k, di, dj, dk);
        setBlocks(temp1, temp2, this.chance);
        addPlaneY(world, random, i, j, k, di, dk);
        addPlaneY(world, random, i, j + dj - 1, k, di, dk);
        addPlaneX(world, random, i, j, k, dj, dk);
        addPlaneX(world, random, i + di - 1, j, k, dj, dk);
        addPlaneZ(world, random, i, j, k, di, dj);
        addPlaneZ(world, random, i, j, k + dk - 1, di, dj);
    }

    public void addSquareTube(World world, Random random, int i, int j, int k, int di, int dj, int dk, int dir)
    {
        int temp1 = this.blockID1;
        int temp2 = this.blockID2;
        setBlocks(0, 0, this.chance);
        addSolidBox(world, random, i, j, k, di, dj, dk);
        setBlocks(temp1, temp2, this.chance);

        if ((dir == 0) || (dir == 2))
        {
            addPlaneY(world, random, i, j, k, di, dk);
            addPlaneY(world, random, i, j + dj - 1, k, di, dk);
        }

        if ((dir == 1) || (dir == 2))
        {
            addPlaneX(world, random, i, j, k, dj, dk);
            addPlaneX(world, random, i + di - 1, j, k, dj, dk);
        }

        if ((dir == 0) || (dir == 1))
        {
            addPlaneZ(world, random, i, j, k, di, dj);
            addPlaneZ(world, random, i, j, k + dk - 1, di, dj);
        }
    }

    public void addSolidBox(World world, Random random, int i, int j, int k, int di, int dj, int dk)
    {
        for (int x = i; x < i + di; x++)
        {
            for (int y = j; y < j + dj; y++)
            {
                for (int z = k; z < k + dk; z++)
                {
                    if ((this.replaceAir) || (world.getBlockId(x, y, z) != 0))
                    {
                        if ((this.replaceSolid) || (world.getBlockId(x, y, z) == 0))
                        {
                            if (random.nextInt(this.chance) == 0)
                            {
                                world.setBlock(x, y, z, this.blockID2, this.meta2, 2);
                            }
                            else
                            {
                                world.setBlock(x, y, z, this.blockID1, this.meta1, 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isBoxSolid(World world, int i, int j, int k, int di, int dj, int dk)
    {
        boolean flag = true;

        for (int x = i; x < i + di; x++)
        {
            for (int y = j; y < j + dj; y++)
            {
                for (int z = k; z < k + dk; z++)
                {
                    if (world.getBlockId(x, y, z) == 0)
                    {
                        flag = false;
                    }
                }
            }
        }

        return flag;
    }

    public boolean isBoxEmpty(World world, int i, int j, int k, int di, int dj, int dk)
    {
        boolean flag = true;

        for (int x = i; x < i + di; x++)
        {
            for (int y = j; y < j + dj; y++)
            {
                for (int z = k; z < k + dk; z++)
                {
                    if (world.getBlockId(x, y, z) != 0)
                    {
                        flag = false;
                    }
                }
            }
        }

        return flag;
    }
}

