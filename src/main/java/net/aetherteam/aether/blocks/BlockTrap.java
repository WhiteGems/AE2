package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.aetherteam.aether.entities.EntitySentry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockTrap extends BlockBreakable implements IAetherBlock
{
    private HashMap<String, Icon> icons = new HashMap();
    public static final String[] names = new String[] {"Carved Stone", "Angelic Stone", "Hellfire Stone"};

    public BlockTrap(int blockID)
    {
        super(blockID, "sup", Material.rock, false);
        this.setTickRandomly(true);
        this.setHardness(-1.0F);
        this.setResistance(1000000.0F);
        this.setStepSound(Block.soundStoneFootstep);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return true;
    }

    public Block setIconName(String name)
    {
        this.field_111026_f = "aether:" + name;
        return this.setUnlocalizedName("aether:" + name);
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        world.setBlock(x, y, z, this.blockID, meta, 2);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "aether:aemisc.activateTrap", 1.0F, 1.0F);
            int x = MathHelper.floor_double((double)i);
            int y = MathHelper.floor_double((double)j);
            int z = MathHelper.floor_double((double)k);

            if (!world.isRemote)
            {
                switch (world.getBlockMetadata(i, j, k))
                {
                    case 0:
                        EntitySentry entitysentry = new EntitySentry(world);
                        entitysentry.setPosition((double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D);
                        world.spawnEntityInWorld(entitysentry);

                    case 1:
                }
            }

            world.setBlock(i, j, k, AetherBlocks.LockedDungeonStone.blockID, world.getBlockMetadata(i, j, k), 2);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int i)
    {
        return i;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.DungeonStone, 1, meta);
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
}
