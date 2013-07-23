package net.aetherteam.aether.worldgen;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeRoom extends StructureComponent
    implements Serializable
{
    public List roomsLinkedToTheRoom = new LinkedList();
    StructureBronzeDungeonStart structureAsAWhole;
    List entrances = new LinkedList();
    int YOffset = 0;

    public ComponentDungeonBronzeRoom(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart structureBronzeDugneonStart, Random par2Random, int par3, int par4)
    {
        super(par1);
        this.structureAsAWhole = structureBronzeDugneonStart;

        if (previousStructor != null)
        {
            this.roomsLinkedToTheRoom.add(previousStructor);
        }

        this.boundingBox = new StructureBoundingBox(par3, 50, par4, par3 + 7 + par2Random.nextInt(6), 60, par4 + 7 + par2Random.nextInt(6));
        addEntranceToAllFourWalls();
    }

    public ComponentDungeonBronzeRoom(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random)
    {
        super(par1);
        this.structureAsAWhole = Whole;
        this.roomsLinkedToTheRoom.add(previousStructor);
    }

    public ComponentDungeonBronzeRoom(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1);
        this.structureAsAWhole = Whole;
        this.roomsLinkedToTheRoom.add(previousStructor);
        this.boundingBox = structureBoundingBox;
        addEntranceToAllFourWalls();
    }

    public void addEntranceToAllFourWalls()
    {
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.minZ));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + this.YOffset, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
    }

    public void addStructor(List components, Random random, int i, int j, int k, int direction, int componentType)
    {
        StructureComponent var7 = StructureBronzeDungeonPieces.getNextComponent(this.structureAsAWhole, this, components, random, i, j, k, direction, componentType);

        if (var7 != null)
        {
            this.roomsLinkedToTheRoom.add(var7);
        }
    }

    public boolean ChunkCheck(int i, int j, int k, StructureBoundingBox box)
    {
        int x = getXWithOffset(i, k);
        int y = getYWithOffset(j);
        int z = getZWithOffset(i, k);
        return box.isVecInside(x, y, z);
    }

    protected void placeBlockAtCurrentPosition(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int i = getXWithOffset(par4, par6);
        int j = getYWithOffset(par5);
        int k = getZWithOffset(par4, par6);
        boolean var8;

        if (par7StructureBoundingBox.isVecInside(i, j, k))
        {
            Chunk var7 = par1World.getChunkFromChunkCoords(i >> 4, k >> 4);
            var8 = var7.setBlockIDWithMetadata(i & 0xF, j, k & 0xF, par2, par3);
        }
    }

    public void buildComponent(StructureComponent previousStructor, List par2List, Random par3Random)
    {
        buildComponent(par2List, par3Random);
    }

    public void buildComponent(List par2List, Random par3Random)
    {
        int componentType = getComponentType();
        int var6 = 2;

        if (this.boundingBox.getYSize() - var6 <= 0)
        {
            var6 = 1;
        }

        for (int var5 = 0; var5 < this.boundingBox.getXSize(); var5++)
        {
            if (var5 + 3 > this.boundingBox.getXSize())
            {
                break;
            }

            addStructor(par2List, par3Random, this.boundingBox.minX + var5, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ - 1, 2, componentType);
        }

        for (var5 = 0; var5 < this.boundingBox.getXSize(); var5++)
        {
            if (var5 + 3 > this.boundingBox.getXSize())
            {
                break;
            }

            addStructor(par2List, par3Random, this.boundingBox.minX + var5, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.maxZ + 1, 0, componentType);
        }

        for (var5 = 0; var5 < this.boundingBox.getZSize(); var5++)
        {
            if (var5 + 3 > this.boundingBox.getZSize())
            {
                break;
            }

            addStructor(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ + var5, 1, componentType);
        }

        for (var5 = 0; var5 < this.boundingBox.getZSize(); var5++)
        {
            if (var5 + 3 < this.boundingBox.getZSize())
            {
                break;
            }

            addStructor(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + par3Random.nextInt(var6) + this.YOffset, this.boundingBox.minZ + var5, 3, componentType);
        }
    }

    public int GetValue(int value, boolean b)
    {
        value = value >> 4 << 4;

        if (value < 0)
        {
            if (b)
            {
                return value;
            }

            return value + 16;
        }

        if (b)
        {
            return value;
        }

        return value + 16;
    }

    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);
        return true;
    }

    public void cutHolesForEntrances(World objWorld, Random random, StructureBoundingBox par3StructureBoundingBox)
    {
        Iterator iterMyEntrance = this.entrances.iterator();

        while (iterMyEntrance.hasNext())
        {
            StructureBoundingBox myCube = (StructureBoundingBox)iterMyEntrance.next();
            Iterator var4 = this.roomsLinkedToTheRoom.iterator();

            while (var4.hasNext())
            {
                Iterator iterRoomEntrance = ((ComponentDungeonBronzeRoom)var4.next()).entrances.iterator();

                while (iterRoomEntrance.hasNext())
                {
                    StructureBoundingBox cube = findIntercetingCube(myCube, (StructureBoundingBox)iterRoomEntrance.next());

                    if (cube != null)
                    {
                        fillWithBlocks(objWorld, par3StructureBoundingBox, cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.maxZ, 0, 0, false);
                    }
                }
            }
        }
    }

    public StructureBoundingBox findIntercetingCube(StructureBoundingBox b1, StructureBoundingBox b2)
    {
        int minX = Math.max(b1.minX, b2.minX);
        int minY = Math.max(b1.minY, b2.minY);
        int minZ = Math.max(b1.minZ, b2.minZ);
        int maxX = Math.min(b1.maxX, b2.maxX);
        int maxY = Math.min(b1.maxY, b2.maxY);
        int maxZ = Math.min(b1.maxZ, b2.maxZ);

        if ((minX < maxX) && (minY < maxY) && (minZ < maxZ))
        {
            return new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return null;
    }

    public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
    {
        int roomX = 8;
        int roomZ = 8;
        int roomY = 5;
        StructureBoundingBox var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);

        switch (direction)
        {
            case 0:
                var6.minX = (i - 1);
                var6.maxX = (i + roomX - 1);
                var6.maxZ = (k + roomZ);
                break;

            case 1:
                var6.minX = (i - roomX);
                var6.minZ = (k - 1);
                var6.maxZ = (k + roomZ - 1);
                break;

            case 2:
                var6.minX = (i - 1);
                var6.maxX = (i + roomX - 1);
                var6.minZ = (k - roomZ);
                break;

            case 3:
                var6.maxX = (i + roomX);
                var6.minZ = (k - 1);
                var6.maxZ = (k + roomZ - 1);
        }

        return StructureComponent.findIntersecting(components, var6) != null ? null : var6;
    }
}

