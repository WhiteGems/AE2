package net.aetherteam.aether;

import net.aetherteam.aether.entities.EntityAechorPlant;
import net.aetherteam.aether.entities.EntityCockatrice;
import net.aetherteam.aether.entities.EntitySentry;
import net.aetherteam.aether.entities.bosses.EntitySlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AetherPoison
{
    private static final ResourceLocation TEXTURE_POISONVIGNETTE = new ResourceLocation("aether", "textures/poison/poisonvignette.png");
    private static final ResourceLocation TEXTURE_CUREVIGNETTE = new ResourceLocation("aether", "textures/poison/curevignette.png");
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
        return !(entity instanceof EntitySlider) && !(entity instanceof EntitySentry) && !(entity instanceof EntityAechorPlant) && !(entity instanceof EntityCockatrice);
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
        entity.rotationYaw = (float)((double)entity.rotationYaw + rotD);
        entity.rotationPitch = (float)((double)entity.rotationPitch + rotD);
    }

    public static void poisonTick(EntityPlayer player)
    {
        if (player != null && (player.isDead || player.func_110143_aJ() <= 0.0F))
        {
            poisonTime = 0;
        }
        else if (poisonTime < 0)
        {
            ++poisonTime;
        }
        else if (poisonTime != 0)
        {
            long time = player.worldObj.getWorldTime();
            mod = poisonTime % 50;

            if (clock != time)
            {
                distractEntity(player);

                if (!player.worldObj.isRemote && mod == 0)
                {
                    player.attackEntityFrom(DamageSource.generic, 1.0F);
                }

                --poisonTime;
                clock = time;
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

    public static boolean curePoison(int i)
    {
        if (poisonTime == -500)
        {
            return false;
        }
        else
        {
            poisonTime = -500 - i;
            return true;
        }
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
            flashColor(TEXTURE_CUREVIGNETTE, getCureAlpha(-((float)mod) / 100.0F), Aether.proxy.getClient());
        }
    }

    public static void displayPoisonEffect()
    {
        if (poisonTime > 0)
        {
            flashColor(TEXTURE_POISONVIGNETTE, getPoisonAlpha((float)mod / 50.0F), Aether.proxy.getClient());
        }
    }

    public static void flashColor(ResourceLocation texture, float a, Minecraft mc)
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
        mc.renderEngine.func_110577_a(texture);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
    }
}
