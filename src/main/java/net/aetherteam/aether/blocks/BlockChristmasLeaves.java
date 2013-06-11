package net.aetherteam.aether.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChristmasLeaves extends BlockAether implements IAetherBlock
{
    public static Icon leafIcon;
    public static Icon decoratedLeafIcon;

    public BlockChristmasLeaves(int var1)
    {
        super(var1, Material.leaves);
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
        this.setLightOpacity(1);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
        var3.add(new ItemStack(var1, 1, 1));
    }

    public Icon getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return var2 == 0 ? leafIcon : decoratedLeafIcon;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    private boolean isMyTrunkMeta(int var1)
    {
        return this.blockID == AetherBlocks.ChristmasLeaves.blockID ? var1 <= 1 : var1 >= 2;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    private boolean nearTrunk(World var1, int var2, int var3, int var4)
    {
        Loc var5 = new Loc(var2, var3, var4);
        LinkedList var6 = new LinkedList();
        ArrayList var7 = new ArrayList();
        var6.offer(new Loc(var2, var3, var4));
        int var8 = this.blockID;

        while (!var6.isEmpty())
        {
            Loc var9 = (Loc) var6.poll();

            if (!var7.contains(var9))
            {
                if (var9.distSimple(var5) <= 4)
                {
                    int var10 = var9.getBlock(var1);
                    var9.getMeta(var1);

                    if (var10 == AetherBlocks.AetherLog.blockID)
                    {
                        return true;
                    }

                    if (var10 == var8)
                    {
                        var6.addAll(Arrays.asList(var9.adjacent()));
                    }
                }

                var7.add(var9);
            }
        }

        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 0;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        for (int var6 = 0; var6 < 4; ++var6)
        {
            double var7 = (double) var2 + ((double) var5.nextFloat() - 0.5D) * 10.0D;
            double var9 = (double) var3 + ((double) var5.nextFloat() - 0.5D) * 10.0D;
            double var11 = (double) var4 + ((double) var5.nextFloat() - 0.5D) * 10.0D;
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            var13 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double) var5.nextFloat() - 0.5D) * 0.5D;
        }
    }

    private void removeLeaves(World var1, int var2, int var3, int var4)
    {
        var1.setBlock(var2, var3, var4, 0);
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        var1.getBlockId(var2, var3, var4);
        return true;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!this.nearTrunk(var1, var2, var3, var4))
        {
            this.removeLeaves(var1, var2, var3, var4);
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        leafIcon = var1.registerIcon("Aether:Christmas Leaves");
        decoratedLeafIcon = var1.registerIcon("Aether:Decorated Christmas Leaves");
        super.registerIcons(var1);
    }
}
