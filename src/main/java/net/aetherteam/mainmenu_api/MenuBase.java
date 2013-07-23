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
    public JukeboxPlayer jukebox = (new JukeboxPlayer()).setMusicFileName(this.getMusicFileName());

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.jukeboxTexture = this.mc.renderEngine.getTexture("/net/aetherteam/mainmenu_api/gui/jukebox.png");
        this.jukeHeight = 256;
        this.jukeWidth = 256;
        this.updateScreen();

        if (this.useJukebox())
        {
            this.jukebox.start();
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

    public void drawJukeboxBackground(int var1)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator var2 = Tessellator.instance;
        this.mc.renderEngine.bindTexture(this.getJukeboxBackgroundPath());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var3 = 32.0F;
        var2.startDrawingQuads();
        var2.setColorOpaque_I(10066329);
        var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / var3), (double)((float)this.height / var3 + (float)var1));
        var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / var3), (double)(0 + var1));
        var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + var1));
        var2.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        super.drawScreen(var1, var2, var3);
        Minecraft var4 = Minecraft.getMinecraft();
        ScaledResolution var5 = new ScaledResolution(var4.gameSettings, var4.displayWidth, var4.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        this.menuX = var6 + var4.displayWidth / 2;
        this.menuY = var7 + var4.displayHeight / 2;

        if (this.useJukebox())
        {
            this.jukebox.run();
        }

        this.jukeButtonList.clear();
        this.jukeButton = new GuiButtonItemStack(this.fontRenderer, var4, 0, this.getJukeboxButtonX(), this.getJukeboxButtonY(), new ItemStack(Block.jukebox));
        this.menuButton = new GuiMenuButton(0, this.getListButtonX(), this.getListButtonY(), 58, 20, "菜单列表");
        this.menuButton.drawButton(this.mc, var1, var2);

        if (this.useJukebox())
        {
            this.jukeButton.drawButton(this.mc, var1, var2);
        }

        if (this.jukeboxOpen)
        {
            this.drawJukeboxBackground(0);
            this.jukeButton.drawButton(this.mc, var1, var2);
            this.menuButton.drawButton(this.mc, var1, var2);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.jukeboxTexture);
            int centerX = var6 - var6 / 2 - 75;
            int centerY = var7 - var7 / 2 - 37;
            new ScaledResolution(var4.gameSettings, var4.displayWidth, var4.displayHeight);
            this.drawTexturedModalRect(centerX, centerY, 0, 0, 151, this.jukeHeight);
            var4.renderEngine.resetBoundTexture();
            this.fontRenderer.drawStringWithShadow(this.jukebox.getCurrentSongName(), centerX + 76 - this.fontRenderer.getStringWidth(this.jukebox.getCurrentSongName()) / 2, centerY + 14, 16777215);

            if (!this.jukebox.isMusicPlaying() && !MenuBaseConfig.muteMusic)
            {
                this.fontRenderer.drawStringWithShadow("载入音乐中...", centerX + 76 - this.fontRenderer.getStringWidth("载入音乐中...") / 2, centerY - 11, 16777215);
            }
            this.jukeButtonList.add(new GuiButton(0, centerX + 12, centerY + 42, 58, 20, "音乐: " + (MenuBaseConfig.muteMusic ? "关" : "开")));
            this.jukeButtonList.add(new GuiButton(1, centerX + 83, centerY + 42, 58, 20, "循环: " + (MenuBaseConfig.loopMusic ? "开" : "关")));
            this.jukeButtonList.add(new GuiButton(2, centerX + 125, centerY + 8, 20, 20, ">"));
            this.jukeButtonList.add(new GuiButton(3, centerX + 7, centerY + 8, 20, 20, "<"));

            for (int var11 = 0; var11 < this.jukeButtonList.size(); ++var11)
            {
                GuiButton var12 = (GuiButton)this.jukeButtonList.get(var11);
                var12.drawButton(this.mc, var1, var2);
            }
        }
    }

    public int getJukeboxButtonX()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        ScaledResolution var2 = new ScaledResolution(var1.gameSettings, var1.displayWidth, var1.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        return var3 / 2 + 192;
    }

    public int getJukeboxButtonY()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        ScaledResolution var2 = new ScaledResolution(var1.gameSettings, var1.displayWidth, var1.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        return 4;
    }

    public int getListButtonX()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        ScaledResolution var2 = new ScaledResolution(var1.gameSettings, var1.displayWidth, var1.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        return 5;
    }

    public int getListButtonY()
    {
        Minecraft var1 = Minecraft.getMinecraft();
        ScaledResolution var2 = new ScaledResolution(var1.gameSettings, var1.displayWidth, var1.displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        return var4 - 25;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            if (this.jukeButton != null && this.jukeButton.mousePressed(this.mc, var1, var2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.jukeboxOpen = !this.jukeboxOpen;
            }

            if (this.menuButton != null && this.menuButton.mousePressed(this.mc, var1, var2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.menuButton.clickButton();
            }

            int var4;
            GuiButton var5;

            for (var4 = 0; var4 < this.jukeButtonList.size(); ++var4)
            {
                var5 = (GuiButton)this.jukeButtonList.get(var4);

                if (var5.mousePressed(this.mc, var1, var2))
                {
                    this.selectedButton = var5;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    if (var5.id == 0)
                    {
                        this.jukebox.toggleMute();
                    }

                    if (var5.id == 1)
                    {
                        this.jukebox.toggleLoop();
                    }

                    if (var5.id == 2)
                    {
                        ++MenuBaseConfig.musicIndex;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.defaultMusic = false;
                        this.jukebox.muteMusic();
                    }

                    if (var5.id == 3)
                    {
                        --MenuBaseConfig.musicIndex;
                        MenuBaseConfig.setProperty("musicIndex", String.valueOf(MenuBaseConfig.musicIndex));
                        this.jukebox.defaultMusic = false;
                        this.jukebox.muteMusic();
                    }
                }
            }

            if (!this.jukeboxOpen)
            {
                for (var4 = 0; var4 < this.buttonList.size(); ++var4)
                {
                    var5 = (GuiButton)this.buttonList.get(var4);

                    if (var5.mousePressed(this.mc, var1, var2))
                    {
                        this.selectedButton = var5;
                        this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        this.actionPerformed(var5);
                    }
                }
            }
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int var1, int var2, int var3)
    {
        if (this.selectedButton != null && var3 == 0 && !this.jukeboxOpen)
        {
            this.selectedButton.mouseReleased(var1, var2);
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