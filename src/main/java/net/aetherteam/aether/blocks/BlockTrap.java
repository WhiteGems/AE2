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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockTrap extends BlockBreakable
    implements IAetherBlock
{
    private HashMap icons = new HashMap();
    public static final String[] names = { "Carved Stone", "Angelic Stone", "Hellfire Stone" };

    public BlockTrap(int blockID)
    {
        super(blockID, "sup", Material.rock, false);
        setTickRandomly(true);
        setHardness(-1.0F);
        setResistance(1000000.0F);
        setStepSound(Block.soundStoneFootstep);
    }

    public boolean isOpaqueCube()
    {
        return true;
    }

    public Block setIconName(String name)
    {
        return setUnlocalizedName("Aether:" + name);
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        world.setBlock(x, y, z, this.blockID, meta, 2);
    }

    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
        if ((entity instanceof EntityPlayer))
        {
            world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "aemisc.activateTrap", 1.0F, 1.0F);
            int x = MathHelper.floor_double(i);
            int y = MathHelper.floor_double(j);
            int z = MathHelper.floor_double(k);

            if (!world.isRemote)
            {
                switch (world.getBlockMetadata(i, j, k))
                {
                    case 0:
                        EntitySentry entitysentry = new EntitySentry(world);
                        entitysentry.setPosition(x + 0.5D, y + 1.5D, z + 0.5D);
                        world.spawnEntityInWorld(entitysentry);
                        break;

                    case 1:
                }
            }

            world.setBlock(i, j, k, AetherBlocks.LockedDungeonStone.blockID, world.getBlockMetadata(i, j, k), 2);
        }
    }

    public int damageDropped(int i)
    {
        return i;
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(AetherBlocks.DungeonStone, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < names.length; i++)
        {
            this.icons.put(names[i], par1IconRegister.registerIcon("Aether:" + names[i]));
        }
    }
}

