package net.aetherteam.aether.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockIcestone extends BlockAether
    implements IAetherBlock
{
    protected BlockIcestone(int blockID)
    {
        super(blockID, Material.rock);
        setHardness(3.0F);
        setStepSound(Block.soundGlassFootstep);
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);
        dropBlockAsItem(world, i, j, k, l, 0);
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entity, ItemStack stack)
    {
        Material mat0 = world.getBlockMaterial(i, j, k);
        Material mat1 = world.getBlockMaterial(i, j - 1, k);

        for (int l = i - 3; l <= i + 3; l++)
        {
            for (int i1 = j - 3; i1 <= j + 3; i1++)
            {
                for (int j1 = k - 3; j1 <= k + 3; j1++)
                {
                    if (world.getBlockId(l, i1, j1) == 8)
                    {
                        world.setBlock(l, i1, j1, 79);
                    }
                    else if (world.getBlockId(l, i1, j1) == 9)
                    {
                        world.setBlock(l, i1, j1, 79);
                    }
                    else if (world.getBlockId(l, i1, j1) == 10)
                    {
                        world.setBlock(l, i1, j1, 49);
                    }
                    else if (world.getBlockId(l, i1, j1) == 11)
                    {
                        world.setBlock(l, i1, j1, 49);
                    }
                }
            }
        }
    }
}

