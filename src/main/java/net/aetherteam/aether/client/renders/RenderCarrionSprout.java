package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCarrionSprout;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.aetherteam.aether.entities.EntityCarrionSprout;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderCarrionSprout extends RenderMinecartMobSpawner
{
    public ModelCarrionSprout plantModel;

    public RenderCarrionSprout(ModelCarrionSprout model, float f)
    {
        super(model, f);
        a(model);
        this.plantModel = model;
    }

    protected void a(EntityLiving entityliving, float f)
    {
        EntityCarrionSprout sprout = (EntityCarrionSprout)entityliving;
        float f1 = (float)Math.sin(sprout.sinage);
        float f3;
        float f3;

        if (sprout.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin(sprout.sinage + 2.0F) * 1.5F;
        }
        else
        {
            f1 *= 0.25F;
            f3 = 1.75F + (float)Math.sin(sprout.sinage + 2.0F) * 1.5F;
        }

        this.plantModel.sinage = f1;
        this.plantModel.sinage2 = f3;
        this.shadowSize = 0.25F;
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
}

