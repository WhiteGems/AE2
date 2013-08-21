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
        Loc[] aloc = new Loc[] {new Loc(0, 0, 1), new Loc(0, 0, -1), new Loc(0, 1, 0), new Loc(0, -1, 0), new Loc(1, 0, 0), new Loc(-1, 0, 0)};
        return aloc;
    }

    public static Loc[] vecAdjacent2D()
    {
        Loc[] aloc = new Loc[] {new Loc(0, 1), new Loc(0, -1), new Loc(1, 0), new Loc(-1, 0)};
        return aloc;
    }

    public static ArrayList vecInRadius(int i, boolean flag)
    {
        ArrayList arraylist = new ArrayList();
        Loc loc = new Loc();

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                for (int l = -i; l <= i; ++l)
                {
                    Loc loc1 = new Loc(j, l, k);
                    double d = flag ? loc.distAdv(loc1) : (double)loc.distSimple(loc1);

                    if (d <= (double)i)
                    {
                        arraylist.add(loc1);
                    }
                }
            }
        }

        return arraylist;
    }

    public static ArrayList vecInRadius2D(int i, boolean flag)
    {
        ArrayList arraylist = new ArrayList();
        Loc loc = new Loc();

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                Loc loc1 = new Loc(j, k);
                double d = flag ? loc.distAdv(loc1) : (double)loc.distSimple(loc1);

                if (d <= (double)i)
                {
                    arraylist.add(loc1);
                }
            }
        }

        return arraylist;
    }

    public Loc()
    {
        this(0, 0, 0);
    }

    public Loc(double d, double d1)
    {
        this(d, 0.0D, d1);
    }

    public Loc(double d, double d1, double d2)
    {
        this.x = d;
        this.y = d1;
        this.z = d2;
    }

    public Loc(int i, int j)
    {
        this(i, 0, j);
    }

    public Loc(int i, int j, int k)
    {
        this((double)i, (double)j, (double)k);
    }

    public Loc(Loc loc)
    {
        this(loc.x, loc.y, loc.z);
    }

    public Loc(World world)
    {
        this(world.getSpawnPoint().posX, world.getSpawnPoint().posY, world.getSpawnPoint().posZ);
    }

    public Loc add(double d, double d1, double d2)
    {
        return new Loc(this.x + d, this.y + d1, this.z + d2);
    }

    public Loc add(int i, int j, int k)
    {
        return new Loc(this.x + (double)i, this.y + (double)j, this.z + (double)k);
    }

    public Loc add(Loc loc)
    {
        return new Loc(this.x + loc.x, this.y + loc.y, this.z + loc.z);
    }

    public Loc[] adjacent()
    {
        Loc[] aloc = vecAdjacent();

        for (int i = 0; i < aloc.length; ++i)
        {
            aloc[i] = this.add(aloc[i]);
        }

        return aloc;
    }

    public Loc[] adjacent2D()
    {
        Loc[] aloc = vecAdjacent();

        for (int i = 0; i < aloc.length; ++i)
        {
            aloc[i] = this.add(aloc[i]);
        }

        return aloc;
    }

    public double distAdv(Loc loc)
    {
        return Math.sqrt(Math.pow(this.x - loc.x, 2.0D) + Math.pow(this.y - loc.y, 2.0D) + Math.pow(this.z - loc.z, 2.0D));
    }

    public int distSimple(Loc loc)
    {
        return (int)(Math.abs(this.x - loc.x) + Math.abs(this.y - loc.y) + Math.abs(this.z - loc.z));
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof Loc))
        {
            return false;
        }
        else
        {
            Loc loc = (Loc)obj;
            return this.x == loc.x && this.y == loc.y && this.z == loc.z;
        }
    }

    public int getBlock(IBlockAccess iblockaccess)
    {
        return iblockaccess.getBlockId(this.x(), this.y(), this.z());
    }

    public int getLight(World world)
    {
        return world.getFullBlockLightValue(this.x(), this.y(), this.z());
    }

    public int getMeta(IBlockAccess iblockaccess)
    {
        return iblockaccess.getBlockMetadata(this.x(), this.y(), this.z());
    }

    public Loc getSide(int i)
    {
        switch (i)
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

    public TileEntity getTileEntity(IBlockAccess iblockaccess)
    {
        return iblockaccess.getBlockTileEntity(this.x(), this.y(), this.z());
    }

    public ArrayList inRadius(int i, boolean flag)
    {
        ArrayList arraylist = new ArrayList();

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                for (int l = -i; l <= i; ++l)
                {
                    Loc loc = (new Loc(j, l, k)).add(this);
                    double d = flag ? this.distAdv(loc) : (double)this.distSimple(loc);

                    if (d <= (double)i)
                    {
                        arraylist.add(loc);
                    }
                }
            }
        }

        return arraylist;
    }

    public ArrayList inRadius2D(int i, boolean flag)
    {
        ArrayList arraylist = new ArrayList();

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                Loc loc = (new Loc(j, k)).add(this);
                double d = flag ? this.distAdv(loc) : (double)this.distSimple(loc);

                if (d <= (double)i)
                {
                    arraylist.add(loc);
                }
            }
        }

        return arraylist;
    }

    public Loc multiply(double d, double d1, double d2)
    {
        return new Loc(this.x * d, this.y * d1, this.z * d2);
    }

    public Loc notify(World world)
    {
        world.notifyBlocksOfNeighborChange(this.x(), this.y(), this.z(), this.getBlock(world));
        return this;
    }

    public Loc removeTileEntity(World world)
    {
        world.removeBlockTileEntity(this.x(), this.y(), this.z());
        return this;
    }

    public Loc setBlock(World world, int i)
    {
        world.setBlock(this.x(), this.y(), this.z(), i);
        return this;
    }

    public Loc setBlockAndMeta(World world, int i, int j)
    {
        world.setBlock(this.x(), this.y(), this.z(), i, j, 4);
        return this;
    }

    public Loc setBlockAndMetaNotify(World world, int i, int j)
    {
        world.setBlock(this.x(), this.y(), this.z(), i, j, 4);
        return this;
    }

    public Loc setBlockNotify(World world, int i)
    {
        world.setBlock(this.x(), this.y(), this.z(), i);
        return this;
    }

    public Loc setMeta(World world, int i)
    {
        world.setBlockMetadataWithNotify(this.x(), this.y(), this.z(), i, 4);
        return this;
    }

    public Loc setMetaNotify(World world, int i)
    {
        world.setBlockMetadataWithNotify(this.x(), this.y(), this.z(), i, 4);
        return this;
    }

    public Loc setTileEntity(World world, TileEntity tileentity)
    {
        world.setBlockTileEntity(this.x(), this.y(), this.z(), tileentity);
        return this;
    }

    public Loc subtract(double d, double d1, double d2)
    {
        return new Loc(this.x - d, this.y - d1, this.z - d2);
    }

    public Loc subtract(int i, int j, int k)
    {
        return new Loc(this.x - (double)i, this.y - (double)j, this.z - (double)k);
    }

    public Loc subtract(Loc loc)
    {
        return new Loc(this.x - loc.x, this.y - loc.y, this.z - loc.z);
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
