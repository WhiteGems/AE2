package net.aetherteam.aether;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(Side.CLIENT)
public class MenuBaseAetherII extends MenuBase
{
    private static final ResourceLocation TEXTURE_AETHER_2_LOGO = new ResourceLocation("aether", "textures/menu/aether2logo.png");
    private static final ResourceLocation TEXTURE_TUG_BG = new ResourceLocation("aether", "textures/menu/tug/menu_background.png");
    private static final ResourceLocation TEXTURE_TUG_BANNER = new ResourceLocation("aether", "textures/menu/tug/banner.png");
    private static final ResourceLocation TEXTURE_TUG_SCREENY = new ResourceLocation("aether", "textures/menu/tug/screeny.png");
    private static final ResourceLocation TEXTURE_SEED_AND_MOA = new ResourceLocation("aether", "textures/menu/tug/seed_and_moa.jpg");
    private static final Random rand = new Random();
    private static AetherTUGButton TUGbutton;
    private float updateCounter = 0.0F;
    private String splashText = "missingno";
    private GuiAetherIIButton buttonResetDemo;

    /** The button that was just pressed. */
    private GuiButton selectedButton = null;
    private int panoramaTimer = 0;
    private float scalingLol = 0.975F;
    private DynamicTexture viewportTexture;
    private boolean field_96141_q = true;
    private static boolean field_96140_r = false;
    private static boolean field_96139_s = false;
    private String field_92025_p;
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("aether", "textures/menu/bg/panorama0.png"), new ResourceLocation("aether", "textures/menu/bg/panorama1.png"), new ResourceLocation("aether", "textures/menu/bg/panorama2.png"), new ResourceLocation("aether", "textures/menu/bg/panorama3.png"), new ResourceLocation("aether", "textures/menu/bg/panorama4.png"), new ResourceLocation("aether", "textures/menu/bg/panorama5.png")};
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
    private boolean TUGopen = false;
    private ResourceLocation viewportTextureLocation;

    public MenuBaseAetherII()
    {
        BufferedReader bufferedreader = null;

        try
        {
            ArrayList ioexception1 = new ArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().func_110442_L().func_110536_a(this.splashesLocation).func_110527_b(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (s.length() > 0)
                {
                    ioexception1.add(s);
                }
            }

            do
            {
                this.splashText = (String)ioexception1.get(rand.nextInt(ioexception1.size()));
            }
            while (this.splashText.hashCode() == 125780783);
        }
        catch (IOException var12)
        {
            ;
        }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException var11)
                {
                    ;
                }
            }
        }

        this.updateCounter = rand.nextFloat();
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
    protected void keyTyped(char par1, int par2) {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.viewportTexture = new DynamicTexture(256, 256);
        this.viewportTextureLocation = this.mc.renderEngine.func_110578_a("aether2_background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9)
        {
            this.splashText = "Happy birthday, ez!";
        }
        else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
        {
            this.splashText = "Happy birthday, Notch!";
        }
        else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = this.height / 4 + 68;
        this.backButton = new GuiAetherIIButton(13, 3, this.height - 35, 180, 20, "Back To Menu");
        this.buttonList.add(this.backButton);

        if (this.mc.isDemo())
        {
            this.addDemoButtons(i, 24);
        }
        else
        {
            this.addSingleplayerMultiplayerButtons(i, 24);
        }

        this.fmlModButton = new GuiAetherIIButton(6, 30, i + 48 - 45, "Mods");
        this.buttonList.add(this.fmlModButton);
        this.func_96137_a(i, 24);
        this.hideOptionsButton = new GuiAetherIIButton(0, 30, i + 27 + 24, I18n.func_135053_a("menu.options"));
        this.optionsButton = new GuiAetherIIButton(0, 30, i + 27 + 24, 200, 20, I18n.func_135053_a("menu.options"));
        this.quitButton = new GuiAetherIIButton(4, 30, i + 27 + 48, 200, 20, I18n.func_135053_a("menu.quit"));
        this.languageButton = new GuiButtonLanguage(5, this.width - 48, 4);
        this.buttonList.add(this.optionsButton);
        this.buttonList.add(this.quitButton);
        this.buttonList.add(this.languageButton);
        this.field_92025_p = "";
        String s = System.getProperty("os_architecture");
        String s1 = System.getProperty("java_version");

        if ("ppc".equalsIgnoreCase(s))
        {
            this.field_92025_p = "" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " PowerPC compatibility will be dropped in Minecraft 1.6";
        }
        else if (s1 != null && s1.startsWith("1.5"))
        {
            this.field_92025_p = "" + EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET + " Java 1.5 compatibility will be dropped in Minecraft 1.6";
        }

        this.field_92023_s = this.fontRenderer.getStringWidth(this.field_92025_p);
        this.field_92024_r = this.fontRenderer.getStringWidth(field_96138_a);
        int j = Math.max(this.field_92023_s, this.field_92024_r);
        this.field_92022_t = (this.width - j) / 2;
        this.field_92021_u = ((GuiAetherIIButton)this.buttonList.get(0)).yPosition - 24;
        this.field_92020_v = this.field_92022_t + j;
        this.field_92019_w = this.field_92021_u + 24;
        TUGbutton = new AetherTUGButton(14, this.width - 95, 40, 64, 47);
        this.buttonList.add(TUGbutton);
    }

    private void func_96137_a(int par2, int par3)
    {
        if (this.field_96141_q)
        {
            if (!field_96140_r)
            {
                field_96140_r = true;
            }
            else if (field_96139_s)
            {
                this.func_98060_b(par2, par3);
            }
        }
    }

    private void func_98060_b(int par2, int par3)
    {
        this.fmlModButton.xPosition = this.width / 2 + 2;
        GuiAetherIIButton realmButton = new GuiAetherIIButton(3, 30, par2 - 45 + par3 * 2, I18n.func_135053_a("menu.online"));
        realmButton.xPosition = this.width / 2 - 100;
        this.buttonList.add(realmButton);
    }

    private void addSingleplayerMultiplayerButtons(int par1, int par2)
    {
        this.singleplayerButton = new GuiAetherIIButton(1, 30, par1 - 45, I18n.func_135053_a("menu.singleplayer"));
        this.multiplayerButton = new GuiAetherIIButton(2, 30, par1 - 45 + par2 * 1, I18n.func_135053_a("menu.multiplayer"));
        this.officialServerButton = new GuiAetherIIButton(13, 30, par1 - 45 + par2 * 3, "Official Aether Server");
        this.exploreTUG = new GuiButton(15, this.width / 2 - 40, this.height / 2, 80, 20, "Explore TUG!");
        this.buttonList.add(this.singleplayerButton);
        this.buttonList.add(this.multiplayerButton);
        this.buttonList.add(this.officialServerButton);
        this.buttonList.add(this.exploreTUG);
    }

    private void addDemoButtons(int par1, int par2)
    {
        this.buttonList.add(new GuiAetherIIButton(11, 30, par1, I18n.func_135053_a("menu.playdemo")));
        this.buttonList.add(this.buttonResetDemo = new GuiAetherIIButton(12, 30, par1 - 45 + par2 * 1, I18n.func_135053_a("menu.resetdemo")));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (par1GuiButton.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.func_135016_M()));
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
            ISaveFormat e = this.mc.getSaveLoader();
            WorldInfo worldinfo = e.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                GuiYesNo guiyesno = GuiSelectWorld.getDeleteWorldScreen(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }

        if (par1GuiButton.id == 13)
        {
            ServerData e1 = new ServerData(I18n.func_135053_a("selectServer.defaultName"), "198.27.75.96");
            this.connectToServer(e1);
        }

        if (par1GuiButton.id == 14)
        {
            try
            {
                System.out.println("WOW");
                this.TUGopen = !this.TUGopen;
                this.mc.displayGuiScreen(this);
            }
            catch (Exception var6)
            {
                var6.printStackTrace();
            }
        }

        if (par1GuiButton.id == 15)
        {
            try
            {
                Desktop.getDesktop().browse(URI.create("http://nerdkingdom.com/p/pledgepage.aspx"));
                this.mc.displayGuiScreen(this);
            }
            catch (Exception var5)
            {
                var5.printStackTrace();
            }
        }
    }

    private void connectToServer(ServerData par1ServerData)
    {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, par1ServerData));
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (par1 && par2 == 12)
        {
            ISaveFormat throwable1 = this.mc.getSaveLoader();
            throwable1.flushCache();
            throwable1.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (par2 == 13)
        {
            if (par1)
            {
                try
                {
                    Class throwable = Class.forName("java.awt.Desktop");
                    Object object = throwable.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    throwable.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI("http://tinyurl.com/javappc")});
                }
                catch (Throwable var5)
                {
                    var5.printStackTrace();
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    private void drawPanorama(int par1, int par2, float par3)
    {
        Tessellator tessellator = Tessellator.instance;
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
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; ++k)
        {
            GL11.glPushMatrix();
            float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64.0F;
            float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + par3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)this.panoramaTimer + par3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l)
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

                this.mc.renderEngine.func_110577_a(titlePanoramaPaths[l]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f4), (double)(1.0F - f4));
                tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f4), (double)(1.0F - f4));
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
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

    private void rotateAndBlurSkybox(float par1)
    {
        this.mc.renderEngine.func_110577_a(this.viewportTextureLocation);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        byte b0 = 3;

        for (int i = 0; i < b0; ++i)
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float)(i - b0 / 2) / 256.0F;
            tessellator.addVertexWithUV((double)j, (double)k, (double)this.zLevel, (double)(0.0F + f1), 0.0D);
            tessellator.addVertexWithUV((double)j, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 1.0D);
            tessellator.addVertexWithUV(0.0D, (double)k, (double)this.zLevel, (double)(0.0F + f1), 1.0D);
        }

        tessellator.draw();
        GL11.glColorMask(true, true, true, true);
    }

    private void renderSkybox(int par1, int par2, float par3)
    {
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(par1, par2, par3);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        this.rotateAndBlurSkybox(par3);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f2 = (float)this.height * f1 / 256.0F;
        float f3 = (float)this.width * f1 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        tessellator.addVertexWithUV(0.0D, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F + f3));
        tessellator.addVertexWithUV((double)k, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F + f3));
        tessellator.draw();
    }

    public void drawLogo(int k, int b0)
    {
        GL11.glPushMatrix();
        this.mc.renderEngine.func_110577_a(TEXTURE_AETHER_2_LOGO);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.scalingLol += this.scalingLol * 0.001F;

        if (this.scalingLol > 1.0F)
        {
            this.scalingLol = 1.0F;
        }

        GL11.glTranslatef((float)(this.width / 2) - 195.0F * this.scalingLol / 2.0F, (float)(b0 - 20), 1.0F);
        GL11.glScalef(this.scalingLol, this.scalingLol, this.scalingLol);
        this.drawTexturedModalRect(0, 0, 0, 0, 195, 114);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.renderSkybox(par1, par2, par3);
        Tessellator tessellator = Tessellator.instance;
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        byte b0 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int scaling;

        if (this.TUGopen)
        {
            GL11.glPushMatrix();
            Minecraft f1 = Minecraft.getMinecraft();
            ScaledResolution s = new ScaledResolution(f1.gameSettings, f1.displayWidth, f1.displayHeight);
            int brandings = s.getScaledWidth();
            scaling = s.getScaledHeight();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.mc.renderEngine.func_110577_a(TEXTURE_TUG_BG);
            boolean s1 = false;
            byte s2 = 0;
            float u = 0.0F;
            float v = 0.0F;
            float u1 = 1.0F;
            float v1 = 1.0F;
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(u, v);
            GL11.glVertex2f(0.0F, 0.0F);
            GL11.glTexCoord2f(u, v1);
            GL11.glVertex2f(0.0F, (float)scaling);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f((float)brandings, (float)(s2 + scaling));
            GL11.glTexCoord2f(u1, v);
            GL11.glVertex2f((float)brandings, 0.0F);
            GL11.glEnd();
            GL11.glPopMatrix();
        }

        tessellator.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef(215.0F, 50.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float var18 = 1.4F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        var18 = var18 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(var18, var18, var18);
        GL11.glPopMatrix();
        String var19 = "Minecraft 1.5.1";

        if (this.mc.isDemo())
        {
            var19 = var19 + " Demo";
        }

        List var20 = Lists.reverse(FMLCommonHandler.instance().getBrandings());
        String var21;

        for (scaling = 0; scaling < var20.size(); ++scaling)
        {
            var21 = (String)var20.get(scaling);

            if (!Strings.isNullOrEmpty(var21) && !this.TUGopen)
            {
                this.drawString(this.fontRenderer, var21, this.width - 2 - this.fontRenderer.getStringWidth(var21), this.height - (10 + scaling * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }

        if (!this.TUGopen)
        {
            String var23 = "Copyright Mojang AB. Do not distribute!";
            this.drawString(this.fontRenderer, var23, 2, this.height - 10, 16777215);
        }

        if (this.field_92025_p != null && this.field_92025_p.length() > 0)
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRenderer, this.field_92025_p, this.field_92022_t, this.field_92021_u, 16777215);
            this.drawString(this.fontRenderer, field_96138_a, (this.width - this.field_92024_r) / 2, ((GuiAetherIIButton)this.buttonList.get(0)).yPosition - 12, 16777215);
        }

        super.drawScreen(par1, par2, par3);

        if ((double)this.updateCounter < 1.0E-4D)
        {
            this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
            this.drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
            this.drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
            this.drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
            this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
        }
        else if (!this.isJukeboxOpen() && !this.TUGopen)
        {
            this.drawLogo(k, b0);
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
        }
        else
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
            }
            else
            {
                this.exploreTUG.drawButton = false;
            }
        }

        this.backButton.drawButton = false;

        if (this.TUGopen && !this.isJukeboxOpen())
        {
            GL11.glPushMatrix();
            this.mc.renderEngine.func_110577_a(TEXTURE_SEED_AND_MOA);
            float var22 = 0.3F;
            GL11.glTranslatef(15.0F, 10.0F, 1.0F);
            GL11.glScalef(var22, var22, var22);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            this.mc.renderEngine.func_110577_a(TEXTURE_TUG_SCREENY);
            GL11.glTranslatef(15.0F, 105.0F, 1.0F);
            GL11.glScalef(var22, var22, var22);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            var22 = 0.55F;
            this.mc.renderEngine.func_110577_a(TEXTURE_TUG_BANNER);
            GL11.glTranslatef(110.0F, 10.0F, 1.0F);
            GL11.glScalef(var22, var22 * 0.35F, var22);
            this.drawTexturedModalRect(0, 0, 0, 0, 255, 255);
            GL11.glPopMatrix();
            var21 = "The Aether realm will be coming to the world of TUG!";
            String var24 = "Please help us support this amazing game :)";
            this.drawString(this.fontRenderer, var21, this.width / 2 - this.fontRenderer.getStringWidth(var21) / 2, this.height - 40, 16777215);
            this.drawString(this.fontRenderer, var24, this.width / 2 - this.fontRenderer.getStringWidth(var24) / 2, this.height - 27, 16777215);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if (this.field_92025_p.length() > 0 && par1 >= this.field_92022_t && par1 <= this.field_92020_v && par2 >= this.field_92021_u && par2 <= this.field_92019_w)
        {
            GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, "http://tinyurl.com/javappc", 13, true);
            guiconfirmopenlink.func_92026_h();
            this.mc.displayGuiScreen(guiconfirmopenlink);
        }
    }

    static Minecraft func_98058_a(MenuBaseAetherII par0GuiMainMenu)
    {
        return par0GuiMainMenu.mc;
    }

    static void func_98061_a(MenuBaseAetherII par0GuiMainMenu, int par2, int par3)
    {
        par0GuiMainMenu.func_98060_b(par2, par3);
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
        return "Aether II";
    }

    public String getVersion()
    {
        return "Genesis of the Void";
    }

    public ResourceLocation getIconPath()
    {
        return new ResourceLocation("aether", "textures/menu/MenuIcon.png");
    }

    public String[] getPlaylist()
    {
        return new String[] {"aether:Aether Menu Two"};
    }
}
