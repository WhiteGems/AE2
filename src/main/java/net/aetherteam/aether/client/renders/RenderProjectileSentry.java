package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityProjectileSentry;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderProjectileSentry extends Render
{
    private ModelThrowingCube Box = new ModelThrowingCube();

    public void Render(EntityProjectileSentry var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        float var10 = 1.0F;
        char var11 = 61680;
        int var12 = var11 % 65536;
        int var13 = var11 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var12 / 1.0F, (float)var13 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var10);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(var1.rotationYaw + 180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.loadDownloadableImageTexture((String)null, "/net/aetherteam/aether/client/sprites/mobs/sentry/sentry_lit.png");
        this.Box.render(var1, 0.0625F);
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
        this.Render((EntityProjectileSentry)var1, var2, var4, var6, var8, var9);
    }
}
