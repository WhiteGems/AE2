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
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAetherLeaves extends BlockLeaves
    implements IAetherBlock
{
    private Icon normalIcon;
    private Icon opaqueIcon;
    private int itemDropped = AetherBlocks.GreenSkyrootSaplingID;

    protected BlockAetherLeaves(int blockID, int droppedID)
    {
        super(blockID);
        setTickRandomly(true);
        setHardness(0.2F);
        setLightOpacity(1);
        setStepSound(Block.soundGrassFootstep);

        if (droppedID != 0)
        {
            this.itemDropped = droppedID;
        }
    }

    protected BlockAetherLeaves(int blockID)
    {
        this(blockID, 0);
    }

    public int damageDropped(int i)
    {
        return i & 0x3;
    }

    public int getRenderColor(int i)
    {
        return 16777215;
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if ((!world.isRemote) && (entityplayer.cd() != null) && (entityplayer.cd().itemID == Item.shears.itemID))
        {
            entityplayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[this.blockID], 1);
            dropBlockAsItem_do(world, i, j, k, new ItemStack(this.blockID, 1, l & 0x3));
        }
        else
        {
            super.harvestBlock(world, entityplayer, i, j, k, l);
        }
    }

    public int idDropped(int i, Random random, int j)
    {
        if (this.blockID == AetherBlocks.GoldenOakLeaves.blockID)
        {
            if (random.nextInt(10) == 0)
            {
                return Item.appleGold.itemID;
            }

            return AetherBlocks.GoldenOakSapling.blockID;
        }

        if (this.blockID == AetherBlocks.GreenSkyrootLeaves.blockID)
        {
            if (random.nextInt(6) == 0)
            {
                return AetherBlocks.BlockOrangeTree.blockID;
            }

            return AetherBlocks.GreenSkyrootSapling.blockID;
        }

        return this.itemDropped;
    }

    private boolean isMyTrunkMeta(int meta)
    {
        if (this.blockID == AetherBlocks.GoldenOakLeaves.blockID)
        {
            return meta >= 2;
        }

        return meta <= 1;
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

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        int l = 1;
        int i1 = l + 1;

        if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
        {
            for (int j1 = -l; j1 <= l; j1++)
                for (int k1 = -l; k1 <= l; k1++)
                    for (int l1 = -l; l1 <= l; l1++)
                    {
                        int i2 = world.getBlockId(i + j1, j + k1, k + l1);

                        if (i2 == this.blockID)
                        {
                            int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
                            world.setBlockMetadataWithNotify(i + j1, j + k1, k + l1, j2 | 0x8, 4);
                        }
                    }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.normalIcon = par1IconRegister.registerIcon(getUnlocalizedName2());
        this.opaqueIcon = par1IconRegister.registerIcon(getUnlocalizedName2() + "_Opaque");
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        return this.graphicsLevel ? this.normalIcon : this.opaqueIcon;
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

    public int quantityDropped(Random random)
    {
        if (this.blockID == AetherBlocks.GoldenOakLeaves.blockID)
        {
            return random.nextInt(10) == 0 ? 1 : 0;
        }

        return random.nextInt(5) == 0 ? 1 : 0;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        super.randomDisplayTick(world, i, j, k, random);

        if ((this.blockID == AetherBlocks.GoldenOakLeaves.blockID) && (Minecraft.getMinecraft().gameSettings.particleSetting != 2))
        {
            if (world.isRemote)
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
                    EntityNoteFX obj = new EntityGoldenFX(world, d, d1, d2, d3, d4, d5, false);
                    FMLClientHandler.instance().getClient().effectRenderer.a(obj);
                }
            }
        }
    }

    private void removeLeaves(World world, int px, int py, int pz)
    {
        world.setBlock(px, py, pz, 0);
    }

    public void updateTick(World world, int i, int j, int k, Random rand)
    {
        if ((world.isRemote) || (AetherBlocks.GoldenOakSaplingID == this.blockID))
        {
            return;
        }

        if (!nearTrunk(world, i, j, k))
        {
            removeLeaves(world, i, j, k);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }
}

