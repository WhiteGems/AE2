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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class AetherSkyProvider extends IRenderHandler
{
    private static final ResourceLocation TEXTURE_SUN = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation TEXTURE_MOON = new ResourceLocation("textures/environment/moon_phases.png");
    private int glSkyList;
    private int glSkyList2;
    private int starGLCallList;
    private boolean inited;

    public AetherSkyProvider()
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        RenderGlobal rg = mc.renderGlobal;

        try
        {
            this.starGLCallList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, rg, new String[] {"starGLCallList"})).intValue();
            this.glSkyList = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, rg, new String[] {"glSkyList"})).intValue();
            this.glSkyList2 = ((Integer)ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, rg, new String[] {"glSkyList2"})).intValue();
            this.inited = true;
        }
        catch (Exception var4)
        {
            this.inited = false;
        }
    }

    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        if (this.inited)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Vec3 var2 = world.getSkyColor(mc.renderViewEntity, partialTicks);
            float var3 = (float)var2.xCoord;
            float var4 = (float)var2.yCoord;
            float var5 = (float)var2.zCoord;
            float var8;

            if (mc.gameSettings.anaglyph)
            {
                float var23 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
                float var24 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
                var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
                var3 = var23;
                var4 = var24;
                var5 = var8;
            }

            GL11.glColor3f(var3, var4, var5);
            Tessellator var251 = Tessellator.instance;
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glColor3f(var3, var4, var5);
            GL11.glCallList(this.glSkyList);
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            float[] var26 = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
            float var9;
            float var10;
            float var11;
            float var12;
            int var29;
            float var17;
            float var16;

            if (var26 != null)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                GL11.glPushMatrix();
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                var8 = var26[0];
                var9 = var26[1];
                var10 = var26[2];
                float var28;

                if (mc.gameSettings.anaglyph)
                {
                    var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
                    var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
                    var28 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
                    var8 = var11;
                    var9 = var12;
                    var10 = var28;
                }

                var251.startDrawing(6);
                var251.setColorRGBA_F(var8, var9, var10, var26[3]);
                var251.addVertex(0.0D, 100.0D, 0.0D);
                byte var30 = 16;
                var251.setColorRGBA_F(var26[0], var26[1], var26[2], 0.0F);

                for (var29 = 0; var29 <= var30; ++var29)
                {
                    var28 = (float)var29 * (float)Math.PI * 2.0F / (float)var30;
                    var16 = MathHelper.sin(var28);
                    var17 = MathHelper.cos(var28);
                    var251.addVertex((double)(var16 * 120.0F), (double)(var17 * 120.0F), (double)(-var17 * 40.0F * var26[3]));
                }

                var251.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glPushMatrix();
            var8 = 1.0F - world.getRainStrength(partialTicks);
            var9 = 0.0F;
            var10 = 0.0F;
            var11 = 0.0F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
            GL11.glTranslatef(var9, var10, var11);
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            var12 = 30.0F;
            mc.renderEngine.func_110577_a(TEXTURE_SUN);
            var251.startDrawingQuads();
            var251.addVertexWithUV((double)(-var12), 100.0D, (double)(-var12), 0.0D, 0.0D);
            var251.addVertexWithUV((double)var12, 100.0D, (double)(-var12), 1.0D, 0.0D);
            var251.addVertexWithUV((double)var12, 100.0D, (double)var12, 1.0D, 1.0D);
            var251.addVertexWithUV((double)(-var12), 100.0D, (double)var12, 0.0D, 1.0D);
            var251.draw();
            var12 = 20.0F;
            mc.renderEngine.func_110577_a(TEXTURE_MOON);
            int var281 = world.getMoonPhase();
            int var27 = var281 % 4;
            var29 = var281 / 4 % 2;
            var16 = (float)(var27 + 0) / 4.0F;
            var17 = (float)(var29 + 0) / 2.0F;
            float var18 = (float)(var27 + 1) / 4.0F;
            float var19 = (float)(var29 + 1) / 2.0F;
            var251.startDrawingQuads();
            var251.addVertexWithUV((double)(-var12), -100.0D, (double)var12, (double)var18, (double)var19);
            var251.addVertexWithUV((double)var12, -100.0D, (double)var12, (double)var16, (double)var19);
            var251.addVertexWithUV((double)var12, -100.0D, (double)(-var12), (double)var16, (double)var17);
            var251.addVertexWithUV((double)(-var12), -100.0D, (double)(-var12), (double)var18, (double)var17);
            var251.draw();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            float var20 = 1000000.0F + world.getStarBrightness(partialTicks) * var8;

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
            double var25 = mc.thePlayer.getPosition(partialTicks).yCoord - world.getHorizon();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 12.0F, 0.0F);
            GL11.glCallList(this.glSkyList2);
            GL11.glPopMatrix();
            var10 = 1.0F;
            var11 = -((float)(var25 + 65.0D));
            var12 = -var10;
            var251.startDrawingQuads();
            var251.setColorRGBA_F((float)var2.xCoord, (float)var2.yCoord, (float)var2.zCoord, 1.0F);
            var251.addVertex((double)(-var10), (double)var11, (double)var10);
            var251.addVertex((double)var10, (double)var11, (double)var10);
            var251.addVertex((double)var10, (double)var12, (double)var10);
            var251.addVertex((double)(-var10), (double)var12, (double)var10);
            var251.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var251.addVertex((double)var10, (double)var12, (double)(-var10));
            var251.addVertex((double)var10, (double)var11, (double)(-var10));
            var251.addVertex((double)(-var10), (double)var11, (double)(-var10));
            var251.addVertex((double)var10, (double)var12, (double)(-var10));
            var251.addVertex((double)var10, (double)var12, (double)var10);
            var251.addVertex((double)var10, (double)var11, (double)var10);
            var251.addVertex((double)var10, (double)var11, (double)(-var10));
            var251.addVertex((double)(-var10), (double)var11, (double)(-var10));
            var251.addVertex((double)(-var10), (double)var11, (double)var10);
            var251.addVertex((double)(-var10), (double)var12, (double)var10);
            var251.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var251.addVertex((double)(-var10), (double)var12, (double)(-var10));
            var251.addVertex((double)(-var10), (double)var12, (double)var10);
            var251.addVertex((double)var10, (double)var12, (double)var10);
            var251.addVertex((double)var10, (double)var12, (double)(-var10));
            var251.draw();

            if (world.provider.isSkyColored())
            {
                GL11.glColor3f(var3 * 0.9F + 0.04F, var4 * 0.0F + 0.0F, var5 * 0.0F + 0.0F);
            }
            else
            {
                GL11.glColor3f(var3, var4, var5);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -((float)(var25 - 16.0D)), 0.0F);
            GL11.glCallList(this.glSkyList2);
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
    }
}
