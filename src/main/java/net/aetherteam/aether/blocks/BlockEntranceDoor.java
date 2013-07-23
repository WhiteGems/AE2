package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.tile_entities.TileEntityEntranceController;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEntranceDoor extends BlockAether implements IAetherBlock
{
    private Random rand = new Random();

    private Icon door,lock;

    protected BlockEntranceDoor(int var1)
    {
        super(var1, Material.wood);
        this.setHardness(-1.0F);
        this.setResistance(1000000.0F);
    }

    public boolean removeBlockByPlayer(World var1, EntityPlayer var2, int var3, int var4, int var5)
    {
        return false;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3) {}

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        Dungeon var5 = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)var2), MathHelper.floor_double((double)var3), MathHelper.floor_double((double)var4));

        if (var5 != null)
        {
            super.onBlockAdded(var1, var2, var3, var4);
        }
        else
        {
            var1.setBlock(var2, var3, var4, 0);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6)
    {
        var1.setBlock(var2, var3, var4, 0);
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
    public Icon getIcon(int side, int meta)
    {
        switch(meta)
        {
    	case 0:return this.door;
    	case 1:return this.lock;
    	}
       return this.door;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister ir)
    {
        door = ir.registerIcon("Aether:Dungeon Entrance");
        lock = ir.registerIcon("Aether:Dungeon Entrance Lock");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        for (int var10 = var2 - 3; var10 <= var2 + 3; ++var10)
        {
            for (int var11 = var3 - 3; var11 <= var3 + 3; ++var11)
            {
                for (int var12 = var4 - 3; var12 <= var4 + 3; ++var12)
                {
                    if (var1.getBlockId(var10, var11, var12) == AetherBlocks.DungeonEntranceController.blockID)
                    {
                        TileEntityEntranceController var13 = (TileEntityEntranceController)var1.getBlockTileEntity(var10, var11, var12);

                        if (var13 != null && var13.hasDungeon() && var13.getDungeon() != null)
                        {
                            int var14 = AetherGuiHandler.entranceID;
                            var5.openGui(Aether.instance, var14, var1, var13.xCoord, var13.yCoord, var13.zCoord);
                            return true;
                        }

                        return false;
                    }
                }
            }
        }

        return false;
    }
}
