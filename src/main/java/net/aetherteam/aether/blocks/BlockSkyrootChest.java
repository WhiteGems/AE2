package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.tile_entities.TileEntitySkyrootChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.AABBPool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockSkyrootChest extends BlockContainer
{
    private final Random random = new Random();
    public final int field_94443_a;

    protected BlockSkyrootChest(int par1, int par2)
    {
        super(par1, Material.wood);
        this.field_94443_a = par2;
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
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
        return AetherBlocks.skyrootChestRenderId;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == this.blockID)
        {
            setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == this.blockID)
        {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        }
        else if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == this.blockID)
        {
            setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == this.blockID)
        {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        }
        else
        {
            setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        unifyAdjacentChests(par1World, par2, par3, par4);
        int l = par1World.getBlockId(par2, par3, par4 - 1);
        int i1 = par1World.getBlockId(par2, par3, par4 + 1);
        int j1 = par1World.getBlockId(par2 - 1, par3, par4);
        int k1 = par1World.getBlockId(par2 + 1, par3, par4);

        if (l == this.blockID)
        {
            unifyAdjacentChests(par1World, par2, par3, par4 - 1);
        }

        if (i1 == this.blockID)
        {
            unifyAdjacentChests(par1World, par2, par3, par4 + 1);
        }

        if (j1 == this.blockID)
        {
            unifyAdjacentChests(par1World, par2 - 1, par3, par4);
        }

        if (k1 == this.blockID)
        {
            unifyAdjacentChests(par1World, par2 + 1, par3, par4);
        }
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        int l = par1World.getBlockId(par2, par3, par4 - 1);
        int i1 = par1World.getBlockId(par2, par3, par4 + 1);
        int j1 = par1World.getBlockId(par2 - 1, par3, par4);
        int k1 = par1World.getBlockId(par2 + 1, par3, par4);
        byte b0 = 0;
        int l1 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;

        if (l1 == 0)
        {
            b0 = 2;
        }

        if (l1 == 1)
        {
            b0 = 5;
        }

        if (l1 == 2)
        {
            b0 = 3;
        }

        if (l1 == 3)
        {
            b0 = 4;
        }

        if ((l != this.blockID) && (i1 != this.blockID) && (j1 != this.blockID) && (k1 != this.blockID))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
        }
        else
        {
            if (((l == this.blockID) || (i1 == this.blockID)) && ((b0 == 4) || (b0 == 5)))
            {
                if (l == this.blockID)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4 - 1, b0, 3);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4 + 1, b0, 3);
                }

                par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
            }

            if (((j1 == this.blockID) || (k1 == this.blockID)) && ((b0 == 2) || (b0 == 3)))
            {
                if (j1 == this.blockID)
                {
                    par1World.setBlockMetadataWithNotify(par2 - 1, par3, par4, b0, 3);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2 + 1, par3, par4, b0, 3);
                }

                par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
            }
        }
    }

    public void unifyAdjacentChests(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockId(par2, par3, par4 - 1);
            int i1 = par1World.getBlockId(par2, par3, par4 + 1);
            int j1 = par1World.getBlockId(par2 - 1, par3, par4);
            int k1 = par1World.getBlockId(par2 + 1, par3, par4);
            boolean flag = true;
            byte b0;

            if ((l != this.blockID) && (i1 != this.blockID))
            {
                if ((j1 != this.blockID) && (k1 != this.blockID))
                {
                    byte b0 = 3;

                    if ((Block.opaqueCubeLookup[l] != 0) && (Block.opaqueCubeLookup[i1] == 0))
                    {
                        b0 = 3;
                    }

                    if ((Block.opaqueCubeLookup[i1] != 0) && (Block.opaqueCubeLookup[l] == 0))
                    {
                        b0 = 2;
                    }

                    if ((Block.opaqueCubeLookup[j1] != 0) && (Block.opaqueCubeLookup[k1] == 0))
                    {
                        b0 = 5;
                    }

                    if ((Block.opaqueCubeLookup[k1] != 0) && (Block.opaqueCubeLookup[j1] == 0))
                    {
                        b0 = 4;
                    }
                }
                else
                {
                    int l1 = par1World.getBlockId(j1 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 - 1);
                    int i2 = par1World.getBlockId(j1 == this.blockID ? par2 - 1 : par2 + 1, par3, par4 + 1);
                    byte b0 = 3;
                    boolean flag1 = true;
                    int j2;
                    int j2;

                    if (j1 == this.blockID)
                    {
                        j2 = par1World.getBlockMetadata(par2 - 1, par3, par4);
                    }
                    else
                    {
                        j2 = par1World.getBlockMetadata(par2 + 1, par3, par4);
                    }

                    if (j2 == 2)
                    {
                        b0 = 2;
                    }

                    if (((Block.opaqueCubeLookup[l] != 0) || (Block.opaqueCubeLookup[l1] != 0)) && (Block.opaqueCubeLookup[i1] == 0) && (Block.opaqueCubeLookup[i2] == 0))
                    {
                        b0 = 3;
                    }

                    if (((Block.opaqueCubeLookup[i1] != 0) || (Block.opaqueCubeLookup[i2] != 0)) && (Block.opaqueCubeLookup[l] == 0) && (Block.opaqueCubeLookup[l1] == 0))
                    {
                        b0 = 2;
                    }
                }
            }
            else
            {
                int l1 = par1World.getBlockId(par2 - 1, par3, l == this.blockID ? par4 - 1 : par4 + 1);
                int i2 = par1World.getBlockId(par2 + 1, par3, l == this.blockID ? par4 - 1 : par4 + 1);
                b0 = 5;
                boolean flag1 = true;
                int j2;
                int j2;

                if (l == this.blockID)
                {
                    j2 = par1World.getBlockMetadata(par2, par3, par4 - 1);
                }
                else
                {
                    j2 = par1World.getBlockMetadata(par2, par3, par4 + 1);
                }

                if (j2 == 4)
                {
                    b0 = 4;
                }

                if (((Block.opaqueCubeLookup[j1] != 0) || (Block.opaqueCubeLookup[l1] != 0)) && (Block.opaqueCubeLookup[k1] == 0) && (Block.opaqueCubeLookup[i2] == 0))
                {
                    b0 = 5;
                }

                if (((Block.opaqueCubeLookup[k1] != 0) || (Block.opaqueCubeLookup[i2] != 0)) && (Block.opaqueCubeLookup[j1] == 0) && (Block.opaqueCubeLookup[l1] == 0))
                {
                    b0 = 4;
                }
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 3);
        }
    }

    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        int l = 0;

        if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID)
        {
            l++;
        }

        if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID)
        {
            l++;
        }

        if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID)
        {
            l++;
        }

        if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID)
        {
            l++;
        }

        return l <= 1;
    }

    private boolean isThereANeighborChest(World par1World, int par2, int par3, int par4)
    {
        return par1World.getBlockId(par2, par3, par4) == this.blockID;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        TileEntitySkyrootChest TileEntitySkyrootChest = (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (TileEntitySkyrootChest != null)
        {
            TileEntitySkyrootChest.updateContainingBlockInfo();
        }
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntitySkyrootChest TileEntitySkyrootChest = (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (TileEntitySkyrootChest != null)
        {
            for (int j1 = 0; j1 < TileEntitySkyrootChest.getSizeInventory(); j1++)
            {
                ItemStack itemstack = TileEntitySkyrootChest.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem))
                    {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = ((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = ((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = ((float)this.random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }

        IInventory iinventory = func_94442_h_(par1World, par2, par3, par4);

        if (iinventory != null)
        {
            par5EntityPlayer.displayGUIChest(iinventory);
        }

        return true;
    }

    public IInventory func_94442_h_(World par1World, int par2, int par3, int par4)
    {
        Object object = (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (object == null)
        {
            return null;
        }

        if (par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN))
        {
            return null;
        }

        if (isOcelotBlockingChest(par1World, par2, par3, par4))
        {
            return null;
        }

        if ((par1World.getBlockId(par2 - 1, par3, par4) == this.blockID) && ((par1World.isBlockSolidOnSide(par2 - 1, par3 + 1, par4, ForgeDirection.DOWN)) || (isOcelotBlockingChest(par1World, par2 - 1, par3, par4))))
        {
            return null;
        }

        if ((par1World.getBlockId(par2 + 1, par3, par4) == this.blockID) && ((par1World.isBlockSolidOnSide(par2 + 1, par3 + 1, par4, ForgeDirection.DOWN)) || (isOcelotBlockingChest(par1World, par2 + 1, par3, par4))))
        {
            return null;
        }

        if ((par1World.getBlockId(par2, par3, par4 - 1) == this.blockID) && ((par1World.isBlockSolidOnSide(par2, par3 + 1, par4 - 1, ForgeDirection.DOWN)) || (isOcelotBlockingChest(par1World, par2, par3, par4 - 1))))
        {
            return null;
        }

        if ((par1World.getBlockId(par2, par3, par4 + 1) == this.blockID) && ((par1World.isBlockSolidOnSide(par2, par3 + 1, par4 + 1, ForgeDirection.DOWN)) || (isOcelotBlockingChest(par1World, par2, par3, par4 + 1))))
        {
            return null;
        }

        if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID)
        {
            object = new InventoryLargeChest("container.chestDouble", (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2 - 1, par3, par4), (IInventory)object);
        }

        if (par1World.getBlockId(par2 + 1, par3, par4) == this.blockID)
        {
            object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2 + 1, par3, par4));
        }

        if (par1World.getBlockId(par2, par3, par4 - 1) == this.blockID)
        {
            object = new InventoryLargeChest("container.chestDouble", (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2, par3, par4 - 1), (IInventory)object);
        }

        if (par1World.getBlockId(par2, par3, par4 + 1) == this.blockID)
        {
            object = new InventoryLargeChest("container.chestDouble", (IInventory)object, (TileEntitySkyrootChest)par1World.getBlockTileEntity(par2, par3, par4 + 1));
        }

        return (IInventory)object;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("Aether:Skyroot Plank");
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        TileEntitySkyrootChest TileEntitySkyrootChest = new TileEntitySkyrootChest();
        return TileEntitySkyrootChest;
    }

    public boolean canProvidePower()
    {
        return this.field_94443_a == 1;
    }

    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!canProvidePower())
        {
            return 0;
        }

        int i1 = ((TileEntitySkyrootChest)par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).numUsingPlayers;
        return MathHelper.clamp_int(i1, 0, 15);
    }

    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par5 == 1 ? isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5) : 0;
    }

    public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3)
    {
        Iterator iterator = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().getAABB(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1)).iterator();
        EntityOcelot entityocelot;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            EntityOcelot entityocelot1 = (EntityOcelot)iterator.next();
            entityocelot = entityocelot1;
        }
        while (!entityocelot.isSitting());

        return true;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.func_94526_b(func_94442_h_(par1World, par2, par3, par4));
    }
}

