package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class BlockIcestone extends BlockAether implements IAetherBlock
{
    protected BlockIcestone(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(3.0F);
        this.setStepSound(Block.soundGlassFootstep);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);
        this.dropBlockAsItem(var1, var3, var4, var5, var6, 0);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        var1.getBlockMaterial(var2, var3, var4);
        var1.getBlockMaterial(var2, var3 - 1, var4);

        for (int var9 = var2 - 3; var9 <= var2 + 3; ++var9)
        {
            for (int var10 = var3 - 3; var10 <= var3 + 3; ++var10)
            {
                for (int var11 = var4 - 3; var11 <= var4 + 3; ++var11)
                {
                    if (var1.getBlockId(var9, var10, var11) == 8)
                    {
                        var1.setBlock(var9, var10, var11, 79);
                    }
                    else if (var1.getBlockId(var9, var10, var11) == 9)
                    {
                        var1.setBlock(var9, var10, var11, 79);
                    }
                    else if (var1.getBlockId(var9, var10, var11) == 10)
                    {
                        var1.setBlock(var9, var10, var11, 49);
                    }
                    else if (var1.getBlockId(var9, var10, var11) == 11)
                    {
                        var1.setBlock(var9, var10, var11, 49);
                    }
                }
            }
        }
    }
}
