package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.tile_entities.TileEntityAltar;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class RenderHandlerAltar implements ISimpleBlockRenderingHandler
{
    public TileEntityAltar dummyAltar = new TileEntityAltar();

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (modelID == this.getRenderId())
        {
            TileEntityRenderer.instance.renderTileEntityAt(this.dummyAltar, 0.0D, -0.1D, 0.0D, 0.0F);
        }
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        return false;
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return AetherBlocks.altarRenderId;
    }
}
