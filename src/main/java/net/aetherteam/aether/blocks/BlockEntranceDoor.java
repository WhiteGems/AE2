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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockEntranceDoor extends BlockAether implements IAetherBlock
{
    private Random rand = new Random();
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Dungeon Entrance", "Dungeon Entrance Lock"};

    protected BlockEntranceDoor(int blockID)
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
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {}

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
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int i)
    {
        return i;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.DungeonEntrance, 1, meta);
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
                    if (world.getBlockId(l, i1, j1) == AetherBlocks.DungeonEntranceController.blockID)
                    {
                        TileEntityEntranceController controller = (TileEntityEntranceController)world.getBlockTileEntity(l, i1, j1);

                        if (controller != null && controller.hasDungeon() && controller.getDungeon() != null)
                        {
                            int guiID = AetherGuiHandler.entranceID;
                            entityplayer.openGui(Aether.instance, guiID, world, controller.xCoord, controller.yCoord, controller.zCoord);
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
