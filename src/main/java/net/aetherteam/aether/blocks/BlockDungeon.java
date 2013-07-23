package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDungeon extends BlockAether implements IAetherBlock
{
    private Icon carved,angelic,hellfire,sentry,lightangelic,lighthellfire;

    public static int getBlockFromDye(int var0)
    {
        return ~var0 & 15;
    }

    public static int getDyeFromBlock(int var0)
    {
        return ~var0 & 15;
    }

    protected BlockDungeon(int var1, float var2, float var3)
    {
        super(var1, Material.rock);
        this.setStepSound(Block.soundStoneFootstep);
        this.setHardness(var2);
        this.setLightValue(var3);
        this.setLightOpacity(255);
    }

    protected BlockDungeon(int var1, float var2, float var3, float var4)
    {
        this(var1, var2, var3);
        this.setResistance(var4);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
        var3.add(new ItemStack(var1, 1, 1));
        var3.add(new ItemStack(var1, 1, 2));
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
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
    	case 0:
    		if(isLocked())
    		{
    			return this.sentry;
    		}
    		else
    		{
    			return this.carved;
    		}
    	case 1:
    		if(isLit())
    		{
    			return this.lightangelic;
    		}
    		else
    		{	
    			return this.angelic;
    		}
    	case 2:
    		if(isLit())
    		{
    			return this.lighthellfire; 
    		}
    		else
    		{
    			return this.hellfire;
    		}
    	}

        return (Icon)this.sentry;
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        return this.isLocked() ? false : super.removeBlockByPlayer(var1, var2, var3, var4, var5);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (this.isLocked() && DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)var2), MathHelper.floor_double((double)var3), MathHelper.floor_double((double)var4)) == null)
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        if (this.isLocked())
        {
            var1.setBlockToAir(var2, var3, var4);
        }
    }


    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
    	carved = ir.registerIcon("Aether:Carved Stone");
    	angelic = ir.registerIcon("Aether:Angelic Stone");
    	hellfire = ir.registerIcon("Aether:Hellfire Stone");
    	sentry = ir.registerIcon("Aether:Sentry Stone");
    	lightangelic = ir.registerIcon("Aether:Light Angelic Stone");
    	lighthellfire = ir.registerIcon("Aether:Light Hellfire Stone");  
    }

    private boolean isLit()
    {
        return this.blockID == AetherBlocks.LightDungeonStone.blockID || this.blockID == AetherBlocks.LockedLightDungeonStone.blockID;
    }

    private boolean isLocked()
    {
        return this.blockID == AetherBlocks.LockedDungeonStone.blockID || this.blockID == AetherBlocks.LockedLightDungeonStone.blockID;
    }
}
