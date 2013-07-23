package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.client.FMLClientHandler;
import java.util.ArrayList;
import java.util.List;
import net.aetherteam.aether.client.gui.social.GuiYSlider;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationHandler;
import net.aetherteam.aether.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
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
    Minecraft g;
    private int totalHeight;
    private GuiNotificationSlot selectedNotificationSlot;
    private boolean slotsCreated = false;
    private boolean slotIsSelected = false;
    private GuiButton joinButton;
    private EntityPlayer player;

    public GuiNotificationList(EntityPlayer player, GuiScreen parent)
    {
        this.player = player;
        this.g = FMLClientHandler.instance().getClient();
        this.backgroundTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/party.png");
        this.dialogueTexture = this.g.renderEngine.f("/net/aetherteam/aether/client/sprites/gui/dialogue.png");
        this.wParty = 256;
        this.hParty = 256;
        this.parent = parent;
        updateScreen();
    }

    protected void keyTyped(char charTyped, int keyTyped)
    {
        super.keyTyped(charTyped, keyTyped);

        if (keyTyped == Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode)
        {
            this.g.displayGuiScreen((GuiScreen)null);
            this.g.setIngameFocus();
        }
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        updateScreen();

        if (this.sbar != null)
        {
            this.sbarVal = this.sbar.sliderValue;
        }

        this.sbar = new GuiYSlider(-1, this.xParty + 46, this.yParty - 54, 10, 103);
        this.sbar.sliderValue = this.sbarVal;
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            if (this.totalHeight > 103)
            {
                this.sbar.mousePressed(this.g, par1, par2);
            }

            for (int l = 0; l < this.notificationSlots.size(); l++)
            {
                int y = (int)(par2 + this.sbar.sliderValue * (this.totalHeight - 103));
                GuiNotificationSlot notificationSlot = (GuiNotificationSlot)this.notificationSlots.get(l);

                if ((notificationSlot.mousePressed(this.g, par1, y)) && (par2 < this.yParty + 50))
                {
                    notificationSlot.selected = true;
                    this.slotIsSelected = true;
                    this.selectedNotificationSlot = notificationSlot;
                    this.g.sndManager.a("random.click", 1.0F, 1.0F);

                    for (int rr = 0; rr < this.notificationSlots.size(); rr++)
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

    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        if (par3 == 0)
        {
            this.sbar.mouseReleased(par1, par2);
        }

        super.mouseMovedOrUp(par1, par2, par3);
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                this.g.displayGuiScreen(this.parent);
                break;

            case 1:
                if (this.selectedNotificationSlot != null)
                {
                    Notification notification = this.selectedNotificationSlot.notification;
                    NotificationType type = notification.getType();
                    this.g.displayGuiScreen(new GuiViewNotification(this.player, notification, this));
                }

                break;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void drawScreen(int x, int y, float partialTick)
    {
        this.k.clear();
        ArrayList notificationList = NotificationHandler.instance().getNotifications();

        if (notificationList.size() != this.notificationSlots.size())
        {
            this.notificationSlots.clear();
            this.slotsCreated = false;
        }

        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture);
        int dmsy = Mouse.getDWheel();
        this.sbar.sliderValue -= dmsy / 1000.0F;

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
        ScaledResolution sr = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        drawTexturedModalRect(centerX, centerY, 0, 0, 141, this.hParty);
        this.totalHeight = 0;
        int slotW = 100;
        int slotH = 20;
        int gutter = 2;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((centerX + 14) * sr.getScaleFactor(), (centerY + 35) * sr.getScaleFactor(), slotW * sr.getScaleFactor(), 103 * sr.getScaleFactor());
        GL11.glPushMatrix();
        this.totalHeight = (notificationList.size() * (slotH + gutter));
        float sVal = -this.sbar.sliderValue * (this.totalHeight - 105);

        if (this.totalHeight > 103)
        {
            GL11.glTranslatef(0.0F, sVal, 0.0F);
        }

        this.totalHeight = 0;

        if (!this.slotsCreated)
        {
            for (int i = 0; i < notificationList.size(); i++)
            {
                this.notificationSlots.add(new GuiNotificationSlot((Notification)notificationList.get(i), this.notificationSlots.size(), centerX + 15, centerY + this.totalHeight + 30, slotW, slotH));
                this.totalHeight += slotH + gutter;
            }

            this.slotsCreated = true;
        }

        for (int i = 0; i < this.notificationSlots.size(); i++)
        {
            ((GuiNotificationSlot)this.notificationSlots.get(i)).drawPartySlot(centerX + 15, centerY + this.totalHeight + 30, slotW, slotH);
            this.totalHeight += slotH + gutter;
        }

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (this.totalHeight > 103)
        {
            this.sbar.drawButton(this.g, x, y);
        }

        this.g.renderEngine.a();
        drawString(this.m, "Notification List", centerX + 70 - this.m.getStringWidth("Notification List") / 2, centerY + 10, 16777215);

        if (this.notificationSlots.size() == 0)
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialogueTexture);
            float scaleFactor = 1.3F;
            float scaleFactorY = scaleFactor - 0.4F;
            GL11.glTranslatef(this.xParty - 100.0F * scaleFactor, this.yParty - (this.hParty - 201) / 2 * scaleFactorY, 0.0F);
            GL11.glScalef(scaleFactor, scaleFactorY, scaleFactor);
            drawTexturedModalRect(0, 0, 0, 0, 201, this.hParty - 201);
            GL11.glPopMatrix();
            this.g.renderEngine.a();
            String warningLabel = "There are no notifications to display at this time.";
            drawString(this.m, warningLabel, centerX + 70 - this.m.getStringWidth(warningLabel) / 2, (int)(this.yParty - (this.hParty - 241) / 2 * scaleFactorY), 16777215);
        }

        this.joinButton = new GuiButton(1, this.xParty + 3, this.yParty + 85 - 28, 58, 20, "View");

        if ((this.selectedNotificationSlot != null) && (this.slotIsSelected))
        {
            this.joinButton.enabled = true;
        }
        else
        {
            this.joinButton.enabled = false;
        }

        this.k.add(new GuiButton(0, this.xParty - 60, this.yParty + 85 - 28, 58, 20, "Back"));
        this.k.add(this.joinButton);
        super.drawScreen(x, y, partialTick);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ScaledResolution scaledresolution = new ScaledResolution(this.g.gameSettings, this.g.displayWidth, this.g.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        this.xParty = (width / 2);
        this.yParty = (height / 2);
    }
}

