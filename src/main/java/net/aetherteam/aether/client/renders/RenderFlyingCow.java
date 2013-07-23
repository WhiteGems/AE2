package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntityFlyingCow;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.EntityLiving;

public class RenderFlyingCow extends RenderMinecartMobSpawner
{
    private ModelMinecart wingmodel;

    public RenderFlyingCow(ModelMinecart modelbase, ModelMinecart modelbase1, float f)
    {
        super(modelbase, f);
        a(modelbase1);
        this.wingmodel = modelbase1;
    }

    protected int setWoolColorAndRender(EntityFlyingCow flyingcow, int i, float f)
    {
        if (i == 0)
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/flyingcow/wings.png");
            net.aetherteam.aether.client.models.ModelFlyingCow2.flyingcow = flyingcow;
            return 1;
        }

        return -1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setWoolColorAndRender((EntityFlyingCow)entityliving, i, f);
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

    public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
    {
        a(entity, d, d1, d2, f, f1);
    }
}

