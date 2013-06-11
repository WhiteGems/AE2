package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBerryBush implements ISimpleBlockRenderingHandler
{
    public static int renderID = 100;

    public void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4)
    {
        if (var3 == this.getRenderId())
        {
            Tessellator var5 = Tessellator.instance;
            var5.startDrawingQuads();
            var5.setNormal(0.0F, -1.0F, 0.0F);
            var4.drawCrossedSquares(var1, var2, -0.5D, -0.5D, -0.5D, 1.0F);
            var5.draw();
            var1.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            var5.startDrawingQuads();
            var5.setNormal(0.0F, -1.0F, 0.0F);
            var4.renderBottomFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(0, var2));
            var5.draw();
            var5.startDrawingQuads();
            var5.setNormal(0.0F, 1.0F, 0.0F);
            var4.renderTopFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(1, var2));
            var5.draw();
            var5.startDrawingQuads();
            var5.setNormal(0.0F, 0.0F, -1.0F);
            var4.renderEastFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(2, var2));
            var5.draw();
            var5.startDrawingQuads();
            var5.setNormal(0.0F, 0.0F, 1.0F);
            var4.renderWestFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(3, var2));
            var5.draw();
            var5.startDrawingQuads();
            var5.setNormal(-1.0F, 0.0F, 0.0F);
            var4.renderNorthFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(4, var2));
            var5.draw();
            var5.startDrawingQuads();
            var5.setNormal(1.0F, 0.0F, 0.0F);
            var4.renderSouthFace(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(5, var2));
            var5.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
        if (var6 == this.getRenderId())
        {
            var7.renderCrossedSquares(var5, var2, var3, var4);
            var7.renderStandardBlock(var5, var2, var3, var4);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return renderID;
    }
}
