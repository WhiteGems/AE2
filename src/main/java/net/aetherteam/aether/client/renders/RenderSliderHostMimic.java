package net.aetherteam.aether.client.renders;

import java.util.Collection;
import net.aetherteam.aether.entities.bosses.EntitySliderHostMimic;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSliderHostMimic extends RenderMinecartMobSpawner
{
    public RenderSliderHostMimic(ModelMinecart model, float f)
    {
        super(model, f);
        a(model);
    }

    protected int setMarkingBrightness(EntitySliderHostMimic host, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (host.isAwake())
        {
            if (!host.hasBeenAttacked)
            {
                loadTexture("/net/aetherteam/aether/client/sprites/mobs/host/hostblue_glow.png");
            }
            else
            {
                loadTexture("/net/aetherteam/aether/client/sprites/mobs/host/hostred_glow.png");
            }

            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (!host.getActivePotionEffects().isEmpty())
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

        return -1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setMarkingBrightness((EntitySliderHostMimic)entityliving, i, f);
    }
}

