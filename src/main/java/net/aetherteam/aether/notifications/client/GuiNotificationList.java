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
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiNotificationList extends GuiScreen
{
    private static final ResourceLocation TEXTURE_DIALOGUE = new ResourceLocation("aether", "textures/gui/dialogue.png");
    private static final ResourceLocation TEXTURE_PARTY = new ResourceLocation("aether", "textures/gui/party.png");
    private GuiYSlider sbar;
    private float sbarVal = 0.0F;
    private int xParty;
    private int yParty;
    private int wParty;
    private int hParty;
    private GuiScreen parent;
    private ArrayList<GuiNotificationSlot> notificationSlots = new ArrayList();

    /** Reference to the Minecraft object. */
    Minecraft mc;
    private int totalHeight;
    private GuiNotificationSlot selectedNotificationSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiButton joinButton;
    private EntityPlayer player;

    public GuiNotificationList(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.mc = FMLClientHandler.instance().getClient();
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
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
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.mc, par1, par2);
            }

            for (int l = 0; l < this.notificationSlots.size(); ++l)
            {
                int y = (int)((float)par2 + this.sbar.sliderValue * (float)(this.totalHeight - 103));
                GuiNotificationSlot notificationSlot = (GuiNotificationSlot)this.notificationSlots.get(l);

                if (notificationSlot.mousePressed(this.mc, par1, y) && par2 < this.yParty + 50)
                {
                    notificationSlot.selected = true;
                    this.slotIsSelected = true;
                    this.selectedNotificationSlot = notificationSlot;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

                    for (int rr = 0; rr < this.notificationSlots.size(); ++rr)
                    {
                        GuiNotificationSlot notificationSlot2 = (GuiNotificationSlot)this.notificationSlots.get(rr);

                        if (notificationSlot2 != notificationSlot)
                        {
                            notificationSlot2.selected = false;
                        }
                    }

                    return;
                }

                notificationSlot.selected = false;
                this.slotIsSelected = false;
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(this.parent);
                break;

            case 1:
                if (this.selectedNotificationSlot != null)
                {
                    Notification notification = this.selectedNotificationSlot.notification;
                    NotificationType type = notification.getType();
                    this.mc.displayGuiScreen(new GuiViewNotification(this.player, notification, this));
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
    public void drawScreen(int x, int y, float partialTick)
    {
        this.buttonList.clear();
        ArrayList notificationList = NotificationHandler.instance().getNotifications();

        if (notificationList.size() != this.notificationSlots.size())
        {
            this.notificationSlots.clear();
            this.slotsCreated = false;
        }

        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(TEXTURE_PARTY);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= (float)dmsy / 1000.0F;

        if (this.sbar.sliderValue > 1.0F)
        {
            this.sbar.sliderValue = 1.0F;
        }

        if (this.sbar.sliderValue < 0.0F)
        {
            this.sbar.sliderValue = 0.0F;
        }

        int centerX = this.xParty - 70;
        int centerY = this.yParty - 84;
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        this.totalHeight = 0;
        byte slotW = 100;
        byte slotH = 20;
        byte gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = notificationList.size() * (slotH + gutter);
        float sVal = -this.sbar.sliderValue * (float)(this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;
        int scaleFactor;

        if (!this.slotsCreated)
        {
            for (scaleFactor = 0; scaleFactor < notificationList.size(); ++scaleFactor)
            {
                this.notificationSlots.add(new GuiNotificationSlot((Notification)notificationList.get(scaleFactor), this.notificationSlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));
                this.totalHeight += slotH + gutter;
            }

            this.slotsCreated = true;
        }

        for (scaleFactor = 0; scaleFactor < this.notificationSlots.size(); ++scaleFactor)
        {
            ((GuiNotificationSlot)this.notificationSlots.get(scaleFactor)).drawPartySlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.mc, x, y);
        }

        this.drawString(this.fontRenderer, "消息列表", centerX + 70 - this.fontRenderer.getStringWidth("消息列表") / 2, centerY + 10, 16777215);
        if (this.notificationSlots.size() == 0)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.func_110577_a(TEXTURE_DIALOGUE);
            float var16 = 1.3F;
            float scaleFactorY = var16 - 0.4F;
            GL11.glTranslatef((float)this.xParty - 100.0F * var16, (float)this.yParty - (float)((this.hParty - 201) / 2) * scaleFactorY, 0.0F);
            GL11.glScalef(var16, scaleFactorY, var16);
            this.drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            String warningLabel = "没有要在这个时候显示的消息";
            this.drawString(this.fontRenderer, warningLabel, centerX + 70 - this.fontRenderer.getStringWidth(warningLabel) / 2, (int)((float)this.yParty - (float)((this.hParty - 241) / 2) * scaleFactorY), 16777215);
        }

        this.joinButton = new GuiButton(1, this.xParty + 3, this.yParty + 85 - 28, 58, 20, "查看");

        if (this.selectedNotificationSlot != null && this.slotIsSelected)
        {
            this.joinButton.enabled = true;
        }
        else
        {
            this.joinButton.enabled = false;
        }

        this.buttonList.add(new GuiButton(0, this.xParty - 60, this.yParty + 85 - 28, 58, 20, "返回"));
        this.buttonList.add(this.joinButton);
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
