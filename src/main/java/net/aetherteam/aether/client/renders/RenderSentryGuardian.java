package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelSentryGolemBoss;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSentryGuardian extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentrygolemboss/sentryGolemBoss.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation("aether", "textures/mobs/sentrygolemboss/sentryGolemBoss.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("aether", "textures/mobs/sentrygolemboss/glow.png");
    private static final ResourceLocation TEXTURE_GLOW_RED = new ResourceLocation("aether", "textures/mobs/sentrygolemboss/glow_red.png");

    public RenderSentryGuardian(ModelSentryGolemBoss model, float f)
    {
        super(model, f);
        this.setRenderPassModel(model);
    }

    protected int setMarkingBrightness(EntitySentryGuardian golem, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            if (!golem.getHasBeenAttacked())
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW);
            }
            else
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW_RED);
            }

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
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setMarkingBrightness((EntitySentryGuardian)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntitySentryGuardian)entity).getHasBeenAttacked() ? TEXTURE_RED : TEXTURE;
    }
}
