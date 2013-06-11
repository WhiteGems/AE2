package net.aetherteam.aether.blocks;

import java.util.Random;

import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.tile_entities.TileEntityFreezer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
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

    public static void updateFreezerBlockState(boolean var0, World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        TileEntity var6 = var1.getBlockTileEntity(var2, var3, var4);
        var1.setBlockMetadataWithNotify(var2, var3, var4, var5, 4);
        var1.setBlockTileEntity(var2, var3, var4, var6);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.topIcon = var1.registerIcon("Aether:Freezer Top");
        this.sideIcon = var1.registerIcon("Aether:Freezer Side");
        super.registerIcons(var1);
    }

    protected BlockFreezer(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(2.5F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        TileEntityFreezer var10 = (TileEntityFreezer) var1.getBlockTileEntity(var2, var3, var4);
        int var11 = AetherGuiHandler.freezerID;
        var5.openGui(Aether.instance, var11, var1, var2, var3, var4);
        return true;
    }

    public boolean hasTileEntity(int var1)
    {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World var1)
    {
        try
        {
            return new TileEntityFreezer();
        } catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return var1 == 1 ? this.topIcon : (var1 == 0 ? this.topIcon : this.sideIcon);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        super.onBlockAdded(var1, var2, var3, var4);
        this.setDefaultDirection(var1, var2, var3, var4);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        int var7 = MathHelper.floor_double((double) (var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var7 == 0)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 2, 4);
        }

        if (var7 == 1)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5, 4);
        }

        if (var7 == 2)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 3, 4);
        }

        if (var7 == 3)
        {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 4, 4);
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        TileEntityFreezer var7 = (TileEntityFreezer) var1.getBlockTileEntity(var2, var3, var4);

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
                        EntityItem var14 = new EntityItem(var1, (double) ((float) var2 + var10), (double) ((float) var3 + var11), (double) ((float) var4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double) ((float) this.FrozenRand.nextGaussian() * var15);
                        var14.motionY = (double) ((float) this.FrozenRand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double) ((float) this.FrozenRand.nextGaussian() * var15);
                        var1.spawnEntityInWorld(var14);
                    }
                }
            }
        }

        super.breakBlock(var1, var2, var3, var4, var5, var6);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        TileEntityFreezer var6 = (TileEntityFreezer) var1.getBlockTileEntity(var2, var3, var4);

        if (var6.isBurning())
        {
            for (int var7 = 0; var7 < 5; ++var7)
            {
                double var8 = (double) var2 + var5.nextDouble();
                double var10 = (double) var3 + 1.0D + var5.nextDouble() * 0.75D * 2.0D - 0.75D;
                double var12 = (double) var4 + var5.nextDouble();
                Aether.proxy.spawnCloudSmoke(var1, (double) var2 + 0.5D, (double) var3 + 1.0D, (double) var4 + 0.5D, var5, Double.valueOf(0.75D));
            }
        }
    }

    private void setDefaultDirection(World var1, int var2, int var3, int var4)
    {
        if (!var1.isRemote)
        {
            int var5 = var1.getBlockId(var2, var3, var4 - 1);
            int var6 = var1.getBlockId(var2, var3, var4 + 1);
            int var7 = var1.getBlockId(var2 - 1, var3, var4);
            int var8 = var1.getBlockId(var2 + 1, var3, var4);
            byte var9 = 3;

            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
            {
                var9 = 3;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
            {
                var9 = 2;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
            {
                var9 = 5;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
            {
                var9 = 4;
            }

            var1.setBlockMetadataWithNotify(var2, var3, var4, var9, 4);
        }
    }
}
