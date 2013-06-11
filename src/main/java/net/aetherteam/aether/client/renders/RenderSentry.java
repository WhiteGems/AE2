package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntitySentry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSentry extends RenderLiving
{
    public RenderSentry(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        float var3 = 1.75F;
        GL11.glScalef(var3, var3, var3);
    }

    protected int a(EntitySentry var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        } else
        {
            if (var1.getAwake())
            {
                this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentry/eye.png");
                float var4 = 1.0F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                char var5 = 61680;
                int var6 = var5 % 65536;
                int var7 = var5 / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
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
        return this.a((EntitySentry) var1, var2, var3);
    }
}
