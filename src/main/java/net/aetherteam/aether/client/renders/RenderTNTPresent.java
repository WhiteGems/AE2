package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityTNTPresent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderTNTPresent extends RenderManager
{
    private RenderEngine blockRenderer = new RenderEngine();

    public RenderTNTPresent()
    {
        this.d = 0.5F;
    }

    public void renderPrimedTNT(EntityTNTPresent par1EntityTNTPrimed, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);

        if (par1EntityTNTPrimed.fuse - par9 + 1.0F < 10.0F)
        {
            float var10 = 1.0F - (par1EntityTNTPrimed.fuse - par9 + 1.0F) / 10.0F;

            if (var10 < 0.0F)
            {
                var10 = 0.0F;
            }

            if (var10 > 1.0F)
            {
                var10 = 1.0F;
            }

            var10 *= var10;
            var10 *= var10;
            float var11 = 1.0F + var10 * 0.3F;
            GL11.glScalef(1.0F, 1.0F, 1.0F);
        }

        float var10 = (1.0F - (par1EntityTNTPrimed.fuse - par9 + 1.0F) / 100.0F) * 0.8F;
        a("/terrain.png");
        this.blockRenderer.a(AetherBlocks.Present, 0, par1EntityTNTPrimed.getBrightness(par9));

        if (par1EntityTNTPrimed.fuse / 5 % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var10);
            this.blockRenderer.a(Block.tnt, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderPrimedTNT((EntityTNTPresent)par1Entity, par2, par4, par6, par8, par9);
    }
}

