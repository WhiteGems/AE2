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
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAetherGrass extends BlockAether implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprGoldTop;
    public static Icon sprGoldSide;

    protected BlockAetherGrass(int var1)
    {
        super(var1, Material.ground);
        this.setTickRandomly(true);
        this.setHardness(0.6F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        for (int var4 = 0; var4 < 1; ++var4)
        {
            var3.add(new ItemStack(var1, 1, var4));
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6)
    {
        var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        var2.addExhaustion(0.025F);

        if (!var1.isRemote)
        {
            ItemStack var7;

            if (var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
            {
                var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                var7 = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
            }
            else
            {
                var7 = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
            }
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var5 == null)
        {
            return false;
        }
        else
        {
            ItemStack var10 = var5.getCurrentEquippedItem();

            if (var10 == null)
            {
                return false;
            }
            else if (var10.itemID == AetherItems.AmbrosiumShard.itemID && var1.getBlockMetadata(var2, var3, var4) == 0 && !var1.isRemote)
            {
                var1.setBlockMetadataWithNotify(var2, var3, var4, 1, 2);
                --var10.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        if (var1 == 1)
        {
            if (var2 == 0)
            {
                return sprTop;
            }

            if (var2 == 1)
            {
                return sprGoldTop;
            }
        }

        return var1 == 0 ? AetherBlocks.AetherDirt.getIcon(var1, var2) : (var2 == 1 ? sprGoldSide : sprSide);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int var1, Random var2, int var3)
    {
        return AetherBlocks.AetherDirt.idDropped(0, var2, var3);
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5)
    {
        super.randomDisplayTick(var1, var2, var3, var4, var5);

        if (var5.nextInt(4) == 0 && var1.getBlockMetadata(var2, var3, var4) == 1 && var1.isRemote)
        {
            for (int var6 = 0; var6 < 6; ++var6)
            {
                EntityGoldenFX var7 = new EntityGoldenFX(var1, (double)((float)var2 + var5.nextFloat()), (double)((float)var3 + 1.1F), (double)((float)var4 + var5.nextFloat()), 0.0D, 0.0D, 0.0D, true);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(var7);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World var1, int var2, int var3, int var4, Random var5)
    {
        if (!var1.isRemote)
        {
            if (var1.getBlockLightValue(var2, var3 + 1, var4) < 4 && var1.getBlockMaterial(var2, var3 + 1, var4).getCanBlockGrass())
            {
                if (var5.nextInt(4) != 0 || Block.blocksList[var1.getBlockId(var2, var3 + 1, var4)] instanceof BlockWall)
                {
                    return;
                }

                var1.setBlock(var2, var3, var4, AetherBlocks.AetherDirt.blockID);
            }
            else if (var1.getBlockLightValue(var2, var3 + 1, var4) >= 9)
            {
                int var6 = var2 + var5.nextInt(3) - 1;
                int var7 = var3 + var5.nextInt(5) - 3;
                int var8 = var4 + var5.nextInt(3) - 1;

                if (var1.getBlockId(var6, var7, var8) == AetherBlocks.AetherDirt.blockID && var1.getBlockLightValue(var6, var7 + 1, var8) >= 4 && !var1.getBlockMaterial(var6, var7 + 1, var8).getCanBlockGrass())
                {
                    var1.setBlock(var6, var7, var8, AetherBlocks.AetherGrass.blockID, 0, 2);
                }
            }
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        sprTop = var1.registerIcon("Aether:Aether Grass Top");
        sprSide = var1.registerIcon("Aether:Aether Grass Side");
        sprGoldTop = var1.registerIcon("Aether:Enchanted Aether Grass Top");
        sprGoldSide = var1.registerIcon("Aether:Enchanted Aether Grass Side");
        super.registerIcons(var1);
    }
}
