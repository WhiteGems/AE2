package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderFloatingBlock extends RenderManager
{
    static RenderEngine c;

    public RenderFloatingBlock()
    {
        this.d = 0.5F;
        c = new RenderEngine();
    }

    public void renderFloatingBlock(EntityFloatingBlock entityFloatingBlock, double d, double d1, double d2, float f, float f1)
    {
        Block block = Block.blocksList[entityFloatingBlock.getBlockID()];
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        if (block != null)
        {
            Minecraft.getMinecraft().renderEngine.a();
            a("/terrain.png");
            c.a(block, entityFloatingBlock.getMetadata(), entityFloatingBlock.getBrightness(f1));
        }

        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        renderFloatingBlock((EntityFloatingBlock)entity, d, d1, d2, f, f1);
    }
}

