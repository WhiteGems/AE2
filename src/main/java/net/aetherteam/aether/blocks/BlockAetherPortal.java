package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.CommonProxy;
import net.aetherteam.aether.PlayerBaseAetherServer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockAetherPortal extends BlockPortal
    implements IAetherBlock
{
    public BlockAetherPortal(int blockID)
    {
        super(blockID);
        setHardness(-1.0F);
        setResistance(6000000.0F);
        setUnlocalizedName("Aether:Aether Portal");
    }

    @SideOnly(Side.CLIENT)
    public int getDimNumber(EntityPlayer player)
    {
        return player.dimension == 0 ? 3 : 0;
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("Aether:Aether Portal");
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(this));
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if ((entity.ridingEntity == null) && (entity.riddenByEntity == null))
            if ((entity instanceof EntityPlayer))
            {
                if ((entity.timeUntilPortal <= 0) &&
                        (!world.isRemote))
                {
                    Aether.getServerPlayer((EntityPlayer)entity).Z();
                }
            }
            else if (entity.timeUntilPortal <= 0)
            {
                Aether.teleportEntityToAether(entity);
            }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = 0;
        int j1 = 1;

        if ((world.getBlockId(i - 1, j, k) == this.blockID) || (world.getBlockId(i + 1, j, k) == this.blockID))
        {
            i1 = 1;
            j1 = 0;
        }

        for (int k1 = j; world.getBlockId(i, k1 - 1, k) == this.blockID; k1--);

        if (world.getBlockId(i, k1 - 1, k) != Block.glowStone.blockID)
        {
            world.setBlock(i, j, k, 0);
            return;
        }

        for (int l1 = 1; (l1 < 4) && (world.getBlockId(i, k1 + l1, k) == this.blockID); l1++);

        if ((l1 != 3) || (world.getBlockId(i, k1 + l1, k) != Block.glowStone.blockID))
        {
            world.setBlock(i, j, k, 0);
            return;
        }

        boolean flag = (world.getBlockId(i - 1, j, k) == this.blockID) || (world.getBlockId(i + 1, j, k) == this.blockID);
        boolean flag1 = (world.getBlockId(i, j, k - 1) == this.blockID) || (world.getBlockId(i, j, k + 1) == this.blockID);

        if ((flag) && (flag1))
        {
            world.setBlock(i, j, k, 0);
            return;
        }

        if (((world.getBlockId(i + i1, j, k + j1) != Block.glowStone.blockID) || (world.getBlockId(i - i1, j, k - j1) != this.blockID)) && ((world.getBlockId(i - i1, j, k - j1) != Block.glowStone.blockID) || (world.getBlockId(i + i1, j, k + j1) != this.blockID)))
        {
            world.setBlock(i, j, k, 0);
            return;
        }
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (random.nextInt(250) == 0)
        {
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "aeportal.aeportal", 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        Aether.proxy.spawnPortalParticles(world, x, y, z, random, this.blockID);
    }

    public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4)
    {
        byte b0 = 0;
        byte b1 = 0;

        if ((par1World.getBlockId(par2 - 1, par3, par4) == Block.glowStone.blockID) || (par1World.getBlockId(par2 + 1, par3, par4) == Block.glowStone.blockID))
        {
            b0 = 1;
        }

        if ((par1World.getBlockId(par2, par3, par4 - 1) == Block.glowStone.blockID) || (par1World.getBlockId(par2, par3, par4 + 1) == Block.glowStone.blockID))
        {
            b1 = 1;
        }

        if (b0 == b1)
        {
            return false;
        }

        if (par1World.getBlockId(par2 - b0, par3, par4 - b1) == 0)
        {
            par2 -= b0;
            par4 -= b1;
        }

        for (int l = -1; l <= 2; l++)
        {
            for (int i1 = -1; i1 <= 3; i1++)
            {
                boolean flag = (l == -1) || (l == 2) || (i1 == -1) || (i1 == 3);

                if (((l != -1) && (l != 2)) || ((i1 != -1) && (i1 != 3)))
                {
                    int j1 = par1World.getBlockId(par2 + b0 * l, par3 + i1, par4 + b1 * l);

                    if (flag)
                    {
                        if (j1 != Block.glowStone.blockID)
                        {
                            return false;
                        }
                    }
                    else if ((j1 != 0) && (j1 != Block.waterMoving.blockID))
                    {
                        return false;
                    }
                }
            }
        }

        for (l = 0; l < 2; l++)
        {
            for (int i1 = 0; i1 < 3; i1++)
            {
                par1World.setBlock(par2 + b0 * l, par3 + i1, par4 + b1 * l, AetherBlocks.AetherPortal.blockID, 0, 2);
            }
        }

        return true;
    }
}

