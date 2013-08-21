package net.aetherteam.aether.blocks;

import net.aetherteam.aether.dungeons.DungeonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockLockedAetherWall extends BlockAetherWall
{
    public BlockLockedAetherWall(int par1, Block par2Block, int metadata)
    {
        super(par1, par2Block, metadata);
    }

    private boolean isLocked()
    {
        return true;
    }

    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return this.isLocked() ? false : super.removeBlockByPlayer(world, player, x, y, z);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (this.isLocked() && DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)x), MathHelper.floor_double((double)y), MathHelper.floor_double((double)z)) == null)
        {
            world.setBlockToAir(x, y, z);
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        if (this.isLocked())
        {
            world.setBlockToAir(x, y, z);
        }
    }
}
