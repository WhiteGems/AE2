package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityCloudParachute;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCloudParachute extends Render
{
    static RenderBlocks renderBlocks;

    public RenderCloudParachute()
    {
        this.shadowSize = 0.5F;
        renderBlocks = new RenderBlocks();
    }

    public void renderCloud(EntityCloudParachute entitycloud, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();

        if (entitycloud.getRidingHandler().isBeingRidden())
        {
            EntityLivingBase entity = entitycloud.getRidingHandler().getRider();
            d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f1;
            d1 = entity.lastTickPosY - 1.68D + (entity.posY - entity.lastTickPosY) * (double)f1;
            d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f1;
            d -= RenderManager.renderPosX;
            d1 -= RenderManager.renderPosY;
            d2 -= RenderManager.renderPosZ;
            f = (float)((double)entity.prevRotationYaw + (double)(entity.rotationYaw - entity.prevRotationYaw) * d);
        }

        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
        this.func_110777_b(entitycloud);
        GL11.glEnable(GL11.GL_LIGHTING);
        renderBlocks.renderBlockAsItem(AetherBlocks.Aercloud, entitycloud.getColor() ? 2 : 0, entitycloud.getBrightness(f1));
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        this.renderCloud((EntityCloudParachute)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return null;
    }
}
