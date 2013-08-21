package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSlider extends RenderLiving
{
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation("aether", "textures/bosses/slider/sliderAwake_red.png");
    private static final ResourceLocation TEXTURE_AWAKE = new ResourceLocation("aether", "textures/bosses/slider/sliderAwake.png");
    private static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation("aether", "textures/bosses/slider/sliderSleep.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("aether", "textures/bosses/slider/sliderAwakeGlow.png");
    private static final ResourceLocation TEXTURE_GLOW_RED = new ResourceLocation("aether", "textures/bosses/slider/sliderAwakeGlow_red.png");

    public RenderSlider(ModelBase ms, float f)
    {
        super(ms, f);
        this.renderPassModel = ms;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        EntitySlider e1 = (EntitySlider)entityliving;

        if (e1.harvey > 0.01F)
        {
            GL11.glRotatef(e1.harvey * -30.0F, (float)e1.rennis, 0.0F, (float)e1.dennis);
        }
    }

    protected int setSliderEyeBrightness(EntitySlider slider, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            if (slider.getAwake())
            {
                if (slider.getCritical())
                {
                    this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW_RED);
                }
                else
                {
                    this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW);
                }

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
        return this.setSliderEyeBrightness((EntitySlider)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        EntitySlider slider = (EntitySlider)entity;
        return slider.getCritical() ? TEXTURE_RED : (slider.getAwake() ? TEXTURE_AWAKE : TEXTURE_SLEEP);
    }
}
