package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelMimic;
import net.aetherteam.aether.entities.EntityMimic;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderMimic extends RenderMinecartMobSpawner
{
    public RenderMimic(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
        a(modelbase);
    }

    protected int a(EntityMimic entityMimic, int i, float f)
    {
        loadTexture("/net/aetherteam/aether/client/sprites/mobs/mimic/mimic1.png");
        ((ModelMimic)this.i).render1(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
        loadTexture("/net/aetherteam/aether/client/sprites/mobs/mimic/mimic2.png");
        ((ModelMimic)this.i).render2(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return a((EntityMimic)entityliving, i, f);
    }
}

