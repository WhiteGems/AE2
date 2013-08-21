package net.aetherteam.mainmenu_api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class MenuBase extends GuiScreen
{
    private static final ResourceLocation TEXTURE_JUKEBOX = new ResourceLocation("mainmenu_api", "textures/gui/jukebox.png");
    private GuiMenuButton menuButton;
    private GuiButtonItemStack jukeButton;

    /** The button that was just pressed. */
    private GuiButton selectedButton = null;
    private int menuX;
    private int menuY;
    private int jukeWidth;
    private int jukeHeight;
    private boolean jukeboxOpen = false;
    private List jukeButtonList = new ArrayList();
    public JukeboxPlayer jukebox = new JukeboxPlayer();
    protected ResourceLocation splashesLocation = new ResourceLocation("texts/splashes.txt");

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.jukeHeight = 256;
        this.jukeWidth = 256;
        this.updateScreen();

        if (this.useJukebox())
        {
            this.jukebox.start(this.getPlaylist());
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        MenuBaseConfig.hasStartedMusic = false;
        MenuBaseConfig.setProperty("hasStartedMusic", "false");

        if (MenuBaseConfig.hasPlayedMusic)
        {
            ;
        }
    }

    public void drawJukeboxBackground(int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.renderEngine.func_110577_a(this.getJukeboxBackgroundPath());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(10066329);
        tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)i));
        tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)i));
        tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)(0 + i));
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + i));
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float something)
    {
        super.drawScreen(x, y, something);
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.menuX = width + mc.displayWidth / 2;
        this.menuY = height + mc.displayHeight / 2;

        if (this.useJukebox())
        {
            this.jukebox.run();
        }

        this.jukeButtonList.clear();
        this.jukeButton = new GuiButtonItemStack(this.fontRenderer, mc, 0, this.getJukeboxButtonX(), this.getJukeboxButtonY(), new ItemStack(Block.jukebox));
        this.menuButton = new GuiMenuButton(0, this.getListButtonX(), this.getListButtonY(), 58, 20, "Menu List");
        this.menuButton.drawButton(this.mc, x, y);

        if (this.useJukebox())
        {
            this.jukeButton.drawButton(this.mc, x, y);
        }

        if (this.jukeboxOpen)
        {
            this.drawJukeboxBackground(0);
            this.jukeButton.drawButton(this.mc, x, y);
            this.menuButton.drawButton(this.mc, x, y);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            mc.renderEngine.func_110577_a(TEXTURE_JUKEBOX);
            int centerX = width - width / 2 - 75;
            int centerY = height - height / 2 - 37;
            new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            this.drawTexturedModalRect(centerX, centerY, 0, 0, 151, this.jukeHeight);
            this.fontRenderer.drawStringWithShadow(this.jukebox.getCurrentSongName(), centerX + 76 - this.fontRenderer.getStringWidth(this.jukebox.getCurrentSongName()) / 2, centerY + 14, 16777215);

            if (!this.jukebox.isMusicPlaying() && !MenuBaseConfig.muteMusic)
            {
                this.fontRenderer.drawStringWithShadow("Loading Song...", centerX + 76 - this.fontRenderer.getStringWidth("Loading Song...") / 2, centerY - 11, 16777215);
            }

            this.jukeButtonList.add(new GuiButton(0, centerX + 12, centerY + 42, 58, 20, "Music: " + (MenuBaseConfig.muteMusic ? "Off" : "On")));
            this.jukeButtonList.add(new GuiButton(1, centerX + 83, centerY + 42, 58, 20, "Loop: " + (MenuBaseConfig.loopMusic ? "On" : "Off")));
            this.jukeButtonList.add(new GuiButton(2, centerX + 125, centerY + 8, 20, 20, ">"));
            this.jukeButtonList.add(new GuiButton(3, centerX + 7, centerY + 8, 20, 20, "<"));

            for (int k = 0; k < this.jukeButtonList.size(); ++k)
            {
                GuiButton jukebutton = (GuiButton)this.jukeButtonList.get(k);
                jukebutton.drawButton(this.mc, x, y);
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

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.jukeButton != null && this.jukeButton.mousePressed(this.mc, par1, par2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.jukeboxOpen = !this.jukeboxOpen;
            }

            if (this.menuButton != null && this.menuButton.mousePressed(this.mc, par1, par2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.menuButton.clickButton();
            }

            int l;
            GuiButton guibutton;

            for (l = 0; l < this.jukeButtonList.size(); ++l)
            {
                guibutton = (GuiButton)this.jukeButtonList.get(l);

                if (guibutton.mousePressed(this.mc, par1, par2))
                {
                    this.selectedButton = guibutton;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

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
                        ++MenuBaseConfig.musicIndex;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.muteMusic();
                    }

                    if (guibutton.id == 3)
                    {
                        --MenuBaseConfig.musicIndex;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.muteMusic();
                    }
                }
            }

            if (!this.jukeboxOpen)
            {
                for (l = 0; l < this.buttonList.size(); ++l)
                {
                    guibutton = (GuiButton)this.buttonList.get(l);

                    if (guibutton.mousePressed(this.mc, par1, par2))
                    {
                        this.selectedButton = guibutton;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        this.actionPerformed(guibutton);
                    }
                }
            }
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (this.selectedButton != null && par3 == 0 && !this.jukeboxOpen)
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

    public ResourceLocation getIconPath()
    {
        return new ResourceLocation("mainmenu_api", "textures/icons/minecraft.png");
    }

    public ResourceLocation getJukeboxBackgroundPath()
    {
        return new ResourceLocation("mainmenu_api", "textures/icons/dirt.png");
    }

    public String[] getPlaylist()
    {
        return new String[0];
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
