package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityBattleSentry;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderBattleSentry extends RenderMinecartMobSpawner
{
    public RenderBattleSentry(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
        a(modelbase);
    }

    protected void a(EntityLiving entityliving, float f)
    {
        float f1 = 1.75F;
        GL11.glScalef(f1, f1, f1);
    }

    protected int a(EntityBattleSentry entityliving, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        loadTexture("/net/aetherteam/aether/client/sprites/mobs/sentryMelee/eye.png");
        float var4 = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glDepthMask(false);
        char var5 = 61680;
        int var6 = var5 % 65536;
        int var7 = var5 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);

        if ((entityliving.isInView()) && (!entityliving.getHasBeenAttacked()))
        {
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        }

        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return a((EntityBattleSentry)entityliving, i, f);
    }

    protected void a(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        if ((!((EntityBattleSentry)par1EntityLiving).isInView()) || (((EntityBattleSentry)par1EntityLiving).getHasBeenAttacked()))
        {
            super.a(par1EntityLiving, par2, par3, par4, par5, par6, par7);
        }

        if ((((EntityBattleSentry)par1EntityLiving).isInView()) && (!((EntityBattleSentry)par1EntityLiving).getHasBeenAttacked()))
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            super.a(par1EntityLiving, par2, par3, par4, par5, par6, par7);
            GL11.glPopMatrix();
        }
    }
}

