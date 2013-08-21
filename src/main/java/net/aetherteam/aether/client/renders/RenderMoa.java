package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderMoa extends RenderLiving
{
    public RenderMoa(ModelBase modelbase, float f)
    {
        super(modelbase, f);
    }

    protected float getWingRotation(EntityMoa entitymoa, float f)
    {
        float f1 = entitymoa.field_756_e + (entitymoa.field_752_b - entitymoa.field_756_e) * f;
        float f2 = entitymoa.field_757_d + (entitymoa.destPos - entitymoa.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase entityliving, float f)
    {
        return this.getWingRotation((EntityMoa)entityliving, f);
    }

    protected void scalemoa()
    {
        GL11.glScalef(1.8F, 1.8F, 1.8F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        if (!(entityliving instanceof EntityMoa) || !((EntityMoa)entityliving).isBaby())
        {
            this.scalemoa();
        }
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    private float interpolateRotation(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        this.doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntityMoa)entity).getTexture();
    }
}
