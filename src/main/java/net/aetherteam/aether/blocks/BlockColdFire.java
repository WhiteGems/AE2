package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockColdFire extends BlockFire
{
    /** The chance this block will encourage nearby blocks to catch on fire */
    private int[] chanceToEncourageFire = new int[256];

    /**
     * This is an array indexed by block ID the larger the number in the array the more likely a block type will catch
     * fires
     */
    private int[] abilityToCatchFire = new int[256];
    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    protected BlockColdFire(int par1)
    {
        super(par1);
        this.setTickRandomly(true);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * This method is called on a block after all other blocks gets already created. You can use it to reference and
     * configure something on the block that needs the others ones.
     */
    public void initializeBlock()
    {
        this.abilityToCatchFire = Block.blockFlammability;
        this.chanceToEncourageFire = Block.blockFireSpreadSpeed;
        this.setBurnRate(AetherBlocks.SkyrootPlank.blockID, 5, 20);
        this.setBurnRate(AetherBlocks.SkyrootFence.blockID, 5, 20);
        this.setBurnRate(AetherBlocks.SkyrootStairs.blockID, 5, 20);
        this.setBurnRate(AetherBlocks.AetherLog.blockID, 5, 5);
    }

    /**
     * Sets the burn rate for a block. The larger abilityToCatchFire the more easily it will catch. The larger
     * chanceToEncourageFire the faster it will burn and spread to other blocks. Args: blockID, chanceToEncourageFire,
     * abilityToCatchFire
     */
    private void setBurnRate(int par1, int par2, int par3)
    {
        Block.setBurnProperties(par1, par2, par3);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
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
        return 3;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 30;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            Block base = Block.blocksList[par1World.getBlockId(par2, par3 - 1, par4)];
            boolean flag = base != null && base.isFireSource(par1World, par2, par3 - 1, par4, par1World.getBlockMetadata(par2, par3 - 1, par4), ForgeDirection.UP);

            if (!this.canPlaceBlockAt(par1World, par2, par3, par4))
            {
                par1World.setBlockToAir(par2, par3, par4);
            }

            if (!flag && par1World.isRaining() && (par1World.canLightningStrikeAt(par2, par3, par4) || par1World.canLightningStrikeAt(par2 - 1, par3, par4) || par1World.canLightningStrikeAt(par2 + 1, par3, par4) || par1World.canLightningStrikeAt(par2, par3, par4 - 1) || par1World.canLightningStrikeAt(par2, par3, par4 + 1)))
            {
                par1World.setBlockToAir(par2, par3, par4);
            }
            else
            {
                int l = par1World.getBlockMetadata(par2, par3, par4);

                if (l < 15)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, l + par5Random.nextInt(3) / 2, 4);
                }

                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World) + par5Random.nextInt(10));

                if (!flag && !this.canNeighborBurn(par1World, par2, par3, par4))
                {
                    if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || l > 3)
                    {
                        par1World.setBlockToAir(par2, par3, par4);
                    }
                }
                else if (!flag && !this.canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP) && l == 15 && par5Random.nextInt(4) == 0)
                {
                    par1World.setBlockToAir(par2, par3, par4);
                }
                else
                {
                    boolean flag1 = par1World.isBlockHighHumidity(par2, par3, par4);
                    byte b0 = 0;

                    if (flag1)
                    {
                        b0 = -50;
                    }

                    this.tryToCatchBlockOnFire(par1World, par2 + 1, par3, par4, 300 + b0, par5Random, l, ForgeDirection.WEST);
                    this.tryToCatchBlockOnFire(par1World, par2 - 1, par3, par4, 300 + b0, par5Random, l, ForgeDirection.EAST);
                    this.tryToCatchBlockOnFire(par1World, par2, par3 - 1, par4, 250 + b0, par5Random, l, ForgeDirection.UP);
                    this.tryToCatchBlockOnFire(par1World, par2, par3 + 1, par4, 250 + b0, par5Random, l, ForgeDirection.DOWN);
                    this.tryToCatchBlockOnFire(par1World, par2, par3, par4 - 1, 300 + b0, par5Random, l, ForgeDirection.SOUTH);
                    this.tryToCatchBlockOnFire(par1World, par2, par3, par4 + 1, 300 + b0, par5Random, l, ForgeDirection.NORTH);

                    for (int i1 = par2 - 1; i1 <= par2 + 1; ++i1)
                    {
                        for (int j1 = par4 - 1; j1 <= par4 + 1; ++j1)
                        {
                            for (int k1 = par3 - 1; k1 <= par3 + 4; ++k1)
                            {
                                if (i1 != par2 || k1 != par3 || j1 != par4)
                                {
                                    int l1 = 100;

                                    if (k1 > par3 + 1)
                                    {
                                        l1 += (k1 - (par3 + 1)) * 100;
                                    }

                                    int i2 = this.getChanceOfNeighborsEncouragingFire(par1World, i1, k1, j1);

                                    if (i2 > 0)
                                    {
                                        int j2 = (i2 + 40 + par1World.difficultySetting * 7) / (l + 30);

                                        if (flag1)
                                        {
                                            j2 /= 2;
                                        }

                                        if (j2 > 0 && par5Random.nextInt(l1) <= j2 && (!par1World.isRaining() || !par1World.canLightningStrikeAt(i1, k1, j1)) && !par1World.canLightningStrikeAt(i1 - 1, k1, par4) && !par1World.canLightningStrikeAt(i1 + 1, k1, j1) && !par1World.canLightningStrikeAt(i1, k1, j1 - 1) && !par1World.canLightningStrikeAt(i1, k1, j1 + 1))
                                        {
                                            int k2 = l + par5Random.nextInt(5) / 4;

                                            if (k2 > 15)
                                            {
                                                k2 = 15;
                                            }

                                            par1World.setBlock(i1, k1, j1, this.blockID, k2, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean func_82506_l()
    {
        return false;
    }

    @Deprecated
    private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7)
    {
        this.tryToCatchBlockOnFire(par1World, par2, par3, par4, par5, par6Random, par7, ForgeDirection.UP);
    }

    private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7, ForgeDirection face)
    {
        int j1 = 0;
        Block block = Block.blocksList[par1World.getBlockId(par2, par3, par4)];

        if (block != null)
        {
            j1 = block.getFlammability(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), face);
        }

        if (par6Random.nextInt(par5) < j1)
        {
            boolean flag = par1World.getBlockId(par2, par3, par4) == Block.tnt.blockID;

            if (par6Random.nextInt(par7 + 10) < 5 && !par1World.canLightningStrikeAt(par2, par3, par4))
            {
                int k1 = par7 + par6Random.nextInt(5) / 4;

                if (k1 > 15)
                {
                    k1 = 15;
                }

                par1World.setBlock(par2, par3, par4, this.blockID, k1, 3);
            }
            else
            {
                par1World.setBlockToAir(par2, par3, par4);
            }

            if (flag)
            {
                Block.tnt.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            }
        }
    }

    /**
     * Returns true if at least one block next to this one can burn.
     */
    private boolean canNeighborBurn(World par1World, int par2, int par3, int par4)
    {
        return this.canBlockCatchFire(par1World, par2 + 1, par3, par4, ForgeDirection.WEST) || this.canBlockCatchFire(par1World, par2 - 1, par3, par4, ForgeDirection.EAST) || this.canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP) || this.canBlockCatchFire(par1World, par2, par3 + 1, par4, ForgeDirection.DOWN) || this.canBlockCatchFire(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH) || this.canBlockCatchFire(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH);
    }

    /**
     * Gets the highest chance of a neighbor block encouraging this block to catch fire
     */
    private int getChanceOfNeighborsEncouragingFire(World par1World, int par2, int par3, int par4)
    {
        byte b0 = 0;

        if (!par1World.isAirBlock(par2, par3, par4))
        {
            return 0;
        }
        else
        {
            int l = this.getChanceToEncourageFire(par1World, par2 + 1, par3, par4, b0, ForgeDirection.WEST);
            l = this.getChanceToEncourageFire(par1World, par2 - 1, par3, par4, l, ForgeDirection.EAST);
            l = this.getChanceToEncourageFire(par1World, par2, par3 - 1, par4, l, ForgeDirection.UP);
            l = this.getChanceToEncourageFire(par1World, par2, par3 + 1, par4, l, ForgeDirection.DOWN);
            l = this.getChanceToEncourageFire(par1World, par2, par3, par4 - 1, l, ForgeDirection.SOUTH);
            l = this.getChanceToEncourageFire(par1World, par2, par3, par4 + 1, l, ForgeDirection.NORTH);
            return l;
        }
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return false;
    }

    @Deprecated

    /**
     * Checks the specified block coordinate to see if it can catch fire.  Args: blockAccess, x, y, z
     */
    public boolean canBlockCatchFire(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return this.canBlockCatchFire(par1IBlockAccess, par2, par3, par4, ForgeDirection.UP);
    }

    @Deprecated

    /**
     * Retrieves a specified block's chance to encourage their neighbors to burn and if the number is greater than the
     * current number passed in it will return its number instead of the passed in one.  Args: world, x, y, z,
     * curChanceToEncourageFire
     */
    public int getChanceToEncourageFire(World par1World, int par2, int par3, int par4, int par5)
    {
        return this.getChanceToEncourageFire(par1World, par2, par3, par4, par5, ForgeDirection.UP);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || this.canNeighborBurn(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !this.canNeighborBurn(par1World, par2, par3, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.provider.dimensionId > 0 || par1World.getBlockId(par2, par3 - 1, par4) != Block.obsidian.blockID || !Block.portal.tryToCreatePortal(par1World, par2, par3, par4))
        {
            if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !this.canNeighborBurn(par1World, par2, par3, par4))
            {
                par1World.setBlockToAir(par2, par3, par4);
            }
            else
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World) + par1World.rand.nextInt(10));
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(24) == 0)
        {
            par1World.playSound((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F, false);
        }

        int l;
        float f;
        float f1;
        float f2;

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2, par3 - 1, par4, ForgeDirection.UP))
        {
            if (AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2 - 1, par3, par4, ForgeDirection.EAST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)par2 + par5Random.nextFloat() * 0.1F;
                    f1 = (float)par3 + par5Random.nextFloat();
                    f2 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2 + 1, par3, par4, ForgeDirection.WEST))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)(par2 + 1) - par5Random.nextFloat() * 0.1F;
                    f1 = (float)par3 + par5Random.nextFloat();
                    f2 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2, par3, par4 - 1, ForgeDirection.SOUTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)par2 + par5Random.nextFloat();
                    f1 = (float)par3 + par5Random.nextFloat();
                    f2 = (float)par4 + par5Random.nextFloat() * 0.1F;
                    par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2, par3, par4 + 1, ForgeDirection.NORTH))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)par2 + par5Random.nextFloat();
                    f1 = (float)par3 + par5Random.nextFloat();
                    f2 = (float)(par4 + 1) - par5Random.nextFloat() * 0.1F;
                    par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(par1World, par2, par3 + 1, par4, ForgeDirection.DOWN))
            {
                for (l = 0; l < 2; ++l)
                {
                    f = (float)par2 + par5Random.nextFloat();
                    f1 = (float)(par3 + 1) - par5Random.nextFloat() * 0.1F;
                    f2 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (l = 0; l < 3; ++l)
            {
                f = (float)par2 + par5Random.nextFloat();
                f1 = (float)par3 + par5Random.nextFloat() * 0.5F + 0.5F;
                f2 = (float)par4 + par5Random.nextFloat();
                par1World.spawnParticle("largesmoke", (double)f, (double)f1, (double)f2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconArray = new Icon[] {par1IconRegister.registerIcon("aether:coldfire_0"), par1IconRegister.registerIcon("aether:coldfire_1")};
    }

    @SideOnly(Side.CLIENT)
    public Icon func_94438_c(int par1)
    {
        return this.iconArray[par1];
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return this.iconArray[0];
    }

    public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return block != null ? block.isFlammable(world, x, y, z, world.getBlockMetadata(x, y, z), face) : false;
    }

    public int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, ForgeDirection face)
    {
        int newChance = 0;
        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (block != null)
        {
            newChance = block.getFireSpreadSpeed(world, x, y, z, world.getBlockMetadata(x, y, z), face);
        }

        return newChance > oldChance ? newChance : oldChance;
    }
}
