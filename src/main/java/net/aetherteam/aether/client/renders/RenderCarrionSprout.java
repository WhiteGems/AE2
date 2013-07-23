package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCarrionSprout;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.aetherteam.aether.entities.EntityCarrionSprout;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderCarrionSprout extends RenderLiving
{
    public ModelCarrionSprout plantModel;

    public RenderCarrionSprout(ModelCarrionSprout var1, float var2)
    {
        super(var1, var2);
        this.setRenderPassModel(var1);
        this.plantModel = var1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        EntityCarrionSprout var3 = (EntityCarrionSprout)var1;
        float var4 = (float)Math.sin((double)var3.sinage);
        float var5;

        if (var3.hurtTime > 0)
        {
            var4 *= 0.45F;
            var4 -= 0.125F;
            var5 = 1.75F + (float)Math.sin((double)(var3.sinage + 2.0F)) * 1.5F;
        }
        else
        {
            var4 *= 0.25F;
            var5 = 1.75F + (float)Math.sin((double)(var3.sinage + 2.0F)) * 1.5F;
        }

        this.plantModel.sinage = var4;
        this.plantModel.sinage2 = var5;
        this.shadowSize = 0.25F;
    }

    protected int a(EntityAechorPlant var1, int var2, float var3)
    {
        if (var2 != 0)
        {
            return -1;
        }
        else
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.325F);
            return 1;
        }
    }
}
