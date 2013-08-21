package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChestMimic extends BlockSkyrootChest implements IAetherBlock
{
    protected BlockChestMimic(int i)
    {
        super(i, 0);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            world.setBlock(i, j, k, 0);
            EntityMimic mimic = new EntityMimic(world);
            mimic.setPosition((double)i + 0.5D, (double)j + 1.5D, (double)k + 0.5D);
            world.spawnEntityInWorld(mimic);
        }

        return true;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = 0;

        if (world.getBlockId(i - 1, j, k) == this.blockID)
        {
            ++l;
        }

        if (world.getBlockId(i + 1, j, k) == this.blockID)
        {
            ++l;
        }

        if (world.getBlockId(i, j, k - 1) == this.blockID)
        {
            ++l;
        }

        if (world.getBlockId(i, j, k + 1) == this.blockID)
        {
            ++l;
        }

        return l > 1 ? false : (this.isThereANeighborChest(world, i - 1, j, k) ? false : (this.isThereANeighborChest(world, i + 1, j, k) ? false : (this.isThereANeighborChest(world, i, j, k - 1) ? false : !this.isThereANeighborChest(world, i, j, k + 1))));
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("aether:Skyroot Plank");
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return this.blockIcon;
    }

    private boolean isThereANeighborChest(World world, int i, int j, int k)
    {
        return world.getBlockId(i, j, k) != this.blockID ? false : (world.getBlockId(i - 1, j, k) == this.blockID ? true : (world.getBlockId(i + 1, j, k) == this.blockID ? true : (world.getBlockId(i, j, k - 1) == this.blockID ? true : world.getBlockId(i, j, k + 1) == this.blockID)));
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int par5)
    {
        if (!world.isRemote)
        {
            world.setBlock(i, j, k, 0);
            EntityMimic mimic = new EntityMimic(world);
            mimic.setPosition((double)i + 0.5D, (double)j + 1.5D, (double)k + 0.5D);
            world.spawnEntityInWorld(mimic);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
