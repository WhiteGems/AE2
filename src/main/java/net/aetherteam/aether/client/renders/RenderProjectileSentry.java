package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityProjectileSentry;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderProjectileSentry extends RenderManager
{
    private ModelThrowingCube Box;

    public RenderProjectileSentry()
    {
        this.Box = new ModelThrowingCube();
    }

    public void Render(EntityProjectileSentry par1ProjectileSentry, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        float var4 = 1.0F;
        char var5 = 61680;
        int var6 = var5 % 65536;
        int var7 = var5 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(par1ProjectileSentry.rotationYaw + 180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        a((String)null, "/net/aetherteam/aether/client/sprites/mobs/sentry/sentry_lit.png");
        this.Box.render(par1ProjectileSentry, 0.0625F);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render((EntityProjectileSentry)par1Entity, par2, par4, par6, par8, par9);
    }
}

