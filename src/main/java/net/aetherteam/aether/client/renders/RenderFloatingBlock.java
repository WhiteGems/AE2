package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderFloatingBlock extends Render
{
    static RenderBlocks renderBlocks;

    public RenderFloatingBlock()
    {
        this.shadowSize = 0.5F;
        renderBlocks = new RenderBlocks();
    }

    public void renderFloatingBlock(EntityFloatingBlock var1, double var2, double var4, double var6, float var8, float var9)
    {
        Block var10 = Block.blocksList[var1.getBlockID()];
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        if (var10 != null)
        {
            Minecraft.getMinecraft().renderEngine.resetBoundTexture();
            this.loadTexture("/terrain.png");
            renderBlocks.renderBlockAsItem(var10, var1.getMetadata(), var1.getBrightness(var9));
        }

        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.renderFloatingBlock((EntityFloatingBlock) var1, var2, var4, var6, var8, var9);
    }
}
