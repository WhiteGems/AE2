package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import net.aetherteam.aether.client.gui.social.PartyData;
import net.aetherteam.aether.client.gui.social.dialogue.GuiDialogueBox;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationType;
import net.aetherteam.aether.party.Party;
import net.aetherteam.aether.party.PartyController;
import net.aetherteam.aether.party.members.PartyMember;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiViewNotification extends GuiScreen
{
    private final PartyData pm;
    private int backgroundTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private Notification notification;
    private GuiScreen parent;
    private EntityPlayer player;
    private GuiButton acceptButton;
    private GuiButton denyButton;
    private GuiButton backButton;

    public GuiViewNotification(EntityPlayer var1, Notification var2, GuiScreen var3)
    {
        this(new PartyData(), var1, var2, var3);
    }

    public GuiViewNotification(PartyData var1, EntityPlayer var2, Notification var3, GuiScreen var4)
    {
        this.parent = var4;
        this.notification = var3;
        this.player = var2;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = var1;
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/notification/view.png");
        this.wParty = 256;
        this.hParty = 126;
        this.updateScreen();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char var1, int var2)
    {
        super.keyTyped(var1, var2);

        if (var2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateScreen();
        this.buttonList.clear();
        this.acceptButton = new GuiButton(0, this.xParty - 30, this.yParty + 34, 60, 20, "Accept");
        this.denyButton = new GuiButton(1, this.xParty - 100, this.yParty + 34, 60, 20, "Decline");
        this.backButton = new GuiButton(2, this.xParty + 40, this.yParty + 34, 60, 20, "Back");
        this.buttonList.add(this.acceptButton);
        this.buttonList.add(this.denyButton);
        this.buttonList.add(this.backButton);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        NotificationType var2 = this.notification.getType();

        switch (var1.id)
        {
            case 0:
                PartyMember var3 = PartyController.instance().getMember(this.notification.getSenderName());
                Party var4 = PartyController.instance().getParty(var3);
                this.mc.displayGuiScreen(new GuiDialogueBox(this.parent, var2.action.acceptMessage(this.notification), var2.action.failedMessage(this.notification), var2.action.executeAccept(this.notification)));
                break;

            case 1:
                var2.action.executeDecline(this.notification);
                this.mc.displayGuiScreen(this.parent);
                break;

            case 2:
                this.mc.displayGuiScreen(this.parent);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var4 = this.xParty - 128;
        int var5 = this.yParty - 63;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var4, var5, 0, 0, 256, this.hParty);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        String var7 = "Viewing Notification";
        this.mc.renderEngine.resetBoundTexture();
        this.drawString(this.fontRenderer, var7, this.width / 2 - this.fontRenderer.getStringWidth(var7) / 2, var5 + 7, 16777215);
        String var8 = this.notification.getTypeContents().getTitle(this.notification);
        this.mc.renderEngine.resetBoundTexture();
        this.drawString(this.fontRenderer, var8, this.width / 2 - this.fontRenderer.getStringWidth(var8) / 2, var5 + 27, 16777215);
        String var9 = this.notification.getTypeContents().getDescription(this.notification);
        ArrayList var10 = (new StringBox(var9, 45)).getStringList();

        for (int var11 = 0; var11 < var10.size(); ++var11)
        {
            String var12 = (String)var10.get(var11);
            this.mc.renderEngine.resetBoundTexture();
            this.drawString(this.fontRenderer, var12, this.width / 2 - this.fontRenderer.getStringWidth(var12) / 2, var5 + 45 + var11 * 10, 16777215);
        }

        super.drawScreen(var1, var2, var3);
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
        this.xParty = var2 / 2;
        this.yParty = var3 / 2;
    }
}
