package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.aetherteam.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ChestItemRenderHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderAetherTallGrass
    implements ISimpleBlockRenderingHandler
{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderEngine renderer)
    {
        if (modelID == getRenderId())
        {
            Rect2i tessellator = Rect2i.rectX;
            tessellator.b();
            tessellator.b(0.0F, -1.0F, 0.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            renderer.a(block, metadata, -0.5D, -0.5D, -0.5D, 1.0F);
            tessellator.getRectX();
        }
    }

    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderEngine renderer)
    {
        if (modelID == getRenderId())
        {
            Rect2i tessellator = Rect2i.rectX;
            tessellator.c(block.getMixedBrightnessForBlock(world, x, y, z));
            float f = 1.0F;
            int l = block.colorMultiplier(world, x, y, z);
            float f1 = (l >> 16 & 0xFF) / 255.0F;
            float f2 = (l >> 8 & 0xFF) / 255.0F;
            float f3 = (l & 0xFF) / 255.0F;

            if (ChestItemRenderHelper.instance)
            {
                float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f4;
                f2 = f5;
                f3 = f6;
            }

            tessellator.a(f * f1, f * f2, f * f3);
            double d0 = x;
            double d1 = y;
            double d2 = z;
            long i1 = x * 3129871 ^ z * 116129781L ^ y;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d0 += ((float)(i1 >> 16 & 0xF) / 15.0F - 0.5D) * 0.5D;
            d1 += ((float)(i1 >> 20 & 0xF) / 15.0F - 1.0D) * 0.2D;
            d2 += ((float)(i1 >> 24 & 0xF) / 15.0F - 0.5D) * 0.5D;
            drawCrossedSquares(block, world.getBlockMetadata(x, y, z), d0, d1, d2, 1.0F);
            return true;
        }

        return false;
    }

    public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9)
    {
        Rect2i tessellator = Rect2i.rectX;
        Icon icon = par1Block.getIcon(0, par2);
        double d3 = icon.getMinU();
        double d4 = icon.getMinV();
        double d5 = icon.getMaxU();
        double d6 = icon.getMaxV();
        double d7 = 0.45D * par9;
        double d8 = par3 + 0.5D - d7;
        double d9 = par3 + 0.5D + d7;
        double d10 = par7 + 0.5D - d7;
        double d11 = par7 + 0.5D + d7;
        tessellator.a(d8, par5 + par9, d10, d3, d4);
        tessellator.a(d8, par5 + 0.0D, d10, d3, d6);
        tessellator.a(d9, par5 + 0.0D, d11, d5, d6);
        tessellator.a(d9, par5 + par9, d11, d5, d4);
        tessellator.a(d9, par5 + par9, d11, d3, d4);
        tessellator.a(d9, par5 + 0.0D, d11, d3, d6);
        tessellator.a(d8, par5 + 0.0D, d10, d5, d6);
        tessellator.a(d8, par5 + par9, d10, d5, d4);
        tessellator.a(d8, par5 + par9, d11, d3, d4);
        tessellator.a(d8, par5 + 0.0D, d11, d3, d6);
        tessellator.a(d9, par5 + 0.0D, d10, d5, d6);
        tessellator.a(d9, par5 + par9, d10, d5, d4);
        tessellator.a(d9, par5 + par9, d10, d3, d4);
        tessellator.a(d9, par5 + 0.0D, d10, d3, d6);
        tessellator.a(d8, par5 + 0.0D, d11, d5, d6);
        tessellator.a(d8, par5 + par9, d11, d5, d4);
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

