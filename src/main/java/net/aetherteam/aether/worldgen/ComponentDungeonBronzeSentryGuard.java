package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeSentryGuard extends ComponentDungeonBronzeRoom
{
    public int yOffset = 2;

    public ComponentDungeonBronzeSentryGuard(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart structureBronzeDugneonStart, Random par2Random, int par3, int par4)
    {
        super(par1, previousStructor, structureBronzeDugneonStart, par2Random, par3, par4);
        this.boundingBox = new StructureBoundingBox(par3, 50 - this.yOffset, par4, par3 + 24, 60, par4 + 15);
    }

    public ComponentDungeonBronzeSentryGuard(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random, structureBoundingBox, direction);
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

        int entranceOffset = 8;
        int minX = this.boundingBox.minX + entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2 - 1;
        int minY = this.boundingBox.minY + 1;
        int minZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int maxX = this.boundingBox.minX + entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2 + 2;
        int maxY = this.boundingBox.minY + 1;
        int maxZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2;
        fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1 + entranceOffset, minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1, minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1 + entranceOffset, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        fillWithBlocksWithNotify(par1World, ChunkBox, minX, minY + 2, minZ, maxX, maxY + 2, maxZ, AetherBlocks.LockedDungeonStone.blockID, AetherBlocks.LockedDungeonStone.blockID, false);
        fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 4, minY, this.boundingBox.minZ + 4, this.boundingBox.maxX - 4, minY + 1, this.boundingBox.maxZ - 4, AetherBlocks.LockedDungeonStone.blockID, AetherBlocks.LockedDungeonStone.blockID, false);

        for (int j = 1; j <= 2; j++)
        {
            int i = 0;
            int y = this.boundingBox.minY + j;

            for (i = j; i <= this.boundingBox.maxX - this.boundingBox.minX - 2 - j; i++)
            {
                placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 3, this.boundingBox.minX + i + 1, y, this.boundingBox.maxZ - j - 1, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 2, this.boundingBox.minX + i + 1, y, this.boundingBox.minZ + j + 1, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + i, y + 2, this.boundingBox.minZ + j, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + i, y + 2, this.boundingBox.maxZ - j, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + i, y + 1, this.boundingBox.minZ + j, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + i, y + 1, this.boundingBox.maxZ - j, ChunkBox);
            }

            for (i = j; i <= this.boundingBox.maxZ - this.boundingBox.minZ - 2 - j; i++)
            {
                placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 0, this.boundingBox.minX + j + 1, y, this.boundingBox.minZ + i + 1, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 1, this.boundingBox.maxX - j - 1, y, this.boundingBox.minZ + i + 1, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.maxX - j, y + 2, this.boundingBox.minZ + i, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + j, y + 2, this.boundingBox.minZ + i, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.maxX - j, y + 1, this.boundingBox.minZ + i, ChunkBox);
                placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + j, y + 1, this.boundingBox.minZ + i, ChunkBox);
            }
        }

        if (ChunkBox.isVecInside(this.boundingBox.minX + entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2 + 1, maxY + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1))
        {
            EntitySentryGuardian golem = new EntitySentryGuardian(par1World);
            int x = this.boundingBox.minX + entranceOffset + (this.boundingBox.maxX - (this.boundingBox.minX + entranceOffset)) / 2 + 1;
            int y = maxY + 4;
            int z = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
            golem.setPosition(x, y, z);
            DungeonHandler handler = DungeonHandler.instance();
            Dungeon dungeon = handler.getInstanceAt(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));

            if (dungeon != null)
            {
                dungeon.registerEntity(x, y, z, golem);
            }

            if (!par1World.isRemote)
            {
                par1World.spawnEntityInWorld(golem);
            }
        }

        cutHolesForEntrances(par1World, par2Random, ChunkBox);
        return true;
    }

    public void addShapeWithStairs(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        fillWithBlocksWithNotify(world, box, minX, minY, minZ, maxX, maxY, maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
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
            par1World.setBlock(var8, var9, var10, par2, par3, 5);
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

