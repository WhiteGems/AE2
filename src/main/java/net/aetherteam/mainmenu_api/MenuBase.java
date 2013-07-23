package net.aetherteam.mainmenu_api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MenuBase extends GuiScreen
{
    private GuiMenuButton menuButton;
    private GuiButtonItemStack jukeButton;
    private GuiButton selectedButton = null;
    private int jukeboxTexture;
    private int menuX;
    private int menuY;
    private int jukeWidth;
    private int jukeHeight;
    private boolean jukeboxOpen = false;
    private List jukeButtonList = new ArrayList();
    public JukeboxPlayer jukebox = new JukeboxPlayer().setMusicFileName(getMusicFileName());

    public void initGui()
    {
        this.jukeboxTexture = this.g.renderEngine.f("/net/aetherteam/mainmenu_api/gui/jukebox.png");
        this.jukeHeight = 256;
        this.jukeWidth = 256;
        updateScreen();

        if (useJukebox())
        {
            this.jukebox.start();
        }
    }

    public void onGuiClosed()
    {
        MenuBaseConfig.hasStartedMusic = false;
        MenuBaseConfig.setProperty("hasStartedMusic", "false");

        if (MenuBaseConfig.hasPlayedMusic);
    }

    public void drawJukeboxBackground(int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Rect2i tessellator = Rect2i.rectX;
        this.g.renderEngine.b(getJukeboxBackgroundPath());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.b();
        tessellator.d(10066329);
        tessellator.a(0.0D, this.i, 0.0D, 0.0D, this.i / f + i);
        tessellator.a(this.height, this.i, 0.0D, this.height / f, this.i / f + i);
        tessellator.a(this.height, 0.0D, 0.0D, this.height / f, 0 + i);
        tessellator.a(0.0D, 0.0D, 0.0D, 0.0D, 0 + i);
        tessellator.getRectX();
    }

    public void drawScreen(int x, int y, float something)
    {
        super.drawScreen(x, y, something);
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.menuX = (width + mc.displayWidth / 2);
        this.menuY = (height + mc.displayHeight / 2);

        if (useJukebox())
        {
            this.jukebox.run();
        }

        this.jukeButtonList.clear();
        this.jukeButton = new GuiButtonItemStack(this.m, mc, 0, getJukeboxButtonX(), getJukeboxButtonY(), new ItemStack(Block.jukebox));
        this.menuButton = new GuiMenuButton(0, getListButtonX(), getListButtonY(), 58, 20, "Menu List");
        this.menuButton.drawButton(this.g, x, y);

        if (useJukebox())
        {
            this.jukeButton.drawButton(this.g, x, y);
        }

        if (this.jukeboxOpen)
        {
            drawJukeboxBackground(0);
            this.jukeButton.drawButton(this.g, x, y);
            this.menuButton.drawButton(this.g, x, y);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.jukeboxTexture);
            int centerX = width - width / 2 - 75;
            int centerY = height - height / 2 - 37;
            ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            drawTexturedModalRect(centerX, centerY, 0, 0, 151, this.jukeHeight);
            mc.renderEngine.a();
            this.m.drawStringWithShadow(this.jukebox.getCurrentSongName(), centerX + 76 - this.m.getStringWidth(this.jukebox.getCurrentSongName()) / 2, centerY + 14, 16777215);

            if ((!this.jukebox.isMusicPlaying()) && (!MenuBaseConfig.muteMusic))
            {
                this.m.drawStringWithShadow("Loading Song...", centerX + 76 - this.m.getStringWidth("Loading Song...") / 2, centerY - 11, 16777215);
            }

            this.jukeButtonList.add(new GuiButton(0, centerX + 12, centerY + 42, 58, 20, "Music: " + (MenuBaseConfig.muteMusic ? "Off" : "On")));
            this.jukeButtonList.add(new GuiButton(1, centerX + 83, centerY + 42, 58, 20, "Loop: " + (MenuBaseConfig.loopMusic ? "On" : "Off")));
            this.jukeButtonList.add(new GuiButton(2, centerX + 125, centerY + 8, 20, 20, ">"));
            this.jukeButtonList.add(new GuiButton(3, centerX + 7, centerY + 8, 20, 20, "<"));

            for (int k = 0; k < this.jukeButtonList.size(); k++)
            {
                GuiButton jukebutton = (GuiButton)this.jukeButtonList.get(k);
                jukebutton.drawButton(this.g, x, y);
            }
        }
    }

    public int getJukeboxButtonX()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        return width / 2 + 192;
    }

    public int getJukeboxButtonY()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        return 4;
    }

    public int getListButtonX()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        return 5;
    }

    public int getListButtonY()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        return height - 25;
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if ((this.jukeButton != null) && (this.jukeButton.mousePressed(this.g, par1, par2)))
            {
                this.g.sndManager.a("random.click", 1.0F, 1.0F);
                this.jukeboxOpen = (!this.jukeboxOpen);
            }

            if ((this.menuButton != null) && (this.menuButton.mousePressed(this.g, par1, par2)))
            {
                this.g.sndManager.a("random.click", 1.0F, 1.0F);
                this.menuButton.clickButton();
            }

            for (int l = 0; l < this.jukeButtonList.size(); l++)
            {
                GuiButton guibutton = (GuiButton)this.jukeButtonList.get(l);

                if (guibutton.mousePressed(this.g, par1, par2))
                {
                    this.selectedButton = guibutton;
                    this.g.sndManager.a("random.click", 1.0F, 1.0F);

                    if (guibutton.id == 0)
                    {
                        this.jukebox.toggleMute();
                    }

                    if (guibutton.id == 1)
                    {
                        this.jukebox.toggleLoop();
                    }

                    if (guibutton.id == 2)
                    {
                        MenuBaseConfig.musicIndex += 1;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.defaultMusic = false;
                        this.jukebox.muteMusic();
                    }

                    if (guibutton.id == 3)
                    {
                        MenuBaseConfig.musicIndex -= 1;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.defaultMusic = false;
                        this.jukebox.muteMusic();
                    }
                }
            }

            if (!this.jukeboxOpen)
            {
                for (int l = 0; l < this.k.size(); l++)
                {
                    GuiButton guibutton = (GuiButton)this.k.get(l);

                    if (guibutton.mousePressed(this.g, par1, par2))
                    {
                        this.selectedButton = guibutton;
                        this.g.sndManager.a("random.click", 1.0F, 1.0F);
                        actionPerformed(guibutton);
                    }
                }
            }
        }
    }

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if ((this.selectedButton != null) && (par3 == 0) && (!this.jukeboxOpen))
        {
            this.selectedButton.mouseReleased(par1, par2);
            this.selectedButton = null;
        }
    }

    public String getName()
    {
        return "Default Name";
    }

    public String getVersion()
    {
        return "1.0.0";
    }

    public String getMusicFileName()
    {
        return null;
    }

    public String getIconPath()
    {
        return "/net/aetherteam/mainmenu_api/icons/minecraft.png";
    }

    public String getJukeboxBackgroundPath()
    {
        return "/net/aetherteam/mainmenu_api/icons/dirt.png";
    }

    public boolean useJukebox()
    {
        return true;
    }

    public boolean isJukeboxOpen()
    {
        return this.jukeboxOpen;
    }
}

