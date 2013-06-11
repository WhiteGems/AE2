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

    public BlockOrangeTree(int var1)
    {
        super(var1);
        this.setHardness(0.2F);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.updateTick(var1, var2, var3, var4, var5);
        this.checkTreeChange(var1, var2, var3, var4);
        int var6 = this.randomGrowthInt(var1, var2, var3, var4, var5, 5);
        int var7 = var1.getBlockMetadata(var2, var3, var4);

        if (var6 == 0)
        {
            switch (var7)
            {
                case 0:
                    var1.setBlock(var2, var3, var4, this.blockID, 1, 2);
                    break;

                case 1:
                    var1.setBlock(var2, var3, var4, this.blockID, 3, 2);
                    var1.setBlock(var2, var3 + 1, var4, this.blockID, 2, 2);
                    break;

                case 2:
                    var1.setBlock(var2, var3 - 1, var4, this.blockID, 3, 2);
                    var1.setBlock(var2, var3, var4, this.blockID, 4, 2);
                    break;

                case 3:
                    if (var1.getBlockMetadata(var2, var3 + 1, var4) == 4)
                    {
                        var1.setBlock(var2, var3, var4, this.blockID, 5, 2);
                        var1.setBlock(var2, var3 + 1, var4, this.blockID, 6, 2);
                    } else
                    {
                        var1.setBlock(var2, var3, var4, this.blockID, 3, 2);
                        var1.setBlock(var2, var3 + 1, var4, this.blockID, 4, 2);
                    }

                case 4:
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        switch (var2)
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
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        Block var6 = blocksList[var1.getBlockId(var2, var3 - 1, var4)];

        if (var1.getFullBlockLightValue(var2, var3, var4) >= 8 || var1.canBlockSeeTheSky(var2, var3, var4) && var6 != null)
        {
            if ((var5 <= 1 || var5 == 3 || var5 == 5) && (var6 == AetherBlocks.AetherDirt || var6 == AetherBlocks.AetherGrass))
            {
                return true;
            }

            if ((var5 == 2 || var5 == 4 || var5 == 6) && var1.getBlockId(var2, var3 - 1, var4) == this.blockID)
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
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        if (var6 == 6 && var1.getBlockId(var3, var4 - 1, var5) == this.blockID)
        {
            var1.setBlock(var3, var4, var5, this.blockID, 4, 2);
            var1.setBlock(var3, var4 - 1, var5, this.blockID, 3, 2);
        }

        if (var6 == 5 && var1.getBlockId(var3, var4 + 1, var5) == this.blockID)
        {
            var1.setBlock(var3, var4 + 1, var5, this.blockID, 4, 2);
            var1.setBlock(var3, var4, var5, this.blockID, 3, 2);
        }

        if (var6 == 4 && var1.getBlockId(var3, var4 - 1, var5) == this.blockID)
        {
            var1.setBlock(var3, var4, var5, 0);
            var1.setBlock(var3, var4 - 1, var5, 0);
        }

        if (var6 == 3 && var1.getBlockId(var3, var4 + 1, var5) == this.blockID)
        {
            var1.setBlock(var3, var4 + 1, var5, 0);
            var1.setBlock(var3, var4, var5, 0);
        }

        byte var7;
        byte var8;

        if ((var1.getBlockId(var3, var4 - 1, var5) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var3, var4 - 1, var5) != 1) && (var1.getBlockId(var3, var4 - 2, var5) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var3, var4 - 2, var5) != 1))
        {
            if ((var1.getBlockId(var3, var4 - 1, var5) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var3, var4 - 1, var5) != 2) && (var1.getBlockId(var3, var4 - 2, var5) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var3, var4 - 2, var5) != 2))
            {
                var7 = 0;
                var8 = 2;
            } else
            {
                var7 = 3;
                var8 = 6;
            }
        } else
        {
            var7 = 1;
            var8 = 3;
        }

        int var9 = MathHelper.clamp_int(var1.rand.nextInt(), var8, var7) + 1;
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);
        ItemStack var10;

        if (var9 != 0 && var6 >= 5)
        {
            var10 = new ItemStack(AetherItems.Orange.itemID, var9, 0);
            this.dropBlockAsItem_do(var1, var3, var4, var5, var10);
        } else if (var6 < 5)
        {
            var10 = new ItemStack(this, 1, 0);
            this.dropBlockAsItem_do(var1, var3, var4, var5, var10);
        }
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5)
    {
        if (var5 == 6 && var1.getBlockId(var2, var3 - 1, var4) == this.blockID)
        {
            var1.setBlock(var2, var3, var4, this.blockID, 4, 2);
            var1.setBlock(var2, var3 - 1, var4, this.blockID, 3, 2);
        }

        if (var5 == 5 && var1.getBlockId(var2, var3 + 1, var4) == this.blockID)
        {
            var1.setBlock(var2, var3 + 1, var4, this.blockID, 4, 2);
            var1.setBlock(var2, var3, var4, this.blockID, 3, 2);
        }

        if (var5 == 4 && var1.getBlockId(var2, var3 - 1, var4) == this.blockID)
        {
            var1.setBlock(var2, var3, var4, 0);
            var1.setBlock(var2, var3 - 1, var4, 0);
        }

        if (var5 == 3 && var1.getBlockId(var2, var3 + 1, var4) == this.blockID)
        {
            var1.setBlock(var2, var3 + 1, var4, 0);
            var1.setBlock(var2, var3, var4, 0);
        }

        super.onBlockDestroyedByPlayer(var1, var2, var3, var4, var5);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.bottomIcon = var1.registerIcon("Aether:Orange Fruit Tree Bottom");
        this.topIcon = var1.registerIcon("Aether:Orange Fruit Tree Top");
        this.bottomNaked = var1.registerIcon("Aether:Orange Fruit Tree Bottom Naked");
        this.topNaked = var1.registerIcon("Aether:Orange Fruit Tree Top Naked");
        this.stageOne = var1.registerIcon("Aether:Orange Tree Stage One");
        this.stageTwo = var1.registerIcon("Aether:Orange Tree Stage Two");
        this.stageThree = var1.registerIcon("Aether:Orange Tree Top Stage Three");
        super.registerIcons(var1);
    }

    public int randomGrowthInt(World var1, int var2, int var3, int var4, Random var5, int var6)
    {
        return (var1.getBlockId(var2, var3 - 2, var4) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var2, var3 - 2, var4) != 2) && (var1.getBlockId(var2, var3 - 1, var4) != AetherBlocks.AetherGrass.blockID || var1.getBlockMetadata(var2, var3 - 1, var4) != 2) ? var5.nextInt(var6) : var5.nextInt(var6 / 2);
    }

    protected final void checkTreeChange(World var1, int var2, int var3, int var4)
    {
        if (!this.canBlockStay(var1, var2, var3, var4))
        {
            int var5 = var1.getBlockMetadata(var2, var3, var4);
            var1.setBlock(var2, var3, var4, 0);

            if (var5 == 0)
            {
                ItemStack var6 = new ItemStack(AetherBlocks.BlockOrangeTree.blockID, 1, 0);
                this.dropBlockAsItem_do(var1, var2, var3, var4, var6);
            } else
            {
                byte var10;
                byte var7;

                if (var1.getBlockId(var2, var3 - 1, var4) == AetherBlocks.AetherGrass.blockID && var1.getBlockMetadata(var2, var3 - 1, var4) == 1)
                {
                    var10 = 1;
                    var7 = 3;
                } else
                {
                    var10 = 0;
                    var7 = 2;
                }

                int var8 = MathHelper.clamp_int(var1.rand.nextInt(), var7, var10) + 1;

                if (var8 != 0)
                {
                    ItemStack var9 = new ItemStack(AetherItems.Orange.itemID, var8, 0);
                    this.dropBlockAsItem_do(var1, var2, var3, var4, var9);
                }
            }

            var1.setBlock(var2, var3, var4, 0);
        }
    }
}
