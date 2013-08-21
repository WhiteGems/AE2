package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityFloatingBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderFloatingBlock extends Render
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/aechorplant/aechorplant.png");
    static RenderBlocks renderBlocks;

    public RenderFloatingBlock()
    {
        this.shadowSize = 0.5F;
        renderBlocks = new RenderBlocks();
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
            this.renderManager.renderEngine.func_110577_a(TextureMap.field_110575_b);
            renderBlocks.renderBlockAsItem(block, entityFloatingBlock.getMetadata(), entityFloatingBlock.getBrightness(f1));
        }

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
        this.renderFloatingBlock((EntityFloatingBlock)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return null;
    }
}
