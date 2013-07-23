package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSkyrootWorkbench extends BlockWorkbench
{
    @SideOnly(Side.CLIENT)
    private Icon field_94385_a;

    @SideOnly(Side.CLIENT)
    private Icon field_94384_b;

    protected BlockSkyrootWorkbench(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return (par1 != 2) && (par1 != 4) ? this.blockIcon : par1 == 0 ? AetherBlocks.SkyrootPlank.getBlockTextureFromSide(par1) : par1 == 1 ? this.field_94385_a : this.field_94384_b;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("Aether:Skyroot Workbench Side");
        this.field_94385_a = par1IconRegister.registerIcon("Aether:Skyroot Workbench Top");
        this.field_94384_b = par1IconRegister.registerIcon("Aether:Skyroot Workbench Front");
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }

        int guiID = AetherGuiHandler.craftingID;
        player.openGui(Aether.instance, guiID, world, x, y, z);
        return true;
    }
}

