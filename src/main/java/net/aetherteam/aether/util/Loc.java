package net.aetherteam.aether.util;

import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Loc
{
    public final double x;
    public final double y;
    public final double z;

    public static Loc[] vecAdjacent()
    {
        Loc[] var0 = new Loc[] {new Loc(0, 0, 1), new Loc(0, 0, -1), new Loc(0, 1, 0), new Loc(0, -1, 0), new Loc(1, 0, 0), new Loc(-1, 0, 0)};
        return var0;
    }

    public static Loc[] vecAdjacent2D()
    {
        Loc[] var0 = new Loc[] {new Loc(0, 1), new Loc(0, -1), new Loc(1, 0), new Loc(-1, 0)};
        return var0;
    }

    public static ArrayList vecInRadius(int var0, boolean var1)
    {
        ArrayList var2 = new ArrayList();
        Loc var3 = new Loc();

        for (int var4 = -var0; var4 <= var0; ++var4)
        {
            for (int var5 = -var0; var5 <= var0; ++var5)
            {
                for (int var6 = -var0; var6 <= var0; ++var6)
                {
                    Loc var7 = new Loc(var4, var6, var5);
                    double var8 = var1 ? var3.distAdv(var7) : (double)var3.distSimple(var7);

                    if (var8 <= (double)var0)
                    {
                        var2.add(var7);
                    }
                }
            }
        }

        return var2;
    }

    public static ArrayList vecInRadius2D(int var0, boolean var1)
    {
        ArrayList var2 = new ArrayList();
        Loc var3 = new Loc();

        for (int var4 = -var0; var4 <= var0; ++var4)
        {
            for (int var5 = -var0; var5 <= var0; ++var5)
            {
                Loc var6 = new Loc(var4, var5);
                double var7 = var1 ? var3.distAdv(var6) : (double)var3.distSimple(var6);

                if (var7 <= (double)var0)
                {
                    var2.add(var6);
                }
            }
        }

        return var2;
    }

    public Loc()
    {
        this(0, 0, 0);
    }

    public Loc(double var1, double var3)
    {
        this(var1, 0.0D, var3);
    }

    public Loc(double var1, double var3, double var5)
    {
        this.x = var1;
        this.y = var3;
        this.z = var5;
    }

    public Loc(int var1, int var2)
    {
        this(var1, 0, var2);
    }

    public Loc(int var1, int var2, int var3)
    {
        this((double)var1, (double)var2, (double)var3);
    }

    public Loc(Loc var1)
    {
        this(var1.x, var1.y, var1.z);
    }

    public Loc(World var1)
    {
        this(var1.getSpawnPoint().posX, var1.getSpawnPoint().posY, var1.getSpawnPoint().posZ);
    }

    public Loc add(double var1, double var3, double var5)
    {
        return new Loc(this.x + var1, this.y + var3, this.z + var5);
    }

    public Loc add(int var1, int var2, int var3)
    {
        return new Loc(this.x + (double)var1, this.y + (double)var2, this.z + (double)var3);
    }

    public Loc add(Loc var1)
    {
        return new Loc(this.x + var1.x, this.y + var1.y, this.z + var1.z);
    }

    public Loc[] adjacent()
    {
        Loc[] var1 = vecAdjacent();

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            var1[var2] = this.add(var1[var2]);
        }

        return var1;
    }

    public Loc[] adjacent2D()
    {
        Loc[] var1 = vecAdjacent();

        for (int var2 = 0; var2 < var1.length; ++var2)
        {
            var1[var2] = this.add(var1[var2]);
        }

        return var1;
    }

    public double distAdv(Loc var1)
    {
        return Math.sqrt(Math.pow(this.x - var1.x, 2.0D) + Math.pow(this.y - var1.y, 2.0D) + Math.pow(this.z - var1.z, 2.0D));
    }

    public int distSimple(Loc var1)
    {
        return (int)(Math.abs(this.x - var1.x) + Math.abs(this.y - var1.y) + Math.abs(this.z - var1.z));
    }

    public boolean equals(Object var1)
    {
        if (!(var1 instanceof Loc))
        {
            return false;
        }
        else
        {
            Loc var2 = (Loc)var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
        }
    }

    public int getBlock(IBlockAccess var1)
    {
        return var1.getBlockId(this.x(), this.y(), this.z());
    }

    public int getLight(World var1)
    {
        return var1.getFullBlockLightValue(this.x(), this.y(), this.z());
    }

    public int getMeta(IBlockAccess var1)
    {
        return var1.getBlockMetadata(this.x(), this.y(), this.z());
    }

    public Loc getSide(int var1)
    {
        switch (var1)
        {
            case 0:
                return new Loc(this.x, this.y - 1.0D, this.z);

            case 1:
                return new Loc(this.x, this.y + 1.0D, this.z);

            case 2:
                return new Loc(this.x, this.y, this.z - 1.0D);

            case 3:
                return new Loc(this.x, this.y, this.z + 1.0D);

            case 4:
                return new Loc(this.x - 1.0D, this.y, this.z);

            case 5:
                return new Loc(this.x + 1.0D, this.y, this.z);

            default:
                return new Loc(this.x, this.y, this.z);
        }
    }

    public TileEntity getTileEntity(IBlockAccess var1)
    {
        return var1.getBlockTileEntity(this.x(), this.y(), this.z());
    }

    public ArrayList inRadius(int var1, boolean var2)
    {
        ArrayList var3 = new ArrayList();

        for (int var4 = -var1; var4 <= var1; ++var4)
        {
            for (int var5 = -var1; var5 <= var1; ++var5)
            {
                for (int var6 = -var1; var6 <= var1; ++var6)
                {
                    Loc var7 = (new Loc(var4, var6, var5)).add(this);
                    double var8 = var2 ? this.distAdv(var7) : (double)this.distSimple(var7);

                    if (var8 <= (double)var1)
                    {
                        var3.add(var7);
                    }
                }
            }
        }

        return var3;
    }

    public ArrayList inRadius2D(int var1, boolean var2)
    {
        ArrayList var3 = new ArrayList();

        for (int var4 = -var1; var4 <= var1; ++var4)
        {
            for (int var5 = -var1; var5 <= var1; ++var5)
            {
                Loc var6 = (new Loc(var4, var5)).add(this);
                double var7 = var2 ? this.distAdv(var6) : (double)this.distSimple(var6);

                if (var7 <= (double)var1)
                {
                    var3.add(var6);
                }
            }
        }

        return var3;
    }

    public Loc multiply(double var1, double var3, double var5)
    {
        return new Loc(this.x * var1, this.y * var3, this.z * var5);
    }

    public Loc notify(World var1)
    {
        var1.notifyBlocksOfNeighborChange(this.x(), this.y(), this.z(), this.getBlock(var1));
        return this;
    }

    public Loc removeTileEntity(World var1)
    {
        var1.removeBlockTileEntity(this.x(), this.y(), this.z());
        return this;
    }

    public Loc setBlock(World var1, int var2)
    {
        var1.setBlock(this.x(), this.y(), this.z(), var2);
        return this;
    }

    public Loc setBlockAndMeta(World var1, int var2, int var3)
    {
        var1.setBlock(this.x(), this.y(), this.z(), var2, var3, 4);
        return this;
    }

    public Loc setBlockAndMetaNotify(World var1, int var2, int var3)
    {
        var1.setBlock(this.x(), this.y(), this.z(), var2, var3, 4);
        return this;
    }

    public Loc setBlockNotify(World var1, int var2)
    {
        var1.setBlock(this.x(), this.y(), this.z(), var2);
        return this;
    }

    public Loc setMeta(World var1, int var2)
    {
        var1.setBlockMetadataWithNotify(this.x(), this.y(), this.z(), var2, 4);
        return this;
    }

    public Loc setMetaNotify(World var1, int var2)
    {
        var1.setBlockMetadataWithNotify(this.x(), this.y(), this.z(), var2, 4);
        return this;
    }

    public Loc setTileEntity(World var1, TileEntity var2)
    {
        var1.setBlockTileEntity(this.x(), this.y(), this.z(), var2);
        return this;
    }

    public Loc subtract(double var1, double var3, double var5)
    {
        return new Loc(this.x - var1, this.y - var3, this.z - var5);
    }

    public Loc subtract(int var1, int var2, int var3)
    {
        return new Loc(this.x - (double)var1, this.y - (double)var2, this.z - (double)var3);
    }

    public Loc subtract(Loc var1)
    {
        return new Loc(this.x - var1.x, this.y - var1.y, this.z - var1.z);
    }

    public String toString()
    {
        return "(" + this.x + "," + this.y + "," + this.z + ")";
    }

    public int x()
    {
        return (int)this.x;
    }

    public int y()
    {
        return (int)this.y;
    }

    public int z()
    {
        return (int)this.z;
    }
}
