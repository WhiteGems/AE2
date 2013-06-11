package net.aetherteam.aether.blocks;

import java.util.Random;

import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFloating extends BlockAether implements IAetherBlock
{
    public static boolean fallInstantly = false;
    private boolean enchanted;

    public BlockFloating(int var1, boolean var2)
    {
        super(var1, Material.rock);
        this.enchanted = var2;
        this.setHardness(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!this.enchanted || this.enchanted && var1.isBlockIndirectlyGettingPowered(var2, var3, var4))
        {
            this.tryToFall(var1, var2, var3, var4);
        }
    }

    private void tryToFall(World var1, int var2, int var3, int var4)
    {
        int var8 = MathHelper.floor_double((double) var2);
        int var9 = MathHelper.floor_double((double) var3);
        int var10 = MathHelper.floor_double((double) var4);
        int var11 = var1.getBlockId(var2, var3, var4);
        int var12 = var1.getBlockMetadata(var2, var3, var4);

        if (canFallAbove(var1, var2, var3 + 1, var4) && var3 < var1.getHeight())
        {
            byte var13 = 32;

            if (!fallInstantly && var1.checkChunksExist(var2 - var13, var3 - var13, var4 - var13, var2 + var13, var3 + var13, var4 + var13))
            {
                var1.setBlockToAir(var2, var3, var4);
                EntityFloatingBlock var14 = new EntityFloatingBlock(var1, (double) ((float) var8 + 0.5F), (double) ((float) var9 + 0.5F), (double) ((float) var10 + 0.5F), this.blockID, var12);

                if (!var1.isRemote)
                {
                    var1.spawnEntityInWorld(var14);
                }
            } else
            {
                var1.setBlockToAir(var2, var3, var4);

                while (canFallAbove(var1, var2, var3 + 1, var4) && var3 < 128)
                {
                    ++var3;
                }

                if (var3 > 0)
                {
                    var1.setBlock(var2, var3, var4, var11, var12, 2);
                }
            }
        }
    }

    /**
     * Called when the falling block entity for this block is created
     */
    protected void onStartFalling(EntityFloatingBlock var1)
    {}

    /**
     * Called when the falling block entity for this block hits the ground and turns back into a block
     */
    public void onFinishFalling(World var1, int var2, int var3, int var4, int var5)
    {}

    public int tickRate()
    {
        return 3;
    }

    public static boolean canFallAbove(World var0, int var1, int var2, int var3)
    {
        int var4 = var0.getBlockId(var1, var2, var3);

        if (var4 == 0)
        {
            return true;
        } else if (var4 == Block.fire.blockID)
        {
            return true;
        } else
        {
            Material var5 = Block.blocksList[var4].blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }
}
