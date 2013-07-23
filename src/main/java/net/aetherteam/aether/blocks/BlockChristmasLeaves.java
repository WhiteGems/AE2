package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChristmasLeaves extends BlockLeaves
    implements IAetherBlock
{
    private HashMap icons = new HashMap();
    public static final String[] names = { "Christmas Leaves", "Decorative Leaves" };

    public BlockChristmasLeaves(int blockID)
    {
        super(blockID);
        setTickRandomly(true);
        setHardness(0.2F);
        setStepSound(Block.soundGrassFootstep);
        setLightOpacity(1);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < names.length; i++)
        {
            this.icons.put(names[i], iconRegister.registerIcon("Aether:" + names[i]));
            this.icons.put(names[i] + "_Opaque", iconRegister.registerIcon("Aether:" + names[i] + "_Opaque"));
        }
    }

    public int getRenderColor(int i)
    {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 16777215;
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        ItemStack stack = new ItemStack(AetherBlocks.ChristmasLeaves, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name + "_Opaque");
    }

    public int idDropped(int i, Random random, int j)
    {
        return 0;
    }

    private boolean isMyTrunkMeta(int meta)
    {
        if (this.blockID == AetherBlocks.ChristmasLeaves.blockID)
        {
            return meta <= 1;
        }

        return meta >= 2;
    }

    private boolean nearTrunk(World world, int px, int py, int pz)
    {
        Loc startLoc = new Loc(px, py, pz);
        LinkedList toCheck = new LinkedList();
        ArrayList checked = new ArrayList();
        toCheck.offer(new Loc(px, py, pz));
        int bLeaves = this.blockID;

        while (!toCheck.isEmpty())
        {
            Loc curLoc = (Loc)toCheck.poll();

            if (!checked.contains(curLoc))
            {
                if (curLoc.distSimple(startLoc) <= 4)
                {
                    int block = curLoc.getBlock(world);
                    int meta = curLoc.getMeta(world);

                    if (block == AetherBlocks.AetherLog.blockID)
                    {
                        return true;
                    }

                    if (block == bLeaves)
                    {
                        toCheck.addAll(Arrays.asList(curLoc.adjacent()));
                    }
                }

                checked.add(curLoc);
            }
        }

        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        for (int l = 0; l < 4; l++)
        {
            double d = i + (random.nextFloat() - 0.5D) * 10.0D;
            double d1 = j + (random.nextFloat() - 0.5D) * 10.0D;
            double d2 = k + (random.nextFloat() - 0.5D) * 10.0D;
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            d3 = (random.nextFloat() - 0.5D) * 0.5D;
            d4 = (random.nextFloat() - 0.5D) * 0.5D;
            d5 = (random.nextFloat() - 0.5D) * 0.5D;
        }
    }

    private void removeLeaves(World world, int px, int py, int pz)
    {
        world.setBlock(px, py, pz, 0);
    }

    public void updateTick(World world, int i, int j, int k, Random rand)
    {
        if (!nearTrunk(world, i, j, k))
        {
            removeLeaves(world, i, j, k);
        }
    }
}

