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

    public BlockFloating(int i, boolean bool)
    {
        super(i, Material.rock);
        this.enchanted = bool;
        this.setHardness(5.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int i, int j, int k)
    {
        world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (!this.enchanted || this.enchanted && world.isBlockIndirectlyGettingPowered(i, j, k))
        {
            this.tryToFall(world, i, j, k);
        }
    }

    private void tryToFall(World world, int i, int j, int k)
    {
        int x = MathHelper.floor_double((double)i);
        int y = MathHelper.floor_double((double)j);
        int z = MathHelper.floor_double((double)k);
        int id = world.getBlockId(i, j, k);
        int meta = world.getBlockMetadata(i, j, k);

        if (canFallAbove(world, i, j + 1, k) && j < world.getHeight())
        {
            byte byte0 = 32;

            if (!fallInstantly && world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0))
            {
                world.setBlockToAir(i, j, k);
                EntityFloatingBlock floating = new EntityFloatingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.blockID, meta);

                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(floating);
                }
            }
            else
            {
                world.setBlockToAir(i, j, k);

                while (canFallAbove(world, i, j + 1, k) && j < 128)
                {
                    ++j;
                }

                if (j > 0)
                {
                    world.setBlock(i, j, k, id, meta, 2);
                }
            }
        }
    }

    /**
     * Called when the falling block entity for this block is created
     */
    protected void onStartFalling(EntityFloatingBlock floatingBlock) {}

    /**
     * Called when the falling block entity for this block hits the ground and turns back into a block
     */
    public void onFinishFalling(World par1World, int par2, int par3, int par4, int par5) {}

    public int tickRate()
    {
        return 3;
    }

    public static boolean canFallAbove(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);

        if (l == 0)
        {
            return true;
        }
        else if (l == Block.fire.blockID)
        {
            return true;
        }
        else
        {
            Material material = Block.blocksList[l].blockMaterial;
            return material == Material.water ? true : material == Material.lava;
        }
    }
}
