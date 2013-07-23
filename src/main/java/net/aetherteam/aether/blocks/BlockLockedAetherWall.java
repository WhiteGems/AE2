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
        if (isLocked())
        {
            return false;
        }

        return super.removeBlockByPlayer(world, player, x, y, z);
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        if ((isLocked()) && (DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)) == null))
        {
            world.setBlockToAir(x, y, z);
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        if (isLocked())
        {
            world.setBlockToAir(x, y, z);
        }
    }
}

