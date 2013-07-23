package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerbunny;
import net.aetherteam.aether.entities.mounts.EntityAerbunny;
import net.aetherteam.aether.entities.mounts_old.RidingHandler;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAerbunny extends RenderMinecartMobSpawner
{
    public ModelAerbunny mb;

    public RenderAerbunny(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
        this.mb = ((ModelAerbunny)modelbase);
    }

    protected void rotAerbunny(EntityAerbunny entitybunny)
    {
        if ((!entitybunny.onGround) && (entitybunny.ridingEntity == null))
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

        this.mb.puffiness = (entitybunny.getPuffiness() / 10.0F);
    }

    protected void a(EntityLiving entityliving, float f)
    {
        rotAerbunny((EntityAerbunny)entityliving);
    }

    public void a(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        float var10 = interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
        float var11 = interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
        float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;

        if (((par1EntityLiving instanceof EntityAerbunny)) && (((EntityAerbunny)par1EntityLiving).getRidingHandler().isBeingRidden()))
        {
            EntityLiving entity = ((EntityAerbunny)par1EntityLiving).getRidingHandler().getRider();
            par2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * par9;
            par4 = entity.lastTickPosY - 1.68D + (entity.posY - entity.lastTickPosY) * par9 + ((EntityAerbunny)par1EntityLiving).getRidingHandler().getAnimalYOffset();
            par6 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * par9;
            par2 -= RenderEnderman.b;
            par4 -= RenderEnderman.c;
            par6 -= RenderEnderman.d;
            par8 = (float)(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par2);
            var10 = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, par9);
            var12 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
            var11 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, par9);
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.i.onGround = d(par1EntityLiving, par9);

        if (this.j != null)
        {
            this.j.onGround = this.i.onGround;
        }

        this.i.isRiding = par1EntityLiving.isRiding();

        if (this.j != null)
        {
            this.j.isRiding = this.i.isRiding;
        }

        this.i.isChild = par1EntityLiving.isChild();

        if (this.j != null)
        {
            this.j.isChild = this.i.isChild;
        }

        try
        {
            a(par1EntityLiving, par2, par4, par6);
            float var13 = b(par1EntityLiving, par9);
            a(par1EntityLiving, var13, var10, par9);
            float var14 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            a(par1EntityLiving, par9);
            GL11.glTranslatef(0.0F, -24.0F * var14 - 0.007813F, 0.0F);
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
            this.i.setLivingAnimations(par1EntityLiving, var16, var15, par9);
            a(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

            for (int var17 = 0; var17 < 4; var17++)
            {
                int var18 = a(par1EntityLiving, var17, par9);

                if (var18 > 0)
                {
                    this.j.setLivingAnimations(par1EntityLiving, var16, var15, par9);
                    this.j.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    if ((var18 & 0xF0) == 16)
                    {
                        c(par1EntityLiving, var17, par9);
                        this.j.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                    }

                    if ((var18 & 0xF) == 15)
                    {
                        float var19 = par1EntityLiving.ticksExisted + par9;
                        loadTexture("%blur%/misc/glint.png");
                        GL11.glEnable(GL11.GL_BLEND);
                        float var20 = 0.5F;
                        GL11.glColor4f(var20, var20, var20, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (int var21 = 0; var21 < 2; var21++)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            float var22 = 0.76F;
                            GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            float var23 = var19 * (0.001F + var21 * 0.003F) * 20.0F;
                            float var24 = 0.3333333F;
                            GL11.glScalef(var24, var24, var24);
                            GL11.glRotatef(30.0F - var21 * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, var23, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.j.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
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
            c(par1EntityLiving, par9);
            float var26 = par1EntityLiving.getBrightness(par9);
            int var18 = a(par1EntityLiving, var26, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if (((var18 >> 24 & 0xFF) > 0) || (par1EntityLiving.hurtTime > 0) || (par1EntityLiving.deathTime > 0))
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if ((par1EntityLiving.hurtTime > 0) || (par1EntityLiving.deathTime > 0))
                {
                    GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                    this.i.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    for (int var27 = 0; var27 < 4; var27++)
                    {
                        if (b(par1EntityLiving, var27, par9) >= 0)
                        {
                            GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                            this.j.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                        }
                    }
                }

                if ((var18 >> 24 & 0xFF) > 0)
                {
                    float var19 = (var18 >> 16 & 0xFF) / 255.0F;
                    float var20 = (var18 >> 8 & 0xFF) / 255.0F;
                    float var29 = (var18 & 0xFF) / 255.0F;
                    float var22 = (var18 >> 24 & 0xFF) / 255.0F;
                    GL11.glColor4f(var19, var20, var29, var22);
                    this.i.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

                    for (int var28 = 0; var28 < 4; var28++)
                    {
                        if (b(par1EntityLiving, var28, par9) >= 0)
                        {
                            GL11.glColor4f(var19, var20, var29, var22);
                            this.j.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
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
        b(par1EntityLiving, par2, par4, par6);
    }

    private float interpolateRotation(float par1, float par2, float par3)
    {
        for (float var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F);

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

    public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
    {
        a(entity, d, d1, d2, f, f1);
    }
}

