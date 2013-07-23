package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.IBlockAccess;

public class BlockQuicksoilGlass extends BlockBreakable
    implements IAetherBlock
{
    private String iconName;

    public BlockQuicksoilGlass(int blockID)
    {
        super(blockID, "sup", Material.glass, false);
        this.slipperiness = 1.1F;
        setLightValue(0.7375F);
        setHardness(0.2F);
        setLightOpacity(0);
        setStepSound(Block.soundGlassFootstep);
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
    }

    public Block setIconName(String name)
    {
        this.iconName = ("Aether:" + name);
        return setUnlocalizedName("Aether:" + name);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(this.iconName);
    }
}

