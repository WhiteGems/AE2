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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiViewNotification extends GuiScreen
{
    private static final ResourceLocation TEXTURE_NOTIFICATION_VIEW = new ResourceLocation("aether", "textures/gui/notification/view.png");
    private final PartyData pm;
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

    public GuiViewNotification(EntityPlayer player, Notification notification, GuiScreen parent)
    {
        this(new PartyData(), player, notification, parent);
    }

    public GuiViewNotification(PartyData pm, EntityPlayer player, Notification notification, GuiScreen parent)
    {
        this.parent = parent;
        this.notification = notification;
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.pm = pm;
        this.wParty = 256;
        this.hParty = 126;
        this.updateScreen();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
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

        this.acceptButton = new GuiButton(0, this.xParty - 30, this.yParty + 34, 60, 20, "接受");
        this.denyButton = new GuiButton(1, this.xParty - 100, this.yParty + 34, 60, 20, "拒绝");
        this.backButton = new GuiButton(2, this.xParty + 40, this.yParty + 34, 60, 20, "返回");

        this.buttonList.add(this.acceptButton);
        this.buttonList.add(this.denyButton);
        this.buttonList.add(this.backButton);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton button)
    {
        NotificationType type = this.notification.getType();

        switch (button.id)
        {
            case 0:
                PartyMember recruiter = PartyController.instance().getMember(this.notification.getSenderName());
                Party party = PartyController.instance().getParty(recruiter);
                this.mc.displayGuiScreen(new GuiDialogueBox(this.parent, type.action.acceptMessage(this.notification), type.action.failedMessage(this.notification), type.action.executeAccept(this.notification)));
                break;

            case 1:
                type.action.executeDecline(this.notification);
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_NOTIFICATION_VIEW);
        int centerX = this.xParty - 128;
        int centerY = this.yParty - 63;
        new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 256, this.hParty);
        this.mc.renderEngine.func_110577_a(TEXTURE_NOTIFICATION_VIEW);
        String name = "查看通知";
        this.drawString(this.fontRenderer, name, this.width / 2 - this.fontRenderer.getStringWidth(name) / 2, centerY + 7, 16777215);
        String title = this.notification.getTypeContents().getTitle(this.notification);
        this.drawString(this.fontRenderer, title, this.width / 2 - this.fontRenderer.getStringWidth(title) / 2, centerY + 27, 16777215);
        String description = this.notification.getTypeContents().getDescription(this.notification);
        ArrayList descriptions = (new StringBox(description, 45)).getStringList();

        for (int count = 0; count < descriptions.size(); ++count)
        {
            String newLine = (String)descriptions.get(count);
            this.drawString(this.fontRenderer, newLine, this.width / 2 - this.fontRenderer.getStringWidth(newLine) / 2, centerY + 45 + count * 10, 16777215);
        }

        super.drawScreen(x, y, partialTick);
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
        this.xParty = width / 2;
        this.yParty = height / 2;
    }
}
