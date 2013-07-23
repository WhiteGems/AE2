package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelNewZephyr;
import net.aetherteam.aether.entities.EntityNewZephyr;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderNewZephyr extends RenderMinecartMobSpawner
{
    public ModelNewZephyr newZephyrModel;

    public RenderNewZephyr(ModelNewZephyr model)
    {
        super(model, 0.5F);
        this.newZephyrModel = model;
    }

    protected void a(EntityLiving entityliving, float f)
    {
        EntityNewZephyr newZephyr = (EntityNewZephyr)entityliving;
        float f1 = (float)Math.sin(newZephyr.sinage);
        float f3;
        float f3;

        if (newZephyr.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin(newZephyr.sinage + 2.0F) * 1.5F;
        }
        else
        {
            f1 *= 0.25F;
            f3 = 1.75F + (float)Math.sin(newZephyr.sinage + 2.0F) * 1.5F;
        }

        GL11.glRotatef(35.0F, (float)entityliving.motionX, (float)entityliving.motionY, (float)entityliving.motionZ);
        this.newZephyrModel.sinage = f1;
        this.newZephyrModel.sinage2 = f3;
        this.shadowSize = 0.75F;
        GL11.glScalef(1.25F, 1.25F, 1.25F);
    }
}

