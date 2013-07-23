package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonStair extends ComponentDungeonBronzeRoom
{
    private int sectionCount;

    public ComponentDungeonStair(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4);
        this.boundingBox = var5;
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

        for (int var5 = 1; var5 < var4; ++var5)
        {
            this.fillWithBlocks(var1, var3, this.boundingBox.minX, this.boundingBox.minY + 5 * var5, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + 5 * var5, this.boundingBox.maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
            this.fillWithBlocks(var1, var3, this.boundingBox.minX + 2, this.boundingBox.minY + 5 * var5, this.boundingBox.minZ + 2, this.boundingBox.maxX - 2, this.boundingBox.minY + 5 * var5, this.boundingBox.maxZ - 2, 0, 0, false);
            int var6 = 5 * var5 - 5;
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 1 + var6, this.boundingBox.minZ + 3, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 4, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 2 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 5, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 3 + var6, this.boundingBox.minZ + 4, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 5, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 3, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 4, this.boundingBox.minY + 4 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 3, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 2, var3);
            this.placeBlockAtCurrentPosition(var1, AetherBlocks.LockedDungeonStone.blockID, 0, this.boundingBox.minX + 2, this.boundingBox.minY + 5 + var6, this.boundingBox.minZ + 3, var3);
        }

        this.fillWithBlocks(var1, var3, this.boundingBox.minX + 3, this.boundingBox.minY + 1, this.boundingBox.minZ + 3, this.boundingBox.maxX - 3, this.boundingBox.maxY - 2, this.boundingBox.maxZ - 3, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.cutHolesForEntrances(var1, var2, var3);
        return true;
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 7;
        byte var7 = 7;
        int var8 = 10;

        if (var1.nextInt(5) == 0)
        {
            var8 += 5;
        }

        if (var1.nextInt(5) == 0)
        {
            var8 += 5;
        }

        StructureBoundingBox var9;

        if (var1.nextInt(8) != 0)
        {
            var9 = new StructureBoundingBox(var2, var3, var4, var2, var3 + var8, var4);
        }
        else
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
