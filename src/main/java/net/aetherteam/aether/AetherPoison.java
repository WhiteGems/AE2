package net.aetherteam.aether;

import java.util.Random;
import net.aetherteam.aether.entities.EntityAechorPlant;
import net.aetherteam.aether.entities.EntityCockatrice;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.aetherteam.aether.oldcode.EntityFireMonster;
import net.aetherteam.aether.oldcode.EntityFiroBall;
import net.aetherteam.aether.oldcode.EntityHomeShot;
import net.aetherteam.aether.oldcode.EntityMiniCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class AetherPoison
{
    public static long clock;
    public static final float poisonRed = 1.0F;
    public static final float poisonBlue = 1.0F;
    public static final float poisonGreen = 0.0F;
    public static final float cureRed = 0.0F;
    public static final float cureBlue = 0.1F;
    public static final float cureGreen = 1.0F;
    public static int poisonTime;
    public static final int poisonInterval = 50;
    public static final int poisonDmg = 1;
    public static final int poisonHurts = 10;
    public static final int maxPoisonTime = 500;
    public static double rotDFac = (Math.PI / 4D);
    public static double rotD;
    public static double rotTaper = 0.125D;
    public static double motTaper = 0.2D;
    public static double motD;
    public static double motDFac = 0.1D;
    private static int mod;

    public static boolean canPoison(Entity entity)
    {
        if (((entity instanceof EntitySlider)) || ((entity instanceof EntitySentry)) || ((entity instanceof EntityMiniCloud)) || ((entity instanceof EntityFireMonster)) || ((entity instanceof EntityAechorPlant)) || ((entity instanceof EntityFiroBall)) || ((entity instanceof EntityCockatrice)) || ((entity instanceof EntityHomeShot)))
        {
            return false;
        }

        return true;
    }

    public static void distractEntity(Entity entity)
    {
        double gauss = entity.worldObj.rand.nextGaussian();
        double newMotD = motDFac * gauss;
        motD = motTaper * newMotD + (1.0D - motTaper) * motD;
        entity.motionX += motD;
        entity.motionZ += motD;
        double newRotD = rotDFac * gauss;
        rotD = rotTaper * newRotD + (1.0D - rotTaper) * rotD;
        entity.rotationYaw = ((float)(entity.rotationYaw + rotD));
        entity.rotationPitch = ((float)(entity.rotationPitch + rotD));
    }

    public static void poisonTick(EntityPlayer player)
    {
        if ((player != null) && ((player.isDead) || (player.getHealth() <= 0)))
        {
            poisonTime = 0;
            return;
        }

        if (poisonTime < 0)
        {
            poisonTime += 1;
            return;
        }

        if (poisonTime == 0)
        {
            return;
        }

        long time = player.worldObj.I();
        mod = poisonTime % 50;

        if (clock != time)
        {
            distractEntity(player);

            if (!player.worldObj.isRemote)
            {
                if (mod == 0)
                {
                    player.attackEntityFrom(DamageSource.generic, 1);
                }
            }

            poisonTime -= 1;
            clock = time;
        }
    }

    public static boolean afflictPoison()
    {
        if (poisonTime < 0)
        {
            return false;
        }

        poisonTime = 500;
        return true;
    }

    public static boolean curePoison(int i)
    {
        if (poisonTime == -500)
        {
            return false;
        }

        poisonTime = -500 - i;
        return true;
    }

    public static float getPoisonAlpha(float f)
    {
        return f * f / 5.0F + 0.4F;
    }

    public static float getCureAlpha(float f)
    {
        return f * f / 10.0F + 0.4F;
    }

    public static void displayCureEffect()
    {
        if (poisonTime < 0)
        {
            flashColor("%blur%/net/aetherteam/aether/client/sprites/poison/curevignette.png", getCureAlpha(-mod / 100.0F), Aether.proxy.getClient());
        }
    }

    public static void displayPoisonEffect()
    {
        if (poisonTime > 0)
        {
            flashColor("%blur%/net/aetherteam/aether/client/sprites/poison/poisonvignette.png", getPoisonAlpha(mod / 50.0F), Aether.proxy.getClient());
        }
    }

    public static void flashColor(String file, float a, Minecraft mc)
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.f(file));
        Rect2i tessellator = Rect2i.rectX;
        tessellator.b();
        tessellator.a(0.0D, height, -90.0D, 0.0D, 1.0D);
        tessellator.a(width, height, -90.0D, 1.0D, 1.0D);
        tessellator.a(width, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.a(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.getRectX();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
    }
}

