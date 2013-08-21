package net.aetherteam.aether.worldgen;

import java.util.List;
import java.util.Random;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeLarge extends ComponentDungeonBronzeRoom
{
    public int yOffset = 6;

    public ComponentDungeonBronzeLarge(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart structureBronzeDugneonStart, Random par2Random, int par3, int par4)
    {
        super(par1, previousStructor, structureBronzeDugneonStart, par2Random, par3, par4);
    }

    public ComponentDungeonBronzeLarge(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random, structureBoundingBox, direction);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox ChunkBox)
    {
        int minX;
        int minY;

        for (int entranceOffset = this.boundingBox.minY; entranceOffset <= this.boundingBox.maxY; ++entranceOffset)
        {
            for (minX = this.boundingBox.minX; minX <= this.boundingBox.maxX; ++minX)
            {
                for (minY = this.boundingBox.minZ; minY <= this.boundingBox.maxZ; ++minY)
                {
                    this.placeBlockAtCurrentPosition(par1World, par2Random.nextInt(20) == 1 ? AetherBlocks.LockedLightDungeonStone.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, minX, entranceOffset, minY, ChunkBox);
                }
            }
        }

        byte var15 = 8;
        minX = this.boundingBox.minX + var15 + (this.boundingBox.maxX - (this.boundingBox.minX + var15)) / 2 - 1;
        minY = this.boundingBox.minY + this.yOffset + 1;
        int minZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int maxX = this.boundingBox.minX + var15 + (this.boundingBox.maxX - (this.boundingBox.minX + var15)) / 2 + 2;
        int maxY = this.boundingBox.minY + this.yOffset + 1;
        int maxZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2;
        this.fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1 + var15, minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + 1, minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1 + var15, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(par1World, ChunkBox, this.boundingBox.minX + var15, minY, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1, this.boundingBox.minX + var15, minY + 3, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 2, 0, 0, false);
        this.fillWithBlocksWithNotify(par1World, ChunkBox, minX, minY - 3, minZ, maxX, maxY, maxZ, AetherBlocks.LockedDungeonStone.blockID, AetherBlocks.LockedDungeonStone.blockID, false);

        for (int golem = 1; golem <= 6; ++golem)
        {
            boolean i = false;
            int y = this.boundingBox.minY + golem;
            int count;
            int var16;

            for (var16 = golem; var16 <= this.boundingBox.maxX - this.boundingBox.minX - golem; ++var16)
            {
                this.placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 3, this.boundingBox.minX + var16, y, this.boundingBox.maxZ - golem, ChunkBox);
                this.placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 2, this.boundingBox.minX + var16, y, this.boundingBox.minZ + golem, ChunkBox);

                for (count = 1; count < 6; ++count)
                {
                    this.placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + var16, y + count, this.boundingBox.minZ + golem, ChunkBox);
                    this.placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + var16, y + count, this.boundingBox.maxZ - golem, ChunkBox);
                }
            }

            for (var16 = golem; var16 <= this.boundingBox.maxZ - this.boundingBox.minZ - golem; ++var16)
            {
                this.placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 0, this.boundingBox.minX + golem, y, this.boundingBox.minZ + var16, ChunkBox);
                this.placeBlockAtCurrentPositionWithNotify(par1World, AetherBlocks.CarvedDungeonStairs.blockID, 1, this.boundingBox.maxX - golem, y, this.boundingBox.minZ + var16, ChunkBox);

                for (count = 1; count < 6; ++count)
                {
                    this.placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.minX + golem, y + count, this.boundingBox.minZ + var16, ChunkBox);
                    this.placeBlockAtCurrentPositionWithNotify(par1World, 0, 0, this.boundingBox.maxX - golem, y + count, this.boundingBox.minZ + var16, ChunkBox);
                }
            }
        }

        if (ChunkBox.isVecInside(this.boundingBox.minX + var15 + (this.boundingBox.maxX - (this.boundingBox.minX + var15)) / 2 + 1, maxY + 2, this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1))
        {
            EntitySentryGuardian var17 = new EntitySentryGuardian(par1World);
            var17.setPosition((double)(this.boundingBox.minX + var15 + (this.boundingBox.maxX - (this.boundingBox.minX + var15)) / 2 + 1), (double)(maxY + 2), (double)(this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1));
            par1World.spawnEntityInWorld(var17);
        }

        this.cutHolesForEntrances(par1World, par2Random, ChunkBox);
        return true;
    }

    public void setBlockWithChance(World world, Random rand, int rarity, int x, int y, int z, int blockID, int meta, StructureBoundingBox box)
    {
        if (rand.nextInt(rarity) == 1)
        {
            this.placeBlockAtCurrentPosition(world, blockID, meta, x, y, z, box);
        }
    }

    public void setBlockWithChance(World world, Random rand, int rarity, int x, int y, int z, int blockID, StructureBoundingBox box)
    {
        if (rand.nextInt(rarity) == 1)
        {
            this.placeBlockAtCurrentPosition(world, blockID, 0, x, y, z, box);
        }
    }

    protected void fillWithBlocksWithNotify(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, boolean par11)
    {
        for (int var12 = par4; var12 <= par7; ++var12)
        {
            for (int var13 = par3; var13 <= par6; ++var13)
            {
                for (int var14 = par5; var14 <= par8; ++var14)
                {
                    if (!par11 || this.getBlockIdAtCurrentPosition(par1World, var13, var12, var14, par2StructureBoundingBox) != 0)
                    {
                        if (var12 != par4 && var12 != par7 && var13 != par3 && var13 != par6 && var14 != par5 && var14 != par8)
                        {
                            this.placeBlockAtCurrentPositionWithNotify(par1World, par10, 0, var13, var12, var14, par2StructureBoundingBox);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPositionWithNotify(par1World, par9, 0, var13, var12, var14, par2StructureBoundingBox);
                        }
                    }
                }
            }
        }
    }

    public void fillVariedBlocks(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int blockID1, int meta1, int blockID2, int meta2, int rarity, Random rand, boolean replaceBlocks)
    {
        for (int y = minY; y <= maxY; ++y)
        {
            for (int x = minX; x <= maxX; ++x)
            {
                for (int z = minZ; z <= maxZ; ++z)
                {
                    if (world.getBlockId(x, y, z) == 0 || replaceBlocks)
                    {
                        int blockID = blockID2;
                        int meta = meta2;

                        if (rand.nextInt(rarity) == 1)
                        {
                            blockID = blockID1;
                            meta = meta1;
                        }

                        this.placeBlockAtCurrentPositionWithNotify(world, blockID, meta, x, y, z, box);
                    }
                }
            }
        }
    }

    protected void placeBlockAtCurrentPositionWithNotify(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox)
    {
        int var8 = this.getXWithOffset(par4, par6);
        int var9 = this.getYWithOffset(par5);
        int var10 = this.getZWithOffset(par4, par6);

        if (par7StructureBoundingBox.isVecInside(var8, var9, var10))
        {
            par1World.setBlock(var8, var9, var10, par2, par3, ChunkProviderAether.placementFlagType);
        }
    }

    public static StructureBoundingBox findValidPlacement(List components, Random random, int i, int j, int k, int direction)
    {
        int roomX = 20 + random.nextInt(20);
        int roomZ = 20 + random.nextInt(20);
        byte roomY = 16;
        StructureBoundingBox var6 = new StructureBoundingBox(i, j, k, i, j + roomY, k);

        switch (direction)
        {
            case 0:
                var6.minX = i - 1;
                var6.maxX = i + roomX - 1;
                var6.maxZ = k + roomZ;
                break;

            case 1:
                var6.minX = i - roomX;
                var6.minZ = k - 1;
                var6.maxZ = k + roomZ - 1;
                break;

            case 2:
                var6.minX = i - 1;
                var6.maxX = i + roomX - 1;
                var6.minZ = k - roomZ;
                break;

            case 3:
                var6.maxX = i + roomX;
                var6.minZ = k - 1;
                var6.maxZ = k + roomZ - 1;
        }

        return StructureComponent.findIntersecting(components, var6) != null ? null : var6;
    }
}
