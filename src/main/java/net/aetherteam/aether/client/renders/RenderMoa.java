package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntityMoa;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderMoa extends RenderMinecartMobSpawner
{
    public RenderMoa(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
    }

    protected float getWingRotation(EntityMoa entitymoa, float f)
    {
        float f1 = entitymoa.field_756_e + (entitymoa.field_752_b - entitymoa.field_756_e) * f;
        float f2 = entitymoa.field_757_d + (entitymoa.destPos - entitymoa.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    protected float b(EntityLiving entityliving, float f)
    {
        return getWingRotation((EntityMoa)entityliving, f);
    }

    protected void scalemoa()
    {
        GL11.glScalef(1.8F, 1.8F, 1.8F);
    }

    protected void a(EntityLiving entityliving, float f)
    {
        if (((entityliving instanceof EntityMoa)) && (((EntityMoa)entityliving).isBaby()))
        {
            return;
        }

        scalemoa();
    }

    private float interpolateRotation(float par1, float par2, float par3)
    {
        for (float var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F);

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        a((EntityLiving)entity, d, d1, d2, f, f1);
    }
}

