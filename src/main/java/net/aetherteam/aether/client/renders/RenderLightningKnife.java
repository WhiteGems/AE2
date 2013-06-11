package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityLightningKnife;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLightningKnife extends Render
{
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderKnife((EntityLightningKnife) var1, var2, var4, var6, var8, var9);
    }

    public void doRenderKnife(EntityLightningKnife var1, double var2, double var4, double var6, float var8, float var9)
    {
        String var10 = "/gui/items.png";
        Icon var11 = AetherItems.LightningKnife.getIconFromDamage(0);
        float var12 = var11.getMinU();
        float var13 = var11.getMaxU();
        float var14 = var11.getMinV();
        float var15 = var11.getMaxV();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4, (float) var6);
        GL11.glRotatef(var8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        this.loadTexture(var10);
        Tessellator var16 = Tessellator.instance;
        float var17 = 1.0F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var18 = 0.0625F;
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        var16.startDrawingQuads();
        var16.setNormal(0.0F, 0.0F, 1.0F);
        var16.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) var13, (double) var15);
        var16.addVertexWithUV((double) var17, 0.0D, 0.0D, (double) var12, (double) var15);
        var16.addVertexWithUV((double) var17, 0.0D, 1.0D, (double) var12, (double) var14);
        var16.addVertexWithUV(0.0D, 0.0D, 1.0D, (double) var13, (double) var14);
        var16.draw();
        var16.startDrawingQuads();
        var16.setNormal(0.0F, 0.0F, -1.0F);
        var16.addVertexWithUV(0.0D, (double) (0.0F - var18), 1.0D, (double) var13, (double) var14);
        var16.addVertexWithUV((double) var17, (double) (0.0F - var18), 1.0D, (double) var12, (double) var14);
        var16.addVertexWithUV((double) var17, (double) (0.0F - var18), 0.0D, (double) var12, (double) var15);
        var16.addVertexWithUV(0.0D, (double) (0.0F - var18), 0.0D, (double) var13, (double) var15);
        var16.draw();
        var16.startDrawingQuads();
        var16.setNormal(-1.0F, 0.0F, 0.0F);
        int var19;
        float var21;
        float var20;
        float var22;

        for (var19 = 0; var19 < 16; ++var19)
        {
            var20 = (float) var19 / 16.0F;
            var21 = var13 + (var12 - var13) * var20 - 0.001953125F;
            var22 = var17 * var20;
            var16.addVertexWithUV((double) var22, (double) (0.0F - var18), 0.0D, (double) var21, (double) var15);
            var16.addVertexWithUV((double) var22, 0.0D, 0.0D, (double) var21, (double) var15);
            var16.addVertexWithUV((double) var22, 0.0D, 1.0D, (double) var21, (double) var14);
            var16.addVertexWithUV((double) var22, (double) (0.0F - var18), 1.0D, (double) var21, (double) var14);
        }

        var16.draw();
        var16.startDrawingQuads();
        var16.setNormal(1.0F, 0.0F, 0.0F);

        for (var19 = 0; var19 < 16; ++var19)
        {
            var20 = (float) var19 / 16.0F;
            var21 = var13 + (var12 - var13) * var20 - 0.001953125F;
            var22 = var17 * var20 + 0.0625F;
            var16.addVertexWithUV((double) var22, (double) (0.0F - var18), 1.0D, (double) var21, (double) var14);
            var16.addVertexWithUV((double) var22, 0.0D, 1.0D, (double) var21, (double) var14);
            var16.addVertexWithUV((double) var22, 0.0D, 0.0D, (double) var21, (double) var15);
            var16.addVertexWithUV((double) var22, (double) (0.0F - var18), 0.0D, (double) var21, (double) var15);
        }

        var16.draw();
        var16.startDrawingQuads();
        var16.setNormal(0.0F, 1.0F, 0.0F);

        for (var19 = 0; var19 < 16; ++var19)
        {
            var20 = (float) var19 / 16.0F;
            var21 = var15 + (var14 - var15) * var20 - 0.001953125F;
            var22 = var17 * var20 + 0.0625F;
            var16.addVertexWithUV(0.0D, 0.0D, (double) var22, (double) var13, (double) var21);
            var16.addVertexWithUV((double) var17, 0.0D, (double) var22, (double) var12, (double) var21);
            var16.addVertexWithUV((double) var17, (double) (0.0F - var18), (double) var22, (double) var12, (double) var21);
            var16.addVertexWithUV(0.0D, (double) (0.0F - var18), (double) var22, (double) var13, (double) var21);
        }

        var16.draw();
        var16.startDrawingQuads();
        var16.setNormal(0.0F, -1.0F, 0.0F);

        for (var19 = 0; var19 < 16; ++var19)
        {
            var20 = (float) var19 / 16.0F;
            var21 = var15 + (var14 - var15) * var20 - 0.001953125F;
            var22 = var17 * var20;
            var16.addVertexWithUV((double) var17, 0.0D, (double) var22, (double) var12, (double) var21);
            var16.addVertexWithUV(0.0D, 0.0D, (double) var22, (double) var13, (double) var21);
            var16.addVertexWithUV(0.0D, (double) (0.0F - var18), (double) var22, (double) var13, (double) var21);
            var16.addVertexWithUV((double) var17, (double) (0.0F - var18), (double) var22, (double) var12, (double) var21);
        }

        var16.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
