package net.aetherteam.aether.oldcode;

import java.util.Random;
import net.aetherteam.aether.AetherLoot;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
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

    public AetherGenDungeonBronze(int i, int j, int k, int l, int m, int m1, int o, int o1, int p, boolean flag)
    {
        this.lockedBlockID1 = i;
        this.lockedBlockID2 = j;
        this.wallBlockID1 = k;
        this.wallBlockID2 = l;
        this.corridorBlockID1 = m;
        this.corridorMeta1 = m1;
        this.corridorBlockID2 = o;
        this.corridorMeta2 = o1;
        this.numRooms = p;
        this.flat = flag;
        this.finished = false;
    }

    public boolean generate(World world, Random random, int i, int j, int k)
    {
        this.replaceAir = true;
        this.replaceSolid = true;
        this.n = 0;

        if ((!isBoxSolid(world, i, j, k, 16, 12, 16)) || (!isBoxSolid(world, i + 20, j, k + 2, 12, 12, 12)))
        {
            return false;
        }

        setBlocks(this.lockedBlockID1, this.lockedBlockID2, 20);
        addHollowBox(world, random, i, j, k, 16, 12, 16);
        addHollowBox(world, random, i + 6, j - 2, k + 6, 4, 4, 4);
        EntitySlider slider = new EntitySlider(world, i + 8, j + 2, k + 8);
        slider.setDungeon(i, j, k);
        world.spawnEntityInWorld(slider);
        int x = i + 7 + random.nextInt(2);
        int y = j - 1;
        int z = k + 7 + random.nextInt(2);
        world.setBlock(x, y, z, AetherBlocks.TreasureChest.blockID);
        TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(x, y, z);
        x = i + 20;
        y = j;
        z = k + 2;

        if (!isBoxSolid(world, x, y, z, 12, 12, 12))
        {
            return true;
        }

        setBlocks(this.wallBlockID1, this.wallBlockID2, 20);
        addHollowBox(world, random, x, y, z, 12, 12, 12);
        setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
        setMetadata(this.corridorMeta2, this.corridorMeta1);
        addSquareTube(world, random, x - 5, y, z + 3, 6, 6, 6, 0);

        for (int p = x + 2; p < x + 10; p += 3)
        {
            for (int q = z + 2; q < z + 10; q += 3)
            {
                world.setBlock(p, j, q, AetherBlocks.Trap.blockID, 0, 2);
            }
        }

        this.n += 1;
        generateNextRoom(world, random, x, y, z);
        generateNextRoom(world, random, x, y, z);

        if ((this.n > this.numRooms) || (!this.finished))
        {
            endCorridor(world, random, x, y, z);
        }

        return true;
    }

    public boolean generateNextRoom(World world, Random random, int i, int j, int k)
    {
        if ((this.n > this.numRooms) && (!this.finished))
        {
            endCorridor(world, random, i, j, k);
            return false;
        }

        int dir = random.nextInt(4);
        int x = i;
        int y = j;
        int z = k;

        if (dir == 0)
        {
            x += 16;
            z += 0;
        }

        if (dir == 1)
        {
            x += 0;
            z += 16;
        }

        if (dir == 2)
        {
            x -= 16;
            z += 0;
        }

        if (dir == 3)
        {
            x += 0;
            z -= 16;
        }

        if (!isBoxSolid(world, x, y, z, 12, 8, 12))
        {
            return false;
        }

        setBlocks(this.wallBlockID1, this.wallBlockID2, 20);
        setMetadata(0, 0);
        addHollowBox(world, random, x, y, z, 12, 8, 12);

        for (int p = x; p < x + 12; p++)
        {
            for (int q = y; q < y + 8; q++)
            {
                for (int r = z; r < z + 12; r++)
                {
                    if ((world.getBlockId(p, q, r) == this.wallBlockID1) && (random.nextInt(100) == 0))
                    {
                        world.setBlock(p, q, r, AetherBlocks.Trap.blockID);
                    }
                }
            }
        }

        for (int p = x + 2; p < x + 10; p += 7)
        {
            for (int q = z + 2; q < z + 10; q += 7)
            {
                world.setBlock(p, j, q, AetherBlocks.Trap.blockID, 0, 2);
            }
        }

        addPlaneY(world, random, x + 4, y + 1, z + 4, 4, 4);
        int type = random.nextInt(2);
        int p = x + 5 + random.nextInt(2);
        int q = z + 5 + random.nextInt(2);

        switch (type)
        {
            case 0:
                world.setBlock(p, y + 2, q, AetherBlocks.SkyrootChestMimic.blockID);
                break;

            case 1:
                if (world.getBlockId(p, y + 2, q) == 0)
                {
                    world.setBlock(p, y + 2, q, Block.chest.blockID);
                    TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(p, y + 2, q);

                    for (p = 0; p < 3 + random.nextInt(3); p++)
                    {
                        chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), AetherLoot.NORMAL.getRandomItem(random));
                    }
                }

                break;
        }

        setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
        setMetadata(this.corridorMeta2, this.corridorMeta1);

        switch (dir)
        {
            case 0:
                addSquareTube(world, random, x - 5, y, z + 3, 6, 6, 6, 0);
                break;

            case 1:
                addSquareTube(world, random, x + 3, y, z - 5, 6, 6, 6, 2);
                break;

            case 2:
                addSquareTube(world, random, x + 11, y, z + 3, 6, 6, 6, 0);
                break;

            case 3:
                addSquareTube(world, random, x + 3, y, z + 11, 6, 6, 6, 2);
        }

        this.n += 1;

        if (!generateNextRoom(world, random, x, y, z))
        {
            return false;
        }

        return generateNextRoom(world, random, x, y, z);
    }

    public void endCorridor(World world, Random random, int i, int j, int k)
    {
        this.replaceAir = false;
        boolean tunnelling = true;
        int dir = random.nextInt(3);
        int x = i;
        int y = j;
        int z = k;

        if (dir == 0)
        {
            x += 11;
            z += 3;

            while (tunnelling)
            {
                if ((isBoxEmpty(world, x, y, z, 1, 8, 6)) || (x - i > 100))
                {
                    tunnelling = false;
                }

                boolean flag = true;

                while ((flag) && ((world.getBlockId(x, y, z) == this.wallBlockID1) || (world.getBlockId(x, y, z) == this.wallBlockID2) || (world.getBlockId(x, y, z) == this.lockedBlockID1) || (world.getBlockId(x, y, z) == this.lockedBlockID2)))
                {
                    if ((world.getBlockId(x + 1, y, z) == this.wallBlockID1) || (world.getBlockId(x + 1, y, z) == this.wallBlockID2) || (world.getBlockId(x + 1, y, z) == this.lockedBlockID1) || (world.getBlockId(x + 1, y, z) == this.lockedBlockID2))
                    {
                        x++;
                    }
                    else
                    {
                        flag = false;
                    }
                }

                setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                setMetadata(this.corridorMeta2, this.corridorMeta1);
                addPlaneX(world, random, x, y, z, 8, 6);
                setBlocks(0, 0, 1);
                addPlaneX(world, random, x, y + 1, z + 1, 6, 4);
                x++;
            }
        }

        if (dir == 1)
        {
            x += 3;
            z += 11;

            while (tunnelling)
            {
                if ((isBoxEmpty(world, x, y, z, 6, 8, 1)) || (z - k > 100))
                {
                    tunnelling = false;
                }

                boolean flag = true;

                while ((flag) && ((world.getBlockId(x, y, z) == this.wallBlockID1) || (world.getBlockId(x, y, z) == this.wallBlockID2) || (world.getBlockId(x, y, z) == this.lockedBlockID1) || (world.getBlockId(x, y, z) == this.lockedBlockID2)))
                {
                    if ((world.getBlockId(x, y, z + 1) == this.wallBlockID1) || (world.getBlockId(x, y, z + 1) == this.wallBlockID2) || (world.getBlockId(x, y, z + 1) == this.lockedBlockID1) || (world.getBlockId(x, y, z + 1) == this.lockedBlockID2))
                    {
                        z++;
                    }
                    else
                    {
                        flag = false;
                    }
                }

                setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                setMetadata(this.corridorMeta2, this.corridorMeta1);
                addPlaneZ(world, random, x, y, z, 6, 8);
                setBlocks(0, 0, 1);
                addPlaneZ(world, random, x + 1, y + 1, z, 4, 6);
                z++;
            }
        }

        if (dir == 2)
        {
            x += 3;
            z += 0;

            while (tunnelling)
            {
                if ((isBoxEmpty(world, x, y, z, 6, 8, 1)) || (j - z > 100))
                {
                    tunnelling = false;
                }

                boolean flag = true;

                while ((flag) && ((world.getBlockId(x, y, z) == this.wallBlockID1) || (world.getBlockId(x, y, z) == this.wallBlockID2) || (world.getBlockId(x, y, z) == this.lockedBlockID1) || (world.getBlockId(x, y, z) == this.lockedBlockID2)))
                {
                    if ((world.getBlockId(x, y, z - 1) == this.wallBlockID1) || (world.getBlockId(x, y, z - 1) == this.wallBlockID2) || (world.getBlockId(x, y, z - 1) == this.lockedBlockID1) || (world.getBlockId(x, y, z - 1) == this.lockedBlockID2))
                    {
                        z--;
                    }
                    else
                    {
                        flag = false;
                    }
                }

                setBlocks(this.corridorBlockID2, this.corridorBlockID1, 5);
                setMetadata(this.corridorMeta2, this.corridorMeta1);
                addPlaneZ(world, random, x, y, z, 6, 8);
                setBlocks(0, 0, 1);
                addPlaneZ(world, random, x + 1, y + 1, z, 4, 6);
                z--;
            }
        }

        this.finished = true;
    }

    private ItemStack getNormalLoot(Random random)
    {
        int item = random.nextInt(14);

        switch (item)
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
                return new ItemStack(AetherItems.AmbrosiumShard, random.nextInt(10) + 1);

            case 6:
                return new ItemStack(AetherItems.Dart, random.nextInt(5) + 1, 0);

            case 7:
                return new ItemStack(AetherItems.Dart, random.nextInt(3) + 1, 1);

            case 8:
                return new ItemStack(AetherItems.Dart, random.nextInt(3) + 1, 2);

            case 9:
                if (random.nextInt(20) == 0)
                {
                    return new ItemStack(AetherItems.AetherMusicDisk);
                }

                break;

            case 10:
                return new ItemStack(AetherItems.SkyrootBucket);

            case 11:
                if (random.nextInt(10) == 0)
                {
                    return new ItemStack(net.minecraft.item.Item.itemsList[(net.minecraft.item.Item.record13.itemID + random.nextInt(2))]);
                }

                break;

            case 12:
                if (random.nextInt(4) == 0)
                {
                    return new ItemStack(AetherItems.IronRing);
                }

                break;

            case 13:
                if (random.nextInt(10) == 0)
                {
                    return new ItemStack(AetherItems.GoldenRing);
                }

                break;
        }

        return new ItemStack(AetherBlocks.AmbrosiumTorch);
    }

    private ItemStack getBronzeLoot(Random random)
    {
        int item = random.nextInt(8);

        switch (item)
        {
            case 0:
                return new ItemStack(AetherItems.GummieSwet, random.nextInt(7) + 1, random.nextInt(2));

            case 1:
                return new ItemStack(AetherItems.DartShooter, 1, 3);

            case 2:
                return new ItemStack(AetherItems.FlamingSword);

            case 3:
                return new ItemStack(AetherItems.HammerOfNotch);

            case 4:
                return new ItemStack(AetherItems.LightningKnife, random.nextInt(15) + 1);

            case 5:
                return new ItemStack(AetherItems.ValkyrieLance);

            case 6:
                return new ItemStack(AetherItems.AgilityCape);

            case 7:
                return new ItemStack(AetherItems.SentryBoots);
        }

        return new ItemStack(AetherItems.SkyrootStick);
    }
}

