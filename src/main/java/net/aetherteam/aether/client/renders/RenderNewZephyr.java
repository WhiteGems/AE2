package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelNewZephyr;
import net.aetherteam.aether.entities.EntityNewZephyr;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderNewZephyr extends RenderLiving
{
    public ModelNewZephyr newZephyrModel;

    public RenderNewZephyr(ModelNewZephyr var1)
    {
        super(var1, 0.5F);
        this.newZephyrModel = var1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        EntityNewZephyr var3 = (EntityNewZephyr)var1;
        float var4 = (float)Math.sin((double)var3.sinage);
        float var5;

        if (var3.hurtTime > 0)
        {
            var4 *= 0.45F;
            var4 -= 0.125F;
            var5 = 1.75F + (float)Math.sin((double)(var3.sinage + 2.0F)) * 1.5F;
        }
        else
        {
            var4 *= 0.25F;
            var5 = 1.75F + (float)Math.sin((double)(var3.sinage + 2.0F)) * 1.5F;
        }

        GL11.glRotatef(35.0F, (float)var1.motionX, (float)var1.motionY, (float)var1.motionZ);
        this.newZephyrModel.sinage = var4;
        this.newZephyrModel.sinage2 = var5;
        this.shadowSize = 0.75F;
        GL11.glScalef(1.25F, 1.25F, 1.25F);
    }
}
