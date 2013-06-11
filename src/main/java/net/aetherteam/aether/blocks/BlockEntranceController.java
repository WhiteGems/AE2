package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEntranceController extends BlockContainer implements IAetherBlock
{
    private Random rand = new Random();

    public static void updateControllerBlockState(boolean var0, World var1, int var2, int var3, int var4)
    {
        int var5 = var1.getBlockMetadata(var2, var3, var4);
        TileEntity var6 = var1.getBlockTileEntity(var2, var3, var4);
        var1.setBlockMetadataWithNotify(var2, var3, var4, var5, 4);
        var1.setBlockTileEntity(var2, var3, var4, var6);
    }

    protected BlockEntranceController(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(-1.0F);
        this.setResistance(1000000.0F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        return false;
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return AetherBlocks.entranceRenderId;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.blockIcon = var1.registerIcon("Aether:Carved Stone");
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
            return new TileEntityEntranceController();
        } catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        Dungeon var5 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double) var2), MathHelper.floor_double((double) var3), MathHelper.floor_double((double) var4));

        if (var5 != null)
        {
            super.onBlockAdded(var1, var2, var3, var4);
        } else
        {
            var1.setBlock(var2, var3, var4, 0);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        var1.setBlock(var2, var3, var4, 0);
    }
}
