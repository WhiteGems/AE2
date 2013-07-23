package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonStair extends ComponentDungeonBronzeRoom
{
    private int sectionCount;

    public ComponentDungeonStair(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random);
        this.boundingBox = structureBoundingBox;
        int floornumb = this.boundingBox.maxY - this.boundingBox.minY;

        for (int floors = 0; floors < floornumb; floors += 5)
        {
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.minZ));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ + 2));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1));
        }
    }

    public void buildComponent(List par2List, Random par3Random)
    {
        int floornumb = this.boundingBox.maxY - this.boundingBox.minY;

        for (int offset = 1; offset < 3; offset++)
        {
            for (int floors = 0; floors < floornumb; floors += 5)
            {
                addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.maxZ + 1, 0, this.componentType);
                addStructor(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 1, this.componentType);
                addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ - 1, 2, this.componentType);
                addStructor(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 3, this.componentType);
            }

            addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, 0, this.componentType);
            addStructor(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 1, this.componentType);
            addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, 2, this.componentType);
            addStructor(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 3, this.componentType);
        }
    }

    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        int floornumb = (this.boundingBox.maxY - this.boundingBox.minY) / 5;

        for (int floors = 1; floors < floornumb; floors++)
        {
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 2, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ + 2, this.boundingBox.maxX - 2, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ - 2, 0, 0, false);
            int offset = 5 * floors - 5;
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 4, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 2 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + offset, this.boundingBox.minZ + 4, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 4 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + offset, this.boundingBox.minZ + 3, par3StructureBoundingBox);
        }

        fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 3, this.boundingBox.minY + 1, this.boundingBox.minZ + 3, this.boundingBox.maxX - 3, this.boundingBox.maxY - 2, this.boundingBox.maxZ - 3, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);
        return true;
    }

    public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
    {
        int roomX = 7;
        int roomZ = 7;
        int roomY = 10;

        if (random.nextInt(5) == 0)
        {
            roomY += 5;
        }

        if (random.nextInt(5) == 0)
        {
            roomY += 5;
        }

        StructureBoundingBox var6;
        StructureBoundingBox var6;

        if (random.nextInt(8) != 0)
        {
            var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);
        }
        else
        {
            var6 = new StructureBoundingBox(i, j - roomY + 4, k, i, j + 4, k);
        }

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

