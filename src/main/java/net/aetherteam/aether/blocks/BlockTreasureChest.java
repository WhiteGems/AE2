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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTreasureChest extends BlockChest
    implements IAetherBlock
{
    private Random random;
    private int sideTexture;

    protected BlockTreasureChest(int i, int j)
    {
        super(i, j);
        this.random = new Random();
        setHardness(-1.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        TileEntityTreasureChest treasurechest = (TileEntityTreasureChest)world.getBlockTileEntity(i, j, k);

        if (treasurechest.isLocked())
        {
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();

            if ((itemstack != null) && (itemstack.itemID == AetherItems.Key.itemID))
            {
                if (!world.isRemote)
                {
                    treasurechest.unlock(itemstack.getItemDamage());
                }

                itemstack.stackSize -= 1;
            }
            else
            {
                return false;
            }
        }

        int guiID = AetherGuiHandler.treasureChestID;
        entityplayer.openGui(Aether.instance, guiID, world, i, j, k);
        return true;
    }

    public void checkForAdjacentChests()
    {
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

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

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return AetherBlocks.treasureChestRenderId;
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("Aether:Carved Stone");
    }

    public Icon getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return this.blockIcon;
    }
}

