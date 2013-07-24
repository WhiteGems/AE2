package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import net.aetherteam.aether.dungeons.Dungeon;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.tile_entities.TileEntityBronzeDoorController;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBronzeDoor extends BlockAether implements IAetherBlock
{
    private Random rand = new Random();
    private Icon door,lock;

    protected BlockBronzeDoor(int var1)
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
    	door = ir.registerIcon("Aether:Bronze Door");
    	lock = ir.registerIcon("Aether:Bronze Door Lock");  
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int posx, int posy, int posz, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        for (int x = posx - 3; x <= posx + 3; ++x)
            for (int y = posy - 3; y <= posy + 3; ++y)
                for (int z = posz - 3; z <= posz + 3; ++z)
                    if (var1.getBlockId(x, y, z) == AetherBlocks.BronzeDoorController.blockID)
                    {
                        TileEntityBronzeDoorController var13 = (TileEntityBronzeDoorController) var1.getBlockTileEntity(x, y, z);

                        if (var13 != null)
                        {
                            Party var14 = PartyController.instance().getParty(PartyController.instance().getMember(var5));
                            Dungeon var15 = var13.getDungeon();

                            if (var14 != null && var15 != null && var15.isQueuedParty(var14))
                            {
                                int var16 = var15.getKeyAmount();
                                ArrayList var17 = var15.getKeys();
                                System.out.println(var16);

                                if (var1.isRemote)
                                {
                                    if (var16 > 5)
                                    {
                                        var13.chatItUp(var5, "你已经插入 " + var16 + " 把钥匙");
                                        return true;
                                    }

                                    if (var16 <= 0)
                                    {
                                        var16 = 0;
                                        var13.chatItUp(var5, "这个门似乎需要 5 把钥匙, 在地牢的深处能找到这些钥匙?");
                                        return true;
                                    }

                                    if (var13.getKeyAmount() < 5)
                                    {
                                        var13.chatItUp(var5, "你已经插入 " + var16 + " 把钥匙, 还需要 " + (5 - var16) + " 把钥匙");
                                        return true;
                                    }
                                }

                                if (var16 > 0)
                                {
                                    var13.addKeys(var17);
                                    return true;
                                }
                            }
                        }

                        return false;
                    }

        return false;
    }
}
