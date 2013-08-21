package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeEntrance extends ComponentDungeonBronzeRoom
{
    int thedirection;

    public ComponentDungeonBronzeEntrance(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random, structureBoundingBox, direction);
        this.thedirection = direction;
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(par2, par3, par4, par2, par3 + 4, par4);
        int var7;

        for (var7 = 16; var7 > 0; --var7)
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
        boolean i = false;
        boolean k = false;
        boolean i2 = false;
        boolean k2 = false;

        switch (this.thedirection)
        {
            case 0:
                k = true;
                break;

            case 1:
                i2 = true;
                break;

            case 2:
                k2 = true;
                break;

            case 3:
                i = true;
        }

        byte var11 = 0;
        byte var121 = 0;
        byte var131 = 0;
        byte var141 = 0;

        for (int var12 = this.boundingBox.minX; var12 <= this.boundingBox.maxX; ++var12)
        {
            for (int var13 = this.boundingBox.minY; var13 <= this.boundingBox.maxY; ++var13)
            {
                for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; ++var14)
                {
                    if (this.getBlockIdAtCurrentPosition(par1World, var13, var12, var14, par3StructureBoundingBox) != 0)
                    {
                        this.placeBlockAtCurrentPosition(par1World, AetherBlocks.LockedDungeonStone.blockID, 0, var13, var12, var14, par3StructureBoundingBox);
                    }
                }
            }
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, this.boundingBox.minX + 1 + var131, this.boundingBox.minY + 1, this.boundingBox.minZ + 1 + var141, this.boundingBox.maxX - 1 + var11, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1 + var121, 0, 0, false);
        this.cutHolesForEntrances(par1World, par2Random, par3StructureBoundingBox);
        return true;
    }
}
