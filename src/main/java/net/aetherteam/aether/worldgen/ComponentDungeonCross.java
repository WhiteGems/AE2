package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonCross extends StructureComponent
{
    private final int corridorDirection;
    private final boolean isMultipleFloors;
    List roomsLinkedToTheRoom = new LinkedList();

    public ComponentDungeonCross(int var1, Random var2, StructureBoundingBox var3, int var4)
    {
        super(var1);
        this.corridorDirection = var4;
        this.boundingBox = var3;
        this.isMultipleFloors = var3.getYSize() > 3;
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(var2, var3, var4, var2, var3 + 4, var4);

        switch (var5)
        {
            case 0:
                var6.minX = var2 - 1;
                var6.maxX = var2 + 7;
                var6.maxZ = var4 + 8;
                break;

            case 1:
                var6.minX = var2 - 8;
                var6.minZ = var4 - 1;
                var6.maxZ = var4 + 7;
                break;

            case 2:
                var6.minX = var2 - 1;
                var6.maxX = var2 + 7;
                var6.minZ = var4 - 8;
                break;

            case 3:
                var6.maxX = var2 + 8;
                var6.minZ = var4 - 1;
                var6.maxZ = var4 + 7;
        }

        return StructureComponent.findIntersecting(var0, var6) != null ? null : var6;
    }

    public void addStructor(StructureComponent var1, List var2, Random var3, int var4, int var5, int var6, int var7, int var8) {}

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent var1, List var2, Random var3)
    {
        int var4 = this.getComponentType();

        switch (this.corridorDirection)
        {
            case 0:
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 1:
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                break;

            case 2:
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 3:
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
        }

        if (this.isMultipleFloors)
        {
            if (var3.nextBoolean())
            {
                ;
            }

            if (var3.nextBoolean())
            {
                ;
            }

            if (var3.nextBoolean())
            {
                ;
            }

            if (var3.nextBoolean())
            {
                ;
            }
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        if (this.isLiquidInStructureBoundingBox(var1, var3))
        {
            return false;
        }
        else
        {
            if (this.isMultipleFloors)
            {
                this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, 0, 0, false);
                this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
                this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, 0, 0, false);
            }
            else
            {
                this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
            }

            int var4;
            int var5;

            for (var4 = this.boundingBox.minX; var4 <= this.boundingBox.maxX; ++var4)
            {
                for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                {
                    this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, var4, this.boundingBox.minY, var5, var3);
                }
            }

            if (this.corridorDirection != 1)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX - 1, var4, var5, var3);
                    }
                }
            }

            if (this.corridorDirection != 2)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.minZ - 1, var3);
                    }
                }
            }

            if (this.corridorDirection != 3)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.maxX + 1, var4, var5, var3);
                    }
                }
            }

            if (this.corridorDirection != 4)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.maxZ + 1, var3);
                    }
                }
            }

            Iterator var6 = this.roomsLinkedToTheRoom.iterator();

            while (var6.hasNext())
            {
                StructureBoundingBox var7 = (StructureBoundingBox)var6.next();
                this.fillWithBlocks(var1, var3, var7.minX, var7.maxY, var7.minZ, var7.maxX, var7.maxY, var7.maxZ, 0, 0, false);
            }

            return true;
        }
    }
}
