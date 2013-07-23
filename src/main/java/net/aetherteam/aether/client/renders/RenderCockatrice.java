package net.aetherteam.aether.client.renders;

import java.util.Collection;
import net.aetherteam.aether.entities.EntityCockatrice;
import net.minecraft.client.entity.render.RenderMinecartMobSpawner;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderCockatrice extends RenderMinecartMobSpawner
{
    public RenderCockatrice(ModelMinecart modelbase, float f)
    {
        super(modelbase, f);
        a(modelbase);
        this.j = modelbase;
    }

    protected float getWingRotation(EntityCockatrice entitybadmoa, float f)
    {
        float f1 = entitybadmoa.field_756_e + (entitybadmoa.field_752_b - entitybadmoa.field_756_e) * f;
        float f2 = entitybadmoa.field_757_d + (entitybadmoa.destPos - entitybadmoa.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    protected float b(EntityLiving entityliving, float f)
    {
        return getWingRotation((EntityCockatrice)entityliving, f);
    }

    protected void scalemoa()
    {
        GL11.glScalef(1.8F, 1.8F, 1.8F);
    }

    protected void a(EntityLiving entityliving, float f)
    {
        scalemoa();
    }

    protected int setMarkingBrightness(EntityCockatrice cock, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        loadTexture("/net/aetherteam/aether/client/sprites/mobs/cockatrice/markings.png");
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
        return 1;
    }

    protected int a(EntityLiving entityliving, int i, float f)
    {
        return setMarkingBrightness((EntityCockatrice)entityliving, i, f);
    }
}

