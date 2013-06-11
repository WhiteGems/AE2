package net.aetherteam.aether.oldcode;

import java.util.Random;

import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class AetherGenDungeonBronze extends AetherGenBuildings
{
    private int corridorMeta1;
    private int corridorMeta2;
    private int lockedBlockID1;
    private int lockedBlockID2;
    private int wallBlockID1;
    private int wallBlockID2;
    private int corridorBlockID1;
    private int corridorBlockID2;
    private int numRooms;
    private int n;
    private boolean finished;
    private boolean flat;

    public AetherGenDungeonBronze(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10)
    {
        this.lockedBlockID1 = var1;
        this.lockedBlockID2 = var2;
        this.wallBlockID1 = var3;
        this.wallBlockID2 = var4;
        this.corridorBlockID1 = var5;
        this.corridorMeta1 = var6;
        this.corridorBlockID2 = var7;
        this.corridorMeta2 = var8;
        this.numRooms = var9;
        this.flat = var10;
        this.finished = false;
    }

    public boolean generate(World var1, Random var2, int var3, int var4, int var5)
    {
        this.replaceAir = true;
        this.replaceSolid = true;
        this.n = 0;

        if (this.isBoxSolid(var1, var3, var4, var5, 16, 12, 16) && this.isBoxSolid(var1, var3 + 20, var4, var5 + 2, 12, 12, 12))
        {
            this.setBlocks(this.lockedBlockID1, this.lockedBlockID2, 20);
            this.addHollowBox(var1, var2, var3, var4, var5, 16, 12, 16);
            this.addHollowBox(var1, var2, var3 + 6, var4 - 2, var5 + 6, 4, 4, 4);
            EntitySlider var6 = new EntitySlider(var1, (double) (var3 + 8), (double) (var4 + 2), (double) (var5 + 8));
            var6.setDungeon(var3, var4, var5);
            var1.spawnEntityInWorld(var6);
            int var7 = var3 + 7 + var2.nextInt(2);
            int var8 = var4 - 1;
            int var9 = var5 + 7 + var2.nextInt(2);
            var1.setBlock(var7, var8, var9, AetherBlocks.TreasureChest.blockID);
            TileEntityChest var10 = (TileEntityChest) ((TileEntityChest) var1.getBlockTileEntity(var7, var8, var9));
            var7 = var3 + 20;
            var9 = var5 + 2;

            if (!this.isBoxSolid(var1, var7, var4, var9, 12, 12, 12))
            {
                return true;
            } else
            {
                this.setBlocks(this.wallBlockID1, this.wallBlockID2, 20);
                this.addHollowBox(var1, var2, var7, var4, var9, 12, 12, 12);
                this.setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                this.setMetadata(this.corridorMeta2, this.corridorMeta1);
                this.addSquareTube(var1, var2, var7 - 5, var4, var9 + 3, 6, 6, 6, 0);

                for (int var11 = var7 + 2; var11 < var7 + 10; var11 += 3)
                {
                    for (int var12 = var9 + 2; var12 < var9 + 10; var12 += 3)
                    {
                        var1.setBlock(var11, var4, var12, AetherBlocks.Trap.blockID, 0, 2);
                    }
                }

                ++this.n;
                this.generateNextRoom(var1, var2, var7, var4, var9);
                this.generateNextRoom(var1, var2, var7, var4, var9);

                if (this.n > this.numRooms || !this.finished)
                {
                    this.endCorridor(var1, var2, var7, var4, var9);
                }

                return true;
            }
        } else
        {
            return false;
        }
    }

    public boolean generateNextRoom(World var1, Random var2, int var3, int var4, int var5)
    {
        if (this.n > this.numRooms && !this.finished)
        {
            this.endCorridor(var1, var2, var3, var4, var5);
            return false;
        } else
        {
            int var6 = var2.nextInt(4);
            int var7 = var3;
            int var8 = var4;
            int var9 = var5;

            if (var6 == 0)
            {
                var7 = var3 + 16;
                var9 = var5 + 0;
            }

            if (var6 == 1)
            {
                var7 += 0;
                var9 += 16;
            }

            if (var6 == 2)
            {
                var7 -= 16;
                var9 += 0;
            }

            if (var6 == 3)
            {
                var7 += 0;
                var9 -= 16;
            }

            if (!this.isBoxSolid(var1, var7, var4, var9, 12, 8, 12))
            {
                return false;
            } else
            {
                this.setBlocks(this.wallBlockID1, this.wallBlockID2, 20);
                this.setMetadata(0, 0);
                this.addHollowBox(var1, var2, var7, var4, var9, 12, 8, 12);
                int var12;
                int var11;
                int var10;

                for (var10 = var7; var10 < var7 + 12; ++var10)
                {
                    for (var11 = var8; var11 < var8 + 8; ++var11)
                    {
                        for (var12 = var9; var12 < var9 + 12; ++var12)
                        {
                            if (var1.getBlockId(var10, var11, var12) == this.wallBlockID1 && var2.nextInt(100) == 0)
                            {
                                var1.setBlock(var10, var11, var12, AetherBlocks.Trap.blockID);
                            }
                        }
                    }
                }

                for (var10 = var7 + 2; var10 < var7 + 10; var10 += 7)
                {
                    for (var11 = var9 + 2; var11 < var9 + 10; var11 += 7)
                    {
                        var1.setBlock(var10, var4, var11, AetherBlocks.Trap.blockID, 0, 2);
                    }
                }

                this.addPlaneY(var1, var2, var7 + 4, var8 + 1, var9 + 4, 4, 4);
                var10 = var2.nextInt(2);
                var11 = var7 + 5 + var2.nextInt(2);
                var12 = var9 + 5 + var2.nextInt(2);

                switch (var10)
                {
                    case 0:
                        var1.setBlock(var11, var8 + 2, var12, AetherBlocks.SkyrootChestMimic.blockID);
                        break;

                    case 1:
                        if (var1.getBlockId(var11, var8 + 2, var12) == 0)
                        {
                            var1.setBlock(var11, var8 + 2, var12, Block.chest.blockID);
                            TileEntityChest var13 = (TileEntityChest) var1.getBlockTileEntity(var11, var8 + 2, var12);

                            for (var11 = 0; var11 < 3 + var2.nextInt(3); ++var11)
                            {
                                var13.setInventorySlotContents(var2.nextInt(var13.getSizeInventory()), AetherLoot.NORMAL.getRandomItem(var2));
                            }
                        }
                }

                this.setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                this.setMetadata(this.corridorMeta2, this.corridorMeta1);

                switch (var6)
                {
                    case 0:
                        this.addSquareTube(var1, var2, var7 - 5, var8, var9 + 3, 6, 6, 6, 0);
                        break;

                    case 1:
                        this.addSquareTube(var1, var2, var7 + 3, var8, var9 - 5, 6, 6, 6, 2);
                        break;

                    case 2:
                        this.addSquareTube(var1, var2, var7 + 11, var8, var9 + 3, 6, 6, 6, 0);
                        break;

                    case 3:
                        this.addSquareTube(var1, var2, var7 + 3, var8, var9 + 11, 6, 6, 6, 2);
                }

                ++this.n;

                if (!this.generateNextRoom(var1, var2, var7, var8, var9))
                {
                    return false;
                } else
                {
                    return this.generateNextRoom(var1, var2, var7, var8, var9);
                }
            }
        }
    }

    public void endCorridor(World var1, Random var2, int var3, int var4, int var5)
    {
        this.replaceAir = false;
        boolean var6 = true;
        int var8 = var2.nextInt(3);
        int var9 = var3;
        int var10 = var4;
        int var11 = var5;
        boolean var7;

        if (var8 == 0)
        {
            var9 = var3 + 11;

            for (var11 = var5 + 3; var6; ++var9)
            {
                if (this.isBoxEmpty(var1, var9, var10, var11, 1, 8, 6) || var9 - var3 > 100)
                {
                    var6 = false;
                }

                var7 = true;

                while (var7 && (var1.getBlockId(var9, var10, var11) == this.wallBlockID1 || var1.getBlockId(var9, var10, var11) == this.wallBlockID2 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID1 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID2))
                {
                    if (var1.getBlockId(var9 + 1, var10, var11) != this.wallBlockID1 && var1.getBlockId(var9 + 1, var10, var11) != this.wallBlockID2 && var1.getBlockId(var9 + 1, var10, var11) != this.lockedBlockID1 && var1.getBlockId(var9 + 1, var10, var11) != this.lockedBlockID2)
                    {
                        var7 = false;
                    } else
                    {
                        ++var9;
                    }
                }

                this.setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                this.setMetadata(this.corridorMeta2, this.corridorMeta1);
                this.addPlaneX(var1, var2, var9, var10, var11, 8, 6);
                this.setBlocks(0, 0, 1);
                this.addPlaneX(var1, var2, var9, var10 + 1, var11 + 1, 6, 4);
            }
        }

        if (var8 == 1)
        {
            var9 += 3;

            for (var11 += 11; var6; ++var11)
            {
                if (this.isBoxEmpty(var1, var9, var10, var11, 6, 8, 1) || var11 - var5 > 100)
                {
                    var6 = false;
                }

                var7 = true;

                while (var7 && (var1.getBlockId(var9, var10, var11) == this.wallBlockID1 || var1.getBlockId(var9, var10, var11) == this.wallBlockID2 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID1 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID2))
                {
                    if (var1.getBlockId(var9, var10, var11 + 1) != this.wallBlockID1 && var1.getBlockId(var9, var10, var11 + 1) != this.wallBlockID2 && var1.getBlockId(var9, var10, var11 + 1) != this.lockedBlockID1 && var1.getBlockId(var9, var10, var11 + 1) != this.lockedBlockID2)
                    {
                        var7 = false;
                    } else
                    {
                        ++var11;
                    }
                }

                this.setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                this.setMetadata(this.corridorMeta2, this.corridorMeta1);
                this.addPlaneZ(var1, var2, var9, var10, var11, 6, 8);
                this.setBlocks(0, 0, 1);
                this.addPlaneZ(var1, var2, var9 + 1, var10 + 1, var11, 4, 6);
            }
        }

        if (var8 == 2)
        {
            var9 += 3;

            for (var11 += 0; var6; --var11)
            {
                if (this.isBoxEmpty(var1, var9, var10, var11, 6, 8, 1) || var4 - var11 > 100)
                {
                    var6 = false;
                }

                var7 = true;

                while (var7 && (var1.getBlockId(var9, var10, var11) == this.wallBlockID1 || var1.getBlockId(var9, var10, var11) == this.wallBlockID2 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID1 || var1.getBlockId(var9, var10, var11) == this.lockedBlockID2))
                {
                    if (var1.getBlockId(var9, var10, var11 - 1) != this.wallBlockID1 && var1.getBlockId(var9, var10, var11 - 1) != this.wallBlockID2 && var1.getBlockId(var9, var10, var11 - 1) != this.lockedBlockID1 && var1.getBlockId(var9, var10, var11 - 1) != this.lockedBlockID2)
                    {
                        var7 = false;
                    } else
                    {
                        --var11;
                    }
                }

                this.setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                this.setMetadata(this.corridorMeta2, this.corridorMeta1);
                this.addPlaneZ(var1, var2, var9, var10, var11, 6, 8);
                this.setBlocks(0, 0, 1);
                this.addPlaneZ(var1, var2, var9 + 1, var10 + 1, var11, 4, 6);
            }
        }

        this.finished = true;
    }

    private ItemStack getNormalLoot(Random var1)
    {
        int var2 = var1.nextInt(14);

        switch (var2)
        {
            case 0:
                return new ItemStack(AetherItems.ZanitePickaxe);

            case 1:
                return new ItemStack(AetherItems.ZaniteAxe);

            case 2:
                return new ItemStack(AetherItems.ZaniteSword);

            case 3:
                return new ItemStack(AetherItems.ZaniteShovel);

            case 4:
                return new ItemStack(AetherItems.SwetCape);

            case 5:
                return new ItemStack(AetherItems.AmbrosiumShard, var1.nextInt(10) + 1);

            case 6:
                return new ItemStack(AetherItems.Dart, var1.nextInt(5) + 1, 0);

            case 7:
                return new ItemStack(AetherItems.Dart, var1.nextInt(3) + 1, 1);

            case 8:
                return new ItemStack(AetherItems.Dart, var1.nextInt(3) + 1, 2);

            case 9:
                if (var1.nextInt(20) == 0)
                {
                    return new ItemStack(AetherItems.AetherMusicDisk);
                }

                break;

            case 10:
                return new ItemStack(AetherItems.SkyrootBucket);

            case 11:
                if (var1.nextInt(10) == 0)
                {
                    return new ItemStack(Item.itemsList[Item.record13.itemID + var1.nextInt(2)]);
                }

                break;

            case 12:
                if (var1.nextInt(4) == 0)
                {
                    return new ItemStack(AetherItems.IronRing);
                }

                break;

            case 13:
                if (var1.nextInt(10) == 0)
                {
                    return new ItemStack(AetherItems.GoldenRing);
                }
        }

        return new ItemStack(AetherBlocks.AmbrosiumTorch);
    }

    private ItemStack getBronzeLoot(Random var1)
    {
        int var2 = var1.nextInt(8);

        switch (var2)
        {
            case 0:
                return new ItemStack(AetherItems.GummieSwet, var1.nextInt(7) + 1, var1.nextInt(2));

            case 1:
                return new ItemStack(AetherItems.DartShooter, 1, 3);

            case 2:
                return new ItemStack(AetherItems.FlamingSword);

            case 3:
                return new ItemStack(AetherItems.HammerOfNotch);

            case 4:
                return new ItemStack(AetherItems.LightningKnife, var1.nextInt(15) + 1);

            case 5:
                return new ItemStack(AetherItems.ValkyrieLance);

            case 6:
                return new ItemStack(AetherItems.AgilityCape);

            case 7:
                return new ItemStack(AetherItems.SentryBoots);

            default:
                return new ItemStack(AetherItems.SkyrootStick);
        }
    }
}
