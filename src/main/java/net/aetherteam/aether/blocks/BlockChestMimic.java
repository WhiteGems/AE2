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
    protected BlockChestMimic(int var1)
    {
        super(var1, 0);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (!var1.isRemote)
        {
            var1.setBlock(var2, var3, var4, 0);
            EntityMimic var10 = new EntityMimic(var1);
            var10.setPosition((double) var2 + 0.5D, (double) var3 + 1.5D, (double) var4 + 0.5D);
            var1.spawnEntityInWorld(var10);
        }

        return true;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        int var5 = 0;

        if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2, var3, var4 + 1) == this.blockID)
        {
            ++var5;
        }

        return var5 > 1 ? false : (this.isThereANeighborChest(var1, var2 - 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2 + 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2, var3, var4 - 1) ? false : !this.isThereANeighborChest(var1, var2, var3, var4 + 1))));
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.blockIcon = var1.registerIcon("Aether:Skyroot Plank");
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return this.blockIcon;
    }

    private boolean isThereANeighborChest(World var1, int var2, int var3, int var4)
    {
        return var1.getBlockId(var2, var3, var4) != this.blockID ? false : (var1.getBlockId(var2 - 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2 + 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2, var3, var4 - 1) == this.blockID ? true : var1.getBlockId(var2, var3, var4 + 1) == this.blockID)));
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5)
    {
        if (!var1.isRemote)
        {
            var1.setBlock(var2, var3, var4, 0);
            EntityMimic var6 = new EntityMimic(var1);
            var6.setPosition((double) var2 + 0.5D, (double) var3 + 1.5D, (double) var4 + 0.5D);
            var1.spawnEntityInWorld(var6);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 0;
    }
}
