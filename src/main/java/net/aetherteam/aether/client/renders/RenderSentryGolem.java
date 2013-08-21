package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelSentryGolem;
import net.aetherteam.aether.entities.EntitySentryGolem;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSentryGolem extends RenderBiped
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentrygolem/sentryGolemGreen.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("aether", "textures/mobs/sentrygolem/sentryGolemGreenGlow.png");
    private static final ResourceLocation TEXTURE_SENTRY_LIT = new ResourceLocation("aether", "textures/mobs/sentry/sentry_lit.png");
    protected ModelSentryGolem model;
    protected ModelThrowingCube box;

    public RenderSentryGolem(ModelSentryGolem model, float f)
    {
        super(model, f);
        this.setRenderPassModel(model);
        this.model = model;
        this.box = new ModelThrowingCube();
    }

    public void func_82399_a(EntitySentryGolem e, double par2, double par4, double par6, float par8, float par9)
    {
        float p = e.progress;
        float j = 1.0F;

        if (e.getHandState() == 2)
        {
            if ((double)p >= 0.5D)
            {
                return;
            }

            j = Math.min(1.0F - (float)(e.getFire() - 30) / 30.0F, 0.9F);
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float)par2, (float)par4 + 2.2F, (float)par6);
        GL11.glRotatef(e.renderYawOffset + 180.0F, 0.0F, -1.0F, 0.0F);
        GL11.glScalef(j, j, j);
        GL11.glTranslated(-0.25D, 0.0D, -0.25D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float var14 = 0.0625F;
        GL11.glTranslated(0.0D, Math.sin((double)p) * 2.4000000953674316D - 2.0D, Math.sin((double)(1.0F - p)) * -1.399999976158142D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        this.renderManager.renderEngine.func_110577_a(TEXTURE_SENTRY_LIT);
        this.box.render(e, var14);
        GL11.glPopMatrix();
    }

    protected int setMarkingBrightness(EntitySentryGolem golem, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW);
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
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
        this.func_82399_a((EntitySentryGolem)par1Entity, par2, par4, par6, par8, par9);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setMarkingBrightness((EntitySentryGolem)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
