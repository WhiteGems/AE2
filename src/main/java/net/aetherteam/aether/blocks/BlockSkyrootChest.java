package net.aetherteam.aether.blocks;

import java.util.Iterator;
import java.util.Random;

import net.aetherteam.aether.tile_entities.TileEntitySkyrootChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockSkyrootChest extends BlockContainer
{
    private final Random random = new Random();

    /** Determines whether of not the chest is trapped. */
    public final int isTrapped;

    protected BlockSkyrootChest(int var1, int var2)
    {
        super(var1, Material.wood);
        this.isTrapped = var2;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
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
        return AetherBlocks.skyrootChestRenderId;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4)
    {
        if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        } else if (var1.getBlockId(var2, var3, var4 + 1) == this.blockID)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        } else if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        } else if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        } else
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        super.onBlockAdded(var1, var2, var3, var4);
        this.unifyAdjacentChests(var1, var2, var3, var4);
        int var5 = var1.getBlockId(var2, var3, var4 - 1);
        int var6 = var1.getBlockId(var2, var3, var4 + 1);
        int var7 = var1.getBlockId(var2 - 1, var3, var4);
        int var8 = var1.getBlockId(var2 + 1, var3, var4);

        if (var5 == this.blockID)
        {
            this.unifyAdjacentChests(var1, var2, var3, var4 - 1);
        }

        if (var6 == this.blockID)
        {
            this.unifyAdjacentChests(var1, var2, var3, var4 + 1);
        }

        if (var7 == this.blockID)
        {
            this.unifyAdjacentChests(var1, var2 - 1, var3, var4);
        }

        if (var8 == this.blockID)
        {
            this.unifyAdjacentChests(var1, var2 + 1, var3, var4);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        int var7 = var1.getBlockId(var2, var3, var4 - 1);
        int var8 = var1.getBlockId(var2, var3, var4 + 1);
        int var9 = var1.getBlockId(var2 - 1, var3, var4);
        int var10 = var1.getBlockId(var2 + 1, var3, var4);
        byte var11 = 0;
        int var12 = MathHelper.floor_double((double) (var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var12 == 0)
        {
            var11 = 2;
        }

        if (var12 == 1)
        {
            var11 = 5;
        }

        if (var12 == 2)
        {
            var11 = 3;
        }

        if (var12 == 3)
        {
            var11 = 4;
        }

        if (var7 != this.blockID && var8 != this.blockID && var9 != this.blockID && var10 != this.blockID)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, var11, 3);
        } else
        {
            if ((var7 == this.blockID || var8 == this.blockID) && (var11 == 4 || var11 == 5))
            {
                if (var7 == this.blockID)
                {
                    var1.setBlockMetadataWithNotify(var2, var3, var4 - 1, var11, 3);
                } else
                {
                    var1.setBlockMetadataWithNotify(var2, var3, var4 + 1, var11, 3);
                }

                var1.setBlockMetadataWithNotify(var2, var3, var4, var11, 3);
            }

            if ((var9 == this.blockID || var10 == this.blockID) && (var11 == 2 || var11 == 3))
            {
                if (var9 == this.blockID)
                {
                    var1.setBlockMetadataWithNotify(var2 - 1, var3, var4, var11, 3);
                } else
                {
                    var1.setBlockMetadataWithNotify(var2 + 1, var3, var4, var11, 3);
                }

                var1.setBlockMetadataWithNotify(var2, var3, var4, var11, 3);
            }
        }
    }

    public void unifyAdjacentChests(World var1, int var2, int var3, int var4)
    {
        if (!var1.isRemote)
        {
            int var5 = var1.getBlockId(var2, var3, var4 - 1);
            int var6 = var1.getBlockId(var2, var3, var4 + 1);
            int var7 = var1.getBlockId(var2 - 1, var3, var4);
            int var8 = var1.getBlockId(var2 + 1, var3, var4);
            boolean var9 = true;
            int var10;
            int var11;
            int var14;
            boolean var12;
            byte var13;

            if (var5 != this.blockID && var6 != this.blockID)
            {
                if (var7 != this.blockID && var8 != this.blockID)
                {
                    var13 = 3;

                    if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
                    {
                        var13 = 3;
                    }

                    if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
                    {
                        var13 = 2;
                    }

                    if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
                    {
                        var13 = 5;
                    }

                    if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
                    {
                        var13 = 4;
                    }
                } else
                {
                    var10 = var1.getBlockId(var7 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 - 1);
                    var11 = var1.getBlockId(var7 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 + 1);
                    var13 = 3;
                    var12 = true;

                    if (var7 == this.blockID)
                    {
                        var14 = var1.getBlockMetadata(var2 - 1, var3, var4);
                    } else
                    {
                        var14 = var1.getBlockMetadata(var2 + 1, var3, var4);
                    }

                    if (var14 == 2)
                    {
                        var13 = 2;
                    }

                    if ((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11])
                    {
                        var13 = 3;
                    }

                    if ((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var10])
                    {
                        var13 = 2;
                    }
                }
            } else
            {
                var10 = var1.getBlockId(var2 - 1, var3, var5 == this.blockID ? var4 - 1 : var4 + 1);
                var11 = var1.getBlockId(var2 + 1, var3, var5 == this.blockID ? var4 - 1 : var4 + 1);
                var13 = 5;
                var12 = true;

                if (var5 == this.blockID)
                {
                    var14 = var1.getBlockMetadata(var2, var3, var4 - 1);
                } else
                {
                    var14 = var1.getBlockMetadata(var2, var3, var4 + 1);
                }

                if (var14 == 4)
                {
                    var13 = 4;
                }

                if ((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11])
                {
                    var13 = 5;
                }

                if ((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var10])
                {
                    var13 = 4;
                }
            }

            var1.setBlockMetadataWithNotify(var2, var3, var4, var13, 3);
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        int var5 = 0;

        if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID)
        {
            ++var5;
        }

        if (var1.getBlockId(var2, var3, var4 + 1) == this.blockID)
        {
            ++var5;
        }

        return var5 > 1 ? false : (this.isThereANeighborChest(var1, var2 - 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2 + 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2, var3, var4 - 1) ? false : !this.isThereANeighborChest(var1, var2, var3, var4 + 1))));
    }

    private boolean isThereANeighborChest(World var1, int var2, int var3, int var4)
    {
        return var1.getBlockId(var2, var3, var4) != this.blockID ? false : (var1.getBlockId(var2 - 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2 + 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2, var3, var4 - 1) == this.blockID ? true : var1.getBlockId(var2, var3, var4 + 1) == this.blockID)));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        super.onNeighborBlockChange(var1, var2, var3, var4, var5);
        TileEntitySkyrootChest var6 = (TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4);

        if (var6 != null)
        {
            var6.updateContainingBlockInfo();
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        TileEntitySkyrootChest var7 = (TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.random.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem var12;

                    for (float var13 = this.random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; var1.spawnEntityInWorld(var12))
                    {
                        int var14 = this.random.nextInt(21) + 10;

                        if (var14 > var9.stackSize)
                        {
                            var14 = var9.stackSize;
                        }

                        var9.stackSize -= var14;
                        var12 = new EntityItem(var1, (double) ((float) var2 + var10), (double) ((float) var3 + var11), (double) ((float) var4 + var13), new ItemStack(var9.itemID, var14, var9.getItemDamage()));
                        float var15 = 0.05F;
                        var12.motionX = (double) ((float) this.random.nextGaussian() * var15);
                        var12.motionY = (double) ((float) this.random.nextGaussian() * var15 + 0.2F);
                        var12.motionZ = (double) ((float) this.random.nextGaussian() * var15);

                        if (var9.hasTagCompound())
                        {
                            var12.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
                        }
                    }
                }
            }

            var1.func_96440_m(var2, var3, var4, var5);
        }

        super.breakBlock(var1, var2, var3, var4, var5, var6);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var1.isRemote)
        {
            return true;
        } else
        {
            IInventory var10 = this.getInventory(var1, var2, var3, var4);

            if (var10 != null)
            {
                var5.displayGUIChest(var10);
            }

            return true;
        }
    }

    /**
     * Gets the inventory of the chest at the specified coords, accounting for blocks or ocelots on top of the chest,
     * and double chests.
     */
    public IInventory getInventory(World var1, int var2, int var3, int var4)
    {
        Object var5 = (TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4);

        if (var5 == null)
        {
            return null;
        } else if (var1.isBlockSolidOnSide(var2, var3 + 1, var4, ForgeDirection.DOWN))
        {
            return null;
        } else if (isOcelotBlockingChest(var1, var2, var3, var4))
        {
            return null;
        } else if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID && (var1.isBlockSolidOnSide(var2 - 1, var3 + 1, var4, ForgeDirection.DOWN) || isOcelotBlockingChest(var1, var2 - 1, var3, var4)))
        {
            return null;
        } else if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID && (var1.isBlockSolidOnSide(var2 + 1, var3 + 1, var4, ForgeDirection.DOWN) || isOcelotBlockingChest(var1, var2 + 1, var3, var4)))
        {
            return null;
        } else if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID && (var1.isBlockSolidOnSide(var2, var3 + 1, var4 - 1, ForgeDirection.DOWN) || isOcelotBlockingChest(var1, var2, var3, var4 - 1)))
        {
            return null;
        } else if (var1.getBlockId(var2, var3, var4 + 1) == this.blockID && (var1.isBlockSolidOnSide(var2, var3 + 1, var4 + 1, ForgeDirection.DOWN) || isOcelotBlockingChest(var1, var2, var3, var4 + 1)))
        {
            return null;
        } else
        {
            if (var1.getBlockId(var2 - 1, var3, var4) == this.blockID)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (TileEntitySkyrootChest) var1.getBlockTileEntity(var2 - 1, var3, var4), (IInventory) var5);
            }

            if (var1.getBlockId(var2 + 1, var3, var4) == this.blockID)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (IInventory) var5, (TileEntitySkyrootChest) var1.getBlockTileEntity(var2 + 1, var3, var4));
            }

            if (var1.getBlockId(var2, var3, var4 - 1) == this.blockID)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4 - 1), (IInventory) var5);
            }

            if (var1.getBlockId(var2, var3, var4 + 1) == this.blockID)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (IInventory) var5, (TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4 + 1));
            }

            return (IInventory) var5;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World var1)
    {
        TileEntitySkyrootChest var2 = new TileEntitySkyrootChest();
        return var2;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return this.isTrapped == 1;
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        if (!this.canProvidePower())
        {
            return 0;
        } else
        {
            int var6 = ((TileEntitySkyrootChest) var1.getBlockTileEntity(var2, var3, var4)).numUsingPlayers;
            return MathHelper.clamp_int(var6, 0, 15);
        }
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return var5 == 1 ? this.isProvidingWeakPower(var1, var2, var3, var4, var5) : 0;
    }

    public static boolean isOcelotBlockingChest(World var0, int var1, int var2, int var3)
    {
        Iterator var4 = var0.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB((double) var1, (double) (var2 + 1), (double) var3, (double) (var1 + 1), (double) (var2 + 2), (double) (var3 + 1))).iterator();

        while (var4.hasNext())
        {
            EntityOcelot var6 = (EntityOcelot) var4.next();

            if (var6.isSitting())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World var1, int var2, int var3, int var4, int var5)
    {
        return Container.calcRedstoneFromInventory(this.getInventory(var1, var2, var3, var4));
    }
}
