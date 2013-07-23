package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChestMimic extends BlockSkyrootChest
    implements IAetherBlock
{
    protected BlockChestMimic(int i)
    {
        super(i, 0);
        setHardness(2.0F);
        setStepSound(Block.soundWoodFootstep);
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            world.setBlock(i, j, k, 0);
            EntityMimic mimic = new EntityMimic(world);
            mimic.setPosition(i + 0.5D, j + 1.5D, k + 0.5D);
            world.spawnEntityInWorld(mimic);
        }

        return true;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = 0;

        if (world.getBlockId(i - 1, j, k) == this.blockID)
        {
            l++;
        }

        if (world.getBlockId(i + 1, j, k) == this.blockID)
        {
            l++;
        }

        if (world.getBlockId(i, j, k - 1) == this.blockID)
        {
            l++;
        }

        if (world.getBlockId(i, j, k + 1) == this.blockID)
        {
            l++;
        }

        if (l > 1)
        {
            return false;
        }

        if (isThereANeighborChest(world, i - 1, j, k))
        {
            return false;
        }

        if (isThereANeighborChest(world, i + 1, j, k))
        {
            return false;
        }

        if (isThereANeighborChest(world, i, j, k - 1))
        {
            return false;
        }

        return !isThereANeighborChest(world, i, j, k + 1);
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("Aether:Skyroot Plank");
    }

    public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return this.blockIcon;
    }

    private boolean isThereANeighborChest(World world, int i, int j, int k)
    {
        if (world.getBlockId(i, j, k) != this.blockID)
        {
            return false;
        }

        if (world.getBlockId(i - 1, j, k) == this.blockID)
        {
            return true;
        }

        if (world.getBlockId(i + 1, j, k) == this.blockID)
        {
            return true;
        }

        if (world.getBlockId(i, j, k - 1) == this.blockID)
        {
            return true;
        }

        return world.getBlockId(i, j, k + 1) == this.blockID;
    }

    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int par5)
    {
        if (!world.isRemote)
        {
            world.setBlock(i, j, k, 0);
            EntityMimic mimic = new EntityMimic(world);
            mimic.setPosition(i + 0.5D, j + 1.5D, k + 0.5D);
            world.spawnEntityInWorld(mimic);
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }
}

