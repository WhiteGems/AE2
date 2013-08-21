package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCarrionSprout;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.aetherteam.aether.entities.EntityCarrionSprout;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCarrionSprout extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/carrionsprout/sprout.png");
    public ModelCarrionSprout plantModel;

    public RenderCarrionSprout(ModelCarrionSprout model, float f)
    {
        super(model, f);
        this.setRenderPassModel(model);
        this.plantModel = model;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        EntityCarrionSprout sprout = (EntityCarrionSprout)entityliving;
        float f1 = (float)Math.sin((double)sprout.sinage);
        float f3;

        if (sprout.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin((double)(sprout.sinage + 2.0F)) * 1.5F;
        }
        else
        {
            f1 *= 0.25F;
            f3 = 1.75F + (float)Math.sin((double)(sprout.sinage + 2.0F)) * 1.5F;
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
        else
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.325F);
            return 1;
        }
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
