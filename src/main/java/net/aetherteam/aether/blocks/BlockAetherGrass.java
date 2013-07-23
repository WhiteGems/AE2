package net.aetherteam.aether.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityGoldenFX;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAetherGrass extends BlockAether
    implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprGoldTop;
    public static Icon sprGoldSide;

    protected BlockAetherGrass(int blockID)
    {
        super(blockID, Material.ground);
        setTickRandomly(true);
        setHardness(0.6F);
        setStepSound(Block.soundGrassFootstep);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int meta = 0; meta < 1; meta++)
        {
            par3List.add(new ItemStack(par1, 1, meta));
        }
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            if ((entityplayer.cd() != null) && (entityplayer.cd().itemID == AetherItems.SkyrootShovel.itemID))
            {
                entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
                ItemStack stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
            else
            {
                ItemStack stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        if (entityPlayer == null)
        {
            return false;
        }

        ItemStack itemStack = entityPlayer.cd();

        if (itemStack == null)
        {
            return false;
        }

        if ((itemStack.itemID == AetherItems.AmbrosiumShard.itemID) && (world.getBlockMetadata(x, y, z) == 0))
        {
            if (!world.isRemote)
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 2);
                itemStack.stackSize -= 1;
                return true;
            }
        }

        return false;
    }

    public Icon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            if (meta == 0)
            {
                return sprTop;
            }

            if (meta == 1)
            {
                return sprGoldTop;
            }
        }

        if (side == 0)
        {
            return AetherBlocks.AetherDirt.getIcon(side, meta);
        }

        if (meta == 1)
        {
            return sprGoldSide;
        }

        return sprSide;
    }

    public int idDropped(int i, Random random, int meta)
    {
        return AetherBlocks.AetherDirt.idDropped(0, random, meta);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        super.randomDisplayTick(world, x, y, z, random);

        if ((random.nextInt(4) == 0) && (world.getBlockMetadata(x, y, z) == 1))
        {
            if (world.isRemote)
            {
                for (int l = 0; l < 6; l++)
                {
                    EntityNoteFX obj = new EntityGoldenFX(world, x + random.nextFloat(), y + 1.1F, z + random.nextFloat(), 0.0D, 0.0D, 0.0D, true);
                    FMLClientHandler.instance().getClient().effectRenderer.a(obj);
                }
            }
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (world.isRemote)
        {
            return;
        }

        if ((world.getBlockLightValue(x, y + 1, z) < 4) && (world.getBlockMaterial(x, y + 1, z).getCanBlockGrass()))
        {
            if ((random.nextInt(4) != 0) || ((Block.blocksList[world.getBlockId(x, y + 1, z)] instanceof BlockWall)))
            {
                return;
            }

            world.setBlock(x, y, z, AetherBlocks.AetherDirt.blockID);
        }
        else if (world.getBlockLightValue(x, y + 1, z) >= 9)
        {
            int l = x + random.nextInt(3) - 1;
            int i1 = y + random.nextInt(5) - 3;
            int j1 = z + random.nextInt(3) - 1;

            if ((world.getBlockId(l, i1, j1) == AetherBlocks.AetherDirt.blockID) && (world.getBlockLightValue(l, i1 + 1, j1) >= 4) && (!world.getBlockMaterial(l, i1 + 1, j1).getCanBlockGrass()))
            {
                world.setBlock(l, i1, j1, AetherBlocks.AetherGrass.blockID, 0, 2);
            }
        }
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("Aether:Aether Grass Top");
        sprSide = par1IconRegister.registerIcon("Aether:Aether Grass Side");
        sprGoldTop = par1IconRegister.registerIcon("Aether:Enchanted Aether Grass Top");
        sprGoldSide = par1IconRegister.registerIcon("Aether:Enchanted Aether Grass Side");
        super.registerIcons(par1IconRegister);
    }
}

