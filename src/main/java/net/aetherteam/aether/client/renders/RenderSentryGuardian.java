package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelSentryGolemBoss;
import net.aetherteam.aether.entities.bosses.EntitySentryGuardian;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSentryGuardian extends RenderLiving
{
    public RenderSentryGuardian(ModelSentryGolemBoss var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
    }

    protected int setMarkingBrightness(EntitySentryGuardian var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        } else
        {
            if (!var1.getHasBeenAttacked())
            {
                this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/glow.png");
            } else
            {
                this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentrygolemboss/glow_red.png");
            }

            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (!var1.getActivePotionEffects().isEmpty())
            {
                GL11.glDepthMask(false);
            } else
            {
                GL11.glDepthMask(true);
            }

            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setMarkingBrightness((EntitySentryGuardian) var1, var2, var3);
    }
}
