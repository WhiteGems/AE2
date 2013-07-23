package net.aetherteam.aether.client.renders;

import java.util.Collection;
import net.aetherteam.aether.client.models.ModelSentryGolem;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSentryGolem extends RenderGiantZombie
{
    protected ModelSentryGolem model;
    ModelThrowingCube Box;

    public RenderSentryGolem(ModelSentryGolem model, float f)
    {
        super(model, f);
        a(model);
        this.model = model;
        this.Box = new ModelThrowingCube();
    }

    public void func_82399_a(EntitySentryGolem e, double par2, double par4, double par6, float par8, float par9)
    {
        float p = e.progress;
        float j = 1.0F;

        if (e.getHandState() == 2)
        {
            if (p < 0.5D)
            {
                j = Math.min(1.0F - (e.getFire() - 30) / 30.0F, 0.9F);
            }
            else
            {
                return;
            }
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)par2, (float)par4 + 2.2F, (float)par6);
        GL11.glRotatef(e.renderYawOffset + 180.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScalef(j, j, j);
        GL11.glTranslated(-0.25D, 0.0D, -0.25D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var14 = 0.0625F;
        GL11.glTranslated(0.0D, Math.sin(p) * 2.400000095367432D - 2.0D, Math.sin(1.0F - p) * -1.399999976158142D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        loadDownloadableImageTexture((String)null, "/net/aetherteam/aether/client/sprites/mobs/sentry/sentry_lit.png");
        this.Box.render(e, var14);
        GL11.glPopMatrix();
    }

    protected int setMarkingBrightness(EntitySentryGolem golem, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolem/sentryGolemGreenGlow.png");
        this.model.isDefault = false;
        this.model.armState = golem.getHandState();
        float var4 = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        if (!golem.getActivePotionEffects().isEmpty())
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        return 1;
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
        func_82399_a((EntitySentryGolem)par1Entity, par2, par4, par6, par8, par9);
    }

    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return setMarkingBrightness((EntitySentryGolem)entityliving, i, f);
    }
}

