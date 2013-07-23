package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBerryBush
    implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderEngine renderer)
    {
        if (modelID == getRenderId())
        {
            Rect2i tessellator = Rect2i.rectX;
            tessellator.b();
            tessellator.b(0.0F, -1.0F, 0.0F);
            renderer.a(block, metadata, -0.5D, -0.5D, -0.5D, 1.0F);
            tessellator.getRectX();
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.b();
            tessellator.b(0.0F, -1.0F, 0.0F);
            renderer.a(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
            tessellator.getRectX();
            tessellator.b();
            tessellator.b(0.0F, 1.0F, 0.0F);
            renderer.b(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
            tessellator.getRectX();
            tessellator.b();
            tessellator.b(0.0F, 0.0F, -1.0F);
            renderer.d(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
            tessellator.getRectX();
            tessellator.b();
            tessellator.b(0.0F, 0.0F, 1.0F);
            renderer.c(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
            tessellator.getRectX();
            tessellator.b();
            tessellator.b(-1.0F, 0.0F, 0.0F);
            renderer.f(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
            tessellator.getRectX();
            tessellator.b();
            tessellator.b(1.0F, 0.0F, 0.0F);
            renderer.e(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
            tessellator.getRectX();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderEngine renderer)
    {
        if (modelID == getRenderId())
        {
            renderer.k(block, x, y, z);
            renderer.p(block, x, y, z);
            return true;
        }

        return false;
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return AetherBlocks.berryBushRenderId;
    }
}

