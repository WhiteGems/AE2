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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChristmasLeaves extends BlockLeaves implements IAetherBlock
{
    private Icon leafIcon,leafIcon_O;
	private Icon decoratedLeafIcon,decoratedLeafIcon_O;

    private HashMap icons = new HashMap();
    public static final String[] names = new String[] {"Christmas Leaves", "Decorative Leaves"};

    public BlockChristmasLeaves(int var1)
    {
        super(var1);
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
        this.setLightOpacity(1);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister ir)
    {
		leafIcon = ir.registerIcon("Aether:Christmas Leaves");
        decoratedLeafIcon = ir.registerIcon("Aether:Decorated Leaves");
		
		leafIcon_O = ir.registerIcon("Aether:Christmas Leaves_Opaque");
        decoratedLeafIcon_O = ir.registerIcon("Aether:Decorated Leaves_Opaque");
		super.registerIcons(ir);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int var1)
    {
        return 16777215;
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
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
        var3.add(new ItemStack(var1, 1, 1));
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        this.setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
		if(meta == 0)
		{
			return (Icon)this.icons.get(this.graphicsLevel ? "Christmas Leaves" : "Christmas Leaves_Opaque");
		}
        return (Icon)this.icons.get(this.graphicsLevel ? "Decorated Leaves" : "Decorated Leaves_Opaque");
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return 0;
    }

    private boolean isMyTrunkMeta(int var1)
    {
        return this.blockID == AetherBlocks.ChristmasLeaves.blockID ? var1 <= 1 : var1 >= 2;
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
                if (var9.distSimple(var5) <= 4)
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

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random var1)
    {
        return 0;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
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
        if (!this.nearTrunk(var1, var2, var3, var4))
        {
            this.removeLeaves(var1, var2, var3, var4);
        }
    }
}
