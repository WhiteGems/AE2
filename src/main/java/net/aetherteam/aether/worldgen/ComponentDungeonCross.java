package net.aetherteam.aether.worldgen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
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
        this.isMultipleFloors = (par3StructureBoundingBox.getYSize() > 3);
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 4, par4);

        switch (par5)
        {
            case 0:
                var6.minX = (par2 - 1);
                var6.maxX = (par2 + 7);
                var6.maxZ = (par4 + 8);
                break;

            case 1:
                var6.minX = (par2 - 8);
                var6.minZ = (par4 - 1);
                var6.maxZ = (par4 + 7);
                break;

            case 2:
                var6.minX = (par2 - 1);
                var6.maxX = (par2 + 7);
                var6.minZ = (par4 - 8);
                break;

            case 3:
                var6.maxX = (par2 + 8);
                var6.minZ = (par4 - 1);
                var6.maxZ = (par4 + 7);
        }

        return StructureComponent.findIntersecting(par0List, var6) != null ? null : var6;
    }

    public void addStructor(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
    }

    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int var4 = getComponentType();

        switch (this.corridorDirection)
        {
            case 0:
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 1:
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                break;

            case 2:
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 1, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
                break;

            case 3:
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, var4);
                addStructor(par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, 3, var4);
        }

        if (this.isMultipleFloors)
        {
            if ((par3Random.nextBoolean()) && (
                        (par3Random.nextBoolean()) && (
                            (par3Random.nextBoolean()) &&
                            (!par3Random.nextBoolean()))));
        }
    }

    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox))
        {
            return false;
        }

        if (this.isMultipleFloors)
        {
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, 0, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, 0, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, 0, 0, false);
        }
        else
        {
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, 0, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, 0, 0, false);
        }

        for (int var4 = this.boundingBox.minX; var4 <= this.boundingBox.maxX; var4++)
        {
            for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; var5++)
            {
                placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var4, this.boundingBox.minY, var5, par3StructureBoundingBox);
            }
        }

        if (this.corridorDirection != 1)
        {
            for (int var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; var4++)
            {
                for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; var5++)
                {
                    placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX - 1, var4, var5, par3StructureBoundingBox);
                }
            }
        }

        if (this.corridorDirection != 2)
        {
            for (int var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; var4++)
            {
                for (int var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; var5++)
                {
                    placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.minZ - 1, par3StructureBoundingBox);
                }
            }
        }

        if (this.corridorDirection != 3)
        {
            for (int var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; var4++)
            {
                for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; var5++)
                {
                    placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.maxX + 1, var4, var5, par3StructureBoundingBox);
                }
            }
        }

        if (this.corridorDirection != 4)
        {
            for (int var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; var4++)
            {
                for (int var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; var5++)
                {
                    placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, this.boundingBox.maxZ + 1, par3StructureBoundingBox);
                }
            }
        }

        Iterator var4 = this.roomsLinkedToTheRoom.iterator();

        while (var4.hasNext())
        {
            StructureBoundingBox var5 = (StructureBoundingBox)var4.next();
            fillWithBlocks(par1World, par3StructureBoundingBox, var5.minX, var5.maxY, var5.minZ, var5.maxX, var5.maxY, var5.maxZ, 0, 0, false);
        }

        return true;
    }
}

