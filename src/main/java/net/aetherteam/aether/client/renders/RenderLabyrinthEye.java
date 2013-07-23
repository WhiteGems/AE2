package net.aetherteam.aether.client.renders;

import java.util.Collection;
import net.aetherteam.aether.client.models.ModelLabyrinthEye;
import net.aetherteam.aether.entities.bosses.EntityLabyrinthEye;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.lwjgl.opengl.GL11;

public class RenderLabyrinthEye extends RenderMinecartMobSpawner
{
    public RenderLabyrinthEye(ModelLabyrinthEye model, float f)
    {
        super(model, f);
        a(model);
    }

    protected void a(EntityLiving entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        loadDownloadableImageTexture(entity.skinUrl, entity.getTexture());
        EntityLabyrinthEye eye = (EntityLabyrinthEye)entity;
        ((ModelLabyrinthEye)this.i).cogNumber = eye.getStage();
        GL11.glRotatef(entity.ticksExisted * 10, 0.0F, 0.0F, 1.0F);
        this.i.render(entity, par2, par3, par4, par5, par6, par7);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        ((ModelLabyrinthEye)this.i).renderEye(entity, par2, par3, par4, par5, par6, par7);
        float f1 = 1.0F;
        Rect2i tessellator = Rect2i.rectX;
        GL11.glTranslated(entity.posX, entity.posY + 30.0D, entity.posZ);
        GL11.glPopMatrix();
    }

    protected int setMarkingBrightness(EntityLabyrinthEye entity, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (entity.getAwake())
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/cogboss/cogglow.png");
        }
        else
        {
            loadTexture("/net/aetherteam/aether/client/sprites/mobs/cogboss/cogglowblue.png");
        }

        float var4 = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glRotatef(entity.ticksExisted * 10, 0.0F, 1.0F, 0.0F);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        if (!entity.getActivePotionEffects().isEmpty())
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setMarkingBrightness((EntityLabyrinthEye)entityliving, i, f);
    }
}

