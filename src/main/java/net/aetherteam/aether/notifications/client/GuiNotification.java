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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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

    public GuiNotification(Minecraft var1)
    {
        this.theGame = var1;
        this.itemRender = new RenderItem();
    }

    public void queueReceivedNotification(Notification var1)
    {
        if (AetherOptions.getShowNotifications())
        {
            this.headerText = var1.getHeaderText();
            this.senderName = var1.getType() == NotificationType.GENERIC ? var1.getSenderName() : (var1.getSenderName().isEmpty() ? "To " + var1.getReceiverName() : "From " + var1.getSenderName());
            this.notificationTime = Minecraft.getSystemTime();
            this.theNotification = var1;
            this.haveNotification = false;
        }
    }

    public void queueAchievementInformation(Notification var1)
    {
        this.headerText = var1.getHeaderText();
        this.senderName = var1.getType() == NotificationType.GENERIC ? var1.getSenderName() : (var1.getSenderName().isEmpty() ? "To " + var1.getReceiverName() : "From " + var1.getSenderName());
        this.notificationTime = Minecraft.getSystemTime() - 2500L;
        this.theNotification = var1;
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
        ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.gameWindowWidth = var1.getScaledWidth();
        this.gameWindowHeight = var1.getScaledHeight();
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
            double var1 = (double)(Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

            if (!this.haveNotification && (var1 < 0.0D || var1 > 1.0D))
            {
                this.notificationTime = 0L;
            }
            else
            {
                this.updateAchievementWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double var3 = var1 * 2.0D;

                if (var3 > 1.0D)
                {
                    var3 = 2.0D - var3;
                }

                var3 *= 4.0D;
                var3 = 1.0D - var3;

                if (var3 < 0.0D)
                {
                    var3 = 0.0D;
                }

                var3 *= var3;
                var3 *= var3;
                byte var5 = 0;
                int var6 = 0 - (int)(var3 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.theGame.renderEngine.bindTexture("/net/aetherteam/aether/client/sprites/gui/notification/notifications.png");
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);
                byte var7 = 45;

                if (this.haveNotification)
                {
                    this.theGame.fontRenderer.drawSplitString(this.senderName, var5 + var7, var6 + 7, 120, -1);
                }
                else
                {
                    this.theGame.fontRenderer.drawString(this.headerText, var5 + var7, var6 + 7, -256);
                    this.theGame.fontRenderer.drawString(this.senderName, var5 + var7, var6 + 18, -1);
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
