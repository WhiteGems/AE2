package net.aetherteam.aether.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityBlueFX;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrystalLeaves extends BlockAether implements IAetherBlock
{
    private Icon crystalleaves;
    private Icon crystalfruitleaves;
    
    public BlockCrystalLeaves(int var1)
    {
        super(var1, Material.leaves);
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
        var3.add(new ItemStack(var1, 1, 1));
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	crystalleaves = ir.registerIcon("Aether:Crystal Leaves" );
    	crystalfruitleaves =ir.registerIcon("Aether:Crystal Fruit Leaves");
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
	@SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
    	switch(meta)
    	{
    	case 0:
    		return this.crystalleaves;
    	case 1:
    		return this.crystalfruitleaves;
    	}
        return this.crystalleaves;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);

        if (var6 >= 1)
        {
            ItemStack var7 = new ItemStack(AetherItems.WhiteApple.itemID, 1, 0);
            this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
        }
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
            Loc var9 = (Loc)var6.poll();

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

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.randomDisplayTick(var1, var2, var3, var4, var5);

        if (var5.nextInt(10) == 0 && var1.isRemote)
        {
            for (int var6 = 0; var6 < 15; ++var6)
            {
                double var7 = (double)var2 + ((double)var5.nextFloat() - 0.5D) * 6.0D;
                double var9 = (double)var3 + ((double)var5.nextFloat() - 0.5D) * 6.0D;
                double var11 = (double)var4 + ((double)var5.nextFloat() - 0.5D) * 6.0D;
                double var13 = 0.0D;
                double var15 = 0.0D;
                double var17 = 0.0D;
                var13 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                var15 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                var17 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                EntityBlueFX var19 = new EntityBlueFX(var1, var7, var9, var11, var13, var15, var17);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(var19);
            }
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
}
