package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.data.AetherOptions;
import net.aetherteam.aether.notifications.Notification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiNotification extends Gui
{
    private Minecraft theGame;
    private int gameWindowWidth;
    private int gameWindowHeight;
    private String headerText;
    private String senderName;
    private Notification theNotification;
    private long notificationTime;
    private RenderItem itemRender;
    private boolean haveNotification;

    public GuiNotification(Minecraft mc)
    {
        this.theGame = mc;
        this.itemRender = new RenderItem();
    }

    public void queueReceivedNotification(Notification notification)
    {
        if (AetherOptions.getShowNotifications())
        {
            this.headerText = notification.getHeaderText();
            this.senderName = ("来自于 " + notification.getSenderName());
            this.notificationTime = Minecraft.getSystemTime();
            this.theNotification = notification;
            this.haveNotification = false;
        }
    }

    public void queueAchievementInformation(Notification notification)
    {
        this.headerText = notification.getHeaderText();
        this.senderName = ("来自于 " + notification.getSenderName());
        this.notificationTime = (Minecraft.getSystemTime() - 2500L);
        this.theNotification = notification;
        this.haveNotification = true;
    }

    private void updateAchievementWindowScale()
    {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.gameWindowWidth = this.theGame.displayWidth;
        this.gameWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.gameWindowWidth = scaledresolution.getScaledWidth();
        this.gameWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.gameWindowWidth, this.gameWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateNotificationWindow()
    {
        if ((this.theNotification != null) && (this.notificationTime != 0L))
        {
            double d0 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

            if ((!this.haveNotification) && ((d0 < 0.0D) || (d0 > 1.0D)))
            {
                this.notificationTime = 0L;
            } else
            {
                updateAchievementWindowScale();
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D)
                {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D)
                {
                    d1 = 0.0D;
                }

                d1 *= d1;
                d1 *= d1;
                int i = 0;
                int j = 0 - (int) (d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(3553);
                this.theGame.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/notification/notifications.png");
                GL11.glDisable(2896);
                drawTexturedModalRect(i, j, 96, 202, 160, 32);

                int textOffsetX = 45;

                if (this.haveNotification)
                {
                    this.theGame.fontRenderer.drawSplitString(this.senderName, i + textOffsetX, j + 7, 120, -1);
                } else
                {
                    this.theGame.fontRenderer.drawString(this.headerText, i + textOffsetX, j + 7, -256);
                    this.theGame.fontRenderer.drawString(this.senderName, i + textOffsetX, j + 18, -1);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(2896);
                GL11.glEnable(32826);
                GL11.glEnable(2903);
                GL11.glEnable(2896);

                GL11.glDisable(2896);
                GL11.glDepthMask(true);
                GL11.glEnable(2929);
            }
        }
    }
}

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.notifications.client.GuiNotification
 * JD-Core Version:    0.6.2
 */