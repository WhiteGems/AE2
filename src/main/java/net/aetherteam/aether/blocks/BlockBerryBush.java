package net.aetherteam.aether.blocks;

import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockBerryBush extends BlockAetherFlower implements IAetherBlock
{
    protected BlockBerryBush(int var1)
    {
        super(var1);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    protected final void checkBushChange(World var1, int var2, int var3, int var4)
    {
        if (!this.canBlockStay(var1, var2, var3, var4))
        {
            var1.setBlock(var2, var3, var4, 0);
            ItemStack var5 = new ItemStack(AetherBlocks.BerryBush.blockID, 1, 0);
            this.dropBlockAsItem_do(var1, var2, var3, var4, var5);
            var1.setBlock(var2, var3, var4, 0);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return AetherBlocks.BerryBush.blockID;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return AxisAlignedBB.getBoundingBox((double) var2 + this.minX, (double) var3 + this.minY, (double) var4 + this.minZ, (double) var2 + this.maxX, (double) var3 + this.maxY, (double) var4 + this.maxZ);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return Aether.BerryBushRenderID;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        byte var7;
        byte var8;

        if (var1.getBlockId(var3, var4 - 1, var5) == AetherBlocks.AetherGrass.blockID && var1.getBlockMetadata(var3, var4 - 1, var5) == 1)
        {
            var7 = 1;
            var8 = 3;
        } else
        {
            var7 = 0;
            var8 = 2;
        }

        int var9 = var1.rand.nextInt(var8 - var7 + 1) + var7;
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);

        if (var9 != 0)
        {
            ItemStack var10 = new ItemStack(AetherItems.BlueBerry.itemID, var9, 0);
            this.dropBlockAsItem_do(var1, var3, var4, var5, var10);
        }

        var1.setBlock(var3, var4, var5, AetherBlocks.BerryBushStem.blockID);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        this.checkBushChange(var1, var2, var3, var4);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        this.checkBushChange(var1, var2, var3, var4);
    }
}
