package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelFlyingCow2;
import net.aetherteam.aether.entities.mounts.EntityFlyingCow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderFlyingCow extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/flyingcow/flyingcow.png");
    private static final ResourceLocation TEXTURE_SADDLED = new ResourceLocation("aether", "textures/mobs/flyingcow/saddle.png");
    private static final ResourceLocation TEXTURE_WINGS = new ResourceLocation("aether", "textures/mobs/flyingcow/wings.png");
    private ModelBase wingmodel;

    public RenderFlyingCow(ModelBase modelbase, ModelBase modelbase1, float f)
    {
        super(modelbase, f);
        this.setRenderPassModel(modelbase1);
        this.wingmodel = modelbase1;
    }

    protected int setWoolColorAndRender(EntityFlyingCow flyingcow, int i, float f)
    {
        if (i == 0)
        {
            this.renderManager.renderEngine.func_110577_a(TEXTURE_WINGS);
            ModelFlyingCow2.flyingcow = flyingcow;
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setWoolColorAndRender((EntityFlyingCow)entityliving, i, f);
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
        return ((EntityFlyingCow)entity).getSaddled() ? TEXTURE_SADDLED : TEXTURE;
    }
}
