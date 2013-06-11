package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityCockatrice;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderCockatrice extends RenderLiving
{
    public RenderCockatrice(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
        this.renderPassModel = var1;
    }

    protected float getWingRotation(EntityCockatrice var1, float var2)
    {
        float var3 = var1.field_756_e + (var1.field_752_b - var1.field_756_e) * var2;
        float var4 = var1.field_757_d + (var1.destPos - var1.field_757_d) * var2;
        return (MathHelper.sin(var3) + 1.0F) * var4;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLiving var1, float var2)
    {
        return this.getWingRotation((EntityCockatrice) var1, var2);
    }

    protected void scalemoa()
    {
        GL11.glScalef(1.8F, 1.8F, 1.8F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        this.scalemoa();
    }

    protected int setMarkingBrightness(EntityCockatrice var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        } else
        {
            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/cockatrice/markings.png");
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (var1.getHasActivePotion())
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
        return this.setMarkingBrightness((EntityCockatrice) var1, var2, var3);
    }
}
