package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelZephyr;
import net.aetherteam.aether.entities.EntityZephyr;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderZephyr extends RenderMinecartMobSpawner
{
    public RenderZephyr()
    {
        super(new ModelZephyr(), 0.5F);
    }

    protected void func_4014_a(EntityZephyr entityzephyr, float f)
    {
        EntityZephyr entityzephyr1 = entityzephyr;
        float f1 = (entityzephyr1.prevAttackCounter + (entityzephyr1.attackCounter - entityzephyr1.prevAttackCounter) * f) / 20.0F;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        GL11.glScalef(f3, f2, f3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void a(EntityLiving entityliving, float f)
    {
        func_4014_a((EntityZephyr)entityliving, f);
    }
}

