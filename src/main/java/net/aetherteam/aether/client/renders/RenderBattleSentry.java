package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityBattleSentry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderBattleSentry extends RenderLiving
{
    public RenderBattleSentry(ModelBase var1, float var2)
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

    protected int a(EntityBattleSentry var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        }
        else
        {
            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentryMelee/eye.png");
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDepthMask(false);
            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);

            if (var1.isInView() && !var1.getHasBeenAttacked())
            {
                GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            }

            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.a((EntityBattleSentry)var1, var2, var3);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLiving var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        if (!((EntityBattleSentry)var1).isInView() || ((EntityBattleSentry)var1).getHasBeenAttacked())
        {
            super.renderModel(var1, var2, var3, var4, var5, var6, var7);
        }

        if (((EntityBattleSentry)var1).isInView() && !((EntityBattleSentry)var1).getHasBeenAttacked())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            super.renderModel(var1, var2, var3, var4, var5, var6, var7);
            GL11.glPopMatrix();
        }
    }
}
