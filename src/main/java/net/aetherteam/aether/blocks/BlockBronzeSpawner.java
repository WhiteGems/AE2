package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.aetherteam.aether.tile_entities.TileEntityBronzeSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBronzeSpawner extends BlockContainer
{
    protected BlockBronzeSpawner(int par1)
    {
        super(par1, Material.rock);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityBronzeSpawner();
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);
        int j1 = 15 + par1World.rand.nextInt(15) + par1World.rand.nextInt(15);
        dropXpOnBlockBreak(par1World, par2, par3, par4, j1);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
}

