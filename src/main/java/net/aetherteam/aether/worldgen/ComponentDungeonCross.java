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

    public ComponentDungeonCross(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        this.corridorDirection = par4;
        this.boundingBox = par3StructureBoundingBox;
        this.isMultipleFloors = par3StructureBoundingBox.getYSize() > 3;
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 4, par4);

        switch (par5)
        {
            case 0:
                var6.minX = par2 - 1;
                var6.maxX = par2 + 7;
                var6.maxZ = par4 + 8;
                break;

            case 1:
                var6.minX = par2 - 8;
                var6.minZ = par4 - 1;
                var6.maxZ = par4 + 7;
                break;

            case 2:
                var6.minX = par2 - 1;
                var6.maxX = par2 + 7;
                var6.minZ = par4 - 8;
                break;

            case 3:
                var6.maxX = par2 + 8;
                var6.minZ = par4 - 1;
                var6.maxZ = par4 + 7;
        }

        return StructureComponent.findIntersecting(par0List, var6) != null ? null : var6;
    }

    public void addStructor(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7) {}

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int var4 = this.getComponentType();

        switch (this.corridorDirection)
        {
            case 0:
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 1:
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                break;

            case 2:
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 3:
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                this.addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
        }

        if (this.isMultipleFloors)
        {
            if (par3Random.nextBoolean())
            {
                ;
            }

            if (par3Random.nextBoolean())
            {
                ;
            }

            if (par3Random.nextBoolean())
            {
                ;
            }

            if (par3Random.nextBoolean())
            {
                ;
            }
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox))
        {
            return false;
        }
        else
        {
            if (this.isMultipleFloors)
            {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, 0, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, 0, 0, false);
            }
            else
            {
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
                this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
            }

            int var4;
            int var5;

            for (var4 = this.boundingBox.minX; var4 <= this.boundingBox.maxX; ++var4)
            {
                for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                {
                    this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var4, this.boundingBox.minY, var5, par3StructureBoundingBox);
                }
            }

            if (this.corridorDirection != 1)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX - 1, var4, var5, par3StructureBoundingBox);
                    }
                }
            }

            if (this.corridorDirection != 2)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.minZ - 1, par3StructureBoundingBox);
                    }
                }
            }

            if (this.corridorDirection != 3)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.maxX + 1, var4, var5, par3StructureBoundingBox);
                    }
                }
            }

            if (this.corridorDirection != 4)
            {
                for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
                {
                    for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
                    {
                        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.maxZ + 1, par3StructureBoundingBox);
                    }
                }
            }

            Iterator var6 = this.roomsLinkedToTheRoom.iterator();

            while (var6.hasNext())
            {
                StructureBoundingBox var7 = (StructureBoundingBox)var6.next();
                this.fillWithBlocks(par1World, par3StructureBoundingBox, var7.minX, var7.maxY, var7.minZ, var7.maxX, var7.maxY, var7.maxZ, 0, 0, false);
            }

            return true;
        }
    }
}
