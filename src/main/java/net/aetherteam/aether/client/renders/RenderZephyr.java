package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelZephyr;
import net.aetherteam.aether.entities.EntityZephyr;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderZephyr extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/zephyr/zephyr.png");

    public RenderZephyr()
    {
        super(new ModelZephyr(), 0.5F);
    }

    protected void func_4014_a(EntityZephyr entityzephyr, float f)
    {
        float f1 = ((float)entityzephyr.prevAttackCounter + (float)(entityzephyr.attackCounter - entityzephyr.prevAttackCounter) * f) / 20.0F;

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

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        this.func_4014_a((EntityZephyr)entityliving, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
