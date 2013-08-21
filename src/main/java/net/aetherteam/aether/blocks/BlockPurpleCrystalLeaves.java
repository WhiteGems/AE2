package net.aetherteam.aether.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityPurpleFX;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPurpleCrystalLeaves extends BlockLeaves implements IAetherBlock
{
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Purple Crystal Leaves", "Purple Fruit Leaves"};

    public BlockPurpleCrystalLeaves(int blockID)
    {
        super(blockID);
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(Block.soundGrassFootstep);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < names.length; ++i)
        {
            this.icons.put(names[i], iconRegister.registerIcon("aether:" + names[i]));
            this.icons.put(names[i] + "_Opaque", iconRegister.registerIcon("aether:" + names[i] + "_Opaque"));
        }
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int i)
    {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        ItemStack stack = new ItemStack(AetherBlocks.PurpleCrystalLeaves, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(this.graphicsLevel ? name : name + "_Opaque");
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 16777215;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 16777215;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);

        if (meta >= 1)
        {
            ItemStack stack = new ItemStack(AetherItems.WhiteApple.itemID, 1, 0);
            this.dropBlockAsItem_do(world, x, y, z, stack);
        }
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
                    curLoc.getMeta(world);

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

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        super.randomDisplayTick(world, i, j, k, random);

        if (random.nextInt(10) == 0 && Minecraft.getMinecraft().gameSettings.particleSetting != 2 && world.isRemote)
        {
            for (int l = 0; l < 15; ++l)
            {
                double d = (double)i + ((double)random.nextFloat() - 0.5D) * 6.0D;
                double d1 = (double)j + ((double)random.nextFloat() - 0.5D) * 6.0D;
                double d2 = (double)k + ((double)random.nextFloat() - 0.5D) * 6.0D;
                double d3 = 0.0D;
                double d4 = 0.0D;
                double d5 = 0.0D;
                d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                EntityPurpleFX obj = new EntityPurpleFX(world, d, d1, d2, d3, d4, d5);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
            }
        }
    }

    private void removeLeaves(World world, int px, int py, int pz)
    {
        world.setBlock(px, py, pz, 0);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int i, int j, int k, Random rand)
    {
        if (!this.nearTrunk(world, i, j, k))
        {
            this.removeLeaves(world, i, j, k);
        }
    }
}
