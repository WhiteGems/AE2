package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeSentryGuard extends ComponentDungeonBronzeRoom
{
    public int yOffset = 2;

    public ComponentDungeonBronzeSentryGuard(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, int var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.boundingBox = new StructureBoundingBox(var5, 50 - this.yOffset, var6, var5 + 24, 60, var6 + 15);
    }

    public ComponentDungeonBronzeSentryGuard(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        int var6;
        int var5;

        for (int var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
        {
            for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
            {
                for (var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; ++var6)
                {
                    this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, var6, var3);
                }
            }
        }

        byte var17 = 8;
        var5 = this.boundingBox.minX + var17 + (this.boundingBox.maxX - (this.boundingBox.minX + var17)) / 2 - 1;
        var6 = this.boundingBox.minY + 1;
        int var7 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int var8 = this.boundingBox.minX + var17 + (this.boundingBox.maxX - (this.boundingBox.minX + var17)) / 2 + 2;
        int var9 = this.boundingBox.minY + 1;
        int var10 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2;
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 1 + var17, var6, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 1, var6, this.boundingBox.minZ + 1, this.boundingBox.minX + 1 + var17, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(var1, var3, var5, var6 + 2, var7, var8, var9 + 2, var10, AetherBlocks.LockedDungeonStone.blockID, AetherBlocks.LockedDungeonStone.blockID, false);
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 4, var6, this.boundingBox.minZ + 4, this.boundingBox.maxX - 4, var6 + 1, this.boundingBox.maxZ - 4, AetherBlocks.LockedDungeonStone.blockID, AetherBlocks.LockedDungeonStone.blockID, false);
        int var18;
        int var13;

        for (int var11 = 1; var11 <= 2; ++var11)
        {
            boolean var12 = false;
            var13 = this.boundingBox.minY + var11;

            for (var18 = var11; var18 <= this.boundingBox.maxX - this.boundingBox.minX - 2 - var11; ++var18)
            {
                this.placeBlockAtCurrentPositionWithNotify(var1, AetherBlocks.CarvedDungeonStairs.blockID, 3, this.boundingBox.minX + var18 + 1, var13, this.boundingBox.maxZ - var11 - 1, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, AetherBlocks.CarvedDungeonStairs.blockID, 2, this.boundingBox.minX + var18 + 1, var13, this.boundingBox.minZ + var11 + 1, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var18, var13 + 2, this.boundingBox.minZ + var11, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var18, var13 + 2, this.boundingBox.maxZ - var11, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var18, var13 + 1, this.boundingBox.minZ + var11, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var18, var13 + 1, this.boundingBox.maxZ - var11, var3);
            }

            for (var18 = var11; var18 <= this.boundingBox.maxZ - this.boundingBox.minZ - 2 - var11; ++var18)
            {
                this.placeBlockAtCurrentPositionWithNotify(var1, AetherBlocks.CarvedDungeonStairs.blockID, 0, this.boundingBox.minX + var11 + 1, var13, this.boundingBox.minZ + var18 + 1, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, AetherBlocks.CarvedDungeonStairs.blockID, 1, this.boundingBox.maxX - var11 - 1, var13, this.boundingBox.minZ + var18 + 1, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.maxX - var11, var13 + 2, this.boundingBox.minZ + var18, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var11, var13 + 2, this.boundingBox.minZ + var18, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.maxX - var11, var13 + 1, this.boundingBox.minZ + var18, var3);
                this.placeBlockAtCurrentPositionWithNotify(var1, 0, 0, this.boundingBox.minX + var11, var13 + 1, this.boundingBox.minZ + var18, var3);
            }
        }

        if (var3.isVecInside(this.boundingBox.minX + var17 + (this.boundingBox.maxX - (this.boundingBox.minX + var17)) / 2 + 1, var9 + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1))
        {
            EntitySentryGuardian var19 = new EntitySentryGuardian(var1);
            var18 = this.boundingBox.minX + var17 + (this.boundingBox.maxX - (this.boundingBox.minX + var17)) / 2 + 1;
            var13 = var9 + 4;
            int var14 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
            var19.setPosition((double) var18, (double) var13, (double) var14);
            DungeonHandler var15 = DungeonHandler.instance();
            Dungeon var16 = var15.getInstanceAt(MathHelper.floor_double((double) var18), MathHelper.floor_double((double) var13), MathHelper.floor_double((double) var14));

            if (var16 != null)
            {
                var16.registerEntity((float) var18, (float) var13, (float) var14, var19);
            }

            if (!var1.isRemote)
            {
                var1.spawnEntityInWorld(var19);
            }
        }

        this.cutHolesForEntrances(var1, var2, var3);
        return true;
    }

    public void addShapeWithStairs(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8)
    {
        this.fillWithBlocksWithNotify(var1, var2, var3, var4, var5, var6, var7, var8, AetherBlocks.LockedDungeonStone.blockID, 0, false);
    }

    public void setBlockWithChance(World var1, Random var2, int var3, int var4, int var5, int var6, int var7, int var8, StructureBoundingBox var9)
    {
        if (var2.nextInt(var3) == 1)
        {
            this.placeBlockAtCurrentPosition(var1, var7, var8, var4, var5, var6, var9);
        }
    }

    public void setBlockWithChance(World var1, Random var2, int var3, int var4, int var5, int var6, int var7, StructureBoundingBox var8)
    {
        if (var2.nextInt(var3) == 1)
        {
            this.placeBlockAtCurrentPosition(var1, var7, 0, var4, var5, var6, var8);
        }
    }

    protected void fillWithBlocksWithNotify(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11)
    {
        for (int var12 = var4; var12 <= var7; ++var12)
        {
            for (int var13 = var3; var13 <= var6; ++var13)
            {
                for (int var14 = var5; var14 <= var8; ++var14)
                {
                    if (!var11 || this.getBlockIdAtCurrentPosition(var1, var13, var12, var14, var2) != 0)
                    {
                        if (var12 != var4 && var12 != var7 && var13 != var3 && var13 != var6 && var14 != var5 && var14 != var8)
                        {
                            this.placeBlockAtCurrentPositionWithNotify(var1, var10, 0, var13, var12, var14, var2);
                        } else
                        {
                            this.placeBlockAtCurrentPositionWithNotify(var1, var9, 0, var13, var12, var14, var2);
                        }
                    }
                }
            }
        }
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

                        this.placeBlockAtCurrentPositionWithNotify(var1, var19, var20, var17, var16, var18, var2);
                    }
                }
            }
        }
    }

    protected void placeBlockAtCurrentPositionWithNotify(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7)
    {
        int var8 = this.getXWithOffset(var4, var6);
        int var9 = this.getYWithOffset(var5);
        int var10 = this.getZWithOffset(var4, var6);

        if (var7.isVecInside(var8, var9, var10))
        {
            var1.setBlock(var8, var9, var10, var2, var3, 5);
        }
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 24;
        byte var7 = 16;
        byte var8 = 10;
        StructureBoundingBox var9 = new StructureBoundingBox(var2, var3, var4, var2, var3 + var8, var4);

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
