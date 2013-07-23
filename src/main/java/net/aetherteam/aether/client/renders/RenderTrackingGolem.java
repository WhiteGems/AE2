package net.aetherteam.aether.client.renders;

import java.util.Collection;
import net.aetherteam.aether.entities.EntityTrackingGolem;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderTrackingGolem extends RenderGiantZombie
{
    public RenderTrackingGolem(ModelGhast model, float f)
    {
        super(model, f);
        a(model);
    }

    protected int setMarkingBrightness(EntityTrackingGolem golem, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (!golem.getSeenEnemy())
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolem/eyes.png");
        }
        else
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolem/eyes_red.png");
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        return 1;
    }

    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return setMarkingBrightness((EntityTrackingGolem)entityliving, i, f);
    }
}

