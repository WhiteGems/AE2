package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelSentryGolem;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSentryGolem extends RenderBiped
{
    protected ModelSentryGolem model;
    ModelThrowingCube Box;

    public RenderSentryGolem(ModelSentryGolem var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
        this.model = var1;
        this.Box = new ModelThrowingCube();
    }

    public void func_82399_a(EntitySentryGolem var1, double var2, double var4, double var6, float var8, float var9)
    {
        float var10 = var1.progress;
        float var11 = 1.0F;

        if (var1.getHandState() == 2)
        {
            if ((double)var10 >= 0.5D)
            {
                return;
            }

            var11 = Math.min(1.0F - (float)(var1.getFire() - 30) / 30.0F, 0.9F);
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)var2, (float)var4 + 2.2F, (float)var6);
        GL11.glRotatef(var1.renderYawOffset + 180.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScalef(var11, var11, var11);
        GL11.glTranslated(-0.25D, 0.0D, -0.25D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var12 = 0.0625F;
        GL11.glTranslated(0.0D, Math.sin((double)var10) * 2.4000000953674316D - 2.0D, Math.sin((double)(1.0F - var10)) * -1.399999976158142D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.loadDownloadableImageTexture((String)null, "/net/aetherteam/aether/client/sprites/mobs/sentry/sentry_lit.png");
        this.Box.render(var1, var12);
        GL11.glPopMatrix();
    }

    protected int setMarkingBrightness(EntitySentryGolem var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        }
        else
        {
            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolemGreenGlow.png");
            this.model.isDefault = false;
            this.model.armState = var1.getHandState();
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (!var1.getActivePotionEffects().isEmpty())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderLiving((EntityLiving)var1, var2, var4, var6, var8, var9);
        this.func_82399_a((EntitySentryGolem)var1, var2, var4, var6, var8, var9);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setMarkingBrightness((EntitySentryGolem)var1, var2, var3);
    }
}
