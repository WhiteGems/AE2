package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySentry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSentry extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentry/sentry.png");
    private static final ResourceLocation TEXTURE_LIT = new ResourceLocation("aether", "textures/mobs/sentry/sentry_lit.png");
    private static final ResourceLocation TEXTURE_EYE = new ResourceLocation("aether", "textures/mobs/sentry/eye.png");

    public RenderSentry(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.setRenderPassModel(modelbase);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        float f1 = 1.75F;
        GL11.glScalef(f1, f1, f1);
    }

    protected int a(EntitySentry sentry, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            if (sentry.getAwake())
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_EYE);
                float f1 = 1.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                char j = 61680;
                int k = j % 65536;
                int l = j / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            }

            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.a((EntitySentry)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntitySentry)entity).getEntityToAttack() == null ? TEXTURE : TEXTURE_LIT;
    }
}
