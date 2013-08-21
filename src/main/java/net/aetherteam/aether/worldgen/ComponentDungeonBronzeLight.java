package net.aetherteam.aether.worldgen;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.tile_entities.TileEntitySkyrootChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentDungeonBronzeLight extends ComponentDungeonBronzeRoom
{
    public ComponentDungeonBronzeLight(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart structureBronzeDugneonStart, Random par2Random, int par3, int par4)
    {
        super(par1, previousStructor, structureBronzeDugneonStart, par2Random, par3, par4);
    }

    public ComponentDungeonBronzeLight(int par1, StructureComponent previousStructor, StructureBronzeDungeonStart Whole, Random par2Random, StructureBoundingBox structureBoundingBox, int direction)
    {
        super(par1, previousStructor, Whole, par2Random, structureBoundingBox, direction);
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox chunkBox)
    {
        int minX;
        int minY;
        int minZ;

        for (minX = this.boundingBox.minY; minX <= this.boundingBox.maxY; ++minX)
        {
            for (minY = this.boundingBox.minX; minY <= this.boundingBox.maxX; ++minY)
            {
                for (minZ = this.boundingBox.minZ; minZ <= this.boundingBox.maxZ; ++minZ)
                {
                    this.placeBlockAtCurrentPosition(world, rand.nextInt(20) == 1 ? AetherBlocks.Trap.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, minY, minX, minZ, chunkBox);
                }
            }
        }

        minX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
        minY = this.boundingBox.minY + 1;
        minZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int maxX = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
        int maxY = this.boundingBox.minY + 1;
        int maxZ = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
        this.fillWithBlocksWithNotify(world, chunkBox, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(world, chunkBox, minX, minY, minZ, maxX, maxY, maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.fillWithBlocksWithNotify(world, chunkBox, minX, this.boundingBox.maxY - 1, minZ, maxX, this.boundingBox.maxY - 1, maxZ, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        this.fillVariedBlocks(world, chunkBox, minX, this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2, minZ, maxX, this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) / 2, maxZ, AetherBlocks.LockedLightDungeonStone.blockID, 0, AetherBlocks.LockedDungeonStone.blockID, 0, 2, rand, false);
        this.fillVariedBlocks(world, chunkBox, minX + 1, minY, minZ + 1, maxX - 1, this.boundingBox.maxY - 1, maxZ - 1, AetherBlocks.CarvedDungeonWall.blockID, 0, AetherBlocks.CarvedDungeonWall.blockID, 0, 1, rand, true);
        this.fillVariedBlocks(world, chunkBox, minX, minY, minZ, minX, this.boundingBox.maxY - 1, minZ, AetherBlocks.CarvedDungeonWall.blockID, 0, AetherBlocks.CarvedDungeonWall.blockID, 0, 1, rand, false);
        this.fillVariedBlocks(world, chunkBox, maxX, minY, maxZ, maxX, this.boundingBox.maxY - 1, maxZ, AetherBlocks.CarvedDungeonWall.blockID, 0, AetherBlocks.CarvedDungeonWall.blockID, 0, 1, rand, false);
        int x = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2;
        int y = this.boundingBox.minY + 2;
        int z = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2;

        for (int spawner = 1; spawner <= 4; ++spawner)
        {
            byte mobID = 0;
            byte dungeon = 0;
            byte chestDirection = 1;

            switch (spawner)
            {
                case 1:
                    mobID = 1;
                    chestDirection = 1;
                    break;

                case 2:
                    mobID = -1;
                    chestDirection = 2;
                    break;

                case 3:
                    dungeon = 1;
                    chestDirection = 3;
                    break;

                case 4:
                    dungeon = -1;
                    chestDirection = 4;
            }

            int i = this.getXWithOffset(x + mobID, z + dungeon);
            int j = this.getYWithOffset(y);
            int k = this.getZWithOffset(x + mobID, z + dungeon);

            if (chunkBox.isVecInside(i, j, k) && world.getBlockId(i, j, k) != AetherBlocks.SkyrootChest.blockID && rand.nextInt(3) == 0)
            {
                TileEntitySkyrootChest chestEntity = null;
                world.setBlock(i, j, k, AetherBlocks.SkyrootChest.blockID, chestDirection, ChunkProviderAether.placementFlagType);
                world.setBlockMetadataWithNotify(i, j, k, chestDirection, ChunkProviderAether.placementFlagType);
                Dungeon dungeon1 = DungeonHandler.instance().getInstanceAt(i, j, k);
                dungeon1.registerSafeBlock(i, j, k, AetherBlocks.SkyrootChest.blockID, chestDirection);
                Side side = FMLCommonHandler.instance().getEffectiveSide();

                if (side.isServer() && !world.isRemote)
                {
                    chestEntity = (TileEntitySkyrootChest)world.getBlockTileEntity(i, j, k);

                    if (chestEntity != null)
                    {
                        for (int count = 0; count < 3 + rand.nextInt(3); ++count)
                        {
                            ItemStack stack = AetherLoot.NORMAL.getRandomItem(rand);

                            if (stack.stackSize <= 0)
                            {
                                stack.stackSize = 1;
                            }

                            chestEntity.setInventorySlotContents(rand.nextInt(chestEntity.getSizeInventory()), stack);
                        }
                    }
                }
            }
        }

        this.placeBlockAtCurrentPosition(world, Block.mobSpawner.blockID, 0, x, y + 1, z, chunkBox);
        TileEntityMobSpawner var25 = (TileEntityMobSpawner)world.getBlockTileEntity(x, y + 1, z);

        if (var25 != null)
        {
            String var26 = rand.nextInt(3) == 1 ? "SentryGolemRanged" : "SentryMelee";
            var25.getSpawnerLogic().setMobID(var26);
            Dungeon var27 = DungeonHandler.instance().getInstanceAt(x, y + 1, z);
            var27.registerMobSpawner(x, y + 1, z, var26);
        }

        this.cutHolesForEntrances(world, rand, chunkBox);
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
                    if (this.getBlockIdAtCurrentPosition(world, x, y, z, box) == 0 || replaceBlocks)
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
        int roomX = 8 + random.nextInt(16);
        int roomZ = 8 + random.nextInt(16);
        byte roomY = 10;
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
