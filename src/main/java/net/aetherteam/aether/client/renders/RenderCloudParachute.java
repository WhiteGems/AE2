package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityCloudParachute;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderCloudParachute extends Render
{
    static RenderBlocks renderBlocks;

    public RenderCloudParachute()
    {
        this.shadowSize = 0.5F;
        renderBlocks = new RenderBlocks();
    }

    public void renderCloud(EntityCloudParachute var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();

        if (var1.getRidingHandler().isBeingRidden())
        {
            EntityLiving var10 = var1.getRidingHandler().getRider();
            var2 = var10.lastTickPosX + (var10.posX - var10.lastTickPosX) * (double)var9;
            var4 = var10.lastTickPosY - 1.68D + (var10.posY - var10.lastTickPosY) * (double)var9;
            var6 = var10.lastTickPosZ + (var10.posZ - var10.lastTickPosZ) * (double)var9;
            var2 -= RenderManager.renderPosX;
            var4 -= RenderManager.renderPosY;
            var6 -= RenderManager.renderPosZ;
            var8 = (float)((double)var10.prevRotationYaw + (double)(var10.rotationYaw - var10.prevRotationYaw) * var2);
        }

        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glRotatef(180.0F - var8, 0.0F, 1.0F, 0.0F);
        this.loadTexture("/net/aetherteam/aether/client/sprites/aetherBlocks.png");
        GL11.glEnable(GL11.GL_LIGHTING);
        renderBlocks.renderBlockAsItem(AetherBlocks.Aercloud, var1.getColor() ? 2 : 0, var1.getBrightness(var9));
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
        this.renderCloud((EntityCloudParachute)var1, var2, var4, var6, var8, var9);
    }
}
