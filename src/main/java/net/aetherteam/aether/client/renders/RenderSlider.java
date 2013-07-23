package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSlider extends RenderLiving
{
    public RenderSlider(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.renderPassModel = var1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        EntitySlider var3 = (EntitySlider)var1;

        if (var3.harvey > 0.01F)
        {
            GL11.glRotatef(var3.harvey * -30.0F, (float)var3.rennis, 0.0F, (float)var3.dennis);
        }
    }

    protected int setSliderEyeBrightness(EntitySlider var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        }
        else
        {
            if (var1.getAwake())
            {
                if (var1.getCritical())
                {
                    this.loadTexture("/net/aetherteam/aether/client/sprites/bosses/slider/sliderAwakeGlow_red.png");
                }
                else
                {
                    this.loadTexture("/net/aetherteam/aether/client/sprites/bosses/slider/sliderAwakeGlow.png");
                }

                float var4 = 1.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                char var5 = 61680;
                int var6 = var5 % 65536;
                int var7 = var5 / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            }

            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setSliderEyeBrightness((EntitySlider)var1, var2, var3);
    }
}
