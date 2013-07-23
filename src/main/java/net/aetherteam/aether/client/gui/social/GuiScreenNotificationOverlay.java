package net.aetherteam.aether.client.gui.social;

import net.aetherteam.aether.party.Party;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class GuiScreenNotificationOverlay extends GuiScreen
{
    private int guiNotification;
    private int hMenu;
    private int wMenu;
    private int centerX;
    private int centerY;
    private Party party;
    private byte guiType = 0;
    byte REQUEST = 0;
    byte DENY = 1;
    byte ACCEPT = 2;

    public GuiScreenNotificationOverlay(Party var1, byte var2)
    {
        this.guiNotification = Minecraft.getMinecraft().renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/guiNotification.png");
        this.wMenu = 256;
        this.hMenu = 256;
        this.party = var1;
        this.guiType = var2;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        super.keyTyped(var1, var2);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var2 = var1.getScaledWidth();
        int var3 = var1.getScaledHeight();
        this.centerX = var2 / 2;
        this.centerY = var3 / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        if (this.guiType == this.REQUEST)
        {
            drawCenteredString(this.fontRenderer, this.party.getLeader().username + "邀请你加入他们的团队!", this.centerX, this.centerY, 56281239);
        }

        if (this.guiType == this.DENY)
        {
            drawCenteredString(this.fontRenderer, this.party.getLeader().username + "邀请你加入他们的团队!", this.centerX, this.centerY, 56281239);
        }

        if (this.guiType == this.ACCEPT)
        {
            drawCenteredString(this.fontRenderer, this.party.getLeader().username + "邀请你加入他们的团队!", this.centerX, this.centerY, 56281239);
        }

        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.guiNotification);
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(this.centerX, this.centerY, 0, 0, 141, this.hMenu);
        this.mc.renderEngine.resetBoundTexture();
    }
}