package net.aetherteam.aether.client.gui.social;

import net.aetherteam.aether.party.Party;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiScreenNotificationOverlay extends GuiScreen
{
    private static final ResourceLocation TEXTURE_NOTIFICATION = new ResourceLocation("aether", "textures/gui/guiNotification.png");
    private int hMenu = 256;
    private int wMenu = 256;
    private int centerX;
    private int centerY;
    private Party party;
    private byte guiType = 0;
    byte REQUEST = 0;
    byte DENY = 1;
    byte ACCEPT = 2;

    public GuiScreenNotificationOverlay(Party party, byte guiType)
    {
        this.party = party;
        this.guiType = guiType;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.centerX = width / 2;
        this.centerY = height / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
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
        this.mc.renderEngine.func_110577_a(TEXTURE_NOTIFICATION);
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(this.centerX, this.centerY, 0, 0, 141, this.hMenu);
    }
}
