package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTrackingGolem extends RenderBiped
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentrygolem/sentryGolem.png");
    public static final ResourceLocation TEXTURE_RED = new ResourceLocation("aether", "textures/mobs/sentrygolem/sentryGolem_red.png");
    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("aether", "textures/mobs/sentrygolem/eyes.png");
    private static final ResourceLocation TEXTURE_EYES_RED = new ResourceLocation("aether", "textures/mobs/sentrygolem/eyes_red.png");

    public RenderTrackingGolem(ModelBiped model, float f)
    {
        super(model, f);
        this.setRenderPassModel(model);
    }

    protected int setMarkingBrightness(EntityTrackingGolem golem, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            if (!golem.getSeenEnemy())
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_EYES);
            }
            else
            {
                this.renderManager.renderEngine.func_110577_a(TEXTURE_EYES_RED);
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
        return this.setMarkingBrightness((EntityTrackingGolem)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntityTrackingGolem)entity).getSeenEnemy() ? TEXTURE_RED : TEXTURE;
    }
}
