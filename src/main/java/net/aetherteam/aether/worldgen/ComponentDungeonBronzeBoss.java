package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeBoss extends ComponentDungeonBronzeRoom
{
    int yOffset = 3;
    int entranceOffset = 8;

    public ComponentDungeonBronzeBoss(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, int var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
        this.boundingBox = new StructureBoundingBox(var5, 30 - this.yOffset, var6, var5 + 24, 40, var6 + 15);
        this.entrances.clear();
        this.addEntranceToAllFourWalls();
    }

    public void addEntranceToAllFourWalls()
    {
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ - 2, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.minZ));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.maxZ, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3)
    {
        int var4;
        int var5;
        int var6;

        for (var4 = this.boundingBox.minY; var4 <= this.boundingBox.maxY; ++var4)
        {
            for (var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
            {
                for (var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; ++var6)
                {
                    this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, var6, var3);
                }
            }
        }

        var4 = this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 - 1;
        var5 = this.boundingBox.minY + this.yOffset + 1;
        var6 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int var7 = this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 2;
        int var8 = this.boundingBox.minY + this.yOffset + 1;
        int var9 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2;
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 1 + this.entranceOffset, var5, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 1, var5, this.boundingBox.minZ + 1, this.boundingBox.minX - 1 + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        Dungeon var10 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double) (this.boundingBox.minX + this.entranceOffset)), MathHelper.floor_double((double) var5), MathHelper.floor_double((double) (this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 2)));
        var10.registerSafeBlock(this.boundingBox.minX + this.entranceOffset, var5 + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoor.blockID, 1);
        this.fillBlocksAndRegister(var1, var3, this.boundingBox.minX + this.entranceOffset, var5, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 2, this.boundingBox.minX + this.entranceOffset, var5 + 3, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2, AetherBlocks.BronzeDoor.blockID, 0, false, var10);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.BronzeDoor.blockID, 1, this.boundingBox.minX + this.entranceOffset, var5 + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, var3);
        this.placeBlockAtCurrentPosition(var1, AetherBlocks.BronzeDoorController.blockID, 0, this.boundingBox.minX + this.entranceOffset + 1, var5 - 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, var3);
        this.fillWithBlocksWithNotify(var1, var3, var4, var5 - 3, var6, var7, var8, var9, AetherBlocks.LockedDungeonStone.blockID, 0, false);

        if (var3.isVecInside(this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 1, var8 + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1))
        {
            EntitySlider var11 = new EntitySlider(var1, (double) (this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 1), (double) (var8 + 2), (double) (this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1));
            DungeonHandler var12 = DungeonHandler.instance();
            Dungeon var13 = var12.getInstanceAt(MathHelper.floor_double(var11.posX), MathHelper.floor_double(var11.posY), MathHelper.floor_double(var11.posZ));
            int var14 = MathHelper.floor_double(var11.posX);
            int var15 = MathHelper.floor_double(var11.posY);
            int var16 = MathHelper.floor_double(var11.posZ);
            var13.registerEntity((float) var14, (float) var15, (float) var16, var11);
            var13.registerSafeBlock(this.boundingBox.minX + this.entranceOffset, var5 + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoor.blockID, 1);
            var13.registerSafeBlock(this.boundingBox.minX + this.entranceOffset + 1, var5 - 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoorController.blockID, 0);
            var1.spawnEntityInWorld(var11);
        }

        this.fillWithBlocksWithNotify(var1, var3, var4 + 1, var5 - 2, var6 + 1, var7 - 1, var8 - 1, var9 - 1, 0, 0, false);

        if (var3.isVecInside(var4 + 1, var5 - 2, var6 + 1))
        {
            var1.setBlock(var4 + 1, var5 - 2, var6 + 1, AetherBlocks.TreasureChest.blockID);
            TileEntityChest var17 = (TileEntityChest) ((TileEntityChest) var1.getBlockTileEntity(var4 + 1, var5 - 2, var6 + 1));
        }

        this.cutHolesForEntrances(var1, var2, var3);
        return true;
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

    protected void fillBlocksAndRegister(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11, Dungeon var12)
    {
        for (int var13 = var4; var13 <= var7; ++var13)
        {
            for (int var14 = var3; var14 <= var6; ++var14)
            {
                for (int var15 = var5; var15 <= var8; ++var15)
                {
                    if (!var11 || this.getBlockIdAtCurrentPosition(var1, var14, var13, var15, var2) != 0)
                    {
                        if (var13 != var4 && var13 != var7 && var14 != var3 && var14 != var6 && var15 != var5 && var15 != var8)
                        {
                            this.placeBlockAtCurrentPositionWithNotify(var1, var10, 0, var14, var13, var15, var2);
                            var12.registerSafeBlock(var14, var13, var15, var9, 0);
                        } else
                        {
                            this.placeBlockAtCurrentPositionWithNotify(var1, var9, 0, var14, var13, var15, var2);
                            var12.registerSafeBlock(var14, var13, var15, var9, 0);
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
            var1.setBlock(var8, var9, var10, var2, var3, 2);
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
