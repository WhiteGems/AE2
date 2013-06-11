package net.aetherteam.aether.blocks;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDungeonHolystone extends BlockAether implements IAetherBlock
{
    private Icon dungeonholystone;
    private Icon dungeonholymossystone;

    protected BlockDungeonHolystone(int var1)
    {
        super(var1, Material.rock);
        this.setHardness(-1.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setResistance(1000000.0F);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 1));
        var3.add(new ItemStack(var1, 1, 3));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
    	switch(meta)
    	{
    	case 1:return this.dungeonholystone;
    	case 3:return this.dungeonholymossystone;
    	}
 
        return this.dungeonholystone;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	dungeonholystone = ir.registerIcon("Aether:Dungeon Holystone");
    	dungeonholymossystone = ir.registerIcon("Aether:Dungeon Mossy Holystone");
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

            if (var6 == 0)
            {
                if (var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
                {
                    var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    var7 = new ItemStack(AetherBlocks.Holystone.blockID, 2, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
                else
                {
                    var7 = new ItemStack(AetherBlocks.Holystone.blockID, 1, 1);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
            }
            else if (var6 == 2)
            {
                if (var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == AetherItems.SkyrootPickaxe.itemID)
                {
                    var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                    var7 = new ItemStack(AetherBlocks.Holystone.blockID, 2, 3);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
                else
                {
                    var7 = new ItemStack(AetherBlocks.Holystone.blockID, 1, 3);
                    this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
                }
            }
            else
            {
                var7 = new ItemStack(AetherBlocks.Holystone.blockID, 1, var6);
                this.dropBlockAsItem_do(var1, var3, var4, var5, var7);
            }
        }
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        return false;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)var2), MathHelper.floor_double((double)var3), MathHelper.floor_double((double)var4)) == null)
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        var1.setBlockToAir(var2, var3, var4);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }
}
