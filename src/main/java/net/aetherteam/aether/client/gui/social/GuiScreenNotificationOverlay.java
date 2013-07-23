package net.aetherteam.aether.client.gui.social;

import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
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

    public GuiScreenNotificationOverlay(Party party, byte guiType)
    {
        this.guiNotification = Minecraft.getMinecraft().renderEngine.f("/net/aetherteam/aether/client/sprites/gui/guiNotification.png");
        this.wMenu = 256;
        this.hMenu = 256;
        this.party = party;
        this.guiType = guiType;
    }

    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.centerX = (width / 2);
        this.centerY = (height / 2);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        if (this.guiType == this.REQUEST)
        {
            drawCenteredString(this.m, this.party.getLeader().username + " has invited you to join their party!", this.centerX, this.centerY, 56281239);
        }

        if (this.guiType == this.DENY)
        {
            drawCenteredString(this.m, this.party.getLeader().username + " has invited you to join their party!", this.centerX, this.centerY, 56281239);
        }

        if (this.guiType == this.ACCEPT)
        {
            drawCenteredString(this.m, this.party.getLeader().username + " has invited you to join their party!", this.centerX, this.centerY, 56281239);
        }

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.guiNotification);
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(this.centerX, this.centerY, 0, 0, 141, this.hMenu);
        this.g.renderEngine.a();
    }
}

