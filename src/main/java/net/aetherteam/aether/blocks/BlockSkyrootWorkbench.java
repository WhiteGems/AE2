package net.aetherteam.aether.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.Aether;
import net.aetherteam.aether.AetherGuiHandler;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSkyrootWorkbench extends BlockWorkbench
{
    @SideOnly(Side.CLIENT)
    private Icon workbenchIconTop;
    @SideOnly(Side.CLIENT)
    private Icon workbenchIconFront;

    protected BlockSkyrootWorkbench(int par1)
    {
        super(par1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 1 ? this.workbenchIconTop : (par1 == 0 ? AetherBlocks.SkyrootPlank.getBlockTextureFromSide(par1) : (par1 != 2 && par1 != 4 ? this.blockIcon : this.workbenchIconFront));
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("aether:Skyroot Workbench Side");
        this.workbenchIconTop = par1IconRegister.registerIcon("aether:Skyroot Workbench Top");
        this.workbenchIconFront = par1IconRegister.registerIcon("aether:Skyroot Workbench Front");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            int guiID = AetherGuiHandler.craftingID;
            player.openGui(Aether.instance, guiID, world, x, y, z);
            return true;
        }
    }
}
