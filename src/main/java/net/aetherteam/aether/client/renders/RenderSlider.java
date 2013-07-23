package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSlider extends RenderMinecartMobSpawner
{
    public RenderSlider(ModelMinecart ms, float f)
    {
        super(ms, f);
        this.j = ms;
    }

    protected void a(EntityLiving entityliving, float f)
    {
        EntitySlider e1 = (EntitySlider)entityliving;

        if (e1.harvey > 0.01F)
        {
            GL11.glRotatef(e1.harvey * -30.0F, e1.rennis, 0.0F, e1.dennis);
        }
    }

    protected int setSliderEyeBrightness(EntitySlider slider, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (slider.getAwake())
        {
            if (slider.getCritical())
            {
                loadTexture("/net/aetherteam/aether/client/sprites/bosses/slider/sliderAwakeGlow_red.png");
            }
            else
            {
                loadTexture("/net/aetherteam/aether/client/sprites/bosses/slider/sliderAwakeGlow.png");
            }

            float f1 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            int j = 61680;
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
        }

        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setSliderEyeBrightness((EntitySlider)entityliving, i, f);
    }
}

