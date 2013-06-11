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
    private HashMap icons = new HashMap();
    public static final String[] names = new String[]{"Carved Stone", "Angelic Stone", "Hellfire Stone"};

    public BlockTrap(int var1)
    {
        super(var1, "sup", Material.rock, false);
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

    public Block setIconName(String var1)
    {
        return this.setUnlocalizedName("Aether:" + var1);
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
    public int quantityDropped(Random var1)
    {
        return 1;
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5)
    {
        var1.setBlock(var2, var3, var4, this.blockID, var5, 2);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World var1, int var2, int var3, int var4, Entity var5)
    {
        if (var5 instanceof EntityPlayer)
        {
            var1.playSoundEffect((double) ((float) var2 + 0.5F), (double) ((float) var3 + 0.5F), (double) ((float) var4 + 0.5F), "aemisc.activateTrap", 1.0F, 1.0F);
            int var6 = MathHelper.floor_double((double) var2);
            int var7 = MathHelper.floor_double((double) var3);
            int var8 = MathHelper.floor_double((double) var4);

            if (!var1.isRemote)
            {
                switch (var1.getBlockMetadata(var2, var3, var4))
                {
                    case 0:
                        EntitySentry var9 = new EntitySentry(var1);
                        var9.setPosition((double) var6 + 0.5D, (double) var7 + 1.5D, (double) var8 + 0.5D);
                        var1.spawnEntityInWorld(var9);

                    case 1:
                }
            }

            var1.setBlock(var2, var3, var4, AetherBlocks.LockedDungeonStone.blockID, var1.getBlockMetadata(var2, var3, var4), 2);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int var1)
    {
        return var1;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int var1, CreativeTabs var2, List var3)
    {
        var3.add(new ItemStack(var1, 1, 0));
        var3.add(new ItemStack(var1, 1, 1));
        var3.add(new ItemStack(var1, 1, 2));
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        ItemStack var3 = new ItemStack(AetherBlocks.DungeonStone, 1, var2);
        String var4 = var3.getItem().getItemDisplayName(var3);
        return (Icon) this.icons.get(var4);
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
}
