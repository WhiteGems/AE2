package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import net.aetherteam.aether.client.gui.social.GuiYSlider;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiNotificationList extends GuiScreen
{
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int backgroundTexture;
    private int dialogueTexture;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;
    private ArrayList notificationSlots = new ArrayList();

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private int totalHeight;
    private GuiNotificationSlot selectedNotificationSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiButton joinButton;
    private EntityPlayer player;

    public GuiNotificationList(EntityPlayer var1, GuiScreen var2)
    {
        this.player = var1;
        this.mc = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.mc.renderEngine.getTexture("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = var2;
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
        Keyboard.enableRepeatEvents(true);
        this.updateScreen();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, var1, var2);
            }

            for (int var4 = 0; var4 < this.notificationSlots.size(); ++var4)
            {
                int var5 = (int)((float)var2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiNotificationSlot var6 = (GuiNotificationSlot)this.notificationSlots.get(var4);

                if (var6.mousePressed(this.mc, var1, var5) && var2 < this.yParty + 50)
                {
                    var6.selected = true;
                    this.slotIsSelected = true;
                    this.selectedNotificationSlot = var6;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    for (int var7 = 0; var7 < this.notificationSlots.size(); ++var7)
                    {
                        GuiNotificationSlot var8 = (GuiNotificationSlot)this.notificationSlots.get(var7);

                        if (var8 != var6)
                        {
                            var8.selected = false;
                        }
                    }

                    return;
                }

                var6.selected = false;
                this.slotIsSelected = false;
            }
        }

        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int var1, int var2, int var3)
    {
        if (var3 == 0)
        {
            this.sbar.mouseReleased(var1, var2);
        }

        super.mouseMovedOrUp(var1, var2, var3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton var1)
    {
        switch (var1.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;

            case 1:
                if (this.selectedNotificationSlot != null)
                {
                    Notification var2 = this.selectedNotificationSlot.notification;
                    NotificationType var3 = var2.getType();
                    this.mc.displayGuiScreen(new GuiViewNotification(this.player, var2, this));
                }
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
        this.buttonList.clear();
        ArrayList var4 = NotificationHandler.instance().getNotifications();

        if (var4.size() != this.notificationSlots.size())
        {
            this.notificationSlots.clear();
            this.slotsCreated = false;
        }

        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int var5 = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)var5 / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int var6 = this.xParty - 70;
        int var7 = this.yParty - 84;
        ScaledResolution var8 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(var6, var7, 0, 0, 141, this.hParty);
        this.totalHeight = 0;
        byte var9 = 100;
        byte var10 = 20;
        byte var11 = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((var6 + 14) * var8.getScaleFactor(), (var7 + 35) * var8.getScaleFactor(), var9 * var8.getScaleFactor(), 103 * var8.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = var4.size() * (var10 + var11);
        float var12 = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, var12, 0.0F);
        }

        this.totalHeight = 0;
        int var13;

        if (!this.slotsCreated)
        {
            for (var13 = 0; var13 < var4.size(); ++var13)
            {
                this.notificationSlots.add(new GuiNotificationSlot((Notification)var4.get(var13), this.notificationSlots.size(), var6 + 15, var7 + this.totalHeight + 30, var9, var10));
                this.totalHeight += var10 + var11;
            }

            this.slotsCreated = true;
        }

        for (var13 = 0; var13 < this.notificationSlots.size(); ++var13)
        {
            ((GuiNotificationSlot)this.notificationSlots.get(var13)).drawPartySlot(var6 + 15, var7 + this.totalHeight + 30, var9, var10);
            this.totalHeight += var10 + var11;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, var1, var2);
        }

        this.mc.renderEngine.resetBoundTexture();
        this.drawString(this.fontRenderer, "Notification List", var6 + 70 - this.fontRenderer.getStringWidth("Notification List") / 2, var7 + 10, 16777215);

        if (this.notificationSlots.size() == 0)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            float var16 = 1.3F;
            float var14 = var16 - 0.4F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var16, (float)this.yParty - (float)((this.hParty - 201) / 2) * var14, 0.0F);
            GL11.glScalef(var16, var14, var16);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            this.mc.renderEngine.resetBoundTexture();
            String var15 = "There are no notifications to display at this time.";
            this.drawString(this.fontRenderer, var15, var6 + 70 - this.fontRenderer.getStringWidth(var15) / 2, (int)((float)this.yParty - (float)((this.hParty - 241) / 2) * var14), 16777215);
        }

        this.joinButton = new GuiButton(1, this.xParty + 3, this.yParty + 85 - 28, 58, 20, "View");

        if (this.selectedNotificationSlot != null && this.slotIsSelected)
        {
            this.joinButton.enabled = true;
        }
        else
        {
            this.joinButton.enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 85 - 28, 58, 20, "Back"));
        this.buttonList.add(this.joinButton);
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
