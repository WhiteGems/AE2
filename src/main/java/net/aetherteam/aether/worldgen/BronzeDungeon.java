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
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte[] par6ArrayOfByte)
    {
        if (!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par2, par3))))
        {
            this.rand.nextInt();

            if (this.canSpawnStructureAtCoords(par2, par3))
            {
                StructureBronzeDungeonStart var7 = this.getStructureStart(par2, par3);
                this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par2, par3)), var7);
            }
        }
    }

    public boolean generateStructuresInChunk(World par1World, Random par2Random, int par3, int par4, double[] dirtDeapth)
    {
        int var5 = (par3 << 4) + 8;
        int var6 = (par4 << 4) + 8;
        boolean var7 = false;
        this.neqstructureMap.clear();
        Iterator i = this.structureMap.values().iterator();

        try
        {
            while (i.hasNext())
            {
                StructureBronzeDungeonStart e = (StructureBronzeDungeonStart)i.next();

                if (e.getBoundingBox().intersectsWith(var5 - 15, var6 - 15, var5 + 31, var6 + 31))
                {
                    e.generateStructure(par1World, par2Random, new StructureBoundingBox(var5, var6, var5 + 15, var6 + 15), dirtDeapth);
                    var7 = true;
                }
            }
        }
        catch (ConcurrentModificationException var11)
        {
            var11.printStackTrace();
            ++this.tried;

            if (this.tried < 4)
            {
                this.generateStructuresInChunk(par1World, par2Random, par3, par4, dirtDeapth);
            }
        }

        this.tried = 0;
        return var7;
    }

    public boolean hasStructureAt(int par1, int par2, int par3)
    {
        Iterator var4 = this.structureMap.values().iterator();

        while (var4.hasNext())
        {
            StructureBronzeDungeonStart var5 = (StructureBronzeDungeonStart)var4.next();

            if (var5.getBoundingBox().intersectsWith(par1, par3, par1, par3))
            {
                Iterator var6 = var5.getComponents().iterator();

                while (var6.hasNext())
                {
                    StructureComponent var7 = (StructureComponent)var6.next();

                    if (var7.getBoundingBox().isVecInside(par1, par2, par3))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean intersectsWith(StructureBoundingBox box, int x, int y, int z)
    {
        return box.maxX >= x && box.minX <= x && box.maxZ >= z && box.minZ <= z && y <= box.maxY && y >= box.minY;
    }

    public Dungeon getDungeonInstanceAt(int x, int y, int z)
    {
        Iterator var4 = this.structureMap.values().iterator();

        while (var4.hasNext())
        {
            StructureBronzeDungeonStart dungeonStart = (StructureBronzeDungeonStart)var4.next();

            if (dungeonStart.getBoundingBox().intersectsWith(x, z, x, z))
            {
                Iterator var6 = dungeonStart.getComponents().iterator();

                while (var6.hasNext())
                {
                    StructureComponent room = (StructureComponent)var6.next();

                    if (this.intersectsWith(room.getBoundingBox(), x, y, z))
                    {
                        return dungeonStart.dungeonInstance;
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
