package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockAetherPortal extends BlockPortal implements IAetherBlock
{
    public BlockAetherPortal(int var1)
    {
        super(var1);
        this.setHardness(-1.0F);
        this.setResistance(6000000.0F);
        this.setUnlocalizedName("Aether:Aether Portal");
    }

    @SideOnly(Side.CLIENT)
    public int getDimNumber(EntityPlayer var1)
    {
        return var1.dimension == 0 ? 3 : 0;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.blockIcon = var1.registerIcon("Aether:Aether Portal");
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(this));
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World var1, int var2, int var3, int var4, Entity var5)
    {
        if (var5.ridingEntity == null && var5.riddenByEntity == null)
        {
            if (var5 instanceof EntityPlayer)
            {
                if (var5.timeUntilPortal <= 0 && !var1.isRemote)
                {
                    Aether.getServerPlayer((EntityPlayer)var5).setInPortal();
                }
            }
            else if (var5.timeUntilPortal <= 0)
            {
                Aether.teleportEntityToAether(var5);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        byte var6 = 0;
        byte var7 = 1;

        if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID)
        {
            var6 = 1;
            var7 = 0;
        }

        int var8;

        for (var8 = var3; var1.getBlockId(var2, var8 - 1, var4) == this.blockID; --var8)
        {
            ;
        }

        if (var1.getBlockId(var2, var8 - 1, var4) != Block.glowStone.blockID)
        {
            var1.setBlock(var2, var3, var4, 0);
        }
        else
        {
            int var9;

            for (var9 = 1; var9 < 4 && var1.getBlockId(var2, var8 + var9, var4) == this.blockID; ++var9)
            {
                ;
            }

            if (var9 == 3 && var1.getBlockId(var2, var8 + var9, var4) == Block.glowStone.blockID)
            {
                boolean var10 = var1.getBlockId(var2 - 1, var3, var4) == this.blockID || var1.getBlockId(var2 + 1, var3, var4) == this.blockID;
                boolean var11 = var1.getBlockId(var2, var3, var4 - 1) == this.blockID || var1.getBlockId(var2, var3, var4 + 1) == this.blockID;

                if (var10 && var11)
                {
                    var1.setBlock(var2, var3, var4, 0);
                }
                else if ((var1.getBlockId(var2 + var6, var3, var4 + var7) != Block.glowStone.blockID || var1.getBlockId(var2 - var6, var3, var4 - var7) != this.blockID) && (var1.getBlockId(var2 - var6, var3, var4 - var7) != Block.glowStone.blockID || var1.getBlockId(var2 + var6, var3, var4 + var7) != this.blockID))
                {
                    var1.setBlock(var2, var3, var4, 0);
                }
            }
            else
            {
                var1.setBlock(var2, var3, var4, 0);
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (var5.nextInt(250) == 0)
        {
            var1.playSound((double)var2 + 0.5D, (double)var3 + 0.5D, (double)var4 + 0.5D, "aeportal.aeportal", 0.5F, var5.nextFloat() * 0.4F + 0.8F, false);
        }

        Aether.proxy.spawnPortalParticles(var1, var2, var3, var4, var5, this.blockID);
    }

    /**
     * Checks to see if this location is valid to create a portal and will return True if it does. Args: world, x, y, z
     */
    public boolean tryToCreatePortal(World var1, int var2, int var3, int var4)
    {
        byte var5 = 0;
        byte var6 = 0;

        if (var1.getBlockId(var2 - 1, var3, var4) == Block.glowStone.blockID || var1.getBlockId(var2 + 1, var3, var4) == Block.glowStone.blockID)
        {
            var5 = 1;
        }

        if (var1.getBlockId(var2, var3, var4 - 1) == Block.glowStone.blockID || var1.getBlockId(var2, var3, var4 + 1) == Block.glowStone.blockID)
        {
            var6 = 1;
        }

        if (var5 == var6)
        {
            return false;
        }
        else
        {
            if (var1.getBlockId(var2 - var5, var3, var4 - var6) == 0)
            {
                var2 -= var5;
                var4 -= var6;
            }

            int var7;
            int var8;

            for (var7 = -1; var7 <= 2; ++var7)
            {
                for (var8 = -1; var8 <= 3; ++var8)
                {
                    boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;

                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3)
                    {
                        int var10 = var1.getBlockId(var2 + var5 * var7, var3 + var8, var4 + var6 * var7);

                        if (var9)
                        {
                            if (var10 != Block.glowStone.blockID)
                            {
                                return false;
                            }
                        }
                        else if (var10 != 0 && var10 != Block.waterMoving.blockID)
                        {
                            return false;
                        }
                    }
                }
            }

            for (var7 = 0; var7 < 2; ++var7)
            {
                for (var8 = 0; var8 < 3; ++var8)
                {
                    var1.setBlock(var2 + var5 * var7, var3 + var8, var4 + var6 * var7, AetherBlocks.AetherPortal.blockID, 0, 2);
                }
            }

            return true;
        }
    }
}
