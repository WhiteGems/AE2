package net.aetherteam.mainmenu_api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOnlineServers;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(Side.CLIENT)
public class MenuBaseAether extends MenuBase
{
    private static final Random rand = new Random();

    private float updateCounter = 0.0F;

    private String splashText = "missingno";
    private GuiAetherButton buttonResetDemo;
    private int panoramaTimer = 0;

    private float scalingLol = 0.975F;
    private int viewportTexture;
    private boolean field_96141_q = true;
    private static boolean field_96140_r = false;
    private static boolean field_96139_s = false;
    private String field_92025_p;
    private static final String[] titlePanoramaPaths = {"/net/aetherteam/mainmenu_api/title/bg/panorama0.png", "/net/aetherteam/mainmenu_api/title/bg/panorama1.png", "/net/aetherteam/mainmenu_api/title/bg/panorama2.png", "/net/aetherteam/mainmenu_api/title/bg/panorama3.png", "/net/aetherteam/mainmenu_api/title/bg/panorama4.png", "/net/aetherteam/mainmenu_api/title/bg/panorama5.png"};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private GuiAetherButton fmlModButton = null;

    public MenuBaseAether()
    {
        BufferedReader bufferedreader = null;
        try
        {
            ArrayList arraylist = new ArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(MenuBaseAether.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
            String s;
            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (s.length() > 0)
                {
                    arraylist.add(s);
                }
            }

            do
            {
                this.splashText = ((String) arraylist.get(rand.nextInt(arraylist.size())));
            } while (this.splashText.hashCode() == 125780783);
        } catch (IOException ioexception)
        {
        } finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                } catch (IOException ioexception1)
                {
                }
            }

        }

        this.updateCounter = rand.nextFloat();
    }

    public void updateScreen()
    {
        this.panoramaTimer += 1;
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected void keyTyped(char par1, int par2)
    {
    }

    public void initGui()
    {
        super.initGui();

        this.viewportTexture = this.mc.renderEngine.allocateAndSetupTexture(new BufferedImage(256, 256, 2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if ((calendar.get(2) + 1 == 11) && (calendar.get(5) == 9))
        {
            this.splashText = "Happy birthday, ez!";
        } else if ((calendar.get(2) + 1 == 6) && (calendar.get(5) == 1))
        {
            this.splashText = "Happy birthday, Notch!";
        } else if ((calendar.get(2) + 1 == 12) && (calendar.get(5) == 24))
        {
            this.splashText = "Merry X-mas!";
        } else if ((calendar.get(2) + 1 == 1) && (calendar.get(5) == 1))
        {
            this.splashText = "Happy new year!";
        } else if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31))
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = this.height / 4 + 68;

        if (this.mc.isDemo())
        {
            addDemoButtons(i, 24, stringtranslate);
        } else
        {
            addSingleplayerMultiplayerButtons(i, 24, stringtranslate);
        }

        this.fmlModButton = new GuiAetherButton(6, 30, i + 48 - 45, "Mods");
        this.buttonList.add(this.fmlModButton);

        func_96137_a(stringtranslate, i, 24);

        if (this.mc.hideQuitButton)
        {
            this.buttonList.add(new GuiAetherButton(0, 30, i + 27, stringtranslate.translateKey("menu.options")));
        } else
        {
            this.buttonList.add(new GuiAetherButton(0, 30, i + 27 + 12, 200, 20, stringtranslate.translateKey("menu.options")));
            this.buttonList.add(new GuiAetherButton(4, 30, i + 27 + 35, 200, 20, stringtranslate.translateKey("menu.quit")));
        }

        this.buttonList.add(new GuiButtonLanguage(5, this.width - 48, 4));
        this.field_92025_p = "";
        String s = System.getProperty("os_architecture");
        String s1 = System.getProperty("java_version");

        if ("ppc".equalsIgnoreCase(s))
        {
            this.field_92025_p = ("" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " PowerPC compatibility will be dropped in Minecraft 1.6");
        } else if ((s1 != null) && (s1.startsWith("1.5")))
        {
            this.field_92025_p = ("" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " Java 1.5 compatibility will be dropped in Minecraft 1.6");
        }

        this.field_92023_s = this.fontRenderer.getStringWidth(this.field_92025_p);
        this.field_92024_r = this.fontRenderer.getStringWidth(field_96138_a);
        int j = Math.max(this.field_92023_s, this.field_92024_r);
        this.field_92022_t = ((this.width - j) / 2);
        this.field_92021_u = (((GuiAetherButton) this.buttonList.get(0)).yPosition - 24);
        this.field_92020_v = (this.field_92022_t + j);
        this.field_92019_w = (this.field_92021_u + 24);
    }

    private void func_96137_a(StringTranslate par1StringTranslate, int par2, int par3)
    {
        if (this.field_96141_q)
        {
            if (!field_96140_r)
            {
                field_96140_r = true;
            } else if (field_96139_s)
            {
                func_98060_b(par1StringTranslate, par2, par3);
            }
        }
    }

    private void func_98060_b(StringTranslate par1StringTranslate, int par2, int par3)
    {
        this.fmlModButton.xPosition = (this.width / 2 + 2);

        GuiAetherButton realmButton = new GuiAetherButton(3, 30, par2 - 45 + par3 * 2, par1StringTranslate.translateKey("menu.online"));

        realmButton.xPosition = (this.width / 2 - 100);
        this.buttonList.add(realmButton);
    }

    private void addSingleplayerMultiplayerButtons(int par1, int par2, StringTranslate par3StringTranslate)
    {
        this.buttonList.add(new GuiAetherButton(1, 30, par1 - 45, par3StringTranslate.translateKey("menu.singleplayer")));
        this.buttonList.add(new GuiAetherButton(2, 30, par1 - 45 + par2 * 1, par3StringTranslate.translateKey("menu.multiplayer")));
    }

    private void addDemoButtons(int par1, int par2, StringTranslate par3StringTranslate)
    {
        this.buttonList.add(new GuiAetherButton(11, 30, par1, par3StringTranslate.translateKey("menu.playdemo")));
        this.buttonList.add(this.buttonResetDemo = new GuiAetherButton(12, 30, par1 - 45 + par2 * 1, par3StringTranslate.translateKey("menu.resetdemo")));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (par1GuiButton.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings));
        }

        if (par1GuiButton.id == 1)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (par1GuiButton.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (par1GuiButton.id == 3)
        {
            this.mc.displayGuiScreen(new GuiScreenOnlineServers(this));
        }

        if (par1GuiButton.id == 4)
        {
            this.mc.shutdown();
        }

        if (par1GuiButton.id == 6)
        {
            this.mc.displayGuiScreen(new GuiModList(this));
        }

        if (par1GuiButton.id == 11)
        {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (par1GuiButton.id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                GuiYesNo guiyesno = GuiSelectWorld.getDeleteWorldScreen(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if ((par1) && (par2 == 12))
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (par2 == 13)
        {
            if (par1)
            {
                try
                {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{new URI("http://tinyurl.com/javappc")});
                } catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    private void drawPanorama(int par1, int par2, float par3)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; k++)
        {
            GL11.glPushMatrix();
            float f1 = (k % b0 / b0 - 0.5F) / 64.0F;
            float f2 = (k / b0 / b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin((this.panoramaTimer + par3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-(this.panoramaTimer + par3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; l++)
            {
                GL11.glPushMatrix();

                if (l == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.renderEngine.bindTexture(titlePanoramaPaths[l]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + f4, 0.0F + f4);
                tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - f4, 0.0F + f4);
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f4, 1.0F - f4);
                tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + f4, 1.0F - f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(2929);
    }

    private void rotateAndBlurSkybox(float par1)
    {
        GL11.glBindTexture(3553, this.viewportTexture);
        this.mc.renderEngine.resetBoundTexture();
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        byte b0 = 3;

        for (int i = 0; i < b0; i++)
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (i - b0 / 2) / 256.0F;
            tessellator.addVertexWithUV(j, k, this.zLevel, 0.0F + f1, 0.0D);
            tessellator.addVertexWithUV(j, 0.0D, this.zLevel, 1.0F + f1, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, this.zLevel, 1.0F + f1, 1.0D);
            tessellator.addVertexWithUV(0.0D, k, this.zLevel, 0.0F + f1, 1.0D);
        }

        tessellator.draw();
        GL11.glColorMask(true, true, true, true);
        this.mc.renderEngine.resetBoundTexture();
    }

    private void renderSkybox(int par1, int par2, float par3)
    {
        GL11.glViewport(0, 0, 256, 256);
        drawPanorama(par1, par2, par3);
        GL11.glDisable(3553);
        GL11.glEnable(3553);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
        float f2 = this.height * f1 / 256.0F;
        float f3 = this.width * f1 / 256.0F;
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        tessellator.addVertexWithUV(0.0D, l, this.zLevel, 0.5F - f2, 0.5F + f3);
        tessellator.addVertexWithUV(k, l, this.zLevel, 0.5F - f2, 0.5F - f3);
        tessellator.addVertexWithUV(k, 0.0D, this.zLevel, 0.5F + f2, 0.5F - f3);
        tessellator.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + f2, 0.5F + f3);
        tessellator.draw();
    }

    public void drawLogo(int k, int b0)
    {
        GL11.glPushMatrix();

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        this.scalingLol += this.scalingLol * 0.001F;

        if (this.scalingLol > 1.0F)
        {
            this.scalingLol = 1.0F;
        }
        GL11.glScalef(this.scalingLol, this.scalingLol, this.scalingLol);
        drawTexturedModalRect(25, b0 - 10, 0, 0, 155, 44);
        drawTexturedModalRect(180, b0 - 10, 0, 45, 155, 44);

        GL11.glDisable(3042);

        GL11.glPopMatrix();
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        renderSkybox(par1, par2, par3);
        Tessellator tessellator = Tessellator.instance;
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        byte b0 = 30;
        drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        drawGradientRect(0, 0, this.width, this.height, 0, -2147483648);
        this.mc.renderEngine.bindTexture("/net/aetherteam/mainmenu_api/title/mclogomod1.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.updateCounter < 0.0001D)
        {
            drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
            drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
            drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
            drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
            drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
        } else
        {
            drawLogo(k, b0);
        }

        tessellator.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef(215.0F, 50.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.4F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.141593F * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(f1, f1, f1);
        drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
        GL11.glPopMatrix();
        String s = "Minecraft 1.5.1";

        if (this.mc.isDemo())
        {
            s = s + " Demo";
        }

        List brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings());
        for (int i = 0; i < brandings.size(); i++)
        {
            String brd = (String) brandings.get(i);
            if (!Strings.isNullOrEmpty(brd))
            {
                drawString(this.fontRenderer, brd, this.width - 2 - this.fontRenderer.getStringWidth(brd), this.height - (10 + i * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }

        String s1 = "Copyright Mojang AB. Do not distribute!";
        String info="白宝石小组荣誉出品！";
        drawString(this.fontRenderer, s1, 2, this.height - 10, 16777215);
        drawString(this.fontRenderer, info, 2, this.height - 20, 16777215);

        if ((this.field_92025_p != null) && (this.field_92025_p.length() > 0))
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            drawString(this.fontRenderer, this.field_92025_p, this.field_92022_t, this.field_92021_u, 16777215);
            drawString(this.fontRenderer, field_96138_a, (this.width - this.field_92024_r) / 2, ((GuiAetherButton) this.buttonList.get(0)).yPosition - 12, 16777215);
        }

        super.drawScreen(par1, par2, par3);
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if ((this.field_92025_p.length() > 0) && (par1 >= this.field_92022_t) && (par1 <= this.field_92020_v) && (par2 >= this.field_92021_u) && (par2 <= this.field_92019_w))
        {
            GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, "http://tinyurl.com/javappc", 13, true);
            guiconfirmopenlink.func_92026_h();
            this.mc.displayGuiScreen(guiconfirmopenlink);
        }
    }

    static Minecraft func_98058_a(MenuBaseAether par0GuiMainMenu)
    {
        return par0GuiMainMenu.mc;
    }

    static void func_98061_a(MenuBaseAether par0GuiMainMenu, StringTranslate par1StringTranslate, int par2, int par3)
    {
        par0GuiMainMenu.func_98060_b(par1StringTranslate, par2, par3);
    }

    static boolean func_98059_a(boolean par0)
    {
        field_96139_s = par0;
        return par0;
    }

    public int getListButtonX()
    {
        return this.width - 110;
    }

    public int getListButtonY()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();

        return 4;
    }

    public int getJukeboxButtonX()
    {
        return this.width - 24;
    }

    public int getJukeboxButtonY()
    {
        return 4;
    }

    public String getName()
    {
        return "Aether I";
    }

    public String getVersion()
    {
        return "王者归来!";
    }

    public String getMusicFileName()
    {
        return "Aether Menu";
    }

    public String getIconPath()
    {
        return "/net/aetherteam/mainmenu_api/icons/aether/MenuIcon.png";
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.mainmenu_api.MenuBaseAether
 * JD-Core Version:    0.6.2
 */
