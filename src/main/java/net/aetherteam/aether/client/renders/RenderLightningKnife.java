package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityLightningKnife;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLightningKnife extends Render
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/aechorplant/aechorplant.png");

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderKnife((EntityLightningKnife)var1, var2, var4, var6, var8, var9);
    }

    public void doRenderKnife(EntityLightningKnife arr, double d, double d1, double d2, float yaw, float time)
    {
        Icon icon = AetherItems.LightningKnife.getIconFromDamage(0);
        float texMinX = icon.getMinU();
        float texMaxX = icon.getMaxU();
        float texMinY = icon.getMinV();
        float texMaxY = icon.getMaxV();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(arr.prevRotationPitch + (arr.rotationPitch - arr.prevRotationPitch) * time), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.func_110577_a(TextureMap.field_110576_c);
        Tessellator tessellator = Tessellator.instance;
        float f4 = 1.0F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f8 = 0.0625F;
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)texMaxX, (double)texMaxY);
        tessellator.addVertexWithUV((double)f4, 0.0D, 0.0D, (double)texMinX, (double)texMaxY);
        tessellator.addVertexWithUV((double)f4, 0.0D, 1.0D, (double)texMinX, (double)texMinY);
        tessellator.addVertexWithUV(0.0D, 0.0D, 1.0D, (double)texMaxX, (double)texMinY);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(0.0D, (double)(0.0F - f8), 1.0D, (double)texMaxX, (double)texMinY);
        tessellator.addVertexWithUV((double)f4, (double)(0.0F - f8), 1.0D, (double)texMinX, (double)texMinY);
        tessellator.addVertexWithUV((double)f4, (double)(0.0F - f8), 0.0D, (double)texMinX, (double)texMaxY);
        tessellator.addVertexWithUV(0.0D, (double)(0.0F - f8), 0.0D, (double)texMaxX, (double)texMaxY);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        float f12;
        int l;
        float f20;
        float f16;

        for (l = 0; l < 16; ++l)
        {
            f12 = (float)l / 16.0F;
            f16 = texMaxX + (texMinX - texMaxX) * f12 - 0.001953125F;
            f20 = f4 * f12;
            tessellator.addVertexWithUV((double)f20, (double)(0.0F - f8), 0.0D, (double)f16, (double)texMaxY);
            tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)texMaxY);
            tessellator.addVertexWithUV((double)f20, 0.0D, 1.0D, (double)f16, (double)texMinY);
            tessellator.addVertexWithUV((double)f20, (double)(0.0F - f8), 1.0D, (double)f16, (double)texMinY);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (l = 0; l < 16; ++l)
        {
            f12 = (float)l / 16.0F;
            f16 = texMaxX + (texMinX - texMaxX) * f12 - 0.001953125F;
            f20 = f4 * f12 + 0.0625F;
            tessellator.addVertexWithUV((double)f20, (double)(0.0F - f8), 1.0D, (double)f16, (double)texMinY);
            tessellator.addVertexWithUV((double)f20, 0.0D, 1.0D, (double)f16, (double)texMinY);
            tessellator.addVertexWithUV((double)f20, 0.0D, 0.0D, (double)f16, (double)texMaxY);
            tessellator.addVertexWithUV((double)f20, (double)(0.0F - f8), 0.0D, (double)f16, (double)texMaxY);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (l = 0; l < 16; ++l)
        {
            f12 = (float)l / 16.0F;
            f16 = texMaxY + (texMinY - texMaxY) * f12 - 0.001953125F;
            f20 = f4 * f12 + 0.0625F;
            tessellator.addVertexWithUV(0.0D, 0.0D, (double)f20, (double)texMaxX, (double)f16);
            tessellator.addVertexWithUV((double)f4, 0.0D, (double)f20, (double)texMinX, (double)f16);
            tessellator.addVertexWithUV((double)f4, (double)(0.0F - f8), (double)f20, (double)texMinX, (double)f16);
            tessellator.addVertexWithUV(0.0D, (double)(0.0F - f8), (double)f20, (double)texMaxX, (double)f16);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);

        for (l = 0; l < 16; ++l)
        {
            f12 = (float)l / 16.0F;
            f16 = texMaxY + (texMinY - texMaxY) * f12 - 0.001953125F;
            f20 = f4 * f12;
            tessellator.addVertexWithUV((double)f4, 0.0D, (double)f20, (double)texMinX, (double)f16);
            tessellator.addVertexWithUV(0.0D, 0.0D, (double)f20, (double)texMaxX, (double)f16);
            tessellator.addVertexWithUV(0.0D, (double)(0.0F - f8), (double)f20, (double)texMaxX, (double)f16);
            tessellator.addVertexWithUV((double)f4, (double)(0.0F - f8), (double)f20, (double)texMinX, (double)f16);
        }

        tessellator.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
