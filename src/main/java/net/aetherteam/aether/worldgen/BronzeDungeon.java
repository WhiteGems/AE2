package net.aetherteam.aether.worldgen;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.aetherteam.aether.dungeons.Dungeon;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class BronzeDungeon extends MapGenBase implements Serializable
{
    private double rarity = 0.2D;
    public Map structureMap = new HashMap();
    int tried = 0;
    List neqstructureMap = new LinkedList();

    protected boolean canSpawnStructureAtCoords(int var1, int var2)
    {
        return var1 % 13 == 0 && var2 % 13 == 0 && this.rand.nextDouble() < this.rarity;
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6)
    {
        if (!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2, var3))))
        {
            this.rand.nextInt();

            if (this.canSpawnStructureAtCoords(var2, var3))
            {
                StructureBronzeDungeonStart var7 = this.getStructureStart(var2, var3);
                this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(var2, var3)), var7);
            }
        }
    }

    public boolean generateStructuresInChunk(World var1, Random var2, int var3, int var4, double[] var5)
    {
        int var6 = (var3 << 4) + 8;
        int var7 = (var4 << 4) + 8;
        boolean var8 = false;
        this.neqstructureMap.clear();
        Iterator var9 = this.structureMap.values().iterator();

        try
        {
            while (var9.hasNext())
            {
                StructureBronzeDungeonStart var10 = (StructureBronzeDungeonStart) var9.next();

                if (var10.getBoundingBox().intersectsWith(var6 - 15, var7 - 15, var6 + 31, var7 + 31))
                {
                    var10.generateStructure(var1, var2, new StructureBoundingBox(var6, var7, var6 + 15, var7 + 15), var5);
                    var8 = true;
                }
            }
        } catch (ConcurrentModificationException var11)
        {
            var11.printStackTrace();
            ++this.tried;

            if (this.tried < 4)
            {
                this.generateStructuresInChunk(var1, var2, var3, var4, var5);
            }
        }

        this.tried = 0;
        return var8;
    }

    public boolean hasStructureAt(int var1, int var2, int var3)
    {
        Iterator var4 = this.structureMap.values().iterator();

        while (var4.hasNext())
        {
            StructureBronzeDungeonStart var5 = (StructureBronzeDungeonStart) var4.next();

            if (var5.getBoundingBox().intersectsWith(var1, var3, var1, var3))
            {
                Iterator var6 = var5.getComponents().iterator();

                while (var6.hasNext())
                {
                    StructureComponent var7 = (StructureComponent) var6.next();

                    if (var7.getBoundingBox().isVecInside(var1, var2, var3))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean intersectsWith(StructureBoundingBox var1, int var2, int var3, int var4)
    {
        return var1.maxX >= var2 && var1.minX <= var2 && var1.maxZ >= var4 && var1.minZ <= var4 && var3 <= var1.maxY && var3 >= var1.minY;
    }

    public Dungeon getDungeonInstanceAt(int var1, int var2, int var3)
    {
        Iterator var4 = this.structureMap.values().iterator();

        while (var4.hasNext())
        {
            StructureBronzeDungeonStart var5 = (StructureBronzeDungeonStart) var4.next();

            if (var5.getBoundingBox().intersectsWith(var1, var3, var1, var3))
            {
                Iterator var6 = var5.getComponents().iterator();

                while (var6.hasNext())
                {
                    StructureComponent var7 = (StructureComponent) var6.next();

                    if (this.intersectsWith(var7.getBoundingBox(), var1, var2, var3))
                    {
                        return var5.dungeonInstance;
                    }
                }
            }
        }

        return null;
    }

    protected StructureBronzeDungeonStart getStructureStart(int var1, int var2)
    {
        return new StructureBronzeDungeonStart(this.worldObj, this.rand, var1, var2);
    }
}
