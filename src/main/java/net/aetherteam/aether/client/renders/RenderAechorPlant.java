package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAechorPlant;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderAechorPlant extends RenderMinecartMobSpawner
{
    public ModelAechorPlant xd;

    public RenderAechorPlant(ModelAechorPlant mb, float f)
    {
        super(mb, f);
        a(mb);
        this.xd = mb;
    }

    protected void a(EntityLiving entityliving, float f)
    {
        EntityAechorPlant b1 = (EntityAechorPlant)entityliving;
        float f1 = (float)Math.sin(b1.sinage);
        float f3;
        float f3;

        if (b1.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin(b1.sinage + 2.0F) * 1.5F;
        }
        else
        {
            float f3;

            if (b1.seeprey)
            {
                f1 *= 0.25F;
                f3 = 1.75F + (float)Math.sin(b1.sinage + 2.0F) * 1.5F;
            }
            else
            {
                f1 *= 0.125F;
                f3 = 1.75F;
            }
        }

        this.xd.sinage = f1;
        this.xd.sinage2 = f3;
        float f2 = 0.625F + b1.getSize() / 6.0F;
        this.xd.size = f2;
        this.shadowSize = (f2 - 0.25F);
    }

    protected int a(EntityAechorPlant entityaechorplant, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.325F);
        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return a((EntityAechorPlant)entityliving, i, f);
    }
}

