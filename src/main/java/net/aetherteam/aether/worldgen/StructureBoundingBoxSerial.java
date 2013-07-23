package net.aetherteam.aether.worldgen;

import java.io.Serializable;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class StructureBoundingBoxSerial implements Serializable
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public StructureBoundingBoxSerial() {}

    public static StructureBoundingBoxSerial getNewBoundingBox()
    {
        return new StructureBoundingBoxSerial(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static StructureBoundingBoxSerial getComponentToAddBoundingBox(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
    {
        switch (var9)
        {
            case 0:
                return new StructureBoundingBoxSerial(var0 + var3, var1 + var4, var2 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var8 - 1 + var5);

            case 1:
                return new StructureBoundingBoxSerial(var0 - var8 + 1 + var5, var1 + var4, var2 + var3, var0 + var5, var1 + var7 - 1 + var4, var2 + var6 - 1 + var3);

            case 2:
                return new StructureBoundingBoxSerial(var0 + var3, var1 + var4, var2 - var8 + 1 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var5);

            case 3:
                return new StructureBoundingBoxSerial(var0 + var5, var1 + var4, var2 + var3, var0 + var8 - 1 + var5, var1 + var7 - 1 + var4, var2 + var6 - 1 + var3);

            default:
                return new StructureBoundingBoxSerial(var0 + var3, var1 + var4, var2 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var8 - 1 + var5);
        }
    }

    public StructureBoundingBoxSerial(StructureBoundingBox var1)
    {
        this.minX = var1.minX;
        this.minY = var1.minY;
        this.minZ = var1.minZ;
        this.maxX = var1.maxX;
        this.maxY = var1.maxY;
        this.maxZ = var1.maxZ;
    }

    public StructureBoundingBoxSerial(int var1, int var2, int var3, int var4, int var5, int var6)
    {
        this.minX = var1;
        this.minY = var2;
        this.minZ = var3;
        this.maxX = var4;
        this.maxY = var5;
        this.maxZ = var6;
    }

    public StructureBoundingBoxSerial(int var1, int var2, int var3, int var4)
    {
        this.minX = var1;
        this.minZ = var2;
        this.maxX = var3;
        this.maxZ = var4;
        this.minY = 1;
        this.maxY = 512;
    }

    public boolean intersectsWith(StructureBoundingBoxSerial var1)
    {
        return this.maxX >= var1.minX && this.minX <= var1.maxX && this.maxZ >= var1.minZ && this.minZ <= var1.maxZ && this.maxY >= var1.minY && this.minY <= var1.maxY;
    }

    public boolean intersectsWith(int var1, int var2, int var3, int var4)
    {
        return this.maxX >= var1 && this.minX <= var3 && this.maxZ >= var2 && this.minZ <= var4;
    }

    public void expandTo(StructureBoundingBoxSerial var1)
    {
        this.minX = Math.min(this.minX, var1.minX);
        this.minY = Math.min(this.minY, var1.minY);
        this.minZ = Math.min(this.minZ, var1.minZ);
        this.maxX = Math.max(this.maxX, var1.maxX);
        this.maxY = Math.max(this.maxY, var1.maxY);
        this.maxZ = Math.max(this.maxZ, var1.maxZ);
    }

    public void offset(int var1, int var2, int var3)
    {
        this.minX += var1;
        this.minY += var2;
        this.minZ += var3;
        this.maxX += var1;
        this.maxY += var2;
        this.maxZ += var3;
    }

    public boolean isVecInside(int var1, int var2, int var3)
    {
        return var1 >= this.minX && var1 <= this.maxX && var3 >= this.minZ && var3 <= this.maxZ && var2 >= this.minY && var2 <= this.maxY;
    }

    public int getXSize()
    {
        return this.maxX - this.minX + 1;
    }

    public int getYSize()
    {
        return this.maxY - this.minY + 1;
    }

    public int getZSize()
    {
        return this.maxZ - this.minZ + 1;
    }

    public int getCenterX()
    {
        return this.minX + (this.maxX - this.minX + 1) / 2;
    }

    public int getCenterY()
    {
        return this.minY + (this.maxY - this.minY + 1) / 2;
    }

    public int getCenterZ()
    {
        return this.minZ + (this.maxZ - this.minZ + 1) / 2;
    }

    public String toString()
    {
        return "(" + this.minX + ", " + this.minY + ", " + this.minZ + "; " + this.maxX + ", " + this.maxY + ", " + this.maxZ + ")";
    }
}
