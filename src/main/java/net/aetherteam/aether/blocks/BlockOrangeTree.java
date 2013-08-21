package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOrangeTree extends BlockAetherFlower implements IAetherBlock
{
    private Icon stageOne;
    private Icon stageTwo;
    private Icon stageThree;
    private Icon bottomNaked;
    private Icon topNaked;
    private Icon bottomIcon;
    private Icon topIcon;

    public BlockOrangeTree(int blockID)
    {
        super(blockID);
        this.setHardness(0.2F);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        super.updateTick(world, x, y, z, random);
        this.checkTreeChange(world, x, y, z);
        int randomGrowth = this.randomGrowthInt(world, x, y, z, random, 5);
        int meta = world.getBlockMetadata(x, y, z);

        if (randomGrowth == 0)
        {
            switch (meta)
            {
                case 0:
                    world.setBlock(x, y, z, this.blockID, 1, 2);
                    break;

                case 1:
                    world.setBlock(x, y, z, this.blockID, 3, 2);
                    world.setBlock(x, y + 1, z, this.blockID, 2, 2);
                    break;

                case 2:
                    world.setBlock(x, y - 1, z, this.blockID, 3, 2);
                    world.setBlock(x, y, z, this.blockID, 4, 2);
                    break;

                case 3:
                    if (world.getBlockMetadata(x, y + 1, z) == 4)
                    {
                        world.setBlock(x, y, z, this.blockID, 5, 2);
                        world.setBlock(x, y + 1, z, this.blockID, 6, 2);
                    }
                    else
                    {
                        world.setBlock(x, y, z, this.blockID, 3, 2);
                        world.setBlock(x, y + 1, z, this.blockID, 4, 2);
                    }

                case 4:
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        switch (meta)
        {
            case 0:
                return this.stageOne;

            case 1:
                return this.stageTwo;

            case 2:
                return this.stageThree;

            case 3:
                return this.bottomNaked;

            case 4:
                return this.topNaked;

            case 5:
                return this.bottomIcon;

            case 6:
                return this.topIcon;

            default:
                return null;
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random random, int par3)
    {
        return 0;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        int treeMeta = world.getBlockMetadata(x, y, z);
        Block soil = blocksList[world.getBlockId(x, y - 1, z)];

        if (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z) && soil != null)
        {
            if ((treeMeta <= 1 || treeMeta == 3 || treeMeta == 5) && (soil == AetherBlocks.AetherDirt || soil == AetherBlocks.AetherGrass))
            {
                return true;
            }

            if ((treeMeta == 2 || treeMeta == 4 || treeMeta == 6) && world.getBlockId(x, y - 1, z) == this.blockID)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        if (meta == 6 && world.getBlockId(x, y - 1, z) == this.blockID)
        {
            world.setBlock(x, y, z, this.blockID, 4, 2);
            world.setBlock(x, y - 1, z, this.blockID, 3, 2);
        }

        if (meta == 5 && world.getBlockId(x, y + 1, z) == this.blockID)
        {
            world.setBlock(x, y + 1, z, this.blockID, 4, 2);
            world.setBlock(x, y, z, this.blockID, 3, 2);
        }

        if (meta == 4 && world.getBlockId(x, y - 1, z) == this.blockID)
        {
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y - 1, z, 0);
        }

        if (meta == 3 && world.getBlockId(x, y + 1, z) == this.blockID)
        {
            world.setBlock(x, y + 1, z, 0);
            world.setBlock(x, y, z, 0);
        }

        byte min;
        byte max;

        if ((world.getBlockId(x, y - 1, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 1, z) != 1) && (world.getBlockId(x, y - 2, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 2, z) != 1))
        {
            if ((world.getBlockId(x, y - 1, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 1, z) != 2) && (world.getBlockId(x, y - 2, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 2, z) != 2))
            {
                min = 0;
                max = 2;
            }
            else
            {
                min = 3;
                max = 6;
            }
        }
        else
        {
            min = 1;
            max = 3;
        }

        int randomNum = MathHelper.clamp_int(world.rand.nextInt(), max, min) + 1;
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);
        ItemStack stack;

        if (randomNum != 0 && meta >= 5)
        {
            stack = new ItemStack(AetherItems.Orange.itemID, randomNum, 0);
            this.dropBlockAsItem_do(world, x, y, z, stack);
        }
        else if (meta < 5)
        {
            stack = new ItemStack(this, 1, 0);
            this.dropBlockAsItem_do(world, x, y, z, stack);
        }
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        if (meta == 6 && world.getBlockId(x, y - 1, z) == this.blockID)
        {
            world.setBlock(x, y, z, this.blockID, 4, 2);
            world.setBlock(x, y - 1, z, this.blockID, 3, 2);
        }

        if (meta == 5 && world.getBlockId(x, y + 1, z) == this.blockID)
        {
            world.setBlock(x, y + 1, z, this.blockID, 4, 2);
            world.setBlock(x, y, z, this.blockID, 3, 2);
        }

        if (meta == 4 && world.getBlockId(x, y - 1, z) == this.blockID)
        {
            world.setBlock(x, y, z, 0);
            world.setBlock(x, y - 1, z, 0);
        }

        if (meta == 3 && world.getBlockId(x, y + 1, z) == this.blockID)
        {
            world.setBlock(x, y + 1, z, 0);
            world.setBlock(x, y, z, 0);
        }

        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.bottomIcon = par1IconRegister.registerIcon("aether:Orange Fruit Tree Bottom");
        this.topIcon = par1IconRegister.registerIcon("aether:Orange Fruit Tree Top");
        this.bottomNaked = par1IconRegister.registerIcon("aether:Orange Fruit Tree Bottom Naked");
        this.topNaked = par1IconRegister.registerIcon("aether:Orange Fruit Tree Top Naked");
        this.stageOne = par1IconRegister.registerIcon("aether:Orange Tree Stage One");
        this.stageTwo = par1IconRegister.registerIcon("aether:Orange Tree Stage Two");
        this.stageThree = par1IconRegister.registerIcon("aether:Orange Tree Top Stage Three");
    }

    public int randomGrowthInt(World world, int x, int y, int z, Random random, int randomint)
    {
        return (world.getBlockId(x, y - 2, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 2, z) != 2) && (world.getBlockId(x, y - 1, z) != AetherBlocks.AetherGrass.blockID || world.getBlockMetadata(x, y - 1, z) != 2) ? random.nextInt(randomint) : random.nextInt(randomint / 2);
    }

    protected final void checkTreeChange(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            int treeMeta = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, 0);

            if (treeMeta == 0)
            {
                ItemStack min = new ItemStack(AetherBlocks.BlockOrangeTree.blockID, 1, 0);
                this.dropBlockAsItem_do(world, x, y, z, min);
            }
            else
            {
                byte max;
                byte min1;

                if (world.getBlockId(x, y - 1, z) == AetherBlocks.AetherGrass.blockID && world.getBlockMetadata(x, y - 1, z) == 1)
                {
                    min1 = 1;
                    max = 3;
                }
                else
                {
                    min1 = 0;
                    max = 2;
                }

                int randomNum = MathHelper.clamp_int(world.rand.nextInt(), max, min1) + 1;

                if (randomNum != 0)
                {
                    ItemStack stack = new ItemStack(AetherItems.Orange.itemID, randomNum, 0);
                    this.dropBlockAsItem_do(world, x, y, z, stack);
                }
            }

            world.setBlock(x, y, z, 0);
        }
    }
}
