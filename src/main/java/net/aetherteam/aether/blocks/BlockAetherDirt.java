package net.aetherteam.aether.blocks;

import java.util.List;

import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class BlockAetherDirt extends BlockAether implements IAetherBlock
{
    public BlockAetherDirt(int var1)
    {
        super(var1, Material.ground);
        this.setHardness(0.5F);
        this.setStepSound(Block.soundGravelFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 1));
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);

        if (!var1.isRemote)
        {
            ItemStack var7;

            if (var6 == 0)
            {
                if (var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
                {
                    var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    var7 = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                } else
                {
                    var7 = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
            } else
            {
                var7 = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, var6);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        ItemStack var10 = var5.getCurrentEquippedItem();

        if (var10 == null)
        {
            return false;
        } else
        {
            if (var10.itemID == AetherItems.SwettyBall.itemID)
            {
                int var11 = 0;

                for (int var12 = var2 - 1; var12 <= var2 + 1; ++var12)
                {
                    for (int var13 = var4 - 1; var13 <= var4 + 1; ++var13)
                    {
                        if (var1.getBlockId(var12, var3, var13) == this.blockID)
                        {
                            if (var1.getBlockId(var12, var3 + 1, var13) == this.blockID)
                            {
                                if (var1.getBlockId(var12, var3 + 2, var13) == 0 && !var1.isRemote)
                                {
                                    var1.setBlock(var12, var3 + 1, var13, AetherBlocks.AetherGrass.blockID);
                                    ++var11;
                                }
                            } else if (var1.getBlockId(var12, var3 + 1, var13) == 0 && !var1.isRemote)
                            {
                                var1.setBlock(var12, var3, var13, AetherBlocks.AetherGrass.blockID);
                                ++var11;
                            }
                        } else if (var1.getBlockId(var12, var3, var13) == 0 && var1.getBlockId(var12, var3 - 1, var13) == this.blockID && !var1.isRemote)
                        {
                            var1.setBlock(var12, var3 - 1, var13, AetherBlocks.AetherGrass.blockID);
                            ++var11;
                        }
                    }

                    if (var11 > 0)
                    {
                        --var10.stackSize;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        var1.setBlock(var2, var3, var4, this.blockID);
        var1.setBlockMetadataWithNotify(var2, var3, var4, 1, 16);
    }
}
