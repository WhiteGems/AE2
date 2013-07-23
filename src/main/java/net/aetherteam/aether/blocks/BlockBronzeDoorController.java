package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.tile_entities.TileEntityBronzeDoorController;
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

public class BlockBronzeDoorController extends BlockContainer
    implements IAetherBlock
{
    private Random rand;

    public static void updateEnchanterBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        world.setBlockMetadataWithNotify(i, j, k, l, 4);
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    protected BlockBronzeDoorController(int blockID)
    {
        super(blockID, Material.wood);
        this.rand = new Random();
        setHardness(-1.0F);
        setResistance(1000000.0F);
    }

    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return false;
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("Aether:Carved Stone");
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        try
        {
            return new TileEntityBronzeDoorController();
        }
        catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(i), MathHelper.floor_double(j), MathHelper.floor_double(k));

        if (dungeon != null)
        {
            super.onBlockAdded(world, i, j, k);
        }
        else
        {
            world.setBlock(i, j, k, 0);
        }
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entity, ItemStack stack)
    {
        world.setBlock(i, j, k, 0);
    }
}

