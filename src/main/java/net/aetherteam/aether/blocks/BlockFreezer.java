package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.tile_entities.TileEntityFreezer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFreezer extends BlockContainer implements IAetherBlock
{
    private Random FrozenRand = new Random();
    private Icon sideIcon;
    private Icon topIcon;

    public static void updateFreezerBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        world.setBlockMetadataWithNotify(i, j, k, l, 4);
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.topIcon = par1IconRegister.registerIcon("aether:Freezer Top");
        this.sideIcon = par1IconRegister.registerIcon("aether:Freezer Side");
        super.registerIcons(par1IconRegister);
    }

    protected BlockFreezer(int blockID)
    {
        super(blockID, Material.rock);
        this.setHardness(2.5F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        TileEntityFreezer freezer = (TileEntityFreezer)world.getBlockTileEntity(x, y, z);
        int guiID = AetherGuiHandler.freezerID;
        entityplayer.openGui(Aether.instance, guiID, world, x, y, z);
        return true;
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        try
        {
            return new TileEntityFreezer();
        }
        catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        return i == 1 ? this.topIcon : (i == 0 ? this.topIcon : this.sideIcon);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        this.setDefaultDirection(world, i, j, k);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack)
    {
        int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2, 4);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5, 4);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3, 4);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4, 4);
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityFreezer var7 = (TileEntityFreezer)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.FrozenRand.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.FrozenRand.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.FrozenRand.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.FrozenRand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.FrozenRand.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.FrozenRand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.FrozenRand.nextGaussian() * var15);
                        par1World.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityFreezer tileentity = (TileEntityFreezer)world.getBlockTileEntity(i, j, k);

        if (tileentity.isBurning())
        {
            for (int multiply = 0; multiply < 5; ++multiply)
            {
                double x = (double)i + random.nextDouble();
                double y = (double)j + 1.0D + random.nextDouble() * 0.75D * 2.0D - 0.75D;
                double z = (double)k + random.nextDouble();
                Aether.proxy.spawnCloudSmoke(world, (double)i + 0.5D, (double)j + 1.0D, (double)k + 0.5D, random, Double.valueOf(0.75D));
            }
        }
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if (!world.isRemote)
        {
            int l = world.getBlockId(i, j, k - 1);
            int i1 = world.getBlockId(i, j, k + 1);
            int j1 = world.getBlockId(i - 1, j, k);
            int k1 = world.getBlockId(i + 1, j, k);
            byte byte0 = 3;

            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
            {
                byte0 = 3;
            }

            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
            {
                byte0 = 2;
            }

            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
            {
                byte0 = 5;
            }

            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
            {
                byte0 = 4;
            }

            world.setBlockMetadataWithNotify(i, j, k, byte0, 4);
        }
    }
}
