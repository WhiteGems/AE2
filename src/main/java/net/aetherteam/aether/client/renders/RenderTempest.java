package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelTempest;
import net.aetherteam.aether.entities.EntityTempest;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTempest extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/tempest/tempest.png");
    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("aether", "textures/mobs/tempest/glow.png");
    public ModelTempest tempestModel;

    public RenderTempest(ModelTempest model)
    {
        super(model, 0.5F);
        this.setRenderPassModel(model);
        this.tempestModel = model;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        EntityTempest tempest = (EntityTempest)entityliving;
        float f1 = (float)Math.sin((double)tempest.sinage);
        float f3;

        if (tempest.hurtTime > 0)
        {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float)Math.sin((double)(tempest.sinage + 2.0F)) * 1.5F;
        }
        else
        {
            f1 *= 0.25F;
            f3 = 1.75F + (float)Math.sin((double)(tempest.sinage + 2.0F)) * 1.5F;
        }

        this.tempestModel.sinage = f1;
        this.tempestModel.sinage2 = f3;
        this.shadowSize = 0.75F;
        GL11.glScalef(1.5F, 1.5F, 1.5F);
    }

    protected int setMarkingBrightness(EntityTempest tempest, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            this.renderManager.renderEngine.func_110577_a(TEXTURE_GLOW);
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDepthMask(false);
            char var5 = 61680;
            int var6 = var5 % 65536;
            int var7 = var5 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.setMarkingBrightness((EntityTempest)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
