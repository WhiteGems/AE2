package net.aetherteam.aether.blocks;

import net.aetherteam.aether.dungeons.DungeonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockLockedAetherStairs extends BlockAetherStairs
{
    protected BlockLockedAetherStairs(int var1, Block var2, int var3)
    {
        super(var1, var2, var3);
    }

    private boolean isLocked()
    {
        return true;
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        return this.isLocked() ? false : super.removeBlockByPlayer(var1, var2, var3, var4, var5);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (this.isLocked() && DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)var2), MathHelper.floor_double((double)var3), MathHelper.floor_double((double)var4)) == null)
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        if (this.isLocked())
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }
}
