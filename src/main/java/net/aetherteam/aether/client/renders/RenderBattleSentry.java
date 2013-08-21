package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityBattleSentry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBattleSentry extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/sentryMelee/sentryMelee.png");
    public static final ResourceLocation TEXTURE_LIT = new ResourceLocation("aether", "textures/mobs/sentryMelee/sentryMeleeLit.png");
    public static final ResourceLocation TEXTURE_EYE = new ResourceLocation("aether", "textures/mobs/sentryMelee/eye.png");

    public RenderBattleSentry(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.setRenderPassModel(modelbase);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        float f1 = 1.75F;
        GL11.glScalef(f1, f1, f1);
    }

    protected int a(EntityBattleSentry battleSentry, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            this.renderManager.renderEngine.func_110577_a(TEXTURE_EYE);
            float f1 = 1.0F;
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            char j = 61680;
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);

            if (battleSentry.isInView() && !battleSentry.getHasBeenAttacked())
            {
                GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            }

            return 1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f)
    {
        return this.a((EntityBattleSentry)entityliving, i, f);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        if (!((EntityBattleSentry)par1EntityLiving).isInView() || ((EntityBattleSentry)par1EntityLiving).getHasBeenAttacked())
        {
            super.renderModel(par1EntityLiving, par2, par3, par4, par5, par6, par7);
        }

        if (((EntityBattleSentry)par1EntityLiving).isInView() && !((EntityBattleSentry)par1EntityLiving).getHasBeenAttacked())
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            super.renderModel(par1EntityLiving, par2, par3, par4, par5, par6, par7);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE_LIT;
    }
}
