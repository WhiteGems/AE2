package net.aetherteam.aether.oldcode;

import net.aetherteam.aether.client.models.ModelBall;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.EntityLiving;

public class RenderElectricBall extends RenderMinecartMobSpawner
{
    private ModelBall shotty;

    public RenderElectricBall(ModelMinecart ms, float f)
    {
        super(ms, f);
        this.shotty = ((ModelBall)ms);
    }

    public void a(EntityLiving el, float f)
    {
        EntityElectricBall hs = (EntityElectricBall)el;

        for (int i = 0; i < 3; i++)
        {
            this.shotty.sinage[i] = hs.sinage[i];
        }
    }
}

