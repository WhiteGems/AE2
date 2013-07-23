package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntitySwet;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderSwet extends RenderLiving
{
    private ModelBase field_22001_a;

    public RenderSwet(ModelBase var1, ModelBase var2, float var3)
    {
        super(var1, var3);
        this.field_22001_a = var2;
    }

    protected int a(EntitySwet var1, int var2, float var3)
    {
        if (var2 == 0)
        {
            this.setRenderPassModel(this.field_22001_a);
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        }
        else
        {
            if (var2 == 1)
            {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            return -1;
        }
    }

    protected void a(EntitySwet var1, float var2)
    {
        float var3 = 1.0F;
        float var4 = 1.0F;
        float var5 = 1.5F;

        if (!var1.onGround)
        {
            if (var1.motionY > 0.8500000238418579D)
            {
                var4 = 1.425F;
                var3 = 0.575F;
            }
            else if (var1.motionY < -0.8500000238418579D)
            {
                var4 = 0.575F;
                var3 = 1.425F;
            }
            else
            {
                float var6 = (float)var1.motionY * 0.5F;
                var4 += var6;
                var3 -= var6;
            }
        }

        if (var1.riddenByEntity != null)
        {
            var5 = 1.5F + (var1.riddenByEntity.width + var1.riddenByEntity.height) * 0.75F;
        }

        GL11.glScalef(var3 * var5, var4 * var5, var3 * var5);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        this.a((EntitySwet)var1, var2);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.a((EntitySwet)var1, var2, var3);
    }
}
