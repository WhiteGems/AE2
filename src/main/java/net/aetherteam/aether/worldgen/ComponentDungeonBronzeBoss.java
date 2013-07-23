package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeBoss extends ComponentDungeonBronzeRoom
{
    int yOffset = 3;

    int entranceOffset = 8;

    public ComponentDungeonBronzeBoss(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart structureBronzeDugneonStart, Random par2Random, int par3, int par4)
    {
        super(par1, previousStructor, structureBronzeDugneonStart, par2Random, par3, par4);
        this.boundingBox = new StructureBoundingBox(par3, 30 - this.yOffset, par4, par3 + 24, 40, par4 + 15);
        this.entrances.clear();
        addEntranceToAllFourWalls();
    }

    public void addEntranceToAllFourWalls()
    {
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ - 2, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.minZ));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.maxZ, this.boundingBox.minX + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ + 2));
        this.entrances.add(new StructureBoundingBox(this.boundingBox.minX - 2, this.boundingBox.minY + 1 + this.yOffset, this.boundingBox.minZ + 1, this.boundingBox.minX, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1));
    }

    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox ChunkBox)
    {
        for (int var12 = this.boundingBox.minY; var12 <= this.boundingBox.maxY; var12++)
        {
            for (int var13 = this.boundingBox.minX; var13 <= this.boundingBox.maxX; var13++)
            {
                for (int var14 = this.boundingBox.minZ; var14 <= this.boundingBox.maxZ; var14++)
                {
                    placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, var13, var12, var14, ChunkBox);
                }
            }
        }

        int minX = this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 - 1;
        int minY = this.boundingBox.minY + this.yOffset + 1;
        int minZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int maxX = this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 2;
        int maxY = this.boundingBox.minY + this.yOffset + 1;
        int maxZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2;
        fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1 + this.entranceOffset, minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1, minY, this.boundingBox.minZ + 1, this.boundingBox.minX - 1 + this.entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        Dungeon dungeon2 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(this.boundingBox.minX + this.entranceOffset), MathHelper.floor_double(minY), MathHelper.floor_double(this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 2));
        dungeon2.registerSafeBlock(this.boundingBox.minX + this.entranceOffset, minY + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoor.blockID, 1);
        fillBlocksAndRegister(par1World, ChunkBox, this.boundingBox.minX + this.entranceOffset, minY, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 2, this.boundingBox.minX + this.entranceOffset, minY + 3, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2, AetherBlocks.BronzeDoor.blockID, 0, false, dungeon2);
        placeBlockAtCurrentPosition(par1World, AetherBlocks.BronzeDoor.blockID, 1, this.boundingBox.minX + this.entranceOffset, minY + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, ChunkBox);
        placeBlockAtCurrentPosition(par1World, AetherBlocks.BronzeDoorController.blockID, 0, this.boundingBox.minX + this.entranceOffset + 1, minY - 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, ChunkBox);
        fillWithBlocksWithNotify(par1World, ChunkBox, minX, minY - 3, minZ, maxX, maxY, maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);

        if (ChunkBox.isVecInside(this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 1, maxY + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1))
        {
            EntitySlider slider = new EntitySlider(par1World, this.boundingBox.minX + this.entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + this.entranceOffset)) / 2 + 1, maxY + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1);
            DungeonHandler handler = DungeonHandler.instance();
            Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(slider.posX), MathHelper.floor_double(slider.posY), MathHelper.floor_double(slider.posZ));
            int x = MathHelper.floor_double(slider.posX);
            int y = MathHelper.floor_double(slider.posY);
            int z = MathHelper.floor_double(slider.posZ);
            dungeon.registerEntity(x, y, z, slider);
            dungeon.registerSafeBlock(this.boundingBox.minX + this.entranceOffset, minY + 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoor.blockID, 1);
            dungeon.registerSafeBlock(this.boundingBox.minX + this.entranceOffset + 1, minY - 1, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2, AetherBlocks.BronzeDoorController.blockID, 0);
            par1World.spawnEntityInWorld(slider);
        }

        fillWithBlocksWithNotify(par1World, ChunkBox, minX + 1, minY - 2, minZ + 1, maxX - 1, maxY - 1, maxZ - 1, 0, 0, false);
        TileEntityChest chest;

        if (ChunkBox.isVecInside(minX + 1, minY - 2, minZ + 1))
        {
            par1World.setBlock(minX + 1, minY - 2, minZ + 1, AetherBlocks.TreasureChest.blockID);
            chest = (TileEntityChest)par1World.getBlockTileEntity(minX + 1, minY - 2, minZ + 1);
        }

        cutHolesForEntrances(par1World, par2Random, ChunkBox);
        return true;
    }

    public void setBlockWithChance(World world, Random rand, int rarity, int x, int y, int z, int blockID, int meta, StructureBoundingBox box)
    {
        if (rand.nextInt(rarity) == 1)
        {
            placeBlockAtCurrentPosition(world, blockID, meta, x, y, z, box);
        }
    }

    public void setBlockWithChance(World world, Random rand, int rarity, int x, int y, int z, int blockID, StructureBoundingBox box)
    {
        if (rand.nextInt(rarity) == 1)
        {
            placeBlockAtCurrentPosition(world, blockID, 0, x, y, z, box);
        }
    }

    protected void fillWithBlocksWithNotify(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, boolean par11)
    {
        for (int var12 = par4; var12 <= par7; var12++)
        {
            for (int var13 = par3; var13 <= par6; var13++)
            {
                for (int var14 = par5; var14 <= par8; var14++)
                {
                    if ((!par11) || (getBlockIdAtCurrentPosition(par1World, var13, var12, var14, par2StructureBoundingBox) != 0))
                    {
                        if ((var12 != par4) && (var12 != par7) && (var13 != par3) && (var13 != par6) && (var14 != par5) && (var14 != par8))
                        {
                            placeBlockAtCurrentPositionWithNotify(par1World, par10, 0, var13, var12, var14, par2StructureBoundingBox);
                        }
                        else
                        {
                            placeBlockAtCurrentPositionWithNotify(par1World, par9, 0, var13, var12, var14, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    protected void fillBlocksAndRegister(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, boolean par11, Dungeon dungeon)
    {
        for (int var12 = par4; var12 <= par7; var12++)
        {
            for (int var13 = par3; var13 <= par6; var13++)
            {
                for (int var14 = par5; var14 <= par8; var14++)
                {
                    if ((!par11) || (getBlockIdAtCurrentPosition(par1World, var13, var12, var14, par2StructureBoundingBox) != 0))
                    {
                        if ((var12 != par4) && (var12 != par7) && (var13 != par3) && (var13 != par6) && (var14 != par5) && (var14 != par8))
                        {
                            placeBlockAtCurrentPositionWithNotify(par1World, par10, 0, var13, var12, var14, par2StructureBoundingBox);
                            dungeon.registerSafeBlock(var13, var12, var14, par9, 0);
                        }
                        else
                        {
                            placeBlockAtCurrentPositionWithNotify(par1World, par9, 0, var13, var12, var14, par2StructureBoundingBox);
                            dungeon.registerSafeBlock(var13, var12, var14, par9, 0);
                        }
                    }
                }
            }
        }
    }

    public void fillVariedBlocks(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID1, int meta1, int blockID2, int meta2, int rarity, Random rand, boolean replaceBlocks)
    {
        for (int y = minY; y <= maxY; y++)
        {
            for (int x = minX; x <= maxX; x++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    if ((getBlockIdAtCurrentPosition(world, x, y, z, box) == 0) || (replaceBlocks))
                    {
                        int blockID = blockID2;
                        int meta = meta2;

                        if (rand.nextInt(rarity) == 1)
                        {
                            blockID = blockID1;
                            meta = meta1;
                        }

                        placeBlockAtCurrentPositionWithNotify(world, blockID, meta, x, y, z, box);
                    }
                }
            }
        }
    }

    protected void placeBlockAtCurrentPositionWithNotify(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int var8 = getXWithOffset(par4, par6);
        int var9 = getYWithOffset(par5);
        int var10 = getZWithOffset(par4, par6);

        if (par7StructureBoundingBox.isVecInside(var8, var9, var10))
        {
            par1World.setBlock(var8, var9, var10, par2, par3, ChunkProviderAether.placementFlagType);
        }
    }

    public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
    {
        int roomX = 24;
        int roomZ = 16;
        int roomY = 10;
        StructureBoundingBox var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);

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

