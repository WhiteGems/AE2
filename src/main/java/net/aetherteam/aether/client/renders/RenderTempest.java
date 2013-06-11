package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelTempest;
import net.aetherteam.aether.entities.EntityTempest;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderTempest extends RenderLiving
{
    public ModelTempest tempestModel;

    public RenderTempest(ModelTempest var1)
    {
        super(var1, 0.5F);
        this.setRenderPassModel(var1);
        this.tempestModel = var1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        EntityTempest var3 = (EntityTempest) var1;
        float var4 = (float) Math.sin((double) var3.sinage);
        float var5;

        if (var3.hurtTime > 0)
        {
            var4 *= 0.45F;
            var4 -= 0.125F;
            var5 = 1.75F + (float) Math.sin((double) (var3.sinage + 2.0F)) * 1.5F;
        } else
        {
            var4 *= 0.25F;
            var5 = 1.75F + (float) Math.sin((double) (var3.sinage + 2.0F)) * 1.5F;
        }

        this.tempestModel.sinage = var4;
        this.tempestModel.sinage2 = var5;
        this.shadowSize = 0.25F;
        GL11.glScalef(1.5F, 1.5F, 1.5F);
    }

    protected int setMarkingBrightness(EntityTempest var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        } else
        {
            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/tempest/glow.png");
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDepthMask(false);
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
        return this.setMarkingBrightness((EntityTempest) var1, var2, var3);
    }
}
