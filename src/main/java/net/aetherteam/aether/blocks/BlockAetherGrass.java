package net.aetherteam.aether.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntityGoldenFX;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockAetherGrass extends BlockAether implements IAetherBlock
{
    public static Icon sprTop;
    public static Icon sprSide;
    public static Icon sprGoldTop;
    public static Icon sprGoldSide;

    protected BlockAetherGrass(int blockID)
    {
        super(blockID, Material.ground);
        this.setTickRandomly(true);
        this.setHardness(0.6F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int meta = 0; meta < 1; ++meta)
        {
            par3List.add(new ItemStack(par1, 1, meta));
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer entityplayer, int x, int y, int z, int meta)
    {
        entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
        entityplayer.addExhaustion(0.025F);

        if (!world.isRemote)
        {
            ItemStack stack;

            if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == AetherItems.SkyrootShovel.itemID)
            {
                entityplayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
                stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 2, 1);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
            else
            {
                stack = new ItemStack(AetherBlocks.AetherDirt.blockID, 1, 1);
                this.dropBlockAsItem_do(world, x, y, z, stack);
            }
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
        if (entityPlayer == null)
        {
            return false;
        }
        else
        {
            ItemStack itemStack = entityPlayer.getCurrentEquippedItem();

            if (itemStack == null)
            {
                return false;
            }
            else if (itemStack.itemID == AetherItems.AmbrosiumShard.itemID && world.getBlockMetadata(x, y, z) == 0 && !world.isRemote)
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 2);
                --itemStack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            if (meta == 0)
            {
                return sprTop;
            }

            if (meta == 1)
            {
                return sprGoldTop;
            }
        }

        return side == 0 ? AetherBlocks.AetherDirt.getIcon(side, meta) : (meta == 1 ? sprGoldSide : sprSide);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int i, Random random, int meta)
    {
        return AetherBlocks.AetherDirt.idDropped(0, random, meta);
    }

    @SideOnly(Side.CLIENT)

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        super.randomDisplayTick(world, x, y, z, random);

        if (random.nextInt(4) == 0 && world.getBlockMetadata(x, y, z) == 1 && world.isRemote)
        {
            for (int l = 0; l < 6; ++l)
            {
                EntityGoldenFX obj = new EntityGoldenFX(world, (double)((float)x + random.nextFloat()), (double)((float)y + 1.1F), (double)((float)z + random.nextFloat()), 0.0D, 0.0D, 0.0D, true);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockMaterial(x, y + 1, z).getCanBlockGrass())
            {
                if (random.nextInt(4) != 0 || Block.blocksList[world.getBlockId(x, y + 1, z)] instanceof BlockWall)
                {
                    return;
                }

                world.setBlock(x, y, z, AetherBlocks.AetherDirt.blockID);
            }
            else if (world.getBlockLightValue(x, y + 1, z) >= 9)
            {
                int l = x + random.nextInt(3) - 1;
                int i1 = y + random.nextInt(5) - 3;
                int j1 = z + random.nextInt(3) - 1;

                if (world.getBlockId(l, i1, j1) == AetherBlocks.AetherDirt.blockID && world.getBlockLightValue(l, i1 + 1, j1) >= 4 && !world.getBlockMaterial(l, i1 + 1, j1).getCanBlockGrass())
                {
                    world.setBlock(l, i1, j1, AetherBlocks.AetherGrass.blockID, 0, 2);
                }
            }
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        sprTop = par1IconRegister.registerIcon("aether:Aether Grass Top");
        sprSide = par1IconRegister.registerIcon("aether:Aether Grass Side");
        sprGoldTop = par1IconRegister.registerIcon("aether:Enchanted Aether Grass Top");
        sprGoldSide = par1IconRegister.registerIcon("aether:Enchanted Aether Grass Side");
    }
}
