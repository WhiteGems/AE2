package net.aetherteam.aether.blocks;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class BlockTallAetherGrass extends BlockAetherFlower
    implements IShearable, IAetherBlock
{
    protected BlockTallAetherGrass(int par1)
    {
        super(par1, Material.vine);
        float var3 = 0.4F;
        setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
    }

    public Icon getIcon(int par1, int par2)
    {
        return this.blockIcon;
    }

    public int idDropped(int par1, Random random, int par3)
    {
        return -1;
    }

    public int quantityDroppedWithBonus(int par1, Random random)
    {
        return 1 + random.nextInt(par1 * 2 + 1);
    }

    public void harvestBlock(World world, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        super.harvestBlock(world, par2EntityPlayer, par3, par4, par5, par6);
    }

    public int getRenderType()
    {
        return AetherBlocks.tallAetherGrassRenderId;
    }

    public int getDamageValue(World world, int par2, int par3, int par4)
    {
        return world.getBlockMetadata(par2, par3, par4);
    }

    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
        return true;
    }

    public boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        return true;
    }

    public ArrayList onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
        ArrayList ret = new ArrayList();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
        return ret;
    }
}

