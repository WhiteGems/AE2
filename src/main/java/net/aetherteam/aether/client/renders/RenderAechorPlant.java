package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAechorPlant;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAechorPlant extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/aechorplant/aechorplant.png");
    public ModelAechorPlant xd;

    public RenderAechorPlant(ModelAechorPlant mb, float f)
    {
        super(mb, f);
        this.setRenderPassModel(mb);
        this.xd = mb;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        EntityAechorPlant b1 = (EntityAechorPlant)entityliving;
        float f1 = (float)Math.sin((double)b1.sinage);
        float f3;

        if (b1.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin((double)(b1.sinage + 2.0F)) * 1.5F;
        }
        else if (b1.seeprey)
        {
            f1 *= 0.25F;
            f3 = 1.75F + (float)Math.sin((double)(b1.sinage + 2.0F)) * 1.5F;
        }
        else
        {
            f1 *= 0.125F;
            f3 = 1.75F;
        }

        this.xd.sinage = f1;
        this.xd.sinage2 = f3;
        float f2 = 0.625F + (float)b1.getSize() / 6.0F;
        this.xd.size = f2;
        this.shadowSize = f2 - 0.25F;
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

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.a((EntityAechorPlant)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
