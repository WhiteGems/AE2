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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBronzeDoor extends BlockAether implements IAetherBlock
{
    private Random rand = new Random();
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Bronze Door", "Bronze Door Lock"};

    protected BlockBronzeDoor(int blockID)
    {
        super(blockID, Material.wood);
        this.setHardness(-1.0F);
        this.setResistance(1000000.0F);
    }

    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        return false;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int i, int j, int k)
    {
        Dungeon dungeon = DungeonHandler.instance().getInstanceAt(MathHelper.floor_double((double)i), MathHelper.floor_double((double)j), MathHelper.floor_double((double)k));

        if (dungeon != null)
        {
            super.onBlockAdded(world, i, j, k);
        }
        else
        {
            world.setBlock(i, j, k, 0);
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entity, ItemStack stack)
    {
        world.setBlock(i, j, k, 0);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.BronzeDoor, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < names.length; ++i)
        {
            this.icons.put(names[i], par1IconRegister.registerIcon("aether:" + names[i]));
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        for (int l = x - 3; l <= x + 3; ++l)
        {
            for (int i1 = y - 3; i1 <= y + 3; ++i1)
            {
                for (int j1 = z - 3; j1 <= z + 3; ++j1)
                {
                    if (world.getBlockId(l, i1, j1) == AetherBlocks.BronzeDoorController.blockID)
                    {
                        TileEntityBronzeDoorController bronzeDoor = (TileEntityBronzeDoorController)world.getBlockTileEntity(l, i1, j1);

                        if (bronzeDoor != null)
                        {
                            Party party = PartyController.instance().getParty(PartyController.instance().getMember(entityplayer));
                            Dungeon dungeon = bronzeDoor.getDungeon();

                            if (party != null && dungeon != null && dungeon.isQueuedParty(party))
                            {
                                int keyAmount = dungeon.getKeyAmount();
                                ArrayList keys = dungeon.getKeys();
                                System.out.println(keyAmount);

                                if (world.isRemote)
                                {
                                    if (keyAmount <= 0)
                                    {
                                        System.out.println(keyAmount);
                                        bronzeDoor.chatItUp(entityplayer, "This door seems to require " + (5 - bronzeDoor.getKeyAmount()) + (5 - keyAmount < 5 ? " more " : " ") + (5 - keyAmount > 1 ? "keys" : "key") + ". Perhaps they are elsewhere in the dungeon?");
                                        return true;
                                    }

                                    if (bronzeDoor.getKeyAmount() < 5)
                                    {
                                        bronzeDoor.chatItUp(entityplayer, "You have just added " + keyAmount + " keys to this door. It seems to require " + (5 - keyAmount) + (5 - keyAmount < 5 ? " more " : " ") + (5 - keyAmount > 1 ? "keys" : "key") + ".");
                                        return true;
                                    }
                                }

                                if (keyAmount > 0)
                                {
                                    bronzeDoor.addKeys(keys);
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
