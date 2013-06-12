package net.aetherteam.aether;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.aetherteam.aether.client.gui.menu.GuiAetherIIButton;
import net.aetherteam.mainmenu_api.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOnlineServers;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
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
public class MenuBaseAetherII extends MenuBase
{
    private static final Random rand = new Random();
    private static AetherTUGButton TUGbutton;
    private float updateCounter = 0.0F;
    private String splashText = "missingno";
    private GuiAetherIIButton buttonResetDemo;
    private GuiButton selectedButton = null;
    private int panoramaTimer = 0;
    private float scalingLol = 0.975F;
    private int viewportTexture;
    private boolean field_96141_q = true;
    private static boolean field_96140_r = false;
    private static boolean field_96139_s = false;
    private String field_92025_p;
    private static final String[] titlePanoramaPaths = new String[]{"/net/aetherteam/aether/client/sprites/menu/bg/panorama0.png", "/net/aetherteam/aether/client/sprites/menu/bg/panorama1.png", "/net/aetherteam/aether/client/sprites/menu/bg/panorama2.png", "/net/aetherteam/aether/client/sprites/menu/bg/panorama3.png", "/net/aetherteam/aether/client/sprites/menu/bg/panorama4.png", "/net/aetherteam/aether/client/sprites/menu/bg/panorama5.png"};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private GuiAetherIIButton fmlModButton = null;
    private GuiAetherIIButton hideOptionsButton = null;
    private GuiAetherIIButton optionsButton = null;
    private GuiAetherIIButton quitButton = null;
    private GuiButtonLanguage languageButton = null;
    private GuiAetherIIButton singleplayerButton = null;
    private GuiAetherIIButton multiplayerButton = null;
    private GuiAetherIIButton officialServerButton = null;
    private GuiAetherIIButton backButton = null;
    private GuiButton exploreTUG = null;
    private ArrayList serverdata = new ArrayList();
    private ArrayList serverButtons = new ArrayList();
    private boolean TUGopen = false;
    private boolean SERVERlist = false;

