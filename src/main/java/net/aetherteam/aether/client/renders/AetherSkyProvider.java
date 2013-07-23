package net.aetherteam.aether.client.renders;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
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
        Minecraft var1 = FMLClientHandler.instance().getClient();
        RenderGlobal var2 = var1.renderGlobal;

        try
        {
            this.starGLCallList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, var2, new String[] {"starGLCallList"})).intValue();
            this.glSkyList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, var2, new String[] {"glSkyList"})).intValue();
            this.glSkyList2 = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, var2, new String[] {"glSkyList2"})).intValue();
            this.inited = true;
        }
        catch (Exception var4)
        {
            this.inited = false;
        }
    }

    public void render(float var1, WorldClient var2, Minecraft var3)
    {
        if (this.inited)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Vec3 var4 = var2.getSkyColor(var3.renderViewEntity, var1);
            float var5 = (float)var4.xCoord;
            float var6 = (float)var4.yCoord;
            float var7 = (float)var4.zCoord;
            float var8;

            if (var3.gameSettings.anaglyph)
            {
                float var9 = (var5 * 30.0F + var6 * 59.0F + var7 * 11.0F) / 100.0F;
                float var10 = (var5 * 30.0F + var6 * 70.0F) / 100.0F;
                var8 = (var5 * 30.0F + var7 * 70.0F) / 100.0F;
                var5 = var9;
                var6 = var10;
                var7 = var8;
            }

            GL11.glColor3f(var5, var6, var7);
            Tessellator var25 = Tessellator.instance;
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glColor3f(var5, var6, var7);
            GL11.glCallList(this.glSkyList);
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            float[] var26 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(var1), var1);
            float var11;
            float var12;
            float var13;
            float var14;
            int var17;
            float var19;
            float var18;

            if (var26 != null)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                GL11.glPushMatrix();
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(MathHelper.sin(var2.getCelestialAngleRadians(var1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                var8 = var26[0];
                var11 = var26[1];
                var12 = var26[2];
                float var15;

                if (var3.gameSettings.anaglyph)
                {
                    var13 = (var8 * 30.0F + var11 * 59.0F + var12 * 11.0F) / 100.0F;
                    var14 = (var8 * 30.0F + var11 * 70.0F) / 100.0F;
                    var15 = (var8 * 30.0F + var12 * 70.0F) / 100.0F;
                    var8 = var13;
                    var11 = var14;
                    var12 = var15;
                }

                var25.startDrawing(6);
                var25.setColorRGBA_F(var8, var11, var12, var26[3]);
                var25.addVertex(0.0D, 100.0D, 0.0D);
                byte var16 = 16;
                var25.setColorRGBA_F(var26[0], var26[1], var26[2], 0.0F);

                for (var17 = 0; var17 <= var16; ++var17)
                {
                    var15 = (float)var17 * (float)Math.PI * 2.0F / (float)var16;
                    var18 = MathHelper.sin(var15);
                    var19 = MathHelper.cos(var15);
                    var25.addVertex((double)(var18 * 120.0F), (double)(var19 * 120.0F), (double)(-var19 * 40.0F * var26[3]));
                }

                var25.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glPushMatrix();
            var8 = 1.0F - var2.getRainStrength(var1);
            var11 = 0.0F;
            var12 = 0.0F;
            var13 = 0.0F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
            GL11.glTranslatef(var11, var12, var13);
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var2.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
            var14 = 30.0F;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3.renderEngine.getTexture("/environment/sun.png"));
            var25.startDrawingQuads();
            var25.addVertexWithUV((double)(-var14), 100.0D, (double)(-var14), 0.0D, 0.0D);
            var25.addVertexWithUV((double)var14, 100.0D, (double)(-var14), 1.0D, 0.0D);
            var25.addVertexWithUV((double)var14, 100.0D, (double)var14, 1.0D, 1.0D);
            var25.addVertexWithUV((double)(-var14), 100.0D, (double)var14, 0.0D, 1.0D);
            var25.draw();
            var14 = 20.0F;
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3.renderEngine.getTexture("/environment/moon_phases.png"));
            int var28 = var2.getMoonPhase();
            int var27 = var28 % 4;
            var17 = var28 / 4 % 2;
            var18 = (float)(var27 + 0) / 4.0F;
            var19 = (float)(var17 + 0) / 2.0F;
            float var20 = (float)(var27 + 1) / 4.0F;
            float var21 = (float)(var17 + 1) / 2.0F;
            var25.startDrawingQuads();
            var25.addVertexWithUV((double)(-var14), -100.0D, (double)var14, (double)var20, (double)var21);
            var25.addVertexWithUV((double)var14, -100.0D, (double)var14, (double)var18, (double)var21);
            var25.addVertexWithUV((double)var14, -100.0D, (double)(-var14), (double)var18, (double)var19);
            var25.addVertexWithUV((double)(-var14), -100.0D, (double)(-var14), (double)var20, (double)var19);
            var25.draw();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            float var22 = 1000000.0F + var2.getStarBrightness(var1) * var8;

            if (var22 > 0.0F)
            {
                GL11.glColor4f(var22, var22, var22, var22);
                GL11.glCallList(this.starGLCallList);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(0.0F, 0.0F, 0.0F);
            double var23 = var3.thePlayer.getPosition(var1).yCoord - var2.getHorizon();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 12.0F, 0.0F);
            GL11.glCallList(this.glSkyList2);
            GL11.glPopMatrix();
            var12 = 1.0F;
            var13 = -((float)(var23 + 65.0D));
            var14 = -var12;
            var25.startDrawingQuads();
            var25.setColorRGBA_F((float)var4.xCoord, (float)var4.yCoord, (float)var4.zCoord, 1.0F);
            var25.addVertex((double)(-var12), (double)var13, (double)var12);
            var25.addVertex((double)var12, (double)var13, (double)var12);
            var25.addVertex((double)var12, (double)var14, (double)var12);
            var25.addVertex((double)(-var12), (double)var14, (double)var12);
            var25.addVertex((double)(-var12), (double)var14, (double)(-var12));
            var25.addVertex((double)var12, (double)var14, (double)(-var12));
            var25.addVertex((double)var12, (double)var13, (double)(-var12));
            var25.addVertex((double)(-var12), (double)var13, (double)(-var12));
            var25.addVertex((double)var12, (double)var14, (double)(-var12));
            var25.addVertex((double)var12, (double)var14, (double)var12);
            var25.addVertex((double)var12, (double)var13, (double)var12);
            var25.addVertex((double)var12, (double)var13, (double)(-var12));
            var25.addVertex((double)(-var12), (double)var13, (double)(-var12));
            var25.addVertex((double)(-var12), (double)var13, (double)var12);
            var25.addVertex((double)(-var12), (double)var14, (double)var12);
            var25.addVertex((double)(-var12), (double)var14, (double)(-var12));
            var25.addVertex((double)(-var12), (double)var14, (double)(-var12));
            var25.addVertex((double)(-var12), (double)var14, (double)var12);
            var25.addVertex((double)var12, (double)var14, (double)var12);
            var25.addVertex((double)var12, (double)var14, (double)(-var12));
            var25.draw();

            if (var2.provider.isSkyColored())
            {
                GL11.glColor3f(var5 * 0.9F + 0.04F, var6 * 0.0F + 0.0F, var7 * 0.0F + 0.0F);
            }
            else
            {
                GL11.glColor3f(var5, var6, var7);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -((float)(var23 - 16.0D)), 0.0F);
            GL11.glCallList(this.glSkyList2);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
    }
}
