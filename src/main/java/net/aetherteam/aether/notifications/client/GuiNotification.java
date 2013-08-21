package net.aetherteam.aether.notifications.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.aetherteam.aether.data.AetherOptions;
import net.aetherteam.aether.notifications.Notification;
import net.aetherteam.aether.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiNotification extends Gui
{
    private static final ResourceLocation TEXTURE_NOTIFICATIONS = new ResourceLocation("aether", "textures/gui/notification/notifications.png");
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
            this.senderName = notification.getType() == NotificationType.GENERIC ? notification.getSenderName() : (notification.getSenderName().isEmpty() ? "To " + notification.getReceiverName() : "From " + notification.getSenderName());
            this.notificationTime = Minecraft.getSystemTime();
            this.theNotification = notification;
            this.haveNotification = false;
        }
    }

    public void queueAchievementInformation(Notification notification)
    {
        this.headerText = notification.getHeaderText();
        this.senderName = notification.getType() == NotificationType.GENERIC ? notification.getSenderName() : (notification.getSenderName().isEmpty() ? "To " + notification.getReceiverName() : "From " + notification.getSenderName());
        this.notificationTime = Minecraft.getSystemTime() - 2500L;
        this.theNotification = notification;
        this.haveNotification = true;
    }

    private void updateAchievementWindowScale()
    {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.gameWindowWidth = this.theGame.displayWidth;
        this.gameWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.gameWindowWidth = scaledresolution.getScaledWidth();
        this.gameWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.gameWindowWidth, (double)this.gameWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateNotificationWindow()
    {
        if (this.theNotification != null && this.notificationTime != 0L)
        {
            double d0 = (double)(Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

            if (!this.haveNotification && (d0 < 0.0D || d0 > 1.0D))
            {
                this.notificationTime = 0L;
            }
            else
            {
                this.updateAchievementWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
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
                byte i = 0;
                int j = 0 - (int)(d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.theGame.renderEngine.func_110577_a(TEXTURE_NOTIFICATIONS);
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);
                byte textOffsetX = 45;

                if (this.haveNotification)
                {
                    this.theGame.fontRenderer.drawSplitString(this.senderName, i + textOffsetX, j + 7, 120, -1);
                }
                else
                {
                    this.theGame.fontRenderer.drawString(this.headerText, i + textOffsetX, j + 7, -256);
                    this.theGame.fontRenderer.drawString(this.senderName, i + textOffsetX, j + 18, -1);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
