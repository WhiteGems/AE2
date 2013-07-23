package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityNotchWave;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderNotchWave extends Render
{
    private float field_77002_a;

    public RenderNotchWave(float var1)
    {
        this.field_77002_a = var1;
    }

    public void doRenderNotchWave(EntityNotchWave var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var10 = this.field_77002_a;
        GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
        this.loadTexture("/net/aetherteam/aether/client/sprites/projectiles/notchwave/notchwave.png");
        Tessellator var11 = Tessellator.instance;
        float var12 = 1.0F;
        float var13 = 0.5F;
        float var14 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var11.startDrawingQuads();
        var11.setNormal(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV((double)(0.0F - var13), (double)(0.0F - var14), 0.0D, 0.0D, 0.0D);
        var11.addVertexWithUV((double)(var12 - var13), (double)(0.0F - var14), 0.0D, 0.0D, 1.0D);
        var11.addVertexWithUV((double)(var12 - var13), (double)(1.0F - var14), 0.0D, 1.0D, 1.0D);
        var11.addVertexWithUV((double)(0.0F - var13), (double)(1.0F - var14), 0.0D, 1.0D, 0.0D);
        var11.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
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
        this.doRenderNotchWave((EntityNotchWave)var1, var2, var4, var6, var8, var9);
    }
}
