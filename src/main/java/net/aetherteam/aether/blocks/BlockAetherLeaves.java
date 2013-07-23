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

    protected BlockAetherLeaves(int var1, int var2)
    {
        super(var1);
        this.itemDropped = AetherBlocks.GreenSkyrootSaplingID;
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(Block.soundGrassFootstep);

        if (var2 != 0)
        {
            this.itemDropped = var2;
        }
    }

    protected BlockAetherLeaves(int var1)
    {
        this(var1, 0);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1 & 3;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int var1)
    {
        return 16777215;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        if (!var1.isRemote && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == Item.shears.itemID)
        {
            var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem_do(var1, var3, var4, var5, new ItemStack(this.blockID, 1, var6 & 3));
        }
        else
        {
            super.harvestBlock(var1, var2, var3, var4, var5, var6);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? (var2.nextInt(10) == 0 ? Item.appleGold.itemID : AetherBlocks.GoldenOakSapling.blockID) : (this.blockID == AetherBlocks.GreenSkyrootLeaves.blockID ? (var2.nextInt(6) == 0 ? AetherBlocks.BlockOrangeTree.blockID : AetherBlocks.GreenSkyrootSapling.blockID) : this.itemDropped);
    }

    private boolean isMyTrunkMeta(int var1)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? var1 >= 2 : var1 <= 1;
    }

    private boolean nearTrunk(World var1, int var2, int var3, int var4)
    {
        Loc var5 = new Loc(var2, var3, var4);
        LinkedList var6 = new LinkedList();
        ArrayList var7 = new ArrayList();
        var6.offer(new Loc(var2, var3, var4));
        int var8 = this.blockID;

        while (!var6.isEmpty())
        {
            Loc var9 = (Loc)var6.poll();

            if (!var7.contains(var9))
            {
                if (var9.distSimple(var5) <= 6)
                {
                    int var10 = var9.getBlock(var1);
                    var9.getMeta(var1);

                    if (var10 == AetherBlocks.AetherLog.blockID)
                    {
                        return true;
                    }

                    if (var10 == var8)
                    {
                        var6.addAll(Arrays.asList(var9.adjacent()));
                    }
                }

                var7.add(var9);
            }
        }

        return false;
    }

    public void onBlockRemoval(World var1, int var2, int var3, int var4)
    {
        byte var5 = 1;
        int var6 = var5 + 1;

        if (var1.checkChunksExist(var2 - var6, var3 - var6, var4 - var6, var2 + var6, var3 + var6, var4 + var6))
        {
            for (int var7 = -var5; var7 <= var5; ++var7)
            {
                for (int var8 = -var5; var8 <= var5; ++var8)
                {
                    for (int var9 = -var5; var9 <= var5; ++var9)
                    {
                        int var10 = var1.getBlockId(var2 + var7, var3 + var8, var4 + var9);

                        if (var10 == this.blockID)
                        {
                            int var11 = var1.getBlockMetadata(var2 + var7, var3 + var8, var4 + var9);
                            var1.setBlockMetadataWithNotify(var2 + var7, var3 + var8, var4 + var9, var11 | 8, 4);
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
    public void registerIcons(IconRegister var1)
    {
        this.normalIcon = var1.registerIcon(this.getUnlocalizedName2());
        this.opaqueIcon = var1.registerIcon(this.getUnlocalizedName2() + "_Opaque");
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
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
    public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4)
    {
        return 16777215;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return this.blockID == AetherBlocks.GoldenOakLeaves.blockID ? (var1.nextInt(10) == 0 ? 1 : 0) : (var1.nextInt(5) == 0 ? 1 : 0);
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.randomDisplayTick(var1, var2, var3, var4, var5);

        if (this.blockID == AetherBlocks.GoldenOakLeaves.blockID && Minecraft.getMinecraft().gameSettings.particleSetting != 2 && var1.isRemote)
        {
            for (int var6 = 0; var6 < 4; ++var6)
            {
                double var7 = (double)var2 + ((double)var5.nextFloat() - 0.5D) * 10.0D;
                double var9 = (double)var3 + ((double)var5.nextFloat() - 0.5D) * 10.0D;
                double var11 = (double)var4 + ((double)var5.nextFloat() - 0.5D) * 10.0D;
                double var13 = 0.0D;
                double var15 = 0.0D;
                double var17 = 0.0D;
                var13 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                var15 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                var17 = ((double)var5.nextFloat() - 0.5D) * 0.5D;
                EntityGoldenFX var19 = new EntityGoldenFX(var1, var7, var9, var11, var13, var15, var17, false);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(var19);
            }
        }
    }

    private void removeLeaves(World var1, int var2, int var3, int var4)
    {
        var1.setBlock(var2, var3, var4, 0);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!var1.isRemote && AetherBlocks.GoldenOakSaplingID != this.blockID)
        {
            if (!this.nearTrunk(var1, var2, var3, var4))
            {
                this.removeLeaves(var1, var2, var3, var4);
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }
}
