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
    private Icon field_94385_a;
    @SideOnly(Side.CLIENT)
    private Icon field_94384_b;

    protected BlockSkyrootWorkbench(int var1)
    {
        super(var1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int var1, int var2)
    {
        return var1 == 1 ? this.field_94385_a : (var1 == 0 ? AetherBlocks.SkyrootPlank.getBlockTextureFromSide(var1) : (var1 != 2 && var1 != 4 ? this.blockIcon : this.field_94384_b));
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister var1)
    {
        this.blockIcon = var1.registerIcon("Aether:Skyroot Workbench Side");
        this.field_94385_a = var1.registerIcon("Aether:Skyroot Workbench Top");
        this.field_94384_b = var1.registerIcon("Aether:Skyroot Workbench Front");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9)
    {
        if (var1.isRemote)
        {
            return true;
        } else
        {
            int var10 = AetherGuiHandler.craftingID;
            var5.openGui(Aether.instance, var10, var1, var2, var3, var4);
            return true;
        }
    }
}
