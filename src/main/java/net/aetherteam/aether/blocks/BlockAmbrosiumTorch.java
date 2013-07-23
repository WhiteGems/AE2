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

public class BlockAmbrosiumTorch extends BlockAether
    implements IAetherBlock
{
    protected BlockAmbrosiumTorch(int blockID)
    {
        super(blockID, Material.circuits);
        setTickRandomly(true);
        setLightValue(0.9375F);
        setStepSound(Block.soundWoodFootstep);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 2;
    }

    private boolean canPlaceTorchOn(World par1World, int par2, int par3, int par4)
    {
        if (par1World.doesBlockHaveSolidTopSurface(par2, par3, par4))
        {
            return true;
        }

        int l = par1World.getBlockId(par2, par3, par4);
        return (Block.blocksList[l] != null) && (Block.blocksList[l].canPlaceTorchOnTop(par1World, par2, par3, par4));
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST, true)) || (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST, true)) || (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH, true)) || (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH, true)) || (canPlaceTorchOn(par1World, par2, par3 - 1, par4));
    }

    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = par9;

        if ((par5 == 1) && (canPlaceTorchOn(par1World, par2, par3 - 1, par4)))
        {
            j1 = 5;
        }

        if ((par5 == 2) && (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH, true)))
        {
            j1 = 4;
        }

        if ((par5 == 3) && (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH, true)))
        {
            j1 = 3;
        }

        if ((par5 == 4) && (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST, true)))
        {
            j1 = 2;
        }

        if ((par5 == 5) && (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST, true)))
        {
            j1 = 1;
        }

        return j1;
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            onBlockAdded(par1World, par2, par3, par4);
        }
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0)
        {
            if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
            }
            else if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
            }
            else if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
            }
            else if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH, true))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
            }
            else if (canPlaceTorchOn(par1World, par2, par3 - 1, par4))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
            }
        }

        dropTorchIfCantStay(par1World, par2, par3, par4);
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        func_94397_d(par1World, par2, par3, par4, par5);
    }

    protected boolean func_94397_d(World par1World, int par2, int par3, int par4, int par5)
    {
        if (dropTorchIfCantStay(par1World, par2, par3, par4))
        {
            int i1 = par1World.getBlockMetadata(par2, par3, par4);
            boolean flag = false;

            if ((!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST, true)) && (i1 == 1))
            {
                flag = true;
            }

            if ((!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST, true)) && (i1 == 2))
            {
                flag = true;
            }

            if ((!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH, true)) && (i1 == 3))
            {
                flag = true;
            }

            if ((!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH, true)) && (i1 == 4))
            {
                flag = true;
            }

            if ((!canPlaceTorchOn(par1World, par2, par3 - 1, par4)) && (i1 == 5))
            {
                flag = true;
            }

            if (flag)
            {
                dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockToAir(par2, par3, par4);
                return true;
            }

            return false;
        }

        return true;
    }

    protected boolean dropTorchIfCantStay(World par1World, int par2, int par3, int par4)
    {
        if (!canPlaceBlockAt(par1World, par2, par3, par4))
        {
            if (par1World.getBlockId(par2, par3, par4) == this.blockID)
            {
                dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockToAir(par2, par3, par4);
            }

            return false;
        }

        return true;
    }

    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4) & 0x7;
        float f = 0.15F;

        if (l == 1)
        {
            setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else
        {
            f = 0.1F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        double d0 = par2 + 0.5F;
        double d1 = par3 + 0.7F;
        double d2 = par4 + 0.5F;
        double d3 = 0.219999998807907D;
        double d4 = 0.2700000107288361D;

        if (l == 1)
        {
            par1World.spawnParticle("smoke", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2)
        {
            par1World.spawnParticle("smoke", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3)
        {
            par1World.spawnParticle("smoke", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 4)
        {
            par1World.spawnParticle("smoke", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            par1World.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}

