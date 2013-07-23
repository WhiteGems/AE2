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

    public ComponentDungeonCorridor(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4);
        this.boundingBox = var5;

        if (this.coordBaseMode != 2 && this.coordBaseMode != 0)
        {
            this.sectionCount = this.boundingBox.getXSize() / 5;
        }
        else
        {
            this.sectionCount = this.boundingBox.getZSize() / 5;
        }

        this.addEntranceToAllFourWalls();

        if (var6 != 0 && var6 == 2)
        {
            ;
        }
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        StructureBoundingBox var6 = new StructureBoundingBox(var2, var3, var4, var2, var3 + 4, var4);
        int var7;

        for (var7 = var1.nextInt(3) + 2; var7 > 0; --var7)
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
        int var4 = this.sectionCount * 5 - 1;
        this.fillVariedBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.DungeonHolystone.blockID, 2, AetherBlocks.DungeonHolystone.blockID, 0, 2, var2, true);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.cutHolesForEntrances(var1, var2, var3);
        return true;
    }

    public void fillVariedBlocks(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, Random var14, boolean var15)
    {
        for (int var16 = var4; var16 <= var7; ++var16)
        {
            for (int var17 = var3; var17 <= var6; ++var17)
            {
                for (int var18 = var5; var18 <= var8; ++var18)
                {
                    if (this.getBlockIdAtCurrentPosition(var1, var17, var16, var18, var2) == 0 || var15)
                    {
                        int var19 = var11;
                        int var20 = var12;

                        if (var14.nextInt(var13) == 1)
                        {
                            var19 = var9;
                            var20 = var10;
                        }

                        this.placeBlockAtCurrentPosition(var1, var19, var20, var17, var16, var18, var2);
                    }
                }
            }
        }
    }
}
