package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityCockatrice;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCockatrice extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/cockatrice/cockatrice.png");
    private static final ResourceLocation TEXTURE_MARKINGS = new ResourceLocation("aether", "textures/mobs/cockatrice/markings.png");

    public RenderCockatrice(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.setRenderPassModel(modelbase);
        this.renderPassModel = modelbase;
    }

    protected float getWingRotation(EntityCockatrice entitybadmoa, float f)
    {
        float f1 = entitybadmoa.field_756_e + (entitybadmoa.field_752_b - entitybadmoa.field_756_e) * f;
        float f2 = entitybadmoa.field_757_d + (entitybadmoa.destPos - entitybadmoa.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    protected float handleRotationFloat(EntityLiving entityliving, float f)
    {
        return this.getWingRotation((EntityCockatrice)entityliving, f);
    }

    protected void scalemoa()
    {
        GL11.glScalef(1.8F, 1.8F, 1.8F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        this.scalemoa();
    }

    protected int setMarkingBrightness(EntityCockatrice cock, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            this.renderManager.renderEngine.func_110577_a(TEXTURE_MARKINGS);
            float var4 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (!cock.getActivePotionEffects().isEmpty())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

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
        return this.setMarkingBrightness((EntityCockatrice)entityliving, i, f);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
