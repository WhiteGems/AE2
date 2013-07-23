package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderAetherTallGrass implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4)
    {
        if (var3 == this.getRenderId())
        {
            Tessellator var5 = Tessellator.instance;
            var5.startDrawingQuads();
            var5.setNormal(0.0F, -1.0F, 0.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            var4.drawCrossedSquares(var1, var2, -0.5D, -0.5D, -0.5D, 1.0F);
            var5.draw();
        }
    }

    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
        if (var6 == this.getRenderId())
        {
            Tessellator var8 = Tessellator.instance;
            var8.setBrightness(var5.getMixedBrightnessForBlock(var1, var2, var3, var4));
            float var9 = 1.0F;
            int var10 = var5.colorMultiplier(var1, var2, var3, var4);
            float var11 = (float)(var10 >> 16 & 255) / 255.0F;
            float var12 = (float)(var10 >> 8 & 255) / 255.0F;
            float var13 = (float)(var10 & 255) / 255.0F;

            if (EntityRenderer.anaglyphEnable)
            {
                float var14 = (var11 * 30.0F + var12 * 59.0F + var13 * 11.0F) / 100.0F;
                float var15 = (var11 * 30.0F + var12 * 70.0F) / 100.0F;
                float var16 = (var11 * 30.0F + var13 * 70.0F) / 100.0F;
                var11 = var14;
                var12 = var15;
                var13 = var16;
            }

            var8.setColorOpaque_F(var9 * var11, var9 * var12, var9 * var13);
            double var22 = (double)var2;
            double var23 = (double)var3;
            double var18 = (double)var4;
            long var20 = (long)(var2 * 3129871) ^ (long)var4 * 116129781L ^ (long)var3;
            var20 = var20 * var20 * 42317861L + var20 * 11L;
            var22 += ((double)((float)(var20 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            var23 += ((double)((float)(var20 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            var18 += ((double)((float)(var20 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
            this.drawCrossedSquares(var5, var1.getBlockMetadata(var2, var3, var4), var22, var23, var18, 1.0F);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void drawCrossedSquares(Block var1, int var2, double var3, double var5, double var7, float var9)
    {
        Tessellator var10 = Tessellator.instance;
        Icon var11 = var1.getIcon(0, var2);
        double var12 = (double)var11.getMinU();
        double var14 = (double)var11.getMinV();
        double var16 = (double)var11.getMaxU();
        double var18 = (double)var11.getMaxV();
        double var20 = 0.45D * (double)var9;
        double var22 = var3 + 0.5D - var20;
        double var24 = var3 + 0.5D + var20;
        double var26 = var7 + 0.5D - var20;
        double var28 = var7 + 0.5D + var20;
        var10.addVertexWithUV(var22, var5 + (double)var9, var26, var12, var14);
        var10.addVertexWithUV(var22, var5 + 0.0D, var26, var12, var18);
        var10.addVertexWithUV(var24, var5 + 0.0D, var28, var16, var18);
        var10.addVertexWithUV(var24, var5 + (double)var9, var28, var16, var14);
        var10.addVertexWithUV(var24, var5 + (double)var9, var28, var12, var14);
        var10.addVertexWithUV(var24, var5 + 0.0D, var28, var12, var18);
        var10.addVertexWithUV(var22, var5 + 0.0D, var26, var16, var18);
        var10.addVertexWithUV(var22, var5 + (double)var9, var26, var16, var14);
        var10.addVertexWithUV(var22, var5 + (double)var9, var28, var12, var14);
        var10.addVertexWithUV(var22, var5 + 0.0D, var28, var12, var18);
        var10.addVertexWithUV(var24, var5 + 0.0D, var26, var16, var18);
        var10.addVertexWithUV(var24, var5 + (double)var9, var26, var16, var14);
        var10.addVertexWithUV(var24, var5 + (double)var9, var26, var12, var14);
        var10.addVertexWithUV(var24, var5 + 0.0D, var26, var12, var18);
        var10.addVertexWithUV(var22, var5 + 0.0D, var28, var16, var18);
        var10.addVertexWithUV(var22, var5 + (double)var9, var28, var16, var14);
    }

    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    public int getRenderId()
    {
        return AetherBlocks.tallAetherGrassRenderId;
    }
}
