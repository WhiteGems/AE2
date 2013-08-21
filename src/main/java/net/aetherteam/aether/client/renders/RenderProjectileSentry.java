package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityProjectileSentry;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderProjectileSentry extends Render
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentry/sentry_lit.png");
    private ModelThrowingCube Box = new ModelThrowingCube();

    public void Render(EntityProjectileSentry par1ProjectileSentry, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        float var4 = 1.0F;
        char var5 = 61680;
        int var6 = var5 % 65536;
        int var7 = var5 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(par1ProjectileSentry.rotationYaw + 180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.func_110777_b(par1ProjectileSentry);
        this.Box.render(par1ProjectileSentry, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.Render((EntityProjectileSentry)par1Entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
