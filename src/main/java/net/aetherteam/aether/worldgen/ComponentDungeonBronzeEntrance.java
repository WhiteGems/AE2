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

    public ComponentDungeonBronzeEntrance(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.thedirection = var6;
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(var2, var3, var4, var2, var3 + 4, var4);
        int var7;

        for (var7 = 16; var7 > 0; --var7)
        {
            int var8 = var7 * 5;

            switch (var5)
            {
                case 0:
                    var6.maxX = var2 + 4;
                    var6.maxZ = var4 + (var8 - 1);
                    break;

                case 1:
                    var6.minX = var2 - (var8 - 1);
                    var6.maxZ = var4 + 4;
                    break;

                case 2:
                    var6.maxX = var2 + 4;
                    var6.minZ = var4 - (var8 - 1);
                    break;

                case 3:
                    var6.maxX = var2 + (var8 - 1);
                    var6.maxZ = var4 + 4;
            }

            if (StructureComponent.findIntersecting(var0, var6) == null)
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
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;

        switch (this.thedirection)
        {
            case 0:
                var5 = true;
                break;

            case 1:
                var6 = true;
                break;

            case 2:
                var7 = true;
                break;

            case 3:
                var4 = true;
        }

        byte var14 = 0;
        byte var12 = 0;
        byte var11 = 0;
        byte var13 = 0;

        for (int var8 = this.boundingBox.minX; var8 <= this.boundingBox.maxX; ++var8)
        {
            for (int var9 = this.boundingBox.minY; var9 <= this.boundingBox.maxY; ++var9)
            {
                for (int var10 = this.boundingBox.minZ; var10 <= this.boundingBox.maxZ; ++var10)
                {
                    if (this.getBlockIdAtCurrentPosition(var1, var9, var8, var10, var3) != 0)
                    {
                        this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, var9, var8, var10, var3);
                    }
                }
            }
        }

        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1 + var11, this.boundingBox.minY + 1, this.boundingBox.minZ + 1 + var13, this.boundingBox.maxX - 1 + var14, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1 + var12, 0, 0, false);
        this.cutHolesForEntrances(var1, var2, var3);
        return true;
    }
}
