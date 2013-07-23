package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockAmbrosiumTorch extends BlockAether implements IAetherBlock
{
    protected BlockAmbrosiumTorch(int var1)
    {
        super(var1, Material.circuits);
        this.setTickRandomly(true);
        this.setLightValue(0.9375F);
        this.setStepSound(Block.soundWoodFootstep);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return null;
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
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 2;
    }

    private boolean canPlaceTorchOn(World var1, int var2, int var3, int var4)
    {
        if (var1.doesBlockHaveSolidTopSurface(var2, var3, var4))
        {
            return true;
        }
        else
        {
            int var5 = var1.getBlockId(var2, var3, var4);
            return Block.blocksList[var5] != null && Block.blocksList[var5].canPlaceTorchOnTop(var1, var2, var3, var4);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return var1.isBlockSolidOnSide(var2 - 1, var3, var4, ForgeDirection.EAST, true) || var1.isBlockSolidOnSide(var2 + 1, var3, var4, ForgeDirection.WEST, true) || var1.isBlockSolidOnSide(var2, var3, var4 - 1, ForgeDirection.SOUTH, true) || var1.isBlockSolidOnSide(var2, var3, var4 + 1, ForgeDirection.NORTH, true) || this.canPlaceTorchOn(var1, var2, var3 - 1, var4);
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9)
    {
        int var10 = var9;

        if (var5 == 1 && this.canPlaceTorchOn(var1, var2, var3 - 1, var4))
        {
            var10 = 5;
        }

        if (var5 == 2 && var1.isBlockSolidOnSide(var2, var3, var4 + 1, ForgeDirection.NORTH, true))
        {
            var10 = 4;
        }

        if (var5 == 3 && var1.isBlockSolidOnSide(var2, var3, var4 - 1, ForgeDirection.SOUTH, true))
        {
            var10 = 3;
        }

        if (var5 == 4 && var1.isBlockSolidOnSide(var2 + 1, var3, var4, ForgeDirection.WEST, true))
        {
            var10 = 2;
        }

        if (var5 == 5 && var1.isBlockSolidOnSide(var2 - 1, var3, var4, ForgeDirection.EAST, true))
        {
            var10 = 1;
        }

        return var10;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.updateTick(var1, var2, var3, var4, var5);

        if (var1.getBlockMetadata(var2, var3, var4) == 0)
        {
            this.onBlockAdded(var1, var2, var3, var4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (var1.getBlockMetadata(var2, var3, var4) == 0)
        {
            if (var1.isBlockSolidOnSide(var2 - 1, var3, var4, ForgeDirection.EAST, true))
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 1, 2);
            }
            else if (var1.isBlockSolidOnSide(var2 + 1, var3, var4, ForgeDirection.WEST, true))
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 2, 2);
            }
            else if (var1.isBlockSolidOnSide(var2, var3, var4 - 1, ForgeDirection.SOUTH, true))
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 3, 2);
            }
            else if (var1.isBlockSolidOnSide(var2, var3, var4 + 1, ForgeDirection.NORTH, true))
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 4, 2);
            }
            else if (this.canPlaceTorchOn(var1, var2, var3 - 1, var4))
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 5, 2);
            }
        }

        this.dropTorchIfCantStay(var1, var2, var3, var4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        this.func_94397_d(var1, var2, var3, var4, var5);
    }

    protected boolean func_94397_d(World var1, int var2, int var3, int var4, int var5)
    {
        if (this.dropTorchIfCantStay(var1, var2, var3, var4))
        {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            boolean var7 = false;

            if (!var1.isBlockSolidOnSide(var2 - 1, var3, var4, ForgeDirection.EAST, true) && var6 == 1)
            {
                var7 = true;
            }

            if (!var1.isBlockSolidOnSide(var2 + 1, var3, var4, ForgeDirection.WEST, true) && var6 == 2)
            {
                var7 = true;
            }

            if (!var1.isBlockSolidOnSide(var2, var3, var4 - 1, ForgeDirection.SOUTH, true) && var6 == 3)
            {
                var7 = true;
            }

            if (!var1.isBlockSolidOnSide(var2, var3, var4 + 1, ForgeDirection.NORTH, true) && var6 == 4)
            {
                var7 = true;
            }

            if (!this.canPlaceTorchOn(var1, var2, var3 - 1, var4) && var6 == 5)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
                var1.setBlockToAir(var2, var3, var4);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    protected boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4)
    {
        if (!this.canPlaceBlockAt(var1, var2, var3, var4))
        {
            if (var1.getBlockId(var2, var3, var4) == this.blockID)
            {
                this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
                var1.setBlockToAir(var2, var3, var4);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3 var5, Vec3 var6)
    {
        int var7 = var1.getBlockMetadata(var2, var3, var4) & 7;
        float var8 = 0.15F;

        if (var7 == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 2)
        {
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 3)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        }
        else if (var7 == 4)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        }
        else
        {
            var8 = 0.1F;
            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }

        return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        int var6 = var1.getBlockMetadata(var2, var3, var4);
        double var7 = (double)((float)var2 + 0.5F);
        double var9 = (double)((float)var3 + 0.7F);
        double var11 = (double)((float)var4 + 0.5F);
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;

        if (var6 == 1)
        {
            var1.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 2)
        {
            var1.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 3)
        {
            var1.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 4)
        {
            var1.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            var1.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            var1.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
        }
    }
}
