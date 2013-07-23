package net.aetherteam.aether.worldgen;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeRoom extends StructureComponent implements Serializable
{
    public List roomsLinkedToTheRoom = new LinkedList();
    StructureBronzeDungeonStart structureAsAWhole;
    List entrances = new LinkedList();
    int YOffset = 0;

    public ComponentDungeonBronzeRoom(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, int var5, int var6)
    {
        super(var1);
        this.structureAsAWhole = var3;

        if (var2 != null)
        {
            this.roomsLinkedToTheRoom.add(var2);
        }

        this.boundingBox = new StructureBoundingBox(var5, 50, var6, var5 + 7 + var4.nextInt(6), 60, var6 + 7 + var4.nextInt(6));
        this.addEntranceToAllFourWalls();
    }

    public ComponentDungeonBronzeRoom(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4)
    {
        super(var1);
        this.structureAsAWhole = var3;
        this.roomsLinkedToTheRoom.add(var2);
    }

    public ComponentDungeonBronzeRoom(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1);
        this.structureAsAWhole = var3;
        this.roomsLinkedToTheRoom.add(var2);
        this.boundingBox = var5;
        this.addEntranceToAllFourWalls();
    }

    public void addEntranceToAllFourWalls()
    {
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.minZ));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
    }

    public void addStructor(List var1, Random var2, int var3, int var4, int var5, int var6, int var7)
    {
        StructureComponent var8 = StructureBronzeDungeonPieces.getNextComponent(this.structureAsAWhole, this, var1, var2, var3, var4, var5, var6, var7);

        if (var8 != null)
        {
            this.roomsLinkedToTheRoom.add(var8);
        }
    }

    public boolean ChunkCheck(int var1, int var2, int var3, StructureBoundingBox var4)
    {
        int var5 = this.getXWithOffset(var1, var3);
        int var6 = this.getYWithOffset(var2);
        int var7 = this.getZWithOffset(var1, var3);
        return var4.isVecInside(var5, var6, var7);
    }

    /**
     * current Position depends on currently set Coordinates mode, is computed here
     */
    protected void placeBlockAtCurrentPosition(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7)
    {
        int var8 = this.getXWithOffset(var4, var6);
        int var9 = this.getYWithOffset(var5);
        int var10 = this.getZWithOffset(var4, var6);

        if (var7.isVecInside(var8, var9, var10))
        {
            Chunk var11 = var1.getChunkFromChunkCoords(var8 >> 4, var10 >> 4);
            var11.setBlockIDWithMetadata(var8 & 15, var9, var10 & 15, var2, var3);
        }
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent var1, List var2, Random var3)
    {
        this.buildComponent(var2, var3);
    }

    public void buildComponent(List var1, Random var2)
    {
        int var3 = this.getComponentType();
        byte var4 = 2;

        if (this.boundingBox.getYSize() - var4 <= 0)
        {
            var4 = 1;
        }

        int var5;

        for (var5 = 0; var5 < this.boundingBox.getXSize() && var5 + 3 <= this.boundingBox.getXSize(); ++var5)
        {
            this.addStructor(var1, var2, this.boundingBox.minX + var5, this.boundingBox.minY + var2.nextInt(var4) + this.YOffset, this.boundingBox.minZ - 1, 2, var3);
        }

        for (var5 = 0; var5 < this.boundingBox.getXSize() && var5 + 3 <= this.boundingBox.getXSize(); ++var5)
        {
            this.addStructor(var1, var2, this.boundingBox.minX + var5, this.boundingBox.minY + var2.nextInt(var4) + this.YOffset, this.boundingBox.maxZ + 1, 0, var3);
        }

        for (var5 = 0; var5 < this.boundingBox.getZSize() && var5 + 3 <= this.boundingBox.getZSize(); ++var5)
        {
            this.addStructor(var1, var2, this.boundingBox.minX - 1, this.boundingBox.minY + var2.nextInt(var4) + this.YOffset, this.boundingBox.minZ + var5, 1, var3);
        }

        for (var5 = 0; var5 < this.boundingBox.getZSize() && var5 + 3 >= this.boundingBox.getZSize(); ++var5)
        {
            this.addStructor(var1, var2, this.boundingBox.maxX + 1, this.boundingBox.minY + var2.nextInt(var4) + this.YOffset, this.boundingBox.minZ + var5, 3, var3);
        }
    }

    public int GetValue(int var1, boolean var2)
    {
        var1 = var1 >> 4 << 4;
        return var1 < 0 ? (var2 ? var1 : var1 + 16) : (var2 ? var1 : var1 + 16);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.cutHolesForEntrances(var1, var2, var3);
        return true;
    }

    public void cutHolesForEntrances(World var1, Random var2, StructureBoundingBox var3)
    {
        Iterator var4 = this.entrances.iterator();

        while (var4.hasNext())
        {
            StructureBoundingBox var5 = (StructureBoundingBox)var4.next();
            Iterator var6 = this.roomsLinkedToTheRoom.iterator();

            while (var6.hasNext())
            {
                Iterator var7 = ((ComponentDungeonBronzeRoom)var6.next()).entrances.iterator();

                while (var7.hasNext())
                {
                    StructureBoundingBox var8 = this.findIntercetingCube(var5, (StructureBoundingBox)var7.next());

                    if (var8 != null)
                    {
                        this.fillWithBlocks(var1, var3, var8.minX, var8.minY, var8.minZ, var8.maxX, var8.maxY, var8.maxZ, 0, 0, false);
                    }
                }
            }
        }
    }

    public StructureBoundingBox findIntercetingCube(StructureBoundingBox var1, StructureBoundingBox var2)
    {
        int var3 = Math.max(var1.minX, var2.minX);
        int var4 = Math.max(var1.minY, var2.minY);
        int var5 = Math.max(var1.minZ, var2.minZ);
        int var6 = Math.min(var1.maxX, var2.maxX);
        int var7 = Math.min(var1.maxY, var2.maxY);
        int var8 = Math.min(var1.maxZ, var2.maxZ);
        return var3 < var6 && var4 < var7 && var5 < var8 ? new StructureBoundingBox(var3, var4, var5, var6, var7, var8) : null;
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 8;
        byte var7 = 8;
        byte var8 = 5;
        StructureBoundingBox var9 = new StructureBoundingBox(var2, var3, var4, var2, var3 + var8, var4);

        switch (var5)
        {
            case 0:
                var9.minX = var2 - 1;
                var9.maxX = var2 + var6 - 1;
                var9.maxZ = var4 + var7;
                break;

            case 1:
                var9.minX = var2 - var6;
                var9.minZ = var4 - 1;
                var9.maxZ = var4 + var7 - 1;
                break;

            case 2:
                var9.minX = var2 - 1;
                var9.maxX = var2 + var6 - 1;
                var9.minZ = var4 - var7;
                break;

            case 3:
                var9.maxX = var2 + var6;
                var9.minZ = var4 - 1;
                var9.maxZ = var4 + var7 - 1;
        }

        return StructureComponent.findIntersecting(var0, var9) != null ? null : var9;
    }
}
