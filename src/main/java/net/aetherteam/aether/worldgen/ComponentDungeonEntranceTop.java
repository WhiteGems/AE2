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

    public ComponentDungeonEntranceTop(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.boundingBox = var5;
        this.randHeight = var4.nextInt(25);
        this.isTower = var4.nextInt(this.towerRarity) == 0;
        int var7 = this.boundingBox.maxY - this.boundingBox.minY;

        for (int var8 = 0; var8 < var7; var8 += 5)
        {
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var8, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + var8, this.boundingBox.minZ));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var8, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + var8, this.boundingBox.maxZ + 2));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + var8, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.minY + 3 + var8, this.boundingBox.maxZ - 1));
            this.entrances.add(new StructureBoundingBox(this.boundingBox.maxX, this.boundingBox.minY + 1 + var8, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.minY + 3 + var8, this.boundingBox.maxZ - 1));
        }
    }

    public void buildComponent(List var1, Random var2)
    {
        int var3 = this.boundingBox.maxY - this.boundingBox.minY;

        for (int var4 = 1; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < var3; var5 += 5)
            {
                this.addStructor(var1, var2, this.boundingBox.minX + var4, this.boundingBox.minY + var3 + 1, this.boundingBox.maxZ + 1, 0, this.componentType);
                this.addStructor(var1, var2, this.boundingBox.minX - 1, this.boundingBox.minY + var3 + 1, this.boundingBox.minZ + var4, 1, this.componentType);
                this.addStructor(var1, var2, this.boundingBox.minX + var4, this.boundingBox.minY + var3 + 1, this.boundingBox.minZ - 1, 2, this.componentType);
                this.addStructor(var1, var2, this.boundingBox.maxX + 1, this.boundingBox.minY + var3 + 1, this.boundingBox.minZ + var4, 3, this.componentType);
            }

            this.addStructor(var1, var2, this.boundingBox.minX + var4, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, 0, this.componentType);
            this.addStructor(var1, var2, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.minZ + var4, 1, this.componentType);
            this.addStructor(var1, var2, this.boundingBox.minX + var4, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, 2, this.componentType);
            this.addStructor(var1, var2, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ + var4, 3, this.componentType);
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        int var4 = (this.boundingBox.maxY - this.boundingBox.minY) / 5;
        int var6;
        int var5;

        for (var5 = 1; var5 < var4; ++var5)
        {
            this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY + 5 * var5, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + 5 * var5, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
            this.fillWithBlocks(var1, var3, this.boundingBox.minX + 2, this.boundingBox.minY + 5 * var5, this.boundingBox.minZ + 2, this.boundingBox.maxX - 2, this.boundingBox.minY + 5 * var5, this.boundingBox.maxZ - 2, 0, 0, false);
            var6 = 5 * var5 - 5;
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 3, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 4, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 4, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 3, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 3, var3);
        }

        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 3, this.boundingBox.minY + 1, this.boundingBox.minZ + 3, this.boundingBox.maxX - 3, this.boundingBox.maxY - 5, this.boundingBox.maxZ - 3, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        var5 = this.boundingBox.maxY - this.boundingBox.minY - 5;
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ - 2, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + var5, this.boundingBox.minZ, 0, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var5, this.boundingBox.maxZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 + var5, this.boundingBox.maxZ + 2, 0, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX - 2, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.minY + 3 + var5, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.maxX, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ + 1, this.boundingBox.maxX + 2, this.boundingBox.minY + 3 + var5, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + var5, this.boundingBox.minZ + 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var5, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + var5, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.minY + 4 + var5, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.fillWithBlocks(var1, var3, this.boundingBox.maxX - 1, this.boundingBox.minY + 1 + var5, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 4 + var5, this.boundingBox.maxZ - 1, AetherBlocks.DungeonEntrance.blockID, 0, false);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2, this.boundingBox.minY + 2 + var5, this.boundingBox.maxZ - 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1, this.boundingBox.minY + 2 + var5, this.boundingBox.maxZ - 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + 1, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.minX + 1, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntrance.blockID, 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 2 + var5, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.DungeonEntranceController.blockID, 0, this.boundingBox.maxX - 3, this.boundingBox.minY + var5 + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1, var3);

        if (this.isTower)
        {
            this.fillVariedBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.maxY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY + 5 + this.randHeight, this.boundingBox.maxZ, AetherBlocks.LightDungeonStone.blockID, 0, AetherBlocks.DungeonStone.blockID, 0, 15, var2, false);

            for (var6 = 0; var6 <= 6; ++var6)
            {
                boolean var7 = false;
                int var8 = this.boundingBox.maxY + this.randHeight + 5 + var6;
                int var9;

                for (var9 = var6; var9 <= this.boundingBox.maxX - this.boundingBox.minX - var6; ++var9)
                {
                    this.placeBlockAtCurrentPosition(var1, AetherBlocks.CarvedStairs.blockID, 3, this.boundingBox.minX + var9, var8, this.boundingBox.maxZ - var6, var3);
                    this.placeBlockAtCurrentPosition(var1, AetherBlocks.CarvedStairs.blockID, 2, this.boundingBox.minX + var9, var8, this.boundingBox.minZ + var6, var3);
                }

                for (var9 = var6; var9 <= this.boundingBox.maxZ - this.boundingBox.minZ - var6; ++var9)
                {
                    this.placeBlockAtCurrentPosition(var1, AetherBlocks.CarvedStairs.blockID, 0, this.boundingBox.minX + var6, var8, this.boundingBox.minZ + var9, var3);
                    this.placeBlockAtCurrentPosition(var1, AetherBlocks.CarvedStairs.blockID, 1, this.boundingBox.maxX - var6, var8, this.boundingBox.minZ + var9, var3);
                }
            }
        }

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

    public boolean checkForAir(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        for (int var9 = var4; var9 <= var7; ++var9)
        {
            for (int var10 = var3; var10 <= var6; ++var10)
            {
                for (int var11 = var5; var11 <= var8; ++var11)
                {
                    if (this.getBlockIdAtCurrentPosition(var1, var10, var9, var11, var2) != 0 || this.getBlockIdAtCurrentPosition(var1, var10, var9, var11, var2) != AetherBlocks.Aercloud.blockID)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 7;
        byte var7 = 7;
        byte var8 = 20;
        StructureBoundingBox var9;

        if (var1.nextInt(8) != 0)
        {
            var9 = new StructureBoundingBox(var2, var3, var4, var2, var3 + var8, var4);
        } else
        {
            var9 = new StructureBoundingBox(var2, var3 - var8 + 4, var4, var2, var3 + 4, var4);
        }

        switch (var5)
        {
            case 0:
                var9.minX = var2 - 1;
                var9.maxX = var2 + var6 - 1;
                var9.maxZ = var4 + var7;
                break;

            case 1:
                var9.minX = var2 - var6;
                var9.minZ = var4 - 1;
                var9.maxZ = var4 + var7 - 1;
                break;

            case 2:
                var9.minX = var2 - 1;
                var9.maxX = var2 + var6 - 1;
                var9.minZ = var4 - var7;
                break;

            case 3:
                var9.maxX = var2 + var6;
                var9.minZ = var4 - 1;
                var9.maxZ = var4 + var7 - 1;
        }

        return StructureComponent.findIntersecting(var0, var9) != null ? null : var9;
    }
}