    public MenuBaseAetherII()
    {
        BufferedReader var1 = null;

        try
        {
            ArrayList var2 = new ArrayList();
            var1 = new BufferedReader(new InputStreamReader(MenuBaseAetherII.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
            String var3;

            while ((var3 = var1.readLine()) != null)
            {
                var3 = var3.trim();

                if (var3.length() > 0)
                {
                    var2.add(var3);
                }
            }

            do
            {
                this.splashText = (String) var2.get(rand.nextInt(var2.size()));
            } while (this.splashText.hashCode() == 125780783);
        } catch (IOException var12)
        {
            ;
        } finally
        {
            if (var1 != null)
            {
                try
                {
                    var1.close();
                } catch (IOException var11)
                {
                    ;
                }
            }
        }

        this.updateCounter = rand.nextFloat();
    }

    private void addServer(int var1)
    {
        int var2 = this.serverdata.size();
        String var3 = "Aether服务器 " + (var2 + 1);
        this.serverdata.add(new ServerData(var3, "109.72.82.220:" + var1));
        this.serverButtons.add(new GuiAetherIIButton(15 + var2 + 1, 2 + (var2 >= 8 ? 140 : 0), 30 + 25 * (var2 - 1 - (var2 >= 8 ? 8 : 0)), 120, 20, var3));
        this.buttonList.add(this.serverButtons.get(var2));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.panoramaTimer;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.viewportTexture = this.mc.renderEngine.allocateAndSetupTexture(new BufferedImage(256, 256, 2));
        Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());

        if (var1.get(2) + 1 == 11 && var1.get(5) == 9)
        {
            this.splashText = "Happy birthday, ez!";
        } else if (var1.get(2) + 1 == 6 && var1.get(5) == 1)
        {
            this.splashText = "Happy birthday, Notch!";
        } else if (var1.get(2) + 1 == 12 && var1.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        } else if (var1.get(2) + 1 == 1 && var1.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        } else if (var1.get(2) + 1 == 10 && var1.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        StringTranslate var2 = StringTranslate.getInstance();
        int var3 = this.height / 4 + 68;
        this.serverdata.clear();
        this.serverButtons.clear();
        this.addServer(25587);
        this.addServer(25598);
        this.addServer(25609);
        this.addServer(25620);
        this.addServer(25631);
        this.addServer(25642);
        this.addServer(25653);
        this.addServer(25664);
        this.addServer(25675);
        this.addServer(25686);
        this.addServer(25697);
        this.addServer(25708);
        this.addServer(25719);
        this.addServer(25730);
        this.addServer(25741);
        this.addServer(25752);
        this.backButton = new GuiAetherIIButton(13, 3, this.height - 35, 180, 20, "返回主界面");
        this.buttonList.add(this.backButton);
        Iterator var4 = this.serverdata.iterator();

        while (var4.hasNext())
        {
            ServerData var5 = (ServerData) var4.next();
            var5.setHideAddress(true);
        }

        if (this.mc.isDemo())
        {
            this.addDemoButtons(var3, 24, var2);
        } else
        {
            this.addSingleplayerMultiplayerButtons(var3, 24, var2);
        }

        this.fmlModButton = new GuiAetherIIButton(6, 30, var3 + 48 - 45, "Mods");
        this.buttonList.add(this.fmlModButton);
        this.func_96137_a(var2, var3, 24);
        this.hideOptionsButton = new GuiAetherIIButton(0, 30, var3 + 27 + 24, var2.translateKey("menu.options"));
        this.optionsButton = new GuiAetherIIButton(0, 30, var3 + 27 + 24, 200, 20, var2.translateKey("menu.options"));
        this.quitButton = new GuiAetherIIButton(4, 30, var3 + 27 + 48, 200, 20, var2.translateKey("menu.quit"));
        this.languageButton = new GuiButtonLanguage(5, this.width - 48, 4);

        if (this.mc.hideQuitButton)
        {
            this.buttonList.add(this.hideOptionsButton);
        } else
        {
            this.buttonList.add(this.optionsButton);
            this.buttonList.add(this.quitButton);
        }

        this.buttonList.add(this.languageButton);
        this.field_92025_p = "";
        String var7 = System.getProperty("os_architecture");
        String var8 = System.getProperty("java_version");

        if ("ppc".equalsIgnoreCase(var7))
        {
            this.field_92025_p = "" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " PowerPC compatibility will be dropped in Minecraft 1.6";
        } else if (var8 != null && var8.startsWith("1.5"))
        {
            this.field_92025_p = "" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " Java 1.5 compatibility will be dropped in Minecraft 1.6";
        }

        this.field_92023_s = this.fontRenderer.getStringWidth(this.field_92025_p);
        this.field_92024_r = this.fontRenderer.getStringWidth(field_96138_a);
        int var6 = Math.max(this.field_92023_s, this.field_92024_r);
        this.field_92022_t = (this.width - var6) / 2;
        this.field_92021_u = ((GuiAetherIIButton) this.buttonList.get(0)).yPosition - 24;
        this.field_92020_v = this.field_92022_t + var6;
        this.field_92019_w = this.field_92021_u + 24;
        TUGbutton = new AetherTUGButton(14, this.width - 95, 40, 64, 47);
        this.buttonList.add(TUGbutton);
    }

    private void func_96137_a(StringTranslate var1, int var2, int var3)
    {
        if (this.field_96141_q)
        {
            if (!field_96140_r)
            {
                field_96140_r = true;
            } else if (field_96139_s)
            {
                this.func_98060_b(var1, var2, var3);
            }
        }
    }

    private void func_98060_b(StringTranslate var1, int var2, int var3)
    {
        this.fmlModButton.xPosition = this.width / 2 + 2;
        GuiAetherIIButton var4 = new GuiAetherIIButton(3, 30, var2 - 45 + var3 * 2, var1.translateKey("menu.online"));
        var4.xPosition = this.width / 2 - 100;
        this.buttonList.add(var4);
    }

    private void addSingleplayerMultiplayerButtons(int var1, int var2, StringTranslate var3)
    {
        this.singleplayerButton = new GuiAetherIIButton(1, 30, var1 - 45, var3.translateKey("menu.singleplayer"));
        this.multiplayerButton = new GuiAetherIIButton(2, 30, var1 - 45 + var2 * 1, var3.translateKey("menu.multiplayer"));
        this.officialServerButton = new GuiAetherIIButton(13, 30, var1 - 45 + var2 * 3, 7851212, 7851212);
        this.exploreTUG = new GuiButton(15, this.width / 2 - 40, this.height / 2, 80, 20, "浏览TUG!");
        this.buttonList.add(this.singleplayerButton);
        this.buttonList.add(this.multiplayerButton);
        this.buttonList.add(this.officialServerButton);
        this.buttonList.add(this.exploreTUG);
    }

    private void addDemoButtons(int var1, int var2, StringTranslate var3)
    {
        this.buttonList.add(new GuiAetherIIButton(11, 30, var1, var3.translateKey("menu.playdemo")));
        this.buttonList.add(this.buttonResetDemo = new GuiAetherIIButton(12, 30, var1 - 45 + var2 * 1, var3.translateKey("menu.resetdemo")));
        ISaveFormat var4 = this.mc.getSaveLoader();
        WorldInfo var5 = var4.getWorldInfo("Demo_World");

        if (var5 == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (var1.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings));
        }

        if (var1.id == 1)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (var1.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (var1.id == 3)
        {
            this.mc.displayGuiScreen(new GuiScreenOnlineServers(this));
        }

        if (var1.id == 4)
        {
            this.mc.shutdown();
        }

        if (var1.id == 6)
        {
            this.mc.displayGuiScreen(new GuiModList(this));
        }

        if (var1.id == 11)
        {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (var1.id == 12)
        {
            ISaveFormat var2 = this.mc.getSaveLoader();
            WorldInfo var3 = var2.getWorldInfo("Demo_World");

            if (var3 != null)
            {
                GuiYesNo var4 = GuiSelectWorld.getDeleteWorldScreen(this, var3.getWorldName(), 12);
                this.mc.displayGuiScreen(var4);
            }
        }

        if (var1.id == 13)
        {
            this.SERVERlist = !this.SERVERlist;
        }

        if (var1.id == 14)
        {
            try
            {
                System.out.println("WOW");
                this.TUGopen = !this.TUGopen;
                this.mc.displayGuiScreen(this);
            } catch (Exception var6)
            {
                var6.printStackTrace();
            }
        }

        if (var1.id == 15)
        {
            try
            {
                Desktop.getDesktop().browse(URI.create("http://nerdkingdom.com/p/pledgepage.aspx"));
                this.mc.displayGuiScreen(this);
            } catch (Exception var5)
            {
                var5.printStackTrace();
            }
        }

        if (var1.id > 15)
        {
            this.connectToServer((ServerData) this.serverdata.get(var1.id - 16));
        }
    }

    private void connectToServer(ServerData var1)
    {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, var1));
    }

    public void confirmClicked(boolean var1, int var2)
    {
        if (var1 && var2 == 12)
        {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (var2 == 13)
        {
            if (var1)
            {
                try
                {
                    Class var3 = Class.forName("java.awt.Desktop");
                    Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    var3.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{new URI("http://tinyurl.com/javappc")});
                } catch (Throwable var5)
                {
                    var5.printStackTrace();
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    private void drawPanorama(int var1, int var2, float var3)
    {
        Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        byte var5 = 8;

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
            float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
            float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9);
            GL11.glRotatef(MathHelper.sin(((float) this.panoramaTimer + var3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float) this.panoramaTimer + var3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int var10 = 0; var10 < 6; ++var10)
            {
                GL11.glPushMatrix();

                if (var10 == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var10 == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.renderEngine.bindTexture(titlePanoramaPaths[var10]);
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                float var11 = 0.0F;
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double) (0.0F + var11), (double) (0.0F + var11));
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double) (1.0F - var11), (double) (0.0F + var11));
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double) (1.0F - var11), (double) (1.0F - var11));
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double) (0.0F + var11), (double) (1.0F - var11));
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void rotateAndBlurSkybox(float var1)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.viewportTexture);
        this.mc.renderEngine.resetBoundTexture();
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();
        byte var3 = 3;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (var4 + 1));
            int var5 = this.width;
            int var6 = this.height;
            float var7 = (float) (var4 - var3 / 2) / 256.0F;
            var2.addVertexWithUV((double) var5, (double) var6, (double) this.zLevel, (double) (0.0F + var7), 0.0D);
            var2.addVertexWithUV((double) var5, 0.0D, (double) this.zLevel, (double) (1.0F + var7), 0.0D);
            var2.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (1.0F + var7), 1.0D);
            var2.addVertexWithUV(0.0D, (double) var6, (double) this.zLevel, (double) (0.0F + var7), 1.0D);
        }

        var2.draw();
        GL11.glColorMask(true, true, true, true);
        this.mc.renderEngine.resetBoundTexture();
    }

    private void renderSkybox(int var1, int var2, float var3)
    {
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(var1, var2, var3);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        this.rotateAndBlurSkybox(var3);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        float var5 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float var6 = (float) this.height * var5 / 256.0F;
        float var7 = (float) this.width * var5 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int var8 = this.width;
        int var9 = this.height;
        var4.addVertexWithUV(0.0D, (double) var9, (double) this.zLevel, (double) (0.5F - var6), (double) (0.5F + var7));
        var4.addVertexWithUV((double) var8, (double) var9, (double) this.zLevel, (double) (0.5F - var6), (double) (0.5F - var7));
        var4.addVertexWithUV((double) var8, 0.0D, (double) this.zLevel, (double) (0.5F + var6), (double) (0.5F - var7));
        var4.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel, (double) (0.5F + var6), (double) (0.5F + var7));
        var4.draw();
    }

    public void drawLogo(int var1, int var2)
    {
        GL11.glPushMatrix();
        this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/menu/aether2logo.png");
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.scalingLol += this.scalingLol * 0.001F;

        if (this.scalingLol > 1.0F)
        {
            this.scalingLol = 1.0F;
        }

        GL11.glTranslatef((float) (this.width / 2) - 195.0F * this.scalingLol / 2.0F, (float) (var2 - 20), 1.0F);
        GL11.glScalef(this.scalingLol, this.scalingLol, this.scalingLol);
        this.drawTexturedModalRect(0, 0, 0, 0, 195, 114);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        this.mc.renderEngine.resetBoundTexture();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.renderSkybox(var1, var2, var3);
        Tessellator var4 = Tessellator.instance;
        short var5 = 274;
        int var6 = this.width / 2 - var5 / 2;
        byte var7 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int var11;

        if (this.TUGopen)
        {
            GL11.glPushMatrix();
            Minecraft var8 = Minecraft.getMinecraft();
            ScaledResolution var9 = new ScaledResolution(var8.gameSettings, var8.displayWidth, var8.displayHeight);
            int var10 = var9.getScaledWidth();
            var11 = var9.getScaledHeight();
            int var12 = var8.renderEngine.getTextureForDownloadableImage("/net/aetherteam/aether/client/sprites/menu/tug/menu_background.png", "/net/aetherteam/aether/client/sprites/menu/tug/menu_background.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var12);
            boolean var13 = false;
            byte var14 = 0;
            float var15 = 0.0F;
            float var16 = 0.0F;
            float var17 = 1.0F;
            float var18 = 1.0F;
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(var15, var16);
            GL11.glVertex2f(0.0F, 0.0F);
            GL11.glTexCoord2f(var15, var18);
            GL11.glVertex2f(0.0F, (float) var11);
            GL11.glTexCoord2f(var17, var18);
            GL11.glVertex2f((float) var10, (float) (var14 + var11));
            GL11.glTexCoord2f(var17, var16);
            GL11.glVertex2f((float) var10, 0.0F);
            GL11.glEnd();
            GL11.glPopMatrix();
        }

        var4.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef(215.0F, 50.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float var19 = 1.4F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        var19 = var19 * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(var19, var19, var19);
        GL11.glPopMatrix();
        String var20 = "Minecraft 1.5.1";

        if (this.mc.isDemo())
        {
            var20 = var20 + " Demo";
        }

        List var21 = Lists.reverse(FMLCommonHandler.instance().getBrandings());
        String var23;

        for (var11 = 0; var11 < var21.size(); ++var11)
        {
            var23 = (String) var21.get(var11);

            if (!Strings.isNullOrEmpty(var23) && !this.TUGopen)
            {
                this.drawString(this.fontRenderer, var23, this.width - 2 - this.fontRenderer.getStringWidth(var23), this.height - (10 + var11 * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }

        if (!this.TUGopen)
        {
            String var22 = "Copyright Mojang AB. Do not distribute!";
            String info="白宝石小组荣誉出品！";
            this.drawString(this.fontRenderer, var22, 2, this.height - 10, 16777215);
            this.drawString(this.fontRenderer, info, 2, this.height - 20, 16777215);
        }

        if (this.field_92025_p != null && this.field_92025_p.length() > 0)
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRenderer, this.field_92025_p, this.field_92022_t, this.field_92021_u, 16777215);
            this.drawString(this.fontRenderer, field_96138_a, (this.width - this.field_92024_r) / 2, ((GuiAetherIIButton) this.buttonList.get(0)).yPosition - 12, 16777215);
        }

        super.drawScreen(var1, var2, var3);

        if ((double) this.updateCounter < 1.0E-4D)
        {
            this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 99, 44);
            this.drawTexturedModalRect(var6 + 99, var7 + 0, 129, 0, 27, 44);
            this.drawTexturedModalRect(var6 + 99 + 26, var7 + 0, 126, 0, 3, 44);
            this.drawTexturedModalRect(var6 + 99 + 26 + 3, var7 + 0, 99, 0, 26, 44);
            this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
        } else if (!this.isJukeboxOpen() && !this.TUGopen && !this.SERVERlist)
        {
            this.drawLogo(var6, var7);
            TUGbutton.drawButton = true;
            this.fmlModButton.drawButton = true;
            this.hideOptionsButton.drawButton = true;
            this.optionsButton.drawButton = true;
            this.quitButton.drawButton = true;
            this.languageButton.drawButton = true;
            this.singleplayerButton.drawButton = true;
            this.multiplayerButton.drawButton = true;
            this.officialServerButton.drawButton = true;
            this.exploreTUG.drawButton = false;
        } else
        {
            this.fmlModButton.drawButton = false;
            this.hideOptionsButton.drawButton = false;
            this.optionsButton.drawButton = false;
            this.quitButton.drawButton = false;
            this.singleplayerButton.drawButton = false;
            this.multiplayerButton.drawButton = false;
            this.officialServerButton.drawButton = false;

            if (this.TUGopen)
            {
                this.exploreTUG.drawButton = true;
            } else
            {
                this.exploreTUG.drawButton = false;
            }
        }

        GuiAetherIIButton var25;
        Iterator var24;

        if (!this.isJukeboxOpen() && this.SERVERlist)
        {
            for (var24 = this.serverButtons.iterator(); var24.hasNext(); var25.drawButton = true)
            {
                var25 = (GuiAetherIIButton) var24.next();
            }

            this.backButton.drawButton = true;
            TUGbutton.drawButton = false;
        } else
        {
            for (var24 = this.serverButtons.iterator(); var24.hasNext(); var25.drawButton = false)
            {
                var25 = (GuiAetherIIButton) var24.next();
            }

            this.backButton.drawButton = false;
        }

        if (this.TUGopen && !this.isJukeboxOpen())
        {
            GL11.glPushMatrix();
            this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/menu/tug/seed_and_moa.jpg");
            float var26 = 0.3F;
            GL11.glTranslatef(15.0F, 10.0F, 1.0F);
            GL11.glScalef(var26, var26, var26);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/menu/tug/screeny.png");
            GL11.glTranslatef(15.0F, 105.0F, 1.0F);
            GL11.glScalef(var26, var26, var26);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            var26 = 0.55F;
            this.mc.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/menu/tug/banner.png");
            GL11.glTranslatef(110.0F, 10.0F, 1.0F);
            GL11.glScalef(var26, var26 * 0.35F, var26);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            var23 = "以太团队参与了沙盒游戏TUG的开发!";
            String var27 = "请帮助我们支持这个酷毙的游戏 :)";
            this.drawString(this.fontRenderer, var23, this.width / 2 - this.fontRenderer.getStringWidth(var23) / 2, this.height - 40, 16777215);
            this.drawString(this.fontRenderer, var27, this.width / 2 - this.fontRenderer.getStringWidth(var27) / 2, this.height - 27, 16777215);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);

        if (this.field_92025_p.length() > 0 && var1 >= this.field_92022_t && var1 <= this.field_92020_v && var2 >= this.field_92021_u && var2 <= this.field_92019_w)
        {
            GuiConfirmOpenLink var4 = new GuiConfirmOpenLink(this, "http://tinyurl.com/javappc", 13);
            var4.func_92026_h();
            this.mc.displayGuiScreen(var4);
        }
    }

    static Minecraft func_98058_a(MenuBaseAetherII var0)
    {
        return var0.mc;
    }

    static void func_98061_a(MenuBaseAetherII var0, StringTranslate var1, int var2, int var3)
    {
        var0.func_98060_b(var1, var2, var3);
    }

    static boolean func_98059_a(boolean var0)
    {
        field_96139_s = var0;
        return var0;
    }

    public int getListButtonX()
    {
        return this.width - 110;
    }

    public int getListButtonY()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        ScaledResolution var2 = new ScaledResolution(var1.gameSettings, var1.displayWidth, var1.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
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
        return "Aether II";
    }

    public String getVersion()
    {
        return "虚空起源!";
    }

    public String getMusicFileName()
    {
        return "Aether Menu Two";
    }

    public String getIconPath()
    {
        return "/net/aetherteam/aether/client/sprites/menu/MenuIcon.png";
    }
}
