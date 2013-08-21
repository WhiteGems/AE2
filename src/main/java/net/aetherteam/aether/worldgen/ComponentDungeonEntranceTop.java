package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonEntranceTop extends ComponentDungeonBronzeEntrance
{
    public int randHeight;
    public boolean isTower;
    public int towerRarity = 5;

    public ComponentDungeonEntranceTop(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random, structureBoundingBox, direction);
        this.boundingBox = structureBoundingBox;
        this.randHeight = par2Random.nextInt(25);
        this.isTower = par2Random.nextInt(this.towerRarity) == 0;
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

        for (int offset = 1; offset < 3; ++offset)
        {
            for (int floors = 0; floors < floornumb; floors += 5)
            {
                this.addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.maxZ + 1, 0, this.componentType);
                this.addStructor(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 1, this.componentType);
                this.addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ - 1, 2, this.componentType);
                this.addStructor(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + floornumb + 1, this.boundingBox.minZ + offset, 3, this.componentType);
            }

            this.addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, 0, this.componentType);
            this.addStructor(par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 1, this.componentType);
            this.addStructor(par2List, par3Random, this.boundingBox.minX + offset, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, 2, this.componentType);
            this.addStructor(par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ + offset, 3, this.componentType);
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        int floornumb = (this.boundingBox.maxY - this.boundingBox.minY) / 5;
        int floors;
        int j;

        for (floors = 1; floors < floornumb; ++floors)
        {
            this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
            this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 2, this.boundingBox.minY + 5 * floors, this.boundingBox.minZ + 2, this.boundingBox.maxX - 2, this.boundingBox.minY + 5 * floors, this.boundingBox.maxZ - 2, 0, 0, false);
            j = 5 * floors - 5;
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 1 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + j, this.boundingBox.minZ + 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + j, this.boundingBox.minZ + 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + j, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 2 + j, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 3 + j, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + j, this.boundingBox.minZ + 5, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + j, this.boundingBox.minZ + 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + j, this.boundingBox.minZ + 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 4 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 5 + j, this.boundingBox.minZ + 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + j, this.boundingBox.minZ + 3, par3StructureBoundingBox);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 3, this.boundingBox.minY + 1, this.boundingBox.minZ + 3, this.boundingBox.maxX - 3, this.boundingBox.maxY - 5, this.boundingBox.maxZ - 3, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        floors = this.boundingBox.maxY - this.boundingBox.minY - 5;
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.minZ, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ + 2, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX - 2, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.maxX, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.minY + 3 + floors, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + floors, this.boundingBox.minZ + 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + floors, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.minY + 4 + floors, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.maxX - 1, this.boundingBox.minY + 1 + floors, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + floors, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2, this.boundingBox.minY + 2 + floors, this.boundingBox.maxZ - 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1, this.boundingBox.minY + 2 + floors, this.boundingBox.maxZ - 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + 1, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + 1, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 2 + floors, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.DungeonEntranceController.blockID, 0, this.boundingBox.maxX - 3, this.boundingBox.minY + floors + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, par3StructureBoundingBox);

        if (this.isTower)
        {
            this.fillVariedBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.maxY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY + 5 + this.randHeight, this.boundingBox.maxZ, AetherBlocks.LightDungeonStone.blockID, 0, AetherBlocks.DungeonStone.blockID, 0, 15, par2Random, false);

            for (j = 0; j <= 6; ++j)
            {
                boolean i = false;
                int y = this.boundingBox.maxY + this.randHeight + 5 + j;
                int var9;

                for (var9 = j; var9 <= this.boundingBox.maxX - this.boundingBox.minX - j; ++var9)
                {
                    this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs.blockID, 3, this.boundingBox.minX + var9, y, this.boundingBox.maxZ - j, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs.blockID, 2, this.boundingBox.minX + var9, y, this.boundingBox.minZ + j, par3StructureBoundingBox);
                }

                for (var9 = j; var9 <= this.boundingBox.maxZ - this.boundingBox.minZ - j; ++var9)
                {
                    this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs.blockID, 0, this.boundingBox.minX + j, y, this.boundingBox.minZ + var9, par3StructureBoundingBox);
                    this.placeBlockAtCurrentPosition(par1World, AetherBlocks.CarvedStairs.blockID, 1, this.boundingBox.maxX - j, y, this.boundingBox.minZ + var9, par3StructureBoundingBox);
                }
            }
        }

        this.cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);
        return true;
    }

    public void fillVariedBlocks(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID1, int meta1, int blockID2, int meta2, int rarity, Random rand, boolean replaceBlocks)
    {
        for (int y = minY; y <= maxY; ++y)
        {
            for (int x = minX; x <= maxX; ++x)
            {
                for (int z = minZ; z <= maxZ; ++z)
                {
                    if (this.getBlockIdAtCurrentPosition(world, x, y, z, box) == 0 || replaceBlocks)
                    {
                        int blockID = blockID2;
                        int meta = meta2;

                        if (rand.nextInt(rarity) == 1)
                        {
                            blockID = blockID1;
                            meta = meta1;
                        }

                        this.placeBlockAtCurrentPosition(world, blockID, meta, x, y, z, box);
                    }
                }
            }
        }
    }

    public boolean checkForAir(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        for (int y = minY; y <= maxY; ++y)
        {
            for (int x = minX; x <= maxX; ++x)
            {
                for (int z = minZ; z <= maxZ; ++z)
                {
                    if (this.getBlockIdAtCurrentPosition(world, x, y, z, box) != 0 || this.getBlockIdAtCurrentPosition(world, x, y, z, box) != AetherBlocks.Aercloud.blockID)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
    {
        byte roomX = 7;
        byte roomZ = 7;
        byte roomY = 20;
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
                var6.minX = i - 1;
                var6.maxX = i + roomX - 1;
                var6.maxZ = k + roomZ;
                break;

            case 1:
                var6.minX = i - roomX;
                var6.minZ = k - 1;
                var6.maxZ = k + roomZ - 1;
                break;

            case 2:
                var6.minX = i - 1;
                var6.maxX = i + roomX - 1;
                var6.minZ = k - roomZ;
                break;

            case 3:
                var6.maxX = i + roomX;
                var6.minZ = k - 1;
                var6.maxZ = k + roomZ - 1;
        }

        return StructureComponent.findIntersecting(components, var6) != null ? null : var6;
    }
}
