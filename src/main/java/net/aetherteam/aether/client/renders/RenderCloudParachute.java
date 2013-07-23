package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.blocks.AetherBlocks;
import net.aetherteam.aether.entities.EntityCloudParachute;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderCloudParachute extends RenderManager
{
    static RenderEngine c;

    public RenderCloudParachute()
    {
        this.d = 0.5F;
        c = new RenderEngine();
    }

    public void renderCloud(EntityCloudParachute entitycloud, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();

        if (entitycloud.getRidingHandler().isBeingRidden())
        {
            EntityLiving entity = entitycloud.getRidingHandler().getRider();
            d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * f1;
            d1 = entity.lastTickPosY - 1.68D + (entity.posY - entity.lastTickPosY) * f1;
            d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * f1;
            d -= RenderEnderman.b;
            d1 -= RenderEnderman.c;
            d2 -= RenderEnderman.d;
            f = (float)(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * d);
        }

        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
        a("/net/aetherteam/aether/client/sprites/aetherBlocks.png");
        GL11.glEnable(GL11.GL_LIGHTING);
        c.a(AetherBlocks.Aercloud, entitycloud.getColor() ? 2 : 0, entitycloud.getBrightness(f1));
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        renderCloud((EntityCloudParachute)entity, d, d1, d2, f, f1);
    }
}

