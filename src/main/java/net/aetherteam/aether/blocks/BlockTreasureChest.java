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

    protected BlockTreasureChest(int var1, int var2)
    {
        super(var1, var2);
        this.setHardness(-1.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        TileEntityTreasureChest var10 = (TileEntityTreasureChest)var1.getBlockTileEntity(var2, var3, var4);

        if (var10.isLocked())
        {
            ItemStack var11 = var5.inventory.getCurrentItem();

            if (var11 == null || var11.itemID != AetherItems.Key.itemID)
            {
                return false;
            }

            if (!var1.isRemote)
            {
                var10.unlock(var11.getItemDamage());
            }

            --var11.stackSize;
        }

        int var12 = AetherGuiHandler.treasureChestID;
        var5.openGui(Aether.instance, var12, var1, var2, var3, var4);
        return true;
    }

    public void checkForAdjacentChests() {}

    public boolean hasTileEntity(int var1)
    {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World var1)
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
    public int quantityDropped(Random var1)
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
    public void registerIcons(IconRegister var1)
    {
        this.blockIcon = var1.registerIcon("Aether:Carved Stone");
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return this.blockIcon;
    }
}
