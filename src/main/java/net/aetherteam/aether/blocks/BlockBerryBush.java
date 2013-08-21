package net.aetherteam.aether.blocks;

import java.util.Random;
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

    protected final void checkBushChange(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            world.setBlock(x, y, z, 0);
            ItemStack stack = new ItemStack(AetherBlocks.BerryBush.blockID, 1, 0);
            this.dropBlockAsItem_do(world, x, y, z, stack);
            world.setBlock(x, y, z, 0);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return AetherBlocks.BerryBush.blockID;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox((double)i + this.minX, (double)j + this.minY, (double)k + this.minZ, (double)i + this.maxX, (double)j + this.maxY, (double)k + this.maxZ);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return AetherBlocks.berryBushRenderId;
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
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        byte min;
        byte max;

        if (world.getBlockId(x, y - 1, z) == AetherBlocks.AetherGrass.blockID && world.getBlockMetadata(x, y - 1, z) == 1)
        {
            min = 1;
            max = 3;
        }
        else
        {
            min = 0;
            max = 2;
        }

        int randomNum = world.rand.nextInt(max - min + 1) + min;
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (randomNum != 0)
        {
            ItemStack stack = new ItemStack(AetherItems.BlueBerry.itemID, randomNum, 0);
            this.dropBlockAsItem_do(world, x, y, z, stack);
        }

        world.setBlock(x, y, z, AetherBlocks.BerryBushStem.blockID);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        this.checkBushChange(par1World, par2, par3, par4);
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
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        this.checkBushChange(par1World, par2, par3, par4);
    }
}
