package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerbunny;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAerbunny extends RenderLiving
{
    public ModelAerbunny mb;

    public RenderAerbunny(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.mb = (ModelAerbunny)var1;
    }

    protected void rotAerbunny(EntityAerbunny var1)
    {
        if (!var1.onGround && var1.ridingEntity == null)
        {
            if (var1.motionY > 0.5D)
            {
                GL11.glRotatef(15.0F, -1.0F, 0.0F, 0.0F);
            }
            else if (var1.motionY < -0.5D)
            {
                GL11.glRotatef(-15.0F, -1.0F, 0.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((float)(var1.motionY * 30.0D), -1.0F, 0.0F, 0.0F);
            }
        }

        this.mb.puffiness = (float)var1.getPuffiness() / 10.0F;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving var1, float var2)
    {
        this.rotAerbunny((EntityAerbunny)var1);
    }

    public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9)
    {
        float var10 = this.interpolateRotation(var1.prevRenderYawOffset, var1.renderYawOffset, var9);
        float var11 = this.interpolateRotation(var1.prevRotationYawHead, var1.rotationYawHead, var9);
        float var12 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;

        if (var1 instanceof EntityAerbunny && ((EntityAerbunny)var1).getRidingHandler().isBeingRidden())
        {
            EntityLiving var13 = ((EntityAerbunny)var1).getRidingHandler().getRider();
            var2 = var13.lastTickPosX + (var13.posX - var13.lastTickPosX) * (double)var9;
            var4 = var13.lastTickPosY - 1.68D + (var13.posY - var13.lastTickPosY) * (double)var9 + ((EntityAerbunny)var1).getRidingHandler().getAnimalYOffset();
            var6 = var13.lastTickPosZ + (var13.posZ - var13.lastTickPosZ) * (double)var9;
            var2 -= RenderManager.renderPosX;
            var4 -= RenderManager.renderPosY;
            var6 -= RenderManager.renderPosZ;
            var8 = (float)((double)var13.prevRotationYaw + (double)(var13.rotationYaw - var13.prevRotationYaw) * var2);
            var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, var9);
            var12 = var13.prevRotationPitch + (var13.rotationPitch - var13.prevRotationPitch) * var9;
            var11 = this.interpolateRotation(var13.prevRotationYawHead, var13.rotationYawHead, var9);
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(var1, var9);

        if (this.renderPassModel != null)
        {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = var1.isRiding();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = var1.isChild();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try
        {
            this.renderLivingAt(var1, var2, var4, var6);
            float var26 = this.handleRotationFloat(var1, var9);
            this.rotateCorpse(var1, var26, var10, var9);
            float var14 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(var1, var9);
            GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
            float var15 = var1.prevLimbYaw + (var1.limbYaw - var1.prevLimbYaw) * var9;
            float var16 = var1.limbSwing - var1.limbYaw * (1.0F - var9);

            if (var1.isChild())
            {
                var16 *= 3.0F;
            }

            if (var15 > 1.0F)
            {
                var15 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(var1, var16, var15, var9);
            this.renderModel(var1, var16, var15, var26, var11 - var10, var12, var14);
            float var17;
            float var19;
            int var18;
            float var20;
            int var22;

            for (int var21 = 0; var21 < 4; ++var21)
            {
                var18 = this.shouldRenderPass(var1, var21, var9);

                if (var18 > 0)
                {
                    this.renderPassModel.setLivingAnimations(var1, var16, var15, var9);
                    this.renderPassModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);

                    if ((var18 & 240) == 16)
                    {
                        this.func_82408_c(var1, var21, var9);
                        this.renderPassModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);
                    }

                    if ((var18 & 15) == 15)
                    {
                        var17 = (float)var1.ticksExisted + var9;
                        this.loadTexture("%blur%/misc/glint.png");
                        GL11.glEnable(GL11.GL_BLEND);
                        var19 = 0.5F;
                        GL11.glColor4f(var19, var19, var19, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (var22 = 0; var22 < 2; ++var22)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            var20 = 0.76F;
                            GL11.glColor4f(0.5F * var20, 0.25F * var20, 0.8F * var20, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            float var23 = var17 * (0.001F + (float)var22 * 0.003F) * 20.0F;
                            float var24 = 0.33333334F;
                            GL11.glScalef(var24, var24, var24);
                            GL11.glRotatef(30.0F - (float)var22 * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, var23, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.renderPassModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);
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
            this.renderEquippedItems(var1, var9);
            float var29 = var1.getBrightness(var9);
            var18 = this.getColorMultiplier(var1, var29, var9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((var18 >> 24 & 255) > 0 || var1.hurtTime > 0 || var1.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (var1.hurtTime > 0 || var1.deathTime > 0)
                {
                    GL11.glColor4f(var29, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);

                    for (var22 = 0; var22 < 4; ++var22)
                    {
                        if (this.inheritRenderPass(var1, var22, var9) >= 0)
                        {
                            GL11.glColor4f(var29, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);
                        }
                    }
                }

                if ((var18 >> 24 & 255) > 0)
                {
                    var17 = (float)(var18 >> 16 & 255) / 255.0F;
                    var19 = (float)(var18 >> 8 & 255) / 255.0F;
                    float var28 = (float)(var18 & 255) / 255.0F;
                    var20 = (float)(var18 >> 24 & 255) / 255.0F;
                    GL11.glColor4f(var17, var19, var28, var20);
                    this.mainModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);

                    for (int var27 = 0; var27 < 4; ++var27)
                    {
                        if (this.inheritRenderPass(var1, var27, var9) >= 0)
                        {
                            GL11.glColor4f(var17, var19, var28, var20);
                            this.renderPassModel.render(var1, var16, var15, var26, var11 - var10, var12, var14);
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
        catch (Exception var25)
        {
            var25.printStackTrace();
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(var1, var2, var4, var6);
    }

    private float interpolateRotation(float var1, float var2, float var3)
    {
        float var4;

        for (var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return var1 + var3 * var4;
    }

    public void doRender(EntityLiving var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }
}
