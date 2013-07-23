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
    private HashMap icons = new HashMap();
    public static final String[] names = new String[] {"Bronze Door", "Bronze Door Lock"};

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
    public Icon getIcon(int var1, int var2)
    {
        ItemStack var3 = new ItemStack(AetherBlocks.BronzeDoor, 1, var2);
        String var4 = var3.getItem().getItemDisplayName(var3);
        return (Icon)this.icons.get(var4);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        for (int var2 = 0; var2 < names.length; ++var2)
        {
            this.icons.put(names[var2], var1.registerIcon("Aether:" + names[var2]));
        }
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
                    if (var1.getBlockId(var10, var11, var12) == AetherBlocks.BronzeDoorController.blockID)
                    {
                        TileEntityBronzeDoorController var13 = (TileEntityBronzeDoorController)var1.getBlockTileEntity(var10, var11, var12);

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
                                    if (var16 <= 0)
                                    {
                                        System.out.println(var16);
                                        var13.chatItUp(var5, "This door seems to require " + (5 - var13.getKeyAmount()) + (5 - var16 < 5 ? " more " : " ") + (5 - var16 > 1 ? "keys" : "key") + ". Perhaps they are elsewhere in the dungeon?");
                                        return true;
                                    }

                                    if (var13.getKeyAmount() < 5)
                                    {
                                        var13.chatItUp(var5, "You have just added " + var16 + " keys to this door. It seems to require " + (5 - var16) + (5 - var16 < 5 ? " more " : " ") + (5 - var16 > 1 ? "keys" : "key") + ".");
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
                }
            }
        }

        return false;
    }
}
