package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonCorridor extends ComponentDungeonBronzeRoom
{
    private int sectionCount;

    public ComponentDungeonCorridor(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random);
        this.boundingBox = structureBoundingBox;

        if (this.coordBaseMode != 2 && this.coordBaseMode != 0)
        {
            this.sectionCount = this.boundingBox.getXSize() / 5;
        }
        else
        {
            this.sectionCount = this.boundingBox.getZSize() / 5;
        }

        this.addEntranceToAllFourWalls();

        if (direction != 0 && direction == 2)
        {
            ;
        }
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 4, par4);
        int var7;

        for (var7 = par1Random.nextInt(3) + 2; var7 > 0; --var7)
        {
            int var8 = var7 * 5;

            switch (par5)
            {
                case 0:
                    var6.maxX = par2 + 4;
                    var6.maxZ = par4 + (var8 - 1);
                    break;

                case 1:
                    var6.minX = par2 - (var8 - 1);
                    var6.maxZ = par4 + 4;
                    break;

                case 2:
                    var6.maxX = par2 + 4;
                    var6.minZ = par4 - (var8 - 1);
                    break;

                case 3:
                    var6.maxX = par2 + (var8 - 1);
                    var6.maxZ = par4 + 4;
            }

            if (StructureComponent.findIntersecting(par0List, var6) == null)
            {
                break;
            }
        }

        return var7 > 0 ? var6 : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        int var8 = this.sectionCount * 5 - 1;
        this.fillVariedBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.DungeonHolystone.blockID, 2, AetherBlocks.DungeonHolystone.blockID, 0, 2, par2Random, true);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
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
}
