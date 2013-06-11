package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderMoa extends RenderLiving
{
    public RenderMoa(ModelBase var1, float var2)
    {
        super(var1, var2);
    }

    protected float getWingRotation(EntityMoa var1, float var2)
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
        return this.getWingRotation((EntityMoa) var1, var2);
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
        if (!(var1 instanceof EntityMoa) || !((EntityMoa) var1).isBaby())
        {
            this.scalemoa();
        }
    }

    private float interpolateRotation(float var1, float var2, float var3)
    {
        float var4;

        for (var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return var1 + var3 * var4;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderLiving((EntityLiving) var1, var2, var4, var6, var8, var9);
    }
}
