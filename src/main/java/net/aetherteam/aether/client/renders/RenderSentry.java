package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySentry;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSentry extends RenderMinecartMobSpawner
{
    public RenderSentry(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
        a(modelbase);
    }

    protected void a(EntityLiving entityliving, float f)
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

        if (sentry.getAwake())
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentry/eye.png");
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
        return a((EntitySentry)entityliving, i, f);
    }
}

