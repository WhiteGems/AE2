package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import net.aetherteam.aether.Aether;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockColdFire extends BlockFire
{
    private int[] chanceToEncourageFire = new int[256];
    private int[] abilityToCatchFire = new int[256];
    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    protected BlockColdFire(int var1)
    {
        super(var1);
        this.setTickRandomly(true);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
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

    private void setBurnRate(int var1, int var2, int var3)
    {
        Block.setBurnProperties(var1, var2, var3);
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
        return 3;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 0;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World var1)
    {
        return 30;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (var1.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            Block var6 = Block.blocksList[var1.getBlockId(var2, var3 - 1, var4)];
            boolean var7 = var6 != null && var6.isFireSource(var1, var2, var3 - 1, var4, var1.getBlockMetadata(var2, var3 - 1, var4), ForgeDirection.UP);

            if (!this.canPlaceBlockAt(var1, var2, var3, var4))
            {
                var1.setBlockToAir(var2, var3, var4);
            }

            if (!var7 && var1.isRaining() && (var1.canLightningStrikeAt(var2, var3, var4) || var1.canLightningStrikeAt(var2 - 1, var3, var4) || var1.canLightningStrikeAt(var2 + 1, var3, var4) || var1.canLightningStrikeAt(var2, var3, var4 - 1) || var1.canLightningStrikeAt(var2, var3, var4 + 1)))
            {
                var1.setBlockToAir(var2, var3, var4);
            } else
            {
                int var8 = var1.getBlockMetadata(var2, var3, var4);

                if (var8 < 15)
                {
                    var1.setBlockMetadataWithNotify(var2, var3, var4, var8 + var5.nextInt(3) / 2, 4);
                }

                var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1) + var5.nextInt(10));

                if (!var7 && !this.canNeighborBurn(var1, var2, var3, var4))
                {
                    if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) || var8 > 3)
                    {
                        var1.setBlockToAir(var2, var3, var4);
                    }
                } else if (!var7 && !this.canBlockCatchFire(var1, var2, var3 - 1, var4, ForgeDirection.UP) && var8 == 15 && var5.nextInt(4) == 0)
                {
                    var1.setBlockToAir(var2, var3, var4);
                } else
                {
                    boolean var9 = var1.isBlockHighHumidity(var2, var3, var4);
                    byte var10 = 0;

                    if (var9)
                    {
                        var10 = -50;
                    }

                    this.tryToCatchBlockOnFire(var1, var2 + 1, var3, var4, 300 + var10, var5, var8, ForgeDirection.WEST);
                    this.tryToCatchBlockOnFire(var1, var2 - 1, var3, var4, 300 + var10, var5, var8, ForgeDirection.EAST);
                    this.tryToCatchBlockOnFire(var1, var2, var3 - 1, var4, 250 + var10, var5, var8, ForgeDirection.UP);
                    this.tryToCatchBlockOnFire(var1, var2, var3 + 1, var4, 250 + var10, var5, var8, ForgeDirection.DOWN);
                    this.tryToCatchBlockOnFire(var1, var2, var3, var4 - 1, 300 + var10, var5, var8, ForgeDirection.SOUTH);
                    this.tryToCatchBlockOnFire(var1, var2, var3, var4 + 1, 300 + var10, var5, var8, ForgeDirection.NORTH);

                    for (int var11 = var2 - 1; var11 <= var2 + 1; ++var11)
                    {
                        for (int var12 = var4 - 1; var12 <= var4 + 1; ++var12)
                        {
                            for (int var13 = var3 - 1; var13 <= var3 + 4; ++var13)
                            {
                                if (var11 != var2 || var13 != var3 || var12 != var4)
                                {
                                    int var14 = 100;

                                    if (var13 > var3 + 1)
                                    {
                                        var14 += (var13 - (var3 + 1)) * 100;
                                    }

                                    int var15 = this.getChanceOfNeighborsEncouragingFire(var1, var11, var13, var12);

                                    if (var15 > 0)
                                    {
                                        int var16 = (var15 + 40 + var1.difficultySetting * 7) / (var8 + 30);

                                        if (var9)
                                        {
                                            var16 /= 2;
                                        }

                                        if (var16 > 0 && var5.nextInt(var14) <= var16 && (!var1.isRaining() || !var1.canLightningStrikeAt(var11, var13, var12)) && !var1.canLightningStrikeAt(var11 - 1, var13, var4) && !var1.canLightningStrikeAt(var11 + 1, var13, var12) && !var1.canLightningStrikeAt(var11, var13, var12 - 1) && !var1.canLightningStrikeAt(var11, var13, var12 + 1))
                                        {
                                            int var17 = var8 + var5.nextInt(5) / 4;

                                            if (var17 > 15)
                                            {
                                                var17 = 15;
                                            }

                                            var1.setBlock(var11, var13, var12, this.blockID, var17, 3);
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
    private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6, int var7)
    {
        this.tryToCatchBlockOnFire(var1, var2, var3, var4, var5, var6, var7, ForgeDirection.UP);
    }

    private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6, int var7, ForgeDirection var8)
    {
        int var9 = 0;
        Block var10 = Block.blocksList[var1.getBlockId(var2, var3, var4)];

        if (var10 != null)
        {
            var9 = var10.getFlammability(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), var8);
        }

        if (var6.nextInt(var5) < var9)
        {
            boolean var11 = var1.getBlockId(var2, var3, var4) == Block.tnt.blockID;

            if (var6.nextInt(var7 + 10) < 5 && !var1.canLightningStrikeAt(var2, var3, var4))
            {
                int var12 = var7 + var6.nextInt(5) / 4;

                if (var12 > 15)
                {
                    var12 = 15;
                }

                var1.setBlock(var2, var3, var4, this.blockID, var12, 3);
            } else
            {
                var1.setBlockToAir(var2, var3, var4);
            }

            if (var11)
            {
                Block.tnt.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
            }
        }
    }

    private boolean canNeighborBurn(World var1, int var2, int var3, int var4)
    {
        return this.canBlockCatchFire(var1, var2 + 1, var3, var4, ForgeDirection.WEST) || this.canBlockCatchFire(var1, var2 - 1, var3, var4, ForgeDirection.EAST) || this.canBlockCatchFire(var1, var2, var3 - 1, var4, ForgeDirection.UP) || this.canBlockCatchFire(var1, var2, var3 + 1, var4, ForgeDirection.DOWN) || this.canBlockCatchFire(var1, var2, var3, var4 - 1, ForgeDirection.SOUTH) || this.canBlockCatchFire(var1, var2, var3, var4 + 1, ForgeDirection.NORTH);
    }

    private int getChanceOfNeighborsEncouragingFire(World var1, int var2, int var3, int var4)
    {
        byte var5 = 0;

        if (!var1.isAirBlock(var2, var3, var4))
        {
            return 0;
        } else
        {
            int var6 = this.getChanceToEncourageFire(var1, var2 + 1, var3, var4, var5, ForgeDirection.WEST);
            var6 = this.getChanceToEncourageFire(var1, var2 - 1, var3, var4, var6, ForgeDirection.EAST);
            var6 = this.getChanceToEncourageFire(var1, var2, var3 - 1, var4, var6, ForgeDirection.UP);
            var6 = this.getChanceToEncourageFire(var1, var2, var3 + 1, var4, var6, ForgeDirection.DOWN);
            var6 = this.getChanceToEncourageFire(var1, var2, var3, var4 - 1, var6, ForgeDirection.SOUTH);
            var6 = this.getChanceToEncourageFire(var1, var2, var3, var4 + 1, var6, ForgeDirection.NORTH);
            return var6;
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
    public boolean canBlockCatchFire(IBlockAccess var1, int var2, int var3, int var4)
    {
        return this.canBlockCatchFire(var1, var2, var3, var4, ForgeDirection.UP);
    }

    @Deprecated

    /**
     * Retrieves a specified block's chance to encourage their neighbors to burn and if the number is greater than the
     * current number passed in it will return its number instead of the passed in one.  Args: world, x, y, z,
     * curChanceToEncourageFire
     */
    public int getChanceToEncourageFire(World var1, int var2, int var3, int var4, int var5)
    {
        return this.getChanceToEncourageFire(var1, var2, var3, var4, var5, ForgeDirection.UP);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) || this.canNeighborBurn(var1, var2, var3, var4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && !this.canNeighborBurn(var1, var2, var3, var4))
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (var1.provider.dimensionId > 0 || var1.getBlockId(var2, var3 - 1, var4) != Block.obsidian.blockID || !Block.portal.tryToCreatePortal(var1, var2, var3, var4))
        {
            if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && !this.canNeighborBurn(var1, var2, var3, var4))
            {
                var1.setBlockToAir(var2, var3, var4);
            } else
            {
                var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1) + var1.rand.nextInt(10));
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        List var6 = var1.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double) var2, (double) var3, (double) var4, (double) (var2 + 1), (double) (var3 + 2), (double) (var4 + 1)));
        int var7;

        if (var6.size() > 0)
        {
            for (var7 = 0; var7 < var6.size(); ++var7)
            {
                if (Aether.getPlayerBase((EntityPlayer) var6.get(var7)) != null)
                {
                    Aether.getPlayerBase((EntityPlayer) var6.get(var7)).setInIce();
                }
            }
        }

        if (var5.nextInt(24) == 0)
        {
            var1.playSound((double) ((float) var2 + 0.5F), (double) ((float) var3 + 0.5F), (double) ((float) var4 + 0.5F), "fire.fire", 1.0F + var5.nextFloat(), var5.nextFloat() * 0.7F + 0.3F, false);
        }

        float var10;
        float var8;
        float var9;

        if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && !AetherBlocks.ColdFire.canBlockCatchFire(var1, var2, var3 - 1, var4, ForgeDirection.UP))
        {
            if (AetherBlocks.ColdFire.canBlockCatchFire(var1, var2 - 1, var3, var4, ForgeDirection.EAST))
            {
                for (var7 = 0; var7 < 2; ++var7)
                {
                    var8 = (float) var2 + var5.nextFloat() * 0.1F;
                    var9 = (float) var3 + var5.nextFloat();
                    var10 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(var1, var2 + 1, var3, var4, ForgeDirection.WEST))
            {
                for (var7 = 0; var7 < 2; ++var7)
                {
                    var8 = (float) (var2 + 1) - var5.nextFloat() * 0.1F;
                    var9 = (float) var3 + var5.nextFloat();
                    var10 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(var1, var2, var3, var4 - 1, ForgeDirection.SOUTH))
            {
                for (var7 = 0; var7 < 2; ++var7)
                {
                    var8 = (float) var2 + var5.nextFloat();
                    var9 = (float) var3 + var5.nextFloat();
                    var10 = (float) var4 + var5.nextFloat() * 0.1F;
                    var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(var1, var2, var3, var4 + 1, ForgeDirection.NORTH))
            {
                for (var7 = 0; var7 < 2; ++var7)
                {
                    var8 = (float) var2 + var5.nextFloat();
                    var9 = (float) var3 + var5.nextFloat();
                    var10 = (float) (var4 + 1) - var5.nextFloat() * 0.1F;
                    var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
                }
            }

            if (AetherBlocks.ColdFire.canBlockCatchFire(var1, var2, var3 + 1, var4, ForgeDirection.DOWN))
            {
                for (var7 = 0; var7 < 2; ++var7)
                {
                    var8 = (float) var2 + var5.nextFloat();
                    var9 = (float) (var3 + 1) - var5.nextFloat() * 0.1F;
                    var10 = (float) var4 + var5.nextFloat();
                    var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
                }
            }
        } else
        {
            for (var7 = 0; var7 < 3; ++var7)
            {
                var8 = (float) var2 + var5.nextFloat();
                var9 = (float) var3 + var5.nextFloat() * 0.5F + 0.5F;
                var10 = (float) var4 + var5.nextFloat();
                var1.spawnParticle("largesmoke", (double) var8, (double) var9, (double) var10, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.iconArray = new Icon[]{var1.registerIcon("Aether:coldfire_0"), var1.registerIcon("Aether:coldfire_1")};
    }

    @SideOnly(Side.CLIENT)
    public Icon func_94438_c(int var1)
    {
        return this.iconArray[var1];
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int var1, int var2)
    {
        return this.iconArray[0];
    }

    public boolean canBlockCatchFire(IBlockAccess var1, int var2, int var3, int var4, ForgeDirection var5)
    {
        Block var6 = Block.blocksList[var1.getBlockId(var2, var3, var4)];
        return var6 != null ? var6.isFlammable(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), var5) : false;
    }

    public int getChanceToEncourageFire(World var1, int var2, int var3, int var4, int var5, ForgeDirection var6)
    {
        int var7 = 0;
        Block var8 = Block.blocksList[var1.getBlockId(var2, var3, var4)];

        if (var8 != null)
        {
            var7 = var8.getFireSpreadSpeed(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), var6);
        }

        return var7 > var5 ? var7 : var5;
    }
}
