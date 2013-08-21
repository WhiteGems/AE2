package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.mounts.EntitySwet;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSwet extends RenderLiving
{
    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("aether", "textures/mobs/swet/swet_blue.png");
    private static final ResourceLocation TEXTURE_GOLDEN = new ResourceLocation("aether", "textures/mobs/swet/swet_golden.png");
    private ModelBase field_22001_a;

    public RenderSwet(ModelBase modelbase, ModelBase modelbase1, float f)
    {
        super(modelbase, f);
        this.field_22001_a = modelbase1;
    }

    protected int a(EntitySwet entityswets, int i, float f)
    {
        if (i == 0)
        {
            this.setRenderPassModel(this.field_22001_a);
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        }
        else
        {
            if (i == 1)
            {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            return -1;
        }
    }

    protected void a(EntitySwet entityswets, float f)
    {
        float f2 = 1.0F;
        float f1 = 1.0F;
        float f3 = 1.5F;

        if (!entityswets.onGround)
        {
            if (entityswets.motionY > 0.8500000238418579D)
            {
                f1 = 1.425F;
                f2 = 0.575F;
            }
            else if (entityswets.motionY < -0.8500000238418579D)
            {
                f1 = 0.575F;
                f2 = 1.425F;
            }
            else
            {
                float f4 = (float)entityswets.motionY * 0.5F;
                f1 += f4;
                f2 -= f4;
            }
        }

        if (entityswets.riddenByEntity != null)
        {
            f3 = 1.5F + (entityswets.riddenByEntity.width + entityswets.riddenByEntity.height) * 0.75F;
        }

        GL11.glScalef(f2 * f3, f1 * f3, f2 * f3);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        this.a((EntitySwet)entityliving, f);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.a((EntitySwet)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntitySwet)entity).textureNum == 1 ? TEXTURE_BLUE : TEXTURE_GOLDEN;
    }
}
