package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.CallableMPL2;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AetherSkyProvider extends IRenderHandler
{
    private int glSkyList;
    private int glSkyList2;
    private int starGLCallList;
    private boolean inited;

    public AetherSkyProvider()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        ItemRenderer rg = mc.renderGlobal;

        try
        {
            this.starGLCallList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, rg, new String[] { "starGLCallList" })).intValue();
            this.glSkyList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, rg, new String[] { "glSkyList" })).intValue();
            this.glSkyList2 = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ItemRenderer.class, rg, new String[] { "glSkyList2" })).intValue();
            this.inited = true;
        }
        catch (Exception e)
        {
            this.inited = false;
        }
    }

    public void render(float partialTicks, PlayerControllerMP world, Minecraft mc)
    {
        if (!this.inited)
        {
            return;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3 var2 = world.a(mc.renderViewEntity, partialTicks);
        float var3 = (float)var2.xCoord;
        float var4 = (float)var2.yCoord;
        float var5 = (float)var2.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
            float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            float var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var5 = var8;
        }

        GL11.glColor3f(var3, var4, var5);
        Rect2i var23 = Rect2i.rectX;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(var3, var4, var5);
        GL11.glCallList(this.glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        float[] var24 = world.t.calcSunriseSunsetColors(world.c(partialTicks), partialTicks);

        if (var24 != null)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(world.d(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            float var8 = var24[0];
            float var9 = var24[1];
            float var10 = var24[2];

            if (mc.gameSettings.anaglyph)
            {
                float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
                float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
                float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
                var8 = var11;
                var9 = var12;
                var10 = var13;
            }

            var23.b(6);
            var23.a(var8, var9, var10, var24[3]);
            var23.a(0.0D, 100.0D, 0.0D);
            byte var26 = 16;
            var23.a(var24[0], var24[1], var24[2], 0.0F);

            for (int var27 = 0; var27 <= var26; var27++)
            {
                float var13 = var27 * (float)Math.PI * 2.0F / var26;
                float var14 = MathHelper.sin(var13);
                float var15 = MathHelper.cos(var13);
                var23.a(var14 * 120.0F, var15 * 120.0F, -var15 * 40.0F * var24[3]);
            }

            var23.getRectX();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        float var8 = 1.0F - world.i(partialTicks);
        float var9 = 0.0F;
        float var10 = 0.0F;
        float var11 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
        GL11.glTranslatef(var9, var10, var11);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(world.c(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        float var12 = 30.0F;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.f("/environment/sun.png"));
        var23.b();
        var23.a(-var12, 100.0D, -var12, 0.0D, 0.0D);
        var23.a(var12, 100.0D, -var12, 1.0D, 0.0D);
        var23.a(var12, 100.0D, var12, 1.0D, 1.0D);
        var23.a(-var12, 100.0D, var12, 0.0D, 1.0D);
        var23.getRectX();
        var12 = 20.0F;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.f("/environment/moon_phases.png"));
        int var28 = world.w();
        int var30 = var28 % 4;
        int var29 = var28 / 4 % 2;
        float var16 = (var30 + 0) / 4.0F;
        float var17 = (var29 + 0) / 2.0F;
        float var18 = (var30 + 1) / 4.0F;
        float var19 = (var29 + 1) / 2.0F;
        var23.b();
        var23.a(-var12, -100.0D, var12, var18, var19);
        var23.a(var12, -100.0D, var12, var16, var19);
        var23.a(var12, -100.0D, -var12, var16, var17);
        var23.a(-var12, -100.0D, -var12, var18, var17);
        var23.getRectX();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float var20 = 1000000.0F + world.g(partialTicks) * var8;

        if (var20 > 0.0F)
        {
            GL11.glColor4f(var20, var20, var20, var20);
            GL11.glCallList(this.starGLCallList);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.0F, 0.0F, 0.0F);
        double var25 = mc.thePlayer.h(partialTicks).yCoord - world.T();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 12.0F, 0.0F);
        GL11.glCallList(this.glSkyList2);
        GL11.glPopMatrix();
        var10 = 1.0F;
        var11 = -(float)(var25 + 65.0D);
        var12 = -var10;
        var23.b();
        var23.a((float)var2.xCoord, (float)var2.yCoord, (float)var2.zCoord, 1.0F);
        var23.a(-var10, var11, var10);
        var23.a(var10, var11, var10);
        var23.a(var10, var12, var10);
        var23.a(-var10, var12, var10);
        var23.a(-var10, var12, -var10);
        var23.a(var10, var12, -var10);
        var23.a(var10, var11, -var10);
        var23.a(-var10, var11, -var10);
        var23.a(var10, var12, -var10);
        var23.a(var10, var12, var10);
        var23.a(var10, var11, var10);
        var23.a(var10, var11, -var10);
        var23.a(-var10, var11, -var10);
        var23.a(-var10, var11, var10);
        var23.a(-var10, var12, var10);
        var23.a(-var10, var12, -var10);
        var23.a(-var10, var12, -var10);
        var23.a(-var10, var12, var10);
        var23.a(var10, var12, var10);
        var23.a(var10, var12, -var10);
        var23.getRectX();

        if (world.t.isSkyColored())
        {
            GL11.glColor3f(var3 * 0.9F + 0.04F, var4 * 0.0F + 0.0F, var5 * 0.0F + 0.0F);
        }
        else
        {
            GL11.glColor3f(var3, var4, var5);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -(float)(var25 - 16.0D), 0.0F);
        GL11.glCallList(this.glSkyList2);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }
}

