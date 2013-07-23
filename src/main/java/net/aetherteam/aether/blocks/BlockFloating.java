package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFloating extends BlockAether
    implements IAetherBlock
{
    public static boolean fallInstantly = false;
    private boolean enchanted;

    public BlockFloating(int i, boolean bool)
    {
        super(i, Material.rock);
        this.enchanted = bool;
        setHardness(5.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        world.scheduleBlockUpdate(i, j, k, this.blockID, tickRate());
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        world.scheduleBlockUpdate(i, j, k, this.blockID, tickRate());
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if ((!this.enchanted) || ((this.enchanted) && (world.isBlockIndirectlyGettingPowered(i, j, k))))
        {
            tryToFall(world, i, j, k);
        }
    }

    private void tryToFall(World world, int i, int j, int k)
    {
        int l = i;
        int i1 = j;
        int j1 = k;
        int x = MathHelper.floor_double(i);
        int y = MathHelper.floor_double(j);
        int z = MathHelper.floor_double(k);
        int id = world.getBlockId(i, j, k);
        int meta = world.getBlockMetadata(i, j, k);

        if ((canFallAbove(world, l, i1 + 1, j1)) && (i1 < world.getActualHeight()))
        {
            byte byte0 = 32;

            if ((fallInstantly) || (!world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0)))
            {
                world.setBlockToAir(i, j, k);

                while ((canFallAbove(world, i, j + 1, k)) && (j < 128))
                {
                    j++;
                }

                if (j > 0)
                {
                    world.setBlock(i, j, k, id, meta, 2);
                }
            }
            else
            {
                world.setBlockToAir(i, j, k);
                EntityFloatingBlock floating = new EntityFloatingBlock(world, x + 0.5F, y + 0.5F, z + 0.5F, this.blockID, meta);

                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(floating);
                }
            }
        }
    }

    protected void func_82520_a(EntityFloatingBlock floatingBlock)
    {
    }

    public void func_82519_a_(World par1World, int par2, int par3, int par4, int par5)
    {
    }

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

        if (l == Block.fire.blockID)
        {
            return true;
        }

        Material material = Block.blocksList[l].blockMaterial;

        if (material == Material.water)
        {
            return true;
        }

        return material == Material.lava;
    }
}

