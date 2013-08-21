package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerbunny;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAerbunny extends RenderLiving
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/aerbunny/aerbunny.png");
    private static final ResourceLocation TEXTURE_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public ModelAerbunny mb;

    public RenderAerbunny(ModelBase modelbase, float f)
    {
        super(modelbase, f);
        this.mb = (ModelAerbunny)modelbase;
    }

    protected void rotAerbunny(EntityAerbunny entitybunny)
    {
        if (!entitybunny.onGround && entitybunny.ridingEntity == null)
        {
            if (entitybunny.motionY > 0.5D)
            {
                GL11.glRotatef(15.0F, -1.0F, 0.0F, 0.0F);
            }
            else if (entitybunny.motionY < -0.5D)
            {
                GL11.glRotatef(-15.0F, -1.0F, 0.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((float)(entitybunny.motionY * 30.0D), -1.0F, 0.0F, 0.0F);
            }
        }

        this.mb.puffiness = (float)entitybunny.getPuffiness() / 10.0F;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        this.rotAerbunny((EntityAerbunny)entityliving);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        float var10 = this.interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
        float var11 = this.interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
        float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;

        if (par1EntityLiving instanceof EntityAerbunny && ((EntityAerbunny)par1EntityLiving).getRidingHandler().isBeingRidden())
        {
            EntityLivingBase var25 = ((EntityAerbunny)par1EntityLiving).getRidingHandler().getRider();
            par2 = var25.lastTickPosX + (var25.posX - var25.lastTickPosX) * (double)par9;
            par4 = var25.lastTickPosY - 1.68D + (var25.posY - var25.lastTickPosY) * (double)par9 + ((EntityAerbunny)par1EntityLiving).getRidingHandler().getAnimalYOffset();
            par6 = var25.lastTickPosZ + (var25.posZ - var25.lastTickPosZ) * (double)par9;
            par2 -= RenderManager.renderPosX;
            par4 -= RenderManager.renderPosY;
            par6 -= RenderManager.renderPosZ;
            par8 = (float)((double)var25.prevRotationYaw + (double)(var25.rotationYaw - var25.prevRotationYaw) * par2);
            var10 = this.interpolateRotation(var25.prevRenderYawOffset, var25.renderYawOffset, par9);
            var12 = var25.prevRotationPitch + (var25.rotationPitch - var25.prevRotationPitch) * par9;
            var11 = this.interpolateRotation(var25.prevRotationYawHead, var25.rotationYawHead, par9);
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);

        if (this.renderPassModel != null)
        {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = par1EntityLiving.isRiding();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = par1EntityLiving.isChild();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try
        {
            this.renderLivingAt(par1EntityLiving, par2, par4, par6);
            float var261 = this.handleRotationFloat(par1EntityLiving, par9);
            this.rotateCorpse(par1EntityLiving, var261, var10, par9);
            float var14 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(par1EntityLiving, par9);
            GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
            float var15 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
            float var16 = par1EntityLiving.limbSwing - par1EntityLiving.limbYaw * (1.0F - par9);

            if (par1EntityLiving.isChild())
            {
                var16 *= 3.0F;
            }

            if (var15 > 1.0F)
            {
                var15 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
            this.renderModel(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);
            float var19;
            float var20;
            int var18;
            float var22;
            int var29;

            for (int var26 = 0; var26 < 4; ++var26)
            {
                var18 = this.shouldRenderPass(par1EntityLiving, var26, par9);

                if (var18 > 0)
                {
                    this.renderPassModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
                    this.renderPassModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);

                    if ((var18 & 240) == 16)
                    {
                        this.func_82408_c(par1EntityLiving, var26, par9);
                        this.renderPassModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);
                    }

                    if ((var18 & 15) == 15)
                    {
                        var19 = (float)par1EntityLiving.ticksExisted + par9;
                        this.renderManager.renderEngine.func_110577_a(TEXTURE_GLINT);
                        GL11.glEnable(GL11.GL_BLEND);
                        var20 = 0.5F;
                        GL11.glColor4f(var20, var20, var20, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (var29 = 0; var29 < 2; ++var29)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            var22 = 0.76F;
                            GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            float var28 = var19 * (0.001F + (float)var29 * 0.003F) * 20.0F;
                            float var24 = 0.33333334F;
                            GL11.glScalef(var24, var24, var24);
                            GL11.glRotatef(30.0F - (float)var29 * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, var28, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glDepthMask(true);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glDepthFunc(GL11.GL_LEQUAL);
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glDepthMask(true);
            this.renderEquippedItems(par1EntityLiving, par9);
            float var291 = par1EntityLiving.getBrightness(par9);
            var18 = this.getColorMultiplier(par1EntityLiving, var291, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((var18 >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
                {
                    GL11.glColor4f(var291, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);

                    for (var29 = 0; var29 < 4; ++var29)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, var29, par9) >= 0)
                        {
                            GL11.glColor4f(var291, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);
                        }
                    }
                }

                if ((var18 >> 24 & 255) > 0)
                {
                    var19 = (float)(var18 >> 16 & 255) / 255.0F;
                    var20 = (float)(var18 >> 8 & 255) / 255.0F;
                    float var281 = (float)(var18 & 255) / 255.0F;
                    var22 = (float)(var18 >> 24 & 255) / 255.0F;
                    GL11.glColor4f(var19, var20, var281, var22);
                    this.mainModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);

                    for (int var27 = 0; var27 < 4; ++var27)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, var27, par9) >= 0)
                        {
                            GL11.glColor4f(var19, var20, var281, var22);
                            this.renderPassModel.render(par1EntityLiving, var16, var15, var261, var11 - var10, var12, var14);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (Exception var251)
        {
            var251.printStackTrace();
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(par1EntityLiving, par2, par4, par6);
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    private float interpolateRotation(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        this.doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
