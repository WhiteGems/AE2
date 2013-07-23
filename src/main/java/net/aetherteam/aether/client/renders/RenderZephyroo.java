package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityZephyroo;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.EntityLiving;

public class RenderZephyroo extends RenderMinecartMobSpawner
{
    public RenderZephyroo(ModelMinecart par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public void a(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        super.a(par1EntityLiving, par2, par4, par6, par8, par9);

        if ((par1EntityLiving instanceof EntityZephyroo))
        {
            EntityZephyroo roo = (EntityZephyroo)par1EntityLiving;

            if (roo.isLovingClash())
            {
                a(par1EntityLiving, par2, par4, par6, "pls lov me clashy <3", par9, 1.0D);
            }
        }
    }
}

