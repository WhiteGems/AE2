package net.aetherteam.aether;

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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
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

    public static boolean canPoison(Entity var0)
    {
        return !(var0 instanceof EntitySlider) && !(var0 instanceof EntitySentry) && !(var0 instanceof EntityMiniCloud) && !(var0 instanceof EntityFireMonster) && !(var0 instanceof EntityAechorPlant) && !(var0 instanceof EntityFiroBall) && !(var0 instanceof EntityCockatrice) && !(var0 instanceof EntityHomeShot);
    }

    public static void distractEntity(Entity var0)
    {
        double var1 = var0.worldObj.rand.nextGaussian();
        double var3 = motDFac * var1;
        motD = motTaper * var3 + (1.0D - motTaper) * motD;
        var0.motionX += motD;
        var0.motionZ += motD;
        double var5 = rotDFac * var1;
        rotD = rotTaper * var5 + (1.0D - rotTaper) * rotD;
        var0.rotationYaw = (float)((double)var0.rotationYaw + rotD);
        var0.rotationPitch = (float)((double)var0.rotationPitch + rotD);
    }

    public static void poisonTick(EntityPlayer var0)
    {
        if (var0 != null && (var0.isDead || var0.getHealth() <= 0))
        {
            poisonTime = 0;
        }
        else if (poisonTime < 0)
        {
            ++poisonTime;
        }
        else if (poisonTime != 0)
        {
            long var1 = var0.worldObj.getWorldTime();
            mod = poisonTime % 50;

            if (clock != var1)
            {
                distractEntity(var0);

                if (!var0.worldObj.isRemote && mod == 0)
                {
                    var0.attackEntityFrom(DamageSource.generic, 1);
                }

                --poisonTime;
                clock = var1;
            }
        }
    }

    public static boolean afflictPoison()
    {
        if (poisonTime < 0)
        {
            return false;
        }
        else
        {
            poisonTime = 500;
            return true;
        }
    }

    public static boolean curePoison(int var0)
    {
        if (poisonTime == -500)
        {
            return false;
        }
        else
        {
            poisonTime = -500 - var0;
            return true;
        }
    }

    public static float getPoisonAlpha(float var0)
    {
        return var0 * var0 / 5.0F + 0.4F;
    }

    public static float getCureAlpha(float var0)
    {
        return var0 * var0 / 10.0F + 0.4F;
    }

    public static void displayCureEffect()
    {
        if (poisonTime < 0)
        {
            flashColor("%blur%/net/aetherteam/aether/client/sprites/poison/curevignette.png", getCureAlpha(-((float)mod) / 100.0F), Aether.proxy.getClient());
        }
    }

    public static void displayPoisonEffect()
    {
        if (poisonTime > 0)
        {
            flashColor("%blur%/net/aetherteam/aether/client/sprites/poison/poisonvignette.png", getPoisonAlpha((float)mod / 50.0F), Aether.proxy.getClient());
        }
    }

    public static void flashColor(String var0, float var1, Minecraft var2)
    {
        ScaledResolution var3 = new ScaledResolution(var2.gameSettings, var2.displayWidth, var2.displayHeight);
        int var4 = var3.getScaledWidth();
        int var5 = var3.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2.renderEngine.getTexture(var0));
        Tessellator var6 = Tessellator.instance;
        var6.startDrawingQuads();
        var6.addVertexWithUV(0.0D, (double)var5, -90.0D, 0.0D, 1.0D);
        var6.addVertexWithUV((double)var4, (double)var5, -90.0D, 1.0D, 1.0D);
        var6.addVertexWithUV((double)var4, 0.0D, -90.0D, 1.0D, 0.0D);
        var6.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var6.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
    }
}
