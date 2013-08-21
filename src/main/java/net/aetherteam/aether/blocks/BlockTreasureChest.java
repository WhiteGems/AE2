package net.aetherteam.aether.blocks;

import java.util.Random;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.aetherteam.aether.items.AetherItems;
import net.aetherteam.aether.tile_entities.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTreasureChest extends BlockChest implements IAetherBlock
{
    private Random random = new Random();
    private int sideTexture;

    protected BlockTreasureChest(int i, int j)
    {
        super(i, j);
        this.setHardness(-1.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        TileEntityTreasureChest treasurechest = (TileEntityTreasureChest)world.getBlockTileEntity(i, j, k);

        if (treasurechest.isLocked())
        {
            ItemStack guiID = entityplayer.inventory.getCurrentItem();

            if (guiID == null || guiID.itemID != AetherItems.Key.itemID)
            {
                return false;
            }

            if (!world.isRemote)
            {
                treasurechest.unlock(guiID.getItemDamage());
            }

            --guiID.stackSize;
        }

        int var12 = AetherGuiHandler.treasureChestID;
        entityplayer.openGui(Aether.instance, var12, world, i, j, k);
        return true;
    }

    public void checkForAdjacentChests() {}

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        try
        {
            return new TileEntityTreasureChest();
        }
        catch (Exception var3)
        {
            throw new RuntimeException(var3);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return AetherBlocks.treasureChestRenderId;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("aether:Carved Stone");
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return this.blockIcon;
    }
}
