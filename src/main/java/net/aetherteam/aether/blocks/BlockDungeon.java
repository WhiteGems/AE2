package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import net.aetherteam.aether.dungeons.DungeonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDungeon extends BlockAether
    implements IAetherBlock
{
    private HashMap icons = new HashMap();
    public static final String[] names = { "Carved Stone", "Angelic Stone", "Hellfire Stone", "Sentry Stone", "Light Angelic Stone", "Light Hellfire Stone" };

    public static int getBlockFromDye(int i)
    {
        return (i ^ 0xFFFFFFFF) & 0xF;
    }

    public static int getDyeFromBlock(int i)
    {
        return (i ^ 0xFFFFFFFF) & 0xF;
    }

    protected BlockDungeon(int i, float hardness, float light)
    {
        super(i, Material.rock);
        setStepSound(Block.soundStoneFootstep);
        setHardness(hardness);
        setLightValue(light);
        setLightOpacity(255);
    }

    protected BlockDungeon(int i, float hardness, float light, float resistance)
    {
        this(i, hardness, light);
        setResistance(resistance);
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
    }

    public int damageDropped(int i)
    {
        return i;
    }

    public Icon getIcon(int i, int meta)
    {
        ItemStack stack = new ItemStack(isLit() ? AetherBlocks.LightDungeonStone : AetherBlocks.DungeonStone, 1, meta);
        String name = stack.getItem().getItemDisplayName(stack);
        return (Icon)this.icons.get(name);
    }

    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        if (isLocked())
        {
            return false;
        }

        return super.removeBlockByPlayer(world, player, x, y, z);
    }

    public void onBlockAdded(World world, int x, int y, int z)
    {
        if ((isLocked()) && (DungeonHandler.instance().getInstanceAt(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z)) == null))
        {
            world.setBlockToAir(x, y, z);
        }
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        if (isLocked())
        {
            world.setBlockToAir(x, y, z);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < names.length; i++)
        {
            this.icons.put(names[i], par1IconRegister.registerIcon("Aether:" + names[i]));
        }
    }

    private boolean isLit()
    {
        return (this.blockID == AetherBlocks.LightDungeonStone.blockID) || (this.blockID == AetherBlocks.LockedLightDungeonStone.blockID);
    }

    private boolean isLocked()
    {
        return (this.blockID == AetherBlocks.LockedDungeonStone.blockID) || (this.blockID == AetherBlocks.LockedLightDungeonStone.blockID);
    }
}

