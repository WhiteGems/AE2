package net.aetherteam.aether.blocks;

import java.util.ArrayList;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.tile_entities.TileEntityIncubator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockIncubator extends BlockContainer
    implements IAetherBlock
{
    private Random IncubatorRand;
    private Icon sideIcon;
    private Icon topIcon;

    public static void updateIncubatorBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        world.setBlockMetadataWithNotify(i, j, k, l, 4);
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        this.topIcon = par1IconRegister.registerIcon("Aether:Incubator Top");
        this.sideIcon = par1IconRegister.registerIcon("Aether:Incubator Side");
        super.registerIcons(par1IconRegister);
    }

    protected BlockIncubator(int blockID)
    {
        super(blockID, Material.rock);
        this.IncubatorRand = new Random();
        setHardness(2.0F);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        TileEntityIncubator incubator = (TileEntityIncubator)world.getBlockTileEntity(x, y, z);
        int guiID = AetherGuiHandler.incubatorID;
        entityplayer.openGui(Aether.instance, guiID, world, x, y, z);
        return true;
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        try
        {
            return new TileEntityIncubator();
        }
        catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    public Icon getIcon(int i, int meta)
    {
        if (i == 1)
        {
            return this.topIcon;
        }

        if (i == 0)
        {
            return this.topIcon;
        }

        return this.sideIcon;
    }

    public void addCreativeBlocks(ArrayList itemList)
    {
        itemList.add(new ItemStack(this));
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack stack)
    {
        int l = MathHelper.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;

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

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityIncubator var7 = (TileEntityIncubator)par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); var8++)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.IncubatorRand.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.IncubatorRand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = this.IncubatorRand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
                    {
                        int var13 = this.IncubatorRand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = ((float)this.IncubatorRand.nextGaussian() * var15);
                        var14.motionY = ((float)this.IncubatorRand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = ((float)this.IncubatorRand.nextGaussian() * var15);

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        if (world.isRemote)
        {
            return;
        }

        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;

        if ((Block.opaqueCubeLookup[l] != 0) && (Block.opaqueCubeLookup[i1] == 0))
        {
            byte0 = 3;
        }

        if ((Block.opaqueCubeLookup[i1] != 0) && (Block.opaqueCubeLookup[l] == 0))
        {
            byte0 = 2;
        }

        if ((Block.opaqueCubeLookup[j1] != 0) && (Block.opaqueCubeLookup[k1] == 0))
        {
            byte0 = 5;
        }

        if ((Block.opaqueCubeLookup[k1] != 0) && (Block.opaqueCubeLookup[j1] == 0))
        {
            byte0 = 4;
        }

        world.setBlockMetadataWithNotify(i, j, k, byte0, 4);
    }
}

