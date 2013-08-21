package net.aetherteam.aether.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityGoldenFX;
import net.aetherteam.aether.util.Loc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAetherLeaves extends BlockLeaves implements IAetherBlock
{
    private Icon normalIcon;
    private Icon opaqueIcon;
    private int itemDropped;

    protected BlockAetherLeaves(int blockID, int droppedID)
    {
        super(blockID);
        this.itemDropped = AetherBlocks.GreenSkyrootSaplingID;
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(Block.soundGrassFootstep);

        if (droppedID != 0)
        {
            this.itemDropped = droppedID;
        }
    }

    protected BlockAetherLeaves(int blockID)
    {
        this(blockID, 0);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int i)
    {
        return i & 3;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int i)
    {
        return 16777215;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.itemID)
        {
            entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem_do(world, i, j, k, new ItemStack(this.blockID, 1, l & 3));
        }
        else
        {
            super.harvestBlock(world, entityplayer, i, j, k, l);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int j)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? (random.nextInt(10) == 0 ? Item.appleGold.itemID : AetherBlocks.GoldenOakSapling.blockID) : (this.blockID == AetherBlocks.GreenSkyrootLeaves.blockID ? (random.nextInt(6) == 0 ? AetherBlocks.BlockOrangeTree.blockID : AetherBlocks.GreenSkyrootSapling.blockID) : this.itemDropped);
    }

    private boolean isMyTrunkMeta(int meta)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? meta >= 2 : meta <= 1;
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
                if (curLoc.distSimple(startLoc) <= 6)
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

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        byte l = 1;
        int i1 = l + 1;

        if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
        {
            for (int j1 = -l; j1 <= l; ++j1)
            {
                for (int k1 = -l; k1 <= l; ++k1)
                {
                    for (int l1 = -l; l1 <= l; ++l1)
                    {
                        int i2 = world.getBlockId(i + j1, j + k1, k + l1);

                        if (i2 == this.blockID)
                        {
                            int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
                            world.setBlockMetadataWithNotify(i + j1, j + k1, k + l1, j2 | 8, 4);
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.normalIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().substring(5));
        this.opaqueIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().substring(5) + "_Opaque");
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        return this.graphicsLevel ? this.normalIcon : this.opaqueIcon;
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
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? (random.nextInt(10) == 0 ? 1 : 0) : (random.nextInt(5) == 0 ? 1 : 0);
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        super.randomDisplayTick(world, i, j, k, random);

        if (this.blockID == AetherBlocks.GoldenOakLeaves.blockID && Minecraft.getMinecraft().gameSettings.particleSetting != 2 && world.isRemote)
        {
            for (int l = 0; l < 4; ++l)
            {
                double d = (double)i + ((double)random.nextFloat() - 0.5D) * 10.0D;
                double d1 = (double)j + ((double)random.nextFloat() - 0.5D) * 10.0D;
                double d2 = (double)k + ((double)random.nextFloat() - 0.5D) * 10.0D;
                double d3 = 0.0D;
                double d4 = 0.0D;
                double d5 = 0.0D;
                d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
                EntityGoldenFX obj = new EntityGoldenFX(world, d, d1, d2, d3, d4, d5, false);
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
        if (!world.isRemote && AetherBlocks.GoldenOakSaplingID != this.blockID)
        {
            if (!this.nearTrunk(world, i, j, k))
            {
                this.removeLeaves(world, i, j, k);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }
}
