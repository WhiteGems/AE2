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

public class ComponentDungeonBronzeChest extends ComponentDungeonBronzeRoom
{
    public ComponentDungeonBronzeChest(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, int var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
    }

    public ComponentDungeonBronzeChest(int var1, StructureComponent var2, StructureBronzeDungeonStart var3, Random var4, StructureBoundingBox var5, int var6)
    {
        super(var1, var2, var3, var4, var5, var6);
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
                    this.placeBlockAtCurrentPosition(var1, var2.nextInt(20) == 1 ? AetherBlocks.Trap.blockID : AetherBlocks.LockedDungeonStone.blockID, 0, var5, var4, var6, var3);
                }
            }
        }

        var4 = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
        var5 = this.boundingBox.minY + 1;
        var6 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
        int var7 = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
        int var8 = this.boundingBox.minY + 1;
        int var9 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
        this.fillWithBlocksWithNotify(var1, var3, this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1, 0, 0, false);
        this.fillWithBlocksWithNotify(var1, var3, var4, var5, var6, var7, var8, var9, AetherBlocks.LockedDungeonStone.blockID, 0, false);
        int var10 = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2;
        int var11 = this.boundingBox.minY + 2;
        int var12 = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2;

        for (int var13 = 1; var13 <= 4; ++var13)
        {
            byte var14 = 0;
            byte var15 = 0;
            byte var16 = 1;

            switch (var13)
            {
                case 1:
                    var14 = 1;
                    var16 = 1;
                    break;

                case 2:
                    var14 = -1;
                    var16 = 2;
                    break;

                case 3:
                    var15 = 1;
                    var16 = 3;
                    break;

                case 4:
                    var15 = -1;
                    var16 = 4;
            }

            int var17 = this.getXWithOffset(var10 + var14, var12 + var15);
            int var18 = this.getYWithOffset(var11);
            int var19 = this.getZWithOffset(var10 + var14, var12 + var15);
            Dungeon var20;

            if (var3.isVecInside(var17, var18, var19) && var1.getBlockId(var17, var18, var19) != AetherBlocks.SkyrootChest.blockID && var2.nextInt(3) == 0)
            {
                var20 = null;
                var1.setBlock(var17, var18, var19, AetherBlocks.SkyrootChest.blockID, var16, ChunkProviderAether.placementFlagType);
                var1.setBlockMetadataWithNotify(var17, var18, var19, var16, ChunkProviderAether.placementFlagType);
                Dungeon var21 = DungeonHandler.instance().getInstanceAt(var17, var18, var19);
                var21.registerSafeBlock(var17, var18, var19, AetherBlocks.SkyrootChest.blockID, var16);
                Side var22 = FMLCommonHandler.instance().getEffectiveSide();

                if (var22.isServer() && !var1.isRemote)
                {
                    TileEntitySkyrootChest var28 = (TileEntitySkyrootChest)var1.getBlockTileEntity(var17, var18, var19);

                    if (var28 != null)
                    {
                        for (int var23 = 0; var23 < 3 + var2.nextInt(3); ++var23)
                        {
                            ItemStack var24 = AetherLoot.NORMAL.getRandomItem(var2);

                            if (var24.stackSize <= 0)
                            {
                                var24.stackSize = 1;
                            }

                            var28.setInventorySlotContents(var2.nextInt(var28.getSizeInventory()), var24);
                        }
                    }
                }
            }
            else if (var3.isVecInside(var17, var18, var19) && var1.getBlockId(var17, var18, var19) != AetherBlocks.SkyrootChestMimic.blockID && var2.nextInt(3) == 0)
            {
                var1.setBlock(var17, var18, var19, AetherBlocks.SkyrootChestMimic.blockID, var16, ChunkProviderAether.placementFlagType);
                var20 = DungeonHandler.instance().getInstanceAt(var17, var18, var19);
                var20.registerSafeBlock(var17, var18, var19, AetherBlocks.SkyrootChestMimic.blockID, var16);
            }
        }

        this.placeBlockAtCurrentPosition(var1, Block.mobSpawner.blockID, 0, var10, var11, var12, var3);
        TileEntityMobSpawner var25 = (TileEntityMobSpawner)var1.getBlockTileEntity(var10, var11, var12);

        if (var25 != null)
        {
            String var26 = var2.nextInt(3) == 1 ? "SentryGolem" : "Sentry";
            var25.func_98049_a().setMobID(var26);
            Dungeon var27 = DungeonHandler.instance().getInstanceAt(var10, var11, var12);
            var27.registerMobSpawner(var10, var11, var12, var26);
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
                        }
                        else
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
            var1.setBlock(var8, var9, var10, var2, var3, ChunkProviderAether.placementFlagType);
        }
    }

    public static StructureBoundingBox findValidPlacement(List var0, Random var1, int var2, int var3, int var4, int var5)
    {
        int var6 = 6 + var1.nextInt(3);
        int var7 = 6 + var1.nextInt(3);
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
